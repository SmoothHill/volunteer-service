package com.yueping.volunteer.service;

import com.yueping.volunteer.dto.VerifyIdentityRequest;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
@Transactional
public class IdentityVerificationService {

    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^(\\d{15}|\\d{17}[\\dXx])$");
    private static final int VOLUNTEER_CARD_ID_WIDTH = 6;

    private final UserProfileRepository userProfileRepository;

    public IdentityVerificationService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional(readOnly = true)
    public UserProfile getIdentity(Long userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("当前用户不存在"));
    }

    public UserProfile verifyIdentity(Long userId, VerifyIdentityRequest request) {
        UserProfile user = getIdentity(userId);
        String realName = request.getRealName() == null ? "" : request.getRealName().trim();
        String idCardNo = request.getIdCardNo() == null ? "" : request.getIdCardNo().trim().toUpperCase();

        validateRealName(realName);
        validateIdCardNo(idCardNo);

        String idCardHash = SensitiveDataService.hashForLookup(idCardNo);
        if (userProfileRepository.existsByIdCardHashAndIdNot(idCardHash, userId)) {
            throw new IllegalArgumentException("该身份证号已绑定其他账号");
        }

        user.setRealName(realName);
        user.setIdCardNo(idCardNo);
        user.setIdCardHash(idCardHash);
        user.setVolunteerCardNo(resolveVolunteerCardNo(user));
        user.setVerified(true);
        return userProfileRepository.save(user);
    }

    public void ensureVerified(UserProfile user, String actionName) {
        if (!Boolean.TRUE.equals(user.getVerified())) {
            throw new IllegalArgumentException("请先完成实名认证后再" + actionName);
        }
    }

    private void validateRealName(String realName) {
        if (!StringUtils.hasText(realName)) {
            throw new IllegalArgumentException("真实姓名不能为空");
        }
        if (realName.length() < 2 || realName.length() > 20) {
            throw new IllegalArgumentException("真实姓名长度需要在 2 到 20 个字符之间");
        }
    }

    private void validateIdCardNo(String idCardNo) {
        if (!ID_CARD_PATTERN.matcher(idCardNo).matches()) {
            throw new IllegalArgumentException("身份证号格式不正确");
        }
    }

    private String resolveVolunteerCardNo(UserProfile user) {
        if (StringUtils.hasText(user.getVolunteerCardNo())) {
            return user.getVolunteerCardNo().trim();
        }
        Long userId = user.getId();
        if (userId == null) {
            throw new IllegalArgumentException("用户编号不存在，无法生成志愿者号");
        }
        return "V" + LocalDate.now().getYear()
                + String.format("%0" + VOLUNTEER_CARD_ID_WIDTH + "d", userId);
    }
}
