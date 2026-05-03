package com.yueping.volunteer.model;

public class UserEvidenceSummaryView {

    private int snapshotCount;
    private int trackPointCount;
    private int blockchainProofCount;
    private int anomalyCount;

    public int getSnapshotCount() {
        return snapshotCount;
    }

    public void setSnapshotCount(int snapshotCount) {
        this.snapshotCount = snapshotCount;
    }

    public int getTrackPointCount() {
        return trackPointCount;
    }

    public void setTrackPointCount(int trackPointCount) {
        this.trackPointCount = trackPointCount;
    }

    public int getBlockchainProofCount() {
        return blockchainProofCount;
    }

    public void setBlockchainProofCount(int blockchainProofCount) {
        this.blockchainProofCount = blockchainProofCount;
    }

    public int getAnomalyCount() {
        return anomalyCount;
    }

    public void setAnomalyCount(int anomalyCount) {
        this.anomalyCount = anomalyCount;
    }
}
