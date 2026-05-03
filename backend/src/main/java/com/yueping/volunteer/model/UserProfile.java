package com.yueping.volunteer.model;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_profile",
        indexes = {
                @Index(name = "idx_user_open_id", columnList = "openId", unique = true),
                @Index(name = "idx_user_role", columnList = "role"),
                @Index(name = "idx_user_id_card_hash", columnList = "idCardHash"),
                @Index(name = "idx_user_phone_hash", columnList = "phoneHash")
        }
)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String openId;
    private String name;
    private String avatarUrl;
    @Convert(converter = SensitiveStringConverter.class)
    private String realName;
    @Convert(converter = SensitiveStringConverter.class)
    private String idCardNo;
    private String idCardHash;
    @Convert(converter = SensitiveStringConverter.class)
    private String phoneNo;
    private String phoneHash;
    private LocalDateTime phoneVerifiedAt;
    private String volunteerCardNo;
    private Boolean verified;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    private double totalHours;
    private int totalPoints;
    private int creditScore;
    private String badgeName;
    private LocalDateTime createdAt;

    public UserProfile() {
    }

    public UserProfile(Long id,
                       String openId,
                       String name,
                       String avatarUrl,
                       String realName,
                       String idCardNo,
                       String idCardHash,
                       String phoneNo,
                       String phoneHash,
                       LocalDateTime phoneVerifiedAt,
                       String volunteerCardNo,
                       Boolean verified,
                       UserRole role,
                       double totalHours,
                       int totalPoints,
                       int creditScore,
                       String badgeName,
                       LocalDateTime createdAt) {
        this.id = id;
        this.openId = openId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.realName = realName;
        this.idCardNo = idCardNo;
        this.idCardHash = idCardHash;
        this.phoneNo = phoneNo;
        this.phoneHash = phoneHash;
        this.phoneVerifiedAt = phoneVerifiedAt;
        this.volunteerCardNo = volunteerCardNo;
        this.verified = verified;
        this.role = role;
        this.totalHours = totalHours;
        this.totalPoints = totalPoints;
        this.creditScore = creditScore;
        this.badgeName = badgeName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardHash() {
        return idCardHash;
    }

    public void setIdCardHash(String idCardHash) {
        this.idCardHash = idCardHash;
    }

    public String getVolunteerCardNo() {
        return volunteerCardNo;
    }

    public void setVolunteerCardNo(String volunteerCardNo) {
        this.volunteerCardNo = volunteerCardNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneHash() {
        return phoneHash;
    }

    public void setPhoneHash(String phoneHash) {
        this.phoneHash = phoneHash;
    }

    public LocalDateTime getPhoneVerifiedAt() {
        return phoneVerifiedAt;
    }

    public void setPhoneVerifiedAt(LocalDateTime phoneVerifiedAt) {
        this.phoneVerifiedAt = phoneVerifiedAt;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
