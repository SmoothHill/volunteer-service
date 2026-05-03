package com.yueping.volunteer.service;

import com.yueping.volunteer.model.CheckInMethod;
import com.yueping.volunteer.model.ParticipationRecord;
import com.yueping.volunteer.model.VolunteerActivity;
import org.springframework.stereotype.Service;

@Service
public class RewardEngineService {

    public RewardDecision calculate(VolunteerActivity activity,
                                    ParticipationRecord record,
                                    int snapshotCount,
                                    int trackPointCount,
                                    int serviceRating) {
        int basePoints = Math.max(1, (int) Math.round(record.getServiceHours() * 10));
        double difficultyCoefficient = resolveDifficultyCoefficient(activity.getDifficultyLevel(), activity.getDifficultyCoefficient());
        double ratingCoefficient = resolveRatingCoefficient(serviceRating);

        int trustBonus = 0;
        if (snapshotCount > 0) {
            trustBonus += 2;
        }
        if (trackPointCount >= 3) {
            trustBonus += 3;
        } else if (trackPointCount > 0) {
            trustBonus += 1;
        }
        if (record.getCheckInMethod() != null && record.getCheckInMethod() != CheckInMethod.MANUAL) {
            trustBonus += 1;
        }

        int finalPoints = Math.max(1, (int) Math.round(basePoints * difficultyCoefficient * ratingCoefficient + trustBonus));
        int creditDelta = Math.max(1, Math.min(5, Math.max(0, serviceRating - 2) + (trackPointCount > 0 ? 1 : 0)));
        String summary = String.format(
                "基础积分 %d = %.2f 小时 × 10，难度系数 %.2f，评价系数 %.2f，可信奖励 %d，最终积分 %d",
                basePoints,
                record.getServiceHours(),
                difficultyCoefficient,
                ratingCoefficient,
                trustBonus,
                finalPoints
        );

        return new RewardDecision(basePoints, difficultyCoefficient, ratingCoefficient, trustBonus, finalPoints, creditDelta, summary);
    }

    public double resolveDifficultyCoefficient(Integer difficultyLevel, Double customCoefficient) {
        if (customCoefficient != null && customCoefficient > 0) {
            return customCoefficient;
        }
        int level = difficultyLevel == null || difficultyLevel < 1 ? 3 : difficultyLevel;
        switch (level) {
            case 1:
                return 0.9;
            case 2:
                return 1.0;
            case 3:
                return 1.15;
            case 4:
                return 1.3;
            case 5:
                return 1.5;
            default:
                return 1.0;
        }
    }

    private double resolveRatingCoefficient(int serviceRating) {
        switch (serviceRating) {
            case 1:
                return 0.6;
            case 2:
                return 0.8;
            case 3:
                return 1.0;
            case 4:
                return 1.15;
            case 5:
                return 1.3;
            default:
                return 1.0;
        }
    }

    public static class RewardDecision {
        private final int basePoints;
        private final double difficultyCoefficient;
        private final double ratingCoefficient;
        private final int trustBonus;
        private final int finalPoints;
        private final int creditDelta;
        private final String summary;

        public RewardDecision(int basePoints,
                              double difficultyCoefficient,
                              double ratingCoefficient,
                              int trustBonus,
                              int finalPoints,
                              int creditDelta,
                              String summary) {
            this.basePoints = basePoints;
            this.difficultyCoefficient = difficultyCoefficient;
            this.ratingCoefficient = ratingCoefficient;
            this.trustBonus = trustBonus;
            this.finalPoints = finalPoints;
            this.creditDelta = creditDelta;
            this.summary = summary;
        }

        public int getBasePoints() {
            return basePoints;
        }

        public double getDifficultyCoefficient() {
            return difficultyCoefficient;
        }

        public double getRatingCoefficient() {
            return ratingCoefficient;
        }

        public int getTrustBonus() {
            return trustBonus;
        }

        public int getFinalPoints() {
            return finalPoints;
        }

        public int getCreditDelta() {
            return creditDelta;
        }

        public String getSummary() {
            return summary;
        }
    }
}
