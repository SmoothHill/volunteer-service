package com.yueping.volunteer.service;

import com.yueping.volunteer.model.AdminApplicationStatus;
import com.yueping.volunteer.model.NotificationMessageRecord;
import com.yueping.volunteer.model.NotificationType;
import com.yueping.volunteer.model.RedemptionRecord;
import com.yueping.volunteer.model.SupplementApplication;
import com.yueping.volunteer.model.SupplementStatus;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.model.VolunteerActivity;
import com.yueping.volunteer.repository.NotificationMessageRecordRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private static final String BIZ_TYPE_ACTIVITY = "ACTIVITY";
    private static final String BIZ_TYPE_SUPPLEMENT = "SUPPLEMENT";
    private static final String BIZ_TYPE_PARTICIPATION = "PARTICIPATION_RECORD";
    private static final String BIZ_TYPE_ADMIN_APPLICATION = "ADMIN_APPLICATION";
    private static final String BIZ_TYPE_REDEMPTION = "REDEMPTION";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final NotificationMessageRecordRepository notificationMessageRecordRepository;
    private final NotificationDispatchService notificationDispatchService;
    private final UserProfileRepository userProfileRepository;
    private final NotificationTemplateResolver notificationTemplateResolver;

    public NotificationService(NotificationMessageRecordRepository notificationMessageRecordRepository,
                               NotificationDispatchService notificationDispatchService,
                               UserProfileRepository userProfileRepository,
                               NotificationTemplateResolver notificationTemplateResolver) {
        this.notificationMessageRecordRepository = notificationMessageRecordRepository;
        this.notificationDispatchService = notificationDispatchService;
        this.userProfileRepository = userProfileRepository;
        this.notificationTemplateResolver = notificationTemplateResolver;
    }

    @Transactional(readOnly = true)
    public List<NotificationMessageRecord> listLatestMessages(Long userId) {
        return notificationMessageRecordRepository.findTop20ByUserIdOrderByCreatedAtDesc(userId);
    }

    public void sendEnrollmentSubmitted(UserProfile user, VolunteerActivity activity, Long recordId) {
        send(
                user,
                NotificationType.ENROLLMENT_SUBMITTED,
                "报名已提交",
                "你已提交活动《" + activity.getTitle() + "》的报名申请，请等待管理员审核。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId),
                false
        );
    }

    public void sendEnrollmentPendingReview(VolunteerActivity activity, UserProfile applicant, Long recordId) {
        sendToManagers(
                NotificationType.ENROLLMENT_PENDING_REVIEW,
                "有新的报名待审核",
                resolveUserName(applicant) + " 提交了活动《" + activity.getTitle() + "》的报名申请，请尽快审核。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId)
        );
    }

    public void sendEnrollmentReviewed(UserProfile user, VolunteerActivity activity, Long recordId, boolean approved) {
        send(
                user,
                approved ? NotificationType.ENROLLMENT_APPROVED : NotificationType.ENROLLMENT_REJECTED,
                approved ? "报名审核通过" : "报名审核驳回",
                approved
                        ? "你报名的活动《" + activity.getTitle() + "》已审核通过，请按时签到。"
                        : "你报名的活动《" + activity.getTitle() + "》未通过审核，请查看活动要求后重新报名。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId),
                false
        );
    }

    public void sendEnrollmentReviewedOperator(UserProfile operator,
                                               UserProfile applicant,
                                               VolunteerActivity activity,
                                               Long recordId,
                                               boolean approved) {
        send(
                operator,
                NotificationType.ENROLLMENT_REVIEW_PROCESSED,
                approved ? "报名审核处理完成" : "报名驳回处理完成",
                (approved ? "你已通过 " : "你已驳回 ")
                        + resolveUserName(applicant)
                        + " 对活动《" + activity.getTitle() + "》的报名申请。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId),
                false
        );
    }

    public void sendSupplementSubmitted(UserProfile user, VolunteerActivity activity, SupplementApplication application) {
        send(
                user,
                NotificationType.SUPPLEMENT_SUBMITTED,
                "补签申请已提交",
                "你已提交活动《" + activity.getTitle() + "》的补签申请，请等待管理员审核。",
                BIZ_TYPE_SUPPLEMENT,
                String.valueOf(application.getId()),
                false
        );
    }

    public void sendSupplementPendingReview(VolunteerActivity activity, UserProfile applicant, SupplementApplication application) {
        sendToManagers(
                NotificationType.SUPPLEMENT_PENDING_REVIEW,
                "有新的补签待审核",
                resolveUserName(applicant) + " 提交了活动《" + activity.getTitle() + "》的补签申请，请尽快审核。",
                BIZ_TYPE_SUPPLEMENT,
                String.valueOf(application.getId())
        );
    }

    public void sendSupplementReviewed(UserProfile user, VolunteerActivity activity, SupplementApplication application) {
        boolean approved = application.getStatus() == SupplementStatus.APPROVED;
        send(
                user,
                approved ? NotificationType.SUPPLEMENT_APPROVED : NotificationType.SUPPLEMENT_REJECTED,
                approved ? "补签审核通过" : "补签审核驳回",
                approved
                        ? "你在活动《" + activity.getTitle() + "》中的补签申请已审核通过。"
                        : "你在活动《" + activity.getTitle() + "》中的补签申请未通过审核，请根据反馈调整后重试。",
                BIZ_TYPE_SUPPLEMENT,
                String.valueOf(application.getId()),
                false
        );
    }

    public void sendSupplementReviewedOperator(UserProfile operator,
                                               UserProfile applicant,
                                               VolunteerActivity activity,
                                               SupplementApplication application) {
        boolean approved = application.getStatus() == SupplementStatus.APPROVED;
        send(
                operator,
                NotificationType.SUPPLEMENT_REVIEW_PROCESSED,
                approved ? "补签审核处理完成" : "补签驳回处理完成",
                (approved ? "你已通过 " : "你已驳回 ")
                        + resolveUserName(applicant)
                        + " 在活动《" + activity.getTitle() + "》中的补签申请。",
                BIZ_TYPE_SUPPLEMENT,
                String.valueOf(application.getId()),
                false
        );
    }

    public void sendActivityReminder(UserProfile user, VolunteerActivity activity) {
        send(
                user,
                NotificationType.ACTIVITY_REMINDER,
                "活动开始提醒",
                "你报名的活动《" + activity.getTitle() + "》将于 "
                        + activity.getStartTime().format(DATE_TIME_FORMATTER) + " 开始，请提前签到。",
                BIZ_TYPE_ACTIVITY,
                String.valueOf(activity.getId()),
                true
        );
    }

    public void sendTaskCompleted(UserProfile user,
                                  VolunteerActivity activity,
                                  Long recordId,
                                  double serviceHours) {
        send(
                user,
                NotificationType.TASK_COMPLETED,
                "志愿任务完成",
                "你已完成活动《" + activity.getTitle() + "》的志愿服务，本次服务时长 "
                        + serviceHours + " 小时，等待管理员确认与评价。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId),
                false
        );
    }

    public void sendServiceConfirmPending(VolunteerActivity activity,
                                          UserProfile participant,
                                          Long recordId,
                                          double serviceHours) {
        sendToManagers(
                NotificationType.SERVICE_CONFIRM_PENDING,
                "服务完成待确认",
                resolveUserName(participant) + " 已完成活动《" + activity.getTitle() + "》的服务，服务时长 "
                        + serviceHours + " 小时，请尽快确认并评价。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId)
        );
    }

    public void sendServiceRatingReceived(UserProfile user,
                                          VolunteerActivity activity,
                                          Long recordId,
                                          int serviceRating) {
        send(
                user,
                NotificationType.SERVICE_RATING_RECEIVED,
                "获得服务评分",
                "你在活动《" + activity.getTitle() + "》中获得了 " + serviceRating + " 分评价。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId),
                false
        );
    }

    public void sendRewardFinalized(UserProfile user,
                                    VolunteerActivity activity,
                                    Long recordId,
                                    int earnedPoints,
                                    String certificateNo) {
        send(
                user,
                NotificationType.REWARD_FINALIZED,
                "积分/证书结算完成",
                "活动《" + activity.getTitle() + "》的积分已发放，共获得 " + earnedPoints
                        + " 积分，证书编号：" + (certificateNo == null ? "-" : certificateNo) + "。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId),
                false
        );
    }

    public void sendRewardFinalizedOperator(UserProfile operator,
                                            UserProfile participant,
                                            VolunteerActivity activity,
                                            Long recordId,
                                            int earnedPoints,
                                            int serviceRating) {
        send(
                operator,
                NotificationType.REWARD_FINALIZED_PROCESSED,
                "服务评价处理回执",
                "你已完成 " + resolveUserName(participant)
                        + " 在活动《" + activity.getTitle() + "》中的服务评价与结算，评分 "
                        + serviceRating + " 分，发放积分 " + earnedPoints + "。",
                BIZ_TYPE_PARTICIPATION,
                String.valueOf(recordId),
                false
        );
    }

    public void sendRedemptionCreated(UserProfile user, RedemptionRecord redemptionRecord) {
        String actionText = redemptionRecord.getDeliveryType() == null
                || redemptionRecord.getDeliveryType().name().equals("VIRTUAL")
                ? "请等待管理员发放。"
                : "已提交收货信息，请等待管理员寄出。";
        send(
                user,
                NotificationType.REDEMPTION_CREATED,
                "兑换成功",
                "你已成功兑换商品《" + redemptionRecord.getItemName() + "》，数量 "
                        + redemptionRecord.getQuantity() + "，消耗积分 " + redemptionRecord.getPointsCost() + "。" + actionText,
                BIZ_TYPE_REDEMPTION,
                String.valueOf(redemptionRecord.getId()),
                false
        );
    }

    public void sendRedemptionCreatedOperator(UserProfile user, RedemptionRecord redemptionRecord) {
        sendToManagers(
                NotificationType.REDEMPTION_CREATED_OPERATOR,
                "有新的兑换记录",
                resolveUserName(user) + " 已兑换商品《" + redemptionRecord.getItemName() + "》，数量 "
                        + redemptionRecord.getQuantity() + "，请关注后续发放。",
                BIZ_TYPE_REDEMPTION,
                String.valueOf(redemptionRecord.getId())
        );
    }

    public void sendRedemptionDelivered(UserProfile user, RedemptionRecord redemptionRecord) {
        String deliveryText = redemptionRecord.getDeliveryType() == null
                || redemptionRecord.getDeliveryType().name().equals("VIRTUAL")
                ? "已发放"
                : "已寄出";
        String remark = StringUtils.hasText(redemptionRecord.getDeliveryRemark())
                ? "，备注：" + redemptionRecord.getDeliveryRemark()
                : "";
        send(
                user,
                NotificationType.REDEMPTION_DELIVERED,
                "兑换发放通知",
                "你兑换的商品《" + redemptionRecord.getItemName() + "》" + deliveryText + remark,
                BIZ_TYPE_REDEMPTION,
                String.valueOf(redemptionRecord.getId()),
                false
        );
    }

    public void sendAdminApplicationSubmitted(UserProfile applicant, Long applicationId) {
        sendToRoles(
                new LinkedHashSet<>(Arrays.asList(UserRole.SUPER_ADMIN)),
                NotificationType.ADMIN_APPLICATION_SUBMITTED,
                "有新的管理员申请",
                resolveUserName(applicant) + " 提交了活动管理员申请，请尽快审核。",
                BIZ_TYPE_ADMIN_APPLICATION,
                String.valueOf(applicationId)
        );
    }

    public void sendAdminApplicationReviewed(UserProfile user, Long applicationId, AdminApplicationStatus status) {
        boolean approved = status == AdminApplicationStatus.APPROVED;
        send(
                user,
                approved ? NotificationType.ADMIN_APPLICATION_APPROVED : NotificationType.ADMIN_APPLICATION_REJECTED,
                approved ? "管理员申请已通过" : "管理员申请未通过",
                approved
                        ? "你的活动管理员申请已通过审核，现在可以进入管理端处理活动。"
                        : "你的活动管理员申请未通过审核，请修改后重新提交。",
                BIZ_TYPE_ADMIN_APPLICATION,
                String.valueOf(applicationId),
                false
        );
    }

    public void sendAdminApplicationReviewedOperator(UserProfile operator,
                                                     UserProfile applicant,
                                                     Long applicationId,
                                                     AdminApplicationStatus status) {
        boolean approved = status == AdminApplicationStatus.APPROVED;
        send(
                operator,
                NotificationType.ADMIN_APPLICATION_REVIEW_PROCESSED,
                approved ? "管理员申请处理回执" : "管理员申请驳回回执",
                (approved ? "你已通过 " : "你已驳回 ")
                        + resolveUserName(applicant) + " 的活动管理员申请。",
                BIZ_TYPE_ADMIN_APPLICATION,
                String.valueOf(applicationId),
                false
        );
    }

    private void sendToManagers(NotificationType notificationType,
                                String title,
                                String content,
                                String bizType,
                                String bizId) {
        sendToRoles(
                new LinkedHashSet<>(Arrays.asList(UserRole.ADMIN, UserRole.SUPER_ADMIN)),
                notificationType,
                title,
                content,
                bizType,
                bizId
        );
    }

    private void sendToRoles(Set<UserRole> roles,
                             NotificationType notificationType,
                             String title,
                             String content,
                             String bizType,
                             String bizId) {
        List<UserProfile> recipients = userProfileRepository.findByRoleIn(roles).stream()
                .filter(user -> user.getId() != null)
                .collect(Collectors.toList());
        for (UserProfile recipient : recipients) {
            send(recipient, notificationType, title, content, bizType, bizId, false);
        }
    }

    private NotificationMessageRecord send(UserProfile user,
                                           NotificationType notificationType,
                                           String title,
                                           String content,
                                           String bizType,
                                           String bizId,
                                           boolean deduplicate) {
        if (user == null || user.getId() == null) {
            return null;
        }
        if (deduplicate && notificationMessageRecordRepository.existsByNotificationTypeAndUserIdAndBizTypeAndBizId(
                notificationType,
                user.getId(),
                bizType,
                bizId
        )) {
            return null;
        }

        String templateCode = notificationTemplateResolver.resolveTemplateId(notificationType)
                .orElse(notificationType.name());
        NotificationDispatchResult dispatchResult = notificationDispatchService.dispatch(
                user,
                notificationType,
                title,
                content,
                templateCode
        );

        NotificationMessageRecord record = new NotificationMessageRecord();
        record.setUserId(user.getId());
        record.setOpenId(user.getOpenId());
        record.setNotificationType(notificationType);
        record.setChannel(dispatchResult.getChannel());
        record.setSendStatus(dispatchResult.getSendStatus());
        record.setTitle(title);
        record.setContent(content);
        record.setBizType(bizType);
        record.setBizId(bizId);
        record.setTemplateCode(templateCode);
        record.setProviderMessageId(dispatchResult.getProviderMessageId());
        record.setFailureReason(dispatchResult.getFailureReason());
        record.setCreatedAt(LocalDateTime.now());
        record.setSentAt(dispatchResult.getSentAt());
        return notificationMessageRecordRepository.save(record);
    }

    private String resolveUserName(UserProfile user) {
        if (user == null) {
            return "用户";
        }
        if (StringUtils.hasText(user.getName())) {
            return user.getName().trim();
        }
        return "用户" + user.getId();
    }
}
