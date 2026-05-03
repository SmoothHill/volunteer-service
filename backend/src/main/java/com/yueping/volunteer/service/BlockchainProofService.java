package com.yueping.volunteer.service;

import com.yueping.volunteer.model.BlockchainEventType;

import java.time.LocalDateTime;

public interface BlockchainProofService {

    BlockchainAnchorResult anchor(Long activityId,
                                  Long recordId,
                                  Long userId,
                                  BlockchainEventType eventType,
                                  String evidenceHash,
                                  String payloadSummary,
                                  LocalDateTime eventTime);
}
