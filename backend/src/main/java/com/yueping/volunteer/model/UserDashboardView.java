package com.yueping.volunteer.model;

import java.util.List;

public class UserDashboardView {

    private UserProfile profile;
    private List<ParticipationRecord> participationRecords;

    public UserDashboardView() {
    }

    public UserDashboardView(UserProfile profile, List<ParticipationRecord> participationRecords) {
        this.profile = profile;
        this.participationRecords = participationRecords;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public List<ParticipationRecord> getParticipationRecords() {
        return participationRecords;
    }

    public void setParticipationRecords(List<ParticipationRecord> participationRecords) {
        this.participationRecords = participationRecords;
    }
}

