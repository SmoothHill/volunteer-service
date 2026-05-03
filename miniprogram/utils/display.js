function formatDateTime(value, fallback = '暂无') {
  if (!value) {
    return fallback
  }
  return String(value).replace('T', ' ').replace(/\.\d+$/, '')
}

function toRoleLabel(role) {
  if (role === 'SUPER_ADMIN') {
    return '超级管理员'
  }
  if (role === 'ADMIN') {
    return '活动管理员'
  }
  if (role === 'VOLUNTEER') {
    return '志愿者'
  }
  return '未选择身份'
}

function toActivityStatusLabel(status) {
  if (status === 'PUBLISHED') {
    return '报名中'
  }
  if (status === 'CLOSED' || status === 'FINISHED') {
    return '已结束'
  }
  if (status === 'DRAFT') {
    return '草稿'
  }
  return status || '未知状态'
}

function toParticipationStatusLabel(status) {
  if (status === 'PENDING') {
    return '待审核'
  }
  if (status === 'APPROVED') {
    return '已报名'
  }
  if (status === 'CHECKED_IN') {
    return '服务中'
  }
  if (status === 'COMPLETED') {
    return '已完成'
  }
  if (status === 'REJECTED') {
    return '已驳回'
  }
  return status || '未知状态'
}

function toRedemptionStatusLabel(status) {
  if (status === 'CREATED') {
    return '已提交'
  }
  if (status === 'DELIVERED') {
    return '已发放'
  }
  if (status === 'CANCELLED') {
    return '已取消'
  }
  return status || '未知状态'
}

function toSupplementStatusLabel(status) {
  if (status === 'PENDING') {
    return '待审核'
  }
  if (status === 'APPROVED') {
    return '已通过'
  }
  if (status === 'REJECTED') {
    return '已驳回'
  }
  return status || '未知状态'
}

function toSupplementTypeLabel(type) {
  if (type === 'CHECK_OUT') {
    return '签退补签'
  }
  if (type === 'CHECK_IN') {
    return '签到补签'
  }
  return type || '补签申请'
}

function toDifficultyLabel(level) {
  const value = Number(level || 0)
  if (value <= 1) return '1级·轻量'
  if (value === 2) return '2级·基础'
  if (value === 3) return '3级·常规'
  if (value === 4) return '4级·较高'
  return '5级·重点'
}

function toDemandLabel(level) {
  const value = Number(level || 0)
  if (value <= 1) return '1级·常规'
  if (value === 2) return '2级·一般'
  if (value === 3) return '3级·关注'
  if (value === 4) return '4级·紧缺'
  return '5级·急需'
}

function toTrackPointTypeLabel(pointType) {
  if (pointType === 'START') {
    return '起点轨迹'
  }
  if (pointType === 'MIDDLE') {
    return '中途轨迹'
  }
  if (pointType === 'END') {
    return '终点轨迹'
  }
  if (pointType === 'SITE') {
    return '活动地点'
  }
  if (pointType === 'STAY') {
    return '停留点'
  }
  return pointType || '轨迹点'
}

function maskRealName(value) {
  if (!value) {
    return '未认证'
  }
  const raw = String(value).trim()
  if (!raw) {
    return '未认证'
  }
  return `${raw.slice(0, 1)}*`
}

function maskIdCardNo(value) {
  if (!value) {
    return ''
  }
  const raw = String(value).trim()
  if (raw.length <= 5) {
    return raw
  }
  return `${raw.slice(0, 3)}${'*'.repeat(Math.max(1, raw.length - 5))}${raw.slice(-2)}`
}

function maskPhoneNo(value) {
  if (!value) {
    return ''
  }
  const raw = String(value).trim()
  if (raw.length <= 7) {
    return raw
  }
  return `${raw.slice(0, 3)}${'*'.repeat(Math.max(1, raw.length - 7))}${raw.slice(-4)}`
}

