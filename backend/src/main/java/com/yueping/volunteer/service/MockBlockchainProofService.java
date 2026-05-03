package com.yueping.volunteer.service;

import com.yueping.volunteer.model.BlockchainEventType;
import com.yueping.volunteer.model.BlockchainProofStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class MockBlockchainProofService implements BlockchainProofService {

    @Override
    public BlockchainAnchorResult anchor(Long activityId,
                                         Long recordId,
                                         Long userId,
                                         BlockchainEventType eventType,
                                         String evidenceHash,
                                         String payloadSummary,
                                         LocalDateTime eventTime) {
        BlockchainAnchorResult result = new BlockchainAnchorResult();
        result.setProofStatus(BlockchainProofStatus.MOCK_CHAINED);
        result.setAnchoredAt(LocalDateTime.now());
        result.setProviderName("mock-chain");
        result.setTransactionNo(buildTransactionNo(activityId, recordId, eventType));
        result.setFailureReason("");
        return result;
    }

    private String buildTransactionNo(Long activityId, Long recordId, BlockchainEventType eventType) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        return "MOCK-"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "-"
                + (activityId == null ? "A0" : "A" + activityId)
                + "-"
                + (recordId == null ? "R0" : "R" + recordId)
                + "-"
                + eventType.name()
                + "-"
                + suffix;
    }
}
