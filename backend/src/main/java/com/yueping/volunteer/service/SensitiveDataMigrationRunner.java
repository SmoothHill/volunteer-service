package com.yueping.volunteer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class SensitiveDataMigrationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SensitiveDataMigrationRunner.class);

    private final JdbcTemplate jdbcTemplate;

    public SensitiveDataMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        List<SensitiveUserRow> rows = jdbcTemplate.query(
                "select id, real_name, id_card_no, id_card_hash from user_profile",
                (rs, rowNum) -> new SensitiveUserRow(
                        rs.getLong("id"),
                        rs.getString("real_name"),
                        rs.getString("id_card_no"),
                        rs.getString("id_card_hash")
                )
        );
        for (SensitiveUserRow row : rows) {
            migrateRow(row);
        }
    }

    private void migrateRow(SensitiveUserRow row) {
        String rawRealName = normalize(row.realName);
        String rawIdCardNo = normalize(row.idCardNo);
        String rawIdCardHash = normalize(row.idCardHash);

        String plainRealName;
        String plainIdCardNo;
        try {
            plainRealName = SensitiveDataService.decryptIfNeeded(rawRealName);
            plainIdCardNo = SensitiveDataService.decryptIfNeeded(rawIdCardNo);
        } catch (IllegalStateException ex) {
            log.warn("Skip sensitive data migration for user {} because existing encrypted data cannot be decrypted with current key", row.id);
            return;
        }
        String nextRealName = StringUtils.hasText(plainRealName) ? SensitiveDataService.encrypt(plainRealName) : "";
        String nextIdCardNo = StringUtils.hasText(plainIdCardNo) ? SensitiveDataService.encrypt(plainIdCardNo) : "";
        String nextIdCardHash = StringUtils.hasText(plainIdCardNo) ? SensitiveDataService.hashForLookup(plainIdCardNo) : "";

        boolean needUpdate = !nextRealName.equals(rawRealName)
                || !nextIdCardNo.equals(rawIdCardNo)
                || !nextIdCardHash.equals(rawIdCardHash);
        if (!needUpdate) {
            return;
        }

        jdbcTemplate.update(
                "update user_profile set real_name = ?, id_card_no = ?, id_card_hash = ? where id = ?",
                nextRealName,
                nextIdCardNo,
                nextIdCardHash,
                row.id
        );
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static final class SensitiveUserRow {
        private final Long id;
        private final String realName;
        private final String idCardNo;
        private final String idCardHash;

        private SensitiveUserRow(Long id, String realName, String idCardNo, String idCardHash) {
            this.id = id;
            this.realName = realName;
            this.idCardNo = idCardNo;
            this.idCardHash = idCardHash;
        }
    }
}