function maskVolunteerCardNo(value) {
  if (!value) {
    return ''
  }
  const raw = String(value).trim()
  if (raw.length <= 6) {
    return raw
  }
  return `${raw.slice(0, 3)}${'*'.repeat(Math.max(1, raw.length - 6))}${raw.slice(-3)}`
}

function formatProfile(profile) {
  if (!profile) {
    return null
  }
  return {
    ...profile,
    displayRole: toRoleLabel(profile.role),
    displayVerifiedStatus: profile.verified ? '已认证' : '未认证',
    displayRealName: maskRealName(profile.realName),
    maskedIdCardNo: maskIdCardNo(profile.idCardNo),
    maskedVolunteerCardNo: maskVolunteerCardNo(profile.volunteerCardNo),
    maskedPhoneNo: maskPhoneNo(profile.phoneNo)
  }
}

function formatParticipationRecord(record) {
  return {
    ...record,
    recordKey: record.recordId || record.id,
    displayActivityTitle: record.activityTitle || `活动 ${record.activityId}`,
    displayActivityCategory: record.activityCategory || '综合服务',
    canExportProof: record.status === 'COMPLETED' && Boolean(record.organizerConfirmed) && Boolean(record.rewardFinalizedAt),
    displayStatus: toParticipationStatusLabel(record.status),
    displayCheckInTime: formatDateTime(record.checkInTime, '未签到'),
    displayCheckOutTime: formatDateTime(record.checkOutTime, '未签退'),
    displayCreatedAt: formatDateTime(record.createdAt),
    displayRewardStatus: record.rewardPending ? '待评价结算' : '已结算',
    displayServiceRating: record.serviceRating ? `${record.serviceRating} 分` : '',
    displayConfirmedAt: formatDateTime(record.organizerConfirmedAt, '待确认')
  }
}

function formatSupplement(item) {
  return {
    ...item,
    displayStatus: toSupplementStatusLabel(item.status),
    displayType: toSupplementTypeLabel(item.type),
    displayActivityTitle: item.activityTitle || `活动 ${item.activityId}`,
    displayRequestedTime: formatDateTime(item.requestedTime),
    reviewComment: item.reviewComment || ''
  }
}

function formatRedemption(item) {
  return {
    ...item,
    displayStatus: toRedemptionStatusLabel(item.status),
    displayCreatedAt: formatDateTime(item.createdAt),
    displayDeliveryType: item.deliveryType === 'PHYSICAL' ? '实体商品' : '虚拟商品',
    maskedRecipientPhone: maskPhoneNo(item.recipientPhone)
  }
}

function formatActivity(activity) {
  return {
    ...activity,
    displayStatus: toActivityStatusLabel(activity.status),
    displayCategory: activity.category || '综合服务',
    displayDifficulty: toDifficultyLabel(activity.difficultyLevel),
    displayDemand: toDemandLabel(activity.demandLevel),
    displayStartTime: formatDateTime(activity.startTime),
    displayEndTime: formatDateTime(activity.endTime)
  }
}

function formatStarLeaderboardItem(item) {
  return {
    ...item,
    displayPeriod: item.period === 'month' ? '月榜' : '周榜',
    displayPositiveRatingRate: `${Number(item.positiveRatingRate || 0).toFixed(1)}%`,
    displayTrackCoverageScore: `${Number(item.trackCoverageScore || 0).toFixed(1)} 分`,
    displayStarScore: Number(item.starScore || 0).toFixed(1),
    displayTotalHours: Number(item.totalHours || 0).toFixed(1),
    displayShowcaseText: item.showcaseText || '持续参与社区服务，展现稳定的志愿服务表现。'
  }
}

function formatCommunityInsightItem(item) {
  return {
    ...item,
    displayDemandIndex: Number(item.demandIndex || 0),
    displayVitalityIndex: Number(item.vitalityIndex || 0),
    displayGapCount: Number(item.gapCount || 0),
    displayTrackCoverageScore: Number(item.trackCoverageScore || 0).toFixed(1),
    displayLocation: item.location || item.communityName || '未标注地点',
    displayAreaTag: item.areaTag || '均衡服务区',
    displayGuidanceText: item.guidanceText || '当前区域供需相对平衡，可继续观察。'
  }
}

function formatAdminApplication(application) {
  if (!application) {
    return {
      exists: false,
      status: '',
      displayStatus: '',
      displayRole: '活动管理员',
      reason: '',
      reviewComment: '',
      reviewedAt: '',
      displayCreatedAt: '',
      displayReviewedAt: ''
    }
  }
  const displayStatus = application.status === 'APPROVED'
    ? '已通过'
    : application.status === 'REJECTED'
      ? '已驳回'
      : '待审核'
  return {
    ...application,
    exists: true,
    displayStatus,
    displayRole: toRoleLabel('ADMIN'),
    displayCreatedAt: formatDateTime(application.createdAt),
    displayReviewedAt: formatDateTime(application.reviewedAt, '待审核')
  }
}

function toNotificationTypeLabel(type) {
  const map = {
    ENROLLMENT_SUBMITTED: '报名已提交',
    ENROLLMENT_PENDING_REVIEW: '有新的报名待审核',
    ENROLLMENT_APPROVED: '报名审核通过',
    ENROLLMENT_REJECTED: '报名审核驳回',
    ENROLLMENT_REVIEW_PROCESSED: '报名审核处理回执',
    SUPPLEMENT_SUBMITTED: '补签申请已提交',
    SUPPLEMENT_PENDING_REVIEW: '有新的补签待审核',
    SUPPLEMENT_APPROVED: '补签审核通过',
    SUPPLEMENT_REJECTED: '补签审核驳回',
    SUPPLEMENT_REVIEW_PROCESSED: '补签审核处理回执',
    ACTIVITY_REMINDER: '活动开始提醒',
    TASK_COMPLETED: '志愿任务完成',
    SERVICE_CONFIRM_PENDING: '服务完成待确认',
    SERVICE_RATING_RECEIVED: '获得服务评分',
    REWARD_FINALIZED: '服务结算通知',
    REWARD_FINALIZED_PROCESSED: '服务结算回执',
    REDEMPTION_CREATED: '兑换成功',
    REDEMPTION_DELIVERED: '兑换发放通知',
    REDEMPTION_CREATED_OPERATOR: '有新的兑换记录',
    ADMIN_APPLICATION_SUBMITTED: '有新的管理员申请',
    ADMIN_APPLICATION_APPROVED: '管理员申请通过',
    ADMIN_APPLICATION_REJECTED: '管理员申请驳回',
    ADMIN_APPLICATION_REVIEW_PROCESSED: '管理员申请处理回执'
  }
  return map[type] || type || '系统消息'
}

function toNotificationStatusLabel(status) {
  if (status === 'SENT') {
    return '已发送'
  }
  if (status === 'FAILED') {
    return '发送失败'
  }
  if (status === 'PENDING') {
    return '待发送'
  }
  return status || '未知状态'
}

function formatNotification(item) {
  return {
    ...item,
    displayType: toNotificationTypeLabel(item.notificationType),
    displayStatus: toNotificationStatusLabel(item.sendStatus),
    displayCreatedAt: formatDateTime(item.createdAt),
    displaySentAt: formatDateTime(item.sentAt, '未发送')
  }
}

module.exports = {
  formatActivity,
  formatAdminApplication,
  formatDateTime,
  formatNotification,
  formatParticipationRecord,
  formatProfile,
  formatRedemption,
  formatStarLeaderboardItem,
  formatCommunityInsightItem,
  formatSupplement,
  maskIdCardNo,
  maskPhoneNo,
  maskRealName,
  maskVolunteerCardNo,
  toActivityStatusLabel,
  toDemandLabel,
  toDifficultyLabel,
  toNotificationStatusLabel,
  toNotificationTypeLabel,
  toParticipationStatusLabel,
  toRoleLabel,
  toSupplementStatusLabel,
  toSupplementTypeLabel,
  toTrackPointTypeLabel
}
