const app = getApp()
const { request } = require('../../utils/request')
const { requestNotificationSubscription } = require('../../utils/subscribe')

function formatDateTime(value, fallback = '暂无') {
  if (!value) {
    return fallback
  }
  return String(value).replace('T', ' ').replace(/\.\d+$/, '')
}

function buildDefaultSettings() {
  return {
    systemName: '玉屏志愿平台',
    defaultGeofenceRadiusMeters: 300,
    certificateTitle: '志愿服务电子证书',
    supplementApproveComment: '审核通过，系统已自动补签。',
    adminWebUrl: ''
  }
}

const ACTIVITY_CATEGORIES = [
  '社区服务',
  '环保行动',
  '助老服务',
  '助残帮扶',
  '公益宣传',
  '赛事保障',
  '应急服务',
  '教育支持',
  '医疗协助',
  '综合服务'
]

const STORE_CATEGORIES = [
  '日常用品',
  '虚拟权益',
  '公益权益',
  '学习成长',
  '文创周边',
  '实体礼品'
]

function formatDate(date) {
  const year = date.getFullYear()
  const month = `${date.getMonth() + 1}`.padStart(2, '0')
  const day = `${date.getDate()}`.padStart(2, '0')
  const hour = `${date.getHours()}`.padStart(2, '0')
  const minute = `${date.getMinutes()}`.padStart(2, '0')
  const second = `${date.getSeconds()}`.padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

function buildDefaultForm(systemSettings = buildDefaultSettings()) {
  const start = new Date()
  start.setDate(start.getDate() + 1)
  start.setHours(9, 0, 0, 0)

  const end = new Date(start.getTime())
  end.setHours(12, 0, 0, 0)

  return {
    title: '',
    description: '',
    location: '',
    category: '社区服务',
    difficultyLevel: '3',
    difficultyCoefficient: '1.15',
    demandLevel: '3',
    serviceSiteId: '',
    latitude: '30.5728',
    longitude: '104.0668',
    startTime: formatDate(start),
    endTime: formatDate(end),
    capacity: '20',
    geofenceRadiusMeters: String(systemSettings.defaultGeofenceRadiusMeters || 300)
  }
}

function resolveCategoryIndex(categories = [], value = '') {
  const index = categories.findIndex((item) => item === value)
  return index >= 0 ? index : 0
}

function buildDefaultStoreForm() {
  return {
    id: '',
    name: '',
    category: STORE_CATEGORIES[0],
    description: '',
    pointsCost: '0',
    stock: '0',
    active: true,
    deliveryType: 'VIRTUAL'
  }
}

function buildPublishMapPreview(form = {}) {
  const latitude = Number(form.latitude)
  const longitude = Number(form.longitude)
  const radius = Math.max(50, Number(form.geofenceRadiusMeters) || 300)

  if (!form.location || !Number.isFinite(latitude) || !Number.isFinite(longitude)) {
    return {
      hasPublishMapPreview: false,
      publishMapLatitude: 0,
      publishMapLongitude: 0,
      publishMapScale: 16,
      publishMapCircles: [],
      publishMapIncludePoints: [],
      publishRadiusHint: '请先搜索地点或选择服务站点'
    }
  }

  let publishRadiusHint = '适合广场、院落等较开阔的集中签到场景'
  if (radius <= 120) {
    publishRadiusHint = '适合室内站点、服务大厅、楼宇门口等小范围签到场景'
  } else if (radius <= 250) {
    publishRadiusHint = '适合社区站点、养老院、学校门口等常规签到场景'
  } else if (radius <= 450) {
    publishRadiusHint = '适合小区广场、社区院落等较大范围签到场景'
  } else if (radius <= 700) {
    publishRadiusHint = '范围较大，适合街道集合点或分散式起点签到场景'
  } else {
    publishRadiusHint = '当前范围较大，建议确认是否需要拆分活动或缩小签到区域'
  }

  return {
    hasPublishMapPreview: true,
    publishMapLatitude: latitude,
    publishMapLongitude: longitude,
    publishMapScale: radius > 600 ? 13 : radius > 300 ? 14 : 15,
    publishMapCircles: [
      {
        latitude,
        longitude,
        color: '#16a34a',
        fillColor: 'rgba(22, 163, 74, 0.30)',
        radius: 10,
        strokeWidth: 4
      },
      {
        latitude,
        longitude,
        color: '#2563eb',
        fillColor: 'rgba(37, 99, 235, 0.16)',
        radius,
        strokeWidth: 2
      }
    ],
    publishMapIncludePoints: [
      {
        latitude,
        longitude
      }
    ],
    publishRadiusHint
  }
}

function toLocalDateTime(value) {
  return String(value || '').replace(' ', 'T')
}

function toRoleLabel(role) {
  if (role === 'SUPER_ADMIN') {
    return '超级管理员'
  }
  if (role === 'ADMIN') {
    return '活动管理员'
  }
  return '志愿者'
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
  return status || '未知'
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

function formatProfile(profile) {
  const idCardNo = profile.idCardNo || ''
  const volunteerCardNo = profile.volunteerCardNo || ''
  const maskRealName = (value) => {
    if (!value) {
      return '未认证'
    }
    return `${String(value).slice(0, 1)}*`
  }
  const maskIdCard = (value) => {
    if (!value) {
      return '未填写'
    }
    if (value.length <= 5) {
      return value
    }
    return `${value.slice(0, 3)}${'*'.repeat(Math.max(1, value.length - 5))}${value.slice(-2)}`
  }
  const maskVolunteerCard = (value) => {
    if (!value) {
      return '未填写'
    }
    if (value.length <= 6) {
      return value
    }
    return `${value.slice(0, 3)}${'*'.repeat(Math.max(1, value.length - 6))}${value.slice(-3)}`
  }
  return {
    ...profile,
    displayRole: toRoleLabel(profile.role),
    displayVerifiedStatus: profile.verified ? '已认证' : '未认证',
    displayRealName: maskRealName(profile.realName),
    maskedIdCardNo: maskIdCard(idCardNo),
    maskedVolunteerCardNo: maskVolunteerCard(volunteerCardNo)
  }
}

function buildCheckInQrValue(activityId, checkInCode) {
  if (!activityId || !checkInCode) {
    return ''
  }
  return `volunteer-checkin://check-in?activityId=${activityId}&checkInCode=${encodeURIComponent(checkInCode)}`
}

function buildServiceSiteLocation(site) {
  return site.address || site.name || '未设置地址'
}

function buildServiceSiteDetail(site) {
  return `${site.streetName || '未设置街道'} / ${site.communityName || '未设置社区'} / 推荐半径 ${site.recommendedRadiusMeters || 300} 米`
}

function formatServiceSite(site) {
  return {
    ...site,
    displayName: `${site.name}${site.communityName ? ` · ${site.communityName}` : ''}${site.streetName ? ` · ${site.streetName}` : ''}`,
    displayLocation: buildServiceSiteLocation(site),
    displayDetail: buildServiceSiteDetail(site)
  }
}

function buildServiceSiteOptions(serviceSites = []) {
  return [
    {
      id: '',
      displayName: '不使用站点，直接搜索地点',
      displayLocation: '',
      displayDetail: '适合临时活动或站点库中尚未维护的位置'
    },
    ...serviceSites.filter((item) => item.enabled).map(formatServiceSite)
  ]
}

function findServiceSitePickerIndex(serviceSites = [], serviceSiteId = '') {
  const index = serviceSites.findIndex((item) => String(item.id) === String(serviceSiteId || ''))
  return index >= 0 ? index : 0
}

function applyServiceSiteToForm(form, site) {
  return {
    ...form,
    serviceSiteId: String(site.id),
    location: site.displayLocation,
    latitude: String(site.latitude),
    longitude: String(site.longitude),
    geofenceRadiusMeters: String(site.recommendedRadiusMeters || form.geofenceRadiusMeters || 300)
  }
}

function fillPublishLocation(form, location) {
  return {
    ...form,
    serviceSiteId: '',
    location: location.title || location.name || location.address || form.location,
    latitude: String(location.latitude),
    longitude: String(location.longitude)
  }
}

function searchPublishLocations(keyword, center = { latitude: 30.5728, longitude: 104.0668 }) {
  return request({
    url: '/workflow/admin/map/search',
    data: {
      keyword,
      latitude: Number(center.latitude) || 30.5728,
      longitude: Number(center.longitude) || 104.0668
    }
  }).then((results) => results || [])
}

function formatActivity(activity) {
  return {
    ...activity,
    displayCategory: activity.category || '综合服务',
    displayDifficulty: toDifficultyLabel(activity.difficultyLevel),
    displayDemand: toDemandLabel(activity.demandLevel),
    displayStartTime: formatDateTime(activity.startTime),
    displayEndTime: formatDateTime(activity.endTime),
    qrValue: buildCheckInQrValue(activity.id, activity.checkInCode)
  }
}

function formatParticipationRecord(record) {
  return {
    ...record,
    recordKey: record.recordId || record.id,
    canExportProof: record.status === 'COMPLETED' && Boolean(record.organizerConfirmed) && Boolean(record.rewardFinalizedAt),
    displayStatus: toParticipationStatusLabel(record.status),
    displayCreatedAt: formatDateTime(record.createdAt, '暂无'),
    displayCheckInTime: formatDateTime(record.checkInTime, '未签到'),
    displayCheckOutTime: formatDateTime(record.checkOutTime, '未签退'),
    displayConfirmedAt: formatDateTime(record.organizerConfirmedAt, '待确认'),
    displayConfirmStatus: record.organizerConfirmed ? '已确认' : '待确认',
    displayRewardFinalizedAt: formatDateTime(record.rewardFinalizedAt, '待结算'),
    displayRewardStatus: record.rewardPending ? '待评价结算' : '已结算',
    displayServiceRating: record.serviceRating ? `${record.serviceRating} 分` : '待评价',
    displayActivityCategory: record.activityCategory || '综合服务'
  }
}

function formatSupplement(item) {
  return {
    ...item,
    displayType: item.type === 'CHECK_OUT' ? '签退补签' : '签到补签',
    displayRequestedTime: formatDateTime(item.requestedTime)
  }
}

function formatSnapshot(item) {
  return {
    ...item,
    displayCreatedAt: formatDateTime(item.createdAt),
    displayNote: item.note || '未填写说明'
  }
}

function formatTrackPoint(item) {
  let displayPointType = '轨迹点'
  if (item.pointType === 'START') {
    displayPointType = '起点轨迹'
  } else if (item.pointType === 'MIDDLE') {
    displayPointType = '中途轨迹'
  } else if (item.pointType === 'END') {
    displayPointType = '终点轨迹'
  }
  return {
    ...item,
    displayPointType,
    displayRecordedAt: formatDateTime(item.recordedAt)
  }
}

function formatBlockchainEventType(eventType) {
  if (eventType === 'CHECK_IN') {
    return '签到存证'
  }
  if (eventType === 'CHECK_OUT') {
    return '签退存证'
  }
  if (eventType === 'SNAPSHOT_UPLOAD') {
    return '快照存证'
  }
  if (eventType === 'TRACK_SUMMARY') {
    return '轨迹汇总存证'
  }
  if (eventType === 'ORGANIZER_CONFIRM') {
    return '组织者确认存证'
  }
  if (eventType === 'REWARD_FINALIZE') {
    return '评价结算存证'
  }
  return eventType || '存证事件'
}

function formatBlockchainProofStatus(status) {
  if (status === 'MOCK_CHAINED') {
    return '已上链（模拟）'
  }
  if (status === 'FAILED') {
    return '存证失败'
  }
  if (status === 'PENDING') {
    return '待存证'
  }
  return status || '未知状态'
}

function formatBlockchainProof(item) {
  return {
    ...item,
    displayEventType: formatBlockchainEventType(item.eventType),
    displayProofStatus: formatBlockchainProofStatus(item.proofStatus),
    displayEventTime: formatDateTime(item.eventTime),
    displayAnchoredAt: formatDateTime(item.anchoredAt, '待写入'),
    displayTransactionNo: item.transactionNo || '尚未生成',
    displayPayloadSummary: item.payloadSummary || '暂无摘要',
    displayFailureReason: item.failureReason || '',
    shortEvidenceHash: item.evidenceHash ? `${item.evidenceHash.slice(0, 12)}...` : '暂无哈希'
  }
}

function formatAreaStat(item) {
  return {
    ...item,
    displayValue: item && item.value != null ? item.value : 0
  }
}

function formatTrend(item) {
  return {
    ...item,
    displayValue: Number(item && item.value != null ? item.value : 0)
  }
}

function formatAdminLeaderboard(item) {
  return {
    ...item,
    displayServiceHours: Number(item.totalServiceHours || 0).toFixed(1)
  }
}

function formatAnalyticsHeatmapItem(item) {
  return {
    ...item,
    displayIntensity: item.intensity || 0,
    displayGapCount: item.gapCount || 0,
    displayDemandLevel: item.demandLevel || 0,
    displayLocation: item.location || '未标注地点',
    displayCategory: item.category || '综合服务',
    displayAreaTag: item.areaTag || '常规区域'
  }
}

function formatStarLeaderboardItem(item) {
  return {
    ...item,
    displayPeriod: item.period === 'month' ? '月榜' : '周榜',
    displayStarScore: Number(item.starScore || 0).toFixed(1),
    displayPositiveRatingRate: `${Number(item.positiveRatingRate || 0).toFixed(1)}%`,
    displayTrackCoverageScore: Number(item.trackCoverageScore || 0).toFixed(1),
    displayTotalHours: Number(item.totalHours || 0).toFixed(1),
    displayShowcaseText: item.showcaseText || '持续组织与参与社区服务，整体表现稳定。'
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

function maskPhone(value) {
  const raw = String(value || '').trim()
  if (!raw || raw.length <= 7) {
    return raw
  }
  return `${raw.slice(0, 3)}${'*'.repeat(Math.max(1, raw.length - 7))}${raw.slice(-4)}`
}

function formatStoreItem(item) {
  return {
    ...item,
    displayDeliveryType: item.deliveryType === 'PHYSICAL' ? '实体商品' : '虚拟商品'
  }
}

function formatAdminRedemption(item) {
  return {
    ...item,
    displayStatus: item.status === 'DELIVERED'
      ? '已发放'
      : item.status === 'CANCELLED'
        ? '已取消'
        : '待处理',
    displayCreatedAt: formatDateTime(item.createdAt),
    displayDeliveryType: item.deliveryType === 'PHYSICAL' ? '实体商品' : '虚拟商品',
    displayRecipientPhone: maskPhone(item.maskedRecipientPhone || '')
  }
}

function toTimestamp(value) {
  if (!value) {
    return 0
  }
  const timestamp = new Date(String(value).replace(' ', 'T')).getTime()
  return Number.isFinite(timestamp) ? timestamp : 0
}

function calculateDistanceMeters(first, second) {
  if (!first || !second) {
    return 0
  }
  const earthRadius = 6371000
  const toRadians = (degree) => (Number(degree) * Math.PI) / 180
  const lat1 = toRadians(first.latitude)
  const lng1 = toRadians(first.longitude)
  const lat2 = toRadians(second.latitude)
  const lng2 = toRadians(second.longitude)
  const deltaLat = lat2 - lat1
  const deltaLng = lng2 - lng1
  const a =
    Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
    Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2)
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  return earthRadius * c
}

function buildTrackMap(trackPoints = []) {
  const sorted = [...trackPoints]
    .filter((item) => item && item.latitude != null && item.longitude != null)
    .sort((left, right) => toTimestamp(left.recordedAt) - toTimestamp(right.recordedAt))

  if (!sorted.length) {
    return {
      hasTrackMap: false,
      trackPointCount: 0,
      adminMapLatitude: 0,
      adminMapLongitude: 0,
      adminMapScale: 15,
      adminMapPolyline: [],
      adminMapCircles: [],
      adminMapIncludePoints: []
    }
  }

  const points = sorted.map((item) => ({
    latitude: Number(item.latitude),
    longitude: Number(item.longitude)
  }))
  const start = points[0]
  const end = points[points.length - 1]
  const stayCircles = []

  for (let index = 1; index < sorted.length - 1; index += 1) {
    const current = sorted[index]
    const previous = sorted[index - 1]
    if (calculateDistanceMeters(previous, current) < 30) {
      stayCircles.push({
        latitude: Number(current.latitude),
        longitude: Number(current.longitude),
        color: '#f59e0b',
        fillColor: 'rgba(245, 158, 11, 0.26)',
        radius: 12,
        strokeWidth: 2
      })
    }
  }

  return {
    hasTrackMap: true,
    trackPointCount: sorted.length,
    adminMapLatitude: start.latitude,
    adminMapLongitude: start.longitude,
    adminMapScale: 15,
    adminMapPolyline: [
      {
        points,
        color: '#2563eb',
        width: 8,
        arrowLine: true,
        borderWidth: 2,
        borderColor: '#93c5fd'
      }
    ],
    adminMapCircles: [
      {
        latitude: start.latitude,
        longitude: start.longitude,
        color: '#16a34a',
        fillColor: 'rgba(22, 163, 74, 0.28)',
        radius: 18,
        strokeWidth: 3
      },
      {
        latitude: end.latitude,
        longitude: end.longitude,
        color: '#ef4444',
        fillColor: 'rgba(239, 68, 68, 0.28)',
        radius: 18,
        strokeWidth: 3
      },
      ...stayCircles
    ],
    adminMapIncludePoints: points
  }
}

const RATING_OPTIONS = [
  {
    label: '5分·优秀',
    score: 5,
    comment: '服务过程完整，表现优秀，建议优先激励。'
  },
  {
    label: '4分·良好',
    score: 4,
    comment: '服务过程规范，完成质量良好。'
  },
  {
    label: '3分·合格',
    score: 3,
    comment: '已完成服务任务，整体表现合格。'
  }
]

Page({
  data: {
    profile: null,
    isSuperAdmin: false,
    canParticipate: false,
    activities: [],
    selectedActivityId: null,
    selectedActivityTitle: '',
    records: [],
    pendingEnrollments: [],
    adminTrackPoints: [],
    hasTrackMap: false,
    trackPointCount: 0,
    adminMapLatitude: 0,
    adminMapLongitude: 0,
    adminMapScale: 15,
    adminMapPolyline: [],
    adminMapCircles: [],
    adminMapIncludePoints: [],
    pendingSupplements: [],
    evidenceActivityId: null,
    evidenceActivityTitle: '',
    evidencePickerIndex: 0,
    evidenceRecords: [],
    evidenceSnapshots: [],
    evidenceTrackPoints: [],
    evidenceBlockchainProofs: [],
    hasEvidenceTrackMap: false,
    evidenceTrackPointCount: 0,
    evidenceMapLatitude: 0,
    evidenceMapLongitude: 0,
    evidenceMapScale: 15,
    evidenceMapPolyline: [],
    evidenceMapCircles: [],
    evidenceMapIncludePoints: [],
    systemSettings: buildDefaultSettings(),
    form: buildDefaultForm(),
    activityCategories: ACTIVITY_CATEGORIES,
    activityCategoryIndex: 0,
    storeCategories: STORE_CATEGORIES,
    storeCategoryIndex: 0,
    serviceSites: buildServiceSiteOptions(),
    serviceSitePickerIndex: 0,
    selectedServiceSiteDetail: '可直接选择已维护的服务站点，自动带出地点与推荐签到范围。',
    publishSearchKeyword: '',
    publishSearchAttempted: false,
    publishSearchLoading: false,
    publishSearchResults: [],
    hasPublishMapPreview: true,
    publishMapLatitude: 30.5728,
    publishMapLongitude: 104.0668,
    publishMapScale: 15,
    publishMapCircles: [],
    publishMapIncludePoints: [],
    publishRadiusHint: '',
    analyticsYear: new Date().getFullYear(),
    adminAnnualReport: null,
    adminLeaderboard: [],
    adminCommunityHeatmap: [],
    adminActivityHeatmap: [],
    adminStarLeaderboard: [],
    adminCommunityInsights: [],
    insightPeriod: 'week',
    platformAnnualOverview: null,
    storeItems: [],
    redemptionRecords: [],
    storeForm: buildDefaultStoreForm(),
    currentTab: 'activities'
  },

  onShow() {
    if (!app.requireLogin({
      content: '进入管理端前，请先完成登录或注册。'
    })) {
      return
    }
    this.subscriptionRequestedTabs = {}
    this.loadPageData()
  },

  onTabChange(event) {
    const nextTab = event.detail.value
    this.setData({
      currentTab: nextTab
    })
    this.requestTabSubscription(nextTab)
    if (nextTab === 'leaderboard' || nextTab === 'community') {
      this.loadAnalytics()
    }
    if (nextTab === 'evidence') {
      this.ensureEvidenceLoaded()
    }
  },

  switchTab(event) {
    const { tab } = event.currentTarget.dataset
    if (!tab) {
      return
    }
    this.setData({
      currentTab: tab
    })
    this.requestTabSubscription(tab)
    if (tab === 'analytics' || tab === 'leaderboard' || tab === 'community') {
      this.loadAnalytics()
    }
    if (tab === 'evidence') {
      this.ensureEvidenceLoaded()
    }
  },

  requestTabSubscription(tab) {
    const subscriptionMap = {
      enrollments: ['ENROLLMENT_PENDING_REVIEW'],
      supplements: ['SUPPLEMENT_PENDING_REVIEW'],
      records: ['SERVICE_CONFIRM_PENDING']
    }
    const notificationTypes = subscriptionMap[tab]
    if (!notificationTypes || !notificationTypes.length) {
      return
    }
    if (!this.subscriptionRequestedTabs) {
      this.subscriptionRequestedTabs = {}
    }
    if (this.subscriptionRequestedTabs[tab]) {
      return
    }
    this.subscriptionRequestedTabs[tab] = true
    requestNotificationSubscription(notificationTypes)
  },

  loadPageData() {
    request({ url: '/auth/me' })
      .then((profile) => {
        if (profile.role !== 'ADMIN' && profile.role !== 'SUPER_ADMIN') {
          throw new Error('当前账号没有活动管理权限')
        }
        app.setProfile(profile)
        this.setData({
          profile: formatProfile(profile),
          isSuperAdmin: profile.role === 'SUPER_ADMIN',
          canParticipate: profile.role === 'ADMIN'
        })
        return Promise.all([
          request({ url: '/activities' }),
          request({ url: '/activities/pending-enrollments' }),
          request({ url: '/workflow/admin/supplements/pending' }),
          request({ url: '/admin/system-settings' }),
          request({ url: '/admin/service-sites' }),
          request({ url: '/store/admin/items' }),
          request({ url: '/store/admin/redemptions' })
        ])
      })
      .then(([activities, pendingEnrollments, pendingSupplements, systemSettings, serviceSites, storeItems, redemptionRecords]) => {
        const nextSettings = systemSettings || buildDefaultSettings()
        const nextForm = buildDefaultForm(nextSettings)
        const siteOptions = buildServiceSiteOptions(serviceSites || [])
        this.setData({
          activities: (activities || []).map(formatActivity),
          pendingEnrollments: (pendingEnrollments || []).map(formatParticipationRecord),
          pendingSupplements: (pendingSupplements || []).map(formatSupplement),
          systemSettings: nextSettings,
          form: nextForm,
          activityCategoryIndex: resolveCategoryIndex(ACTIVITY_CATEGORIES, nextForm.category),
          serviceSites: siteOptions,
          serviceSitePickerIndex: findServiceSitePickerIndex(siteOptions, nextForm.serviceSiteId),
          selectedServiceSiteDetail: siteOptions[0].displayDetail,
          publishSearchKeyword: '',
          publishSearchAttempted: false,
          publishSearchLoading: false,
          publishSearchResults: [],
          evidencePickerIndex: 0,
          evidenceActivityId: null,
          evidenceActivityTitle: '',
          evidenceRecords: [],
          evidenceSnapshots: [],
          evidenceTrackPoints: [],
          evidenceBlockchainProofs: [],
          adminAnnualReport: null,
          adminLeaderboard: [],
          adminCommunityHeatmap: [],
          adminActivityHeatmap: [],
          adminStarLeaderboard: [],
          adminCommunityInsights: [],
          platformAnnualOverview: null,
          storeItems: (storeItems || []).map(formatStoreItem),
          redemptionRecords: (redemptionRecords || []).map(formatAdminRedemption),
          storeCategoryIndex: resolveCategoryIndex(STORE_CATEGORIES, this.data.storeForm.category),
          ...buildPublishMapPreview(nextForm)
        })
        if (this.data.currentTab === 'analytics' || this.data.currentTab === 'leaderboard' || this.data.currentTab === 'community') {
          this.loadAnalytics()
        }
        if (this.data.currentTab === 'evidence') {
          this.ensureEvidenceLoaded()
        }
      })
      .catch((error) => {
        wx.showToast({
          title: error.message || '加载管理页失败',
          icon: 'none'
        })
        setTimeout(() => {
          wx.reLaunch({
            url: '/pages/index/index'
          })
        }, 400)
      })
  },

  onInput(event) {
    const field = event.currentTarget.dataset.field
    const nextForm = {
      ...this.data.form,
      [field]: event.detail.value
    }
    this.setData({
      [`form.${field}`]: event.detail.value,
      ...buildPublishMapPreview(nextForm)
    })
  },

  onPublishSearchInput(event) {
    this.setData({
      publishSearchKeyword: event.detail.value,
      publishSearchAttempted: false
    })
  },

  onActivityCategoryChange(event) {
    const index = Number(event.detail.value) || 0
    const category = ACTIVITY_CATEGORIES[index] || ACTIVITY_CATEGORIES[0]
    const nextForm = {
      ...this.data.form,
      category
    }
    this.setData({
      'form.category': category,
      activityCategoryIndex: index,
      ...buildPublishMapPreview(nextForm)
    })
  },

  onStoreFormInput(event) {
    const field = event.currentTarget.dataset.field
    this.setData({
      [`storeForm.${field}`]: event.detail.value
    })
  },

  onStoreCategoryChange(event) {
    const index = Number(event.detail.value) || 0
    const category = STORE_CATEGORIES[index] || STORE_CATEGORIES[0]
    this.setData({
      'storeForm.category': category,
      storeCategoryIndex: index
    })
  },

  onStoreTypeChange(event) {
    const value = Number(event.detail.value) === 1 ? 'PHYSICAL' : 'VIRTUAL'
    this.setData({
      'storeForm.deliveryType': value
    })
  },

  toggleStoreActive() {
    this.setData({
      'storeForm.active': !this.data.storeForm.active
    })
  },

  resetStoreForm() {
    this.setData({
      storeForm: buildDefaultStoreForm(),
      storeCategoryIndex: 0
    })
  },

  editStoreItem(event) {
    const { index } = event.currentTarget.dataset
    const item = (this.data.storeItems || [])[index]
    if (!item) {
      return
    }
    this.setData({
      currentTab: 'store',
      storeForm: {
        id: item.id,
        name: item.name || '',
        category: item.category || STORE_CATEGORIES[0],
        description: item.description || '',
        pointsCost: String(item.pointsCost || 0),
        stock: String(item.stock || 0),
        active: item.active !== false,
        deliveryType: item.deliveryType || 'VIRTUAL'
      },
      storeCategoryIndex: resolveCategoryIndex(STORE_CATEGORIES, item.category || STORE_CATEGORIES[0])
    })
  },

  saveStoreItem() {
    const form = this.data.storeForm
    request({
      url: form.id ? `/store/admin/items/${form.id}` : '/store/admin/items',
      method: 'POST',
      data: {
        name: form.name,
        category: form.category,
        description: form.description,
        pointsCost: Number(form.pointsCost || 0),
        stock: Number(form.stock || 0),
        active: !!form.active,
        deliveryType: form.deliveryType || 'VIRTUAL'
      }
    }).then(() => {
      wx.showToast({
        title: form.id ? '商品已更新' : '商品已新增',
        icon: 'success'
      })
      this.resetStoreForm()
      this.loadPageData()
    }).catch((error) => {
      wx.showToast({
        title: error.message || '保存商品失败',
        icon: 'none'
      })
    })
  },

  deliverRedemption(event) {
    const { id } = event.currentTarget.dataset
    wx.showModal({
      title: '发放备注',
      editable: true,
      placeholderText: '可填写虚拟权益说明或寄出备注',
      success: (res) => {
        if (!res.confirm) {
          return
        }
        request({
          url: `/store/admin/redemptions/${id}/status`,
          method: 'POST',
          data: {
            status: 'DELIVERED',
            deliveryRemark: res.content || ''
          }
        }).then(() => {
          wx.showToast({
            title: '已标记发放',
            icon: 'success'
          })
          requestNotificationSubscription(['REDEMPTION_DELIVERED'])
          this.loadPageData()
        }).catch((error) => {
          wx.showToast({
            title: error.message || '发放失败',
            icon: 'none'
          })
        })
      }
    })
  },

  cancelRedemption(event) {
    const { id } = event.currentTarget.dataset
    request({
      url: `/store/admin/redemptions/${id}/status`,
      method: 'POST',
      data: {
        status: 'CANCELLED',
        deliveryRemark: '管理员取消兑换'
      }
    }).then(() => {
      wx.showToast({
        title: '已取消兑换',
        icon: 'success'
      })
      this.loadPageData()
    }).catch((error) => {
      wx.showToast({
        title: error.message || '取消失败',
        icon: 'none'
      })
    })
  },

  triggerPublishSearch() {
    const keyword = (this.data.publishSearchKeyword || '').trim()
    if (keyword.length < 2) {
      this.setData({
        publishSearchAttempted: false,
        publishSearchLoading: false,
        publishSearchResults: []
      })
      wx.showToast({
        title: '请至少输入两个字',
        icon: 'none'
      })
      return
    }

    this.setData({
      publishSearchAttempted: true,
      publishSearchLoading: true
    })

    searchPublishLocations(keyword, {
      latitude: this.data.form.latitude,
      longitude: this.data.form.longitude
    })
      .then((results) => {
        this.setData({
          publishSearchAttempted: true,
          publishSearchLoading: false,
          publishSearchResults: results
        })
        if (!results.length) {
          wx.showToast({
            title: '未搜索到地点，请换更完整的地址',
            icon: 'none'
          })
        }
      })
      .catch((error) => {
        this.setData({
          publishSearchAttempted: true,
          publishSearchLoading: false,
          publishSearchResults: []
        })
        wx.showToast({
          title: error.message || '地点搜索失败',
          icon: 'none'
        })
      })
  },

  onAnalyticsYearInput(event) {
    this.setData({
      analyticsYear: Number(event.detail.value) || new Date().getFullYear()
    })
  },

  refreshAnalytics() {
    this.loadAnalytics()
  },

  switchInsightPeriod(event) {
    const { period } = event.currentTarget.dataset
    if (!period || period === this.data.insightPeriod) {
      return
    }
    this.setData({
      insightPeriod: period
    })
    this.loadAnalytics()
  },

  loadAnalytics() {
    const year = this.data.analyticsYear || new Date().getFullYear()
    const insightPeriod = this.data.insightPeriod || 'week'
    const requests = [
      request({ url: `/analytics/admin-self/annual-report?year=${year}` }),
      request({ url: `/analytics/admin-self/leaderboard?year=${year}` }),
      request({ url: `/analytics/admin-self/community-heatmap?year=${year}` }),
      request({ url: `/analytics/admin-self/activity-heatmap?year=${year}` }),
      request({ url: `/analytics/admin-self/star-leaderboard?period=${insightPeriod}` }),
      request({ url: `/analytics/admin-self/community-insights?period=${insightPeriod}` })
    ]
    if (this.data.isSuperAdmin) {
      requests.push(request({ url: `/analytics/admin/annual-overview?year=${year}` }))
    }

    Promise.all(requests)
      .then((payload) => {
        const [adminAnnualReport, adminLeaderboard, adminCommunityHeatmap, adminActivityHeatmap, adminStarLeaderboard, adminCommunityInsights, platformAnnualOverview] = payload
        this.setData({
          adminAnnualReport: adminAnnualReport
            ? {
              ...adminAnnualReport,
              coveredCommunities: (adminAnnualReport.coveredCommunities || []).map(formatAreaStat),
              categoryStats: (adminAnnualReport.categoryStats || []).map(formatAreaStat),
              monthlyActivityTrends: (adminAnnualReport.monthlyActivityTrends || []).map(formatTrend),
              monthlyServiceHourTrends: (adminAnnualReport.monthlyServiceHourTrends || []).map(formatTrend),
              communityHeatmap: (adminAnnualReport.communityHeatmap || []).map(formatAnalyticsHeatmapItem),
              activityHeatmap: (adminAnnualReport.activityHeatmap || []).map(formatAnalyticsHeatmapItem)
            }
            : null,
          adminLeaderboard: (adminLeaderboard || []).map(formatAdminLeaderboard),
          adminCommunityHeatmap: (adminCommunityHeatmap || []).map(formatAnalyticsHeatmapItem),
          adminActivityHeatmap: (adminActivityHeatmap || []).map(formatAnalyticsHeatmapItem),
          adminStarLeaderboard: (adminStarLeaderboard || []).map(formatStarLeaderboardItem),
          adminCommunityInsights: (adminCommunityInsights || []).map(formatCommunityInsightItem),
          platformAnnualOverview: platformAnnualOverview
            ? {
              ...platformAnnualOverview,
              hotAreas: (platformAnnualOverview.hotAreas || []).map(formatAreaStat),
              categoryStats: (platformAnnualOverview.categoryStats || []).map(formatAreaStat),
              monthlyActivityTrends: (platformAnnualOverview.monthlyActivityTrends || []).map(formatTrend),
              monthlyServiceHourTrends: (platformAnnualOverview.monthlyServiceHourTrends || []).map(formatTrend),
              communityHeatmap: (platformAnnualOverview.communityHeatmap || []).map(formatAnalyticsHeatmapItem),
              activityHeatmap: (platformAnnualOverview.activityHeatmap || []).map(formatAnalyticsHeatmapItem),
              adminLeaderboard: (platformAnnualOverview.adminLeaderboard || []).map(formatAdminLeaderboard)
            }
            : null
        })
      })
      .catch((error) => {
        wx.showToast({
          title: error.message || '加载分析数据失败',
          icon: 'none'
        })
      })
  },

  onServiceSiteChange(event) {
    const index = Number(event.detail.value)
    const target = this.data.serviceSites[index]
    if (!target) {
      return
    }
    if (!target.id) {
      const nextForm = {
        ...this.data.form,
        serviceSiteId: ''
      }
      this.setData({
        form: nextForm,
        serviceSitePickerIndex: 0,
        selectedServiceSiteDetail: target.displayDetail,
        ...buildPublishMapPreview(nextForm)
      })
      return
    }
    const nextForm = applyServiceSiteToForm(this.data.form, target)
    this.setData({
      form: nextForm,
      serviceSitePickerIndex: index,
      selectedServiceSiteDetail: target.displayDetail,
      publishSearchKeyword: target.name || target.displayName,
      publishSearchAttempted: false,
      publishSearchResults: [],
      publishSearchLoading: false,
      ...buildPublishMapPreview(nextForm)
    })
  },

  choosePublishSuggestion(event) {
    const { index } = event.currentTarget.dataset
    const target = this.data.publishSearchResults[index]
    if (!target) {
      return
    }
    const nextForm = fillPublishLocation(this.data.form, target)
    this.setData({
      form: nextForm,
      serviceSitePickerIndex: 0,
      selectedServiceSiteDetail: this.data.serviceSites[0].displayDetail,
      publishSearchKeyword: target.title,
      publishSearchAttempted: false,
      publishSearchResults: [],
      publishSearchLoading: false,
      ...buildPublishMapPreview(nextForm)
    })
  },

  onRadiusChange(event) {
    const value = String(event.detail.value)
    const nextForm = {
      ...this.data.form,
      geofenceRadiusMeters: value
    }
    this.setData({
      'form.geofenceRadiusMeters': value,
      ...buildPublishMapPreview(nextForm)
    })
  },

  createActivity() {
    const form = this.data.form
    request({
      url: '/activities',
      method: 'POST',
      data: {
        title: form.title,
        description: form.description,
        location: form.location,
        category: form.category,
        difficultyLevel: Number(form.difficultyLevel),
        difficultyCoefficient: Number(form.difficultyCoefficient),
        demandLevel: Number(form.demandLevel),
        serviceSiteId: form.serviceSiteId ? Number(form.serviceSiteId) : null,
        latitude: Number(form.latitude),
        longitude: Number(form.longitude),
        startTime: toLocalDateTime(form.startTime),
        endTime: toLocalDateTime(form.endTime),
        capacity: Number(form.capacity),
        geofenceRadiusMeters: Number(form.geofenceRadiusMeters)
      }
    }).then(() => {
      const nextForm = buildDefaultForm(this.data.systemSettings)
      wx.showToast({
        title: '活动创建成功',
        icon: 'success'
      })
      this.setData({
        form: nextForm,
        activityCategoryIndex: resolveCategoryIndex(ACTIVITY_CATEGORIES, nextForm.category),
        publishSearchKeyword: '',
        publishSearchAttempted: false,
        publishSearchLoading: false,
        publishSearchResults: [],
        ...buildPublishMapPreview(nextForm),
        currentTab: 'activities'
      })
      this.loadPageData()
    }).catch((error) => {
      wx.showToast({
        title: error.message || '活动创建失败',
        icon: 'none'
      })
    })
  },

  refreshCode(event) {
    const { id } = event.currentTarget.dataset
    request({
      url: `/activities/${id}/refresh-code`,
      method: 'POST'
    }).then((data) => {
      wx.showModal({
        title: '动态二维码已刷新',
        content: `新的签到码为：${data.checkInCode}\n二维码会同步更新，志愿者可直接扫码签到。`,
        showCancel: false
      })
      this.loadPageData()
    }).catch((error) => {
      wx.showToast({
        title: error.message || '刷新签到码失败',
        icon: 'none'
      })
    })
  },

  loadRecords(event) {
    const { id } = event.currentTarget.dataset
    const selectedActivity = (this.data.activities || []).find((item) => item.id === id)
    Promise.all([
      request({
        url: `/activities/${id}/records`
      }),
      request({
        url: `/workflow/admin/activities/${id}/track-points`
      }).catch(() => [])
    ]).then(([records, trackPoints]) => {
      this.setData({
        currentTab: 'records',
        selectedActivityId: id,
        selectedActivityTitle: selectedActivity ? selectedActivity.title : '',
        records: (records || []).map(formatParticipationRecord),
        adminTrackPoints: trackPoints || [],
        ...buildTrackMap(trackPoints || [])
      })
    }).catch((error) => {
      wx.showToast({
        title: error.message || '加载服务记录失败',
        icon: 'none'
      })
    })
  },

  ensureEvidenceLoaded() {
    const activities = this.data.activities || []
    if (!activities.length) {
      this.setData({
        evidencePickerIndex: 0,
        evidenceActivityId: null,
        evidenceActivityTitle: '',
        evidenceRecords: [],
        evidenceSnapshots: [],
        evidenceTrackPoints: [],
        evidenceBlockchainProofs: []
      })
      return
    }
    const activityId = this.data.evidenceActivityId || activities[0].id
    this.loadEvidenceBundle(activityId)
  },

  onEvidenceActivityChange(event) {
    const index = Number(event.detail.value)
    const activity = (this.data.activities || [])[index]
    if (!activity) {
      return
    }
    this.loadEvidenceBundle(activity.id)
  },

  openEvidenceCenter(event) {
    const { id } = event.currentTarget.dataset
    this.setData({
      currentTab: 'evidence'
    })
    this.loadEvidenceBundle(id)
  },

  loadEvidenceBundle(activityId) {
    const activities = this.data.activities || []
    const selectedActivity = activities.find((item) => item.id === activityId)
    const evidencePickerIndex = activities.findIndex((item) => item.id === activityId)
    Promise.all([
      request({
        url: `/workflow/admin/activities/${activityId}/records`
      }),
      request({
        url: `/workflow/admin/activities/${activityId}/snapshots`
      }),
      request({
        url: `/workflow/admin/activities/${activityId}/track-points`
      }),
      request({
        url: `/workflow/admin/activities/${activityId}/blockchain-proofs`
      })
    ]).then(([records, snapshots, trackPoints, blockchainProofs]) => {
      const evidenceTrackPoints = (trackPoints || []).map(formatTrackPoint)
      const evidenceTrackMap = buildTrackMap(evidenceTrackPoints)
      this.setData({
        currentTab: 'evidence',
        evidencePickerIndex: evidencePickerIndex >= 0 ? evidencePickerIndex : 0,
        evidenceActivityId: activityId,
        evidenceActivityTitle: selectedActivity ? selectedActivity.title : '',
        evidenceRecords: (records || []).map(formatParticipationRecord),
        evidenceSnapshots: (snapshots || []).map(formatSnapshot),
        evidenceTrackPoints,
        evidenceBlockchainProofs: (blockchainProofs || []).map(formatBlockchainProof),
        hasEvidenceTrackMap: evidenceTrackMap.hasTrackMap,
        evidenceTrackPointCount: evidenceTrackMap.trackPointCount,
        evidenceMapLatitude: evidenceTrackMap.adminMapLatitude,
        evidenceMapLongitude: evidenceTrackMap.adminMapLongitude,
        evidenceMapScale: evidenceTrackMap.adminMapScale,
        evidenceMapPolyline: evidenceTrackMap.adminMapPolyline,
        evidenceMapCircles: evidenceTrackMap.adminMapCircles,
        evidenceMapIncludePoints: evidenceTrackMap.adminMapIncludePoints
      })
    }).catch((error) => {
      wx.showToast({
        title: error.message || '加载证据中心失败',
        icon: 'none'
      })
    })
  },

  approveEnrollment(event) {
    const { id } = event.currentTarget.dataset
    this.reviewEnrollment(id, true)
  },

  rejectEnrollment(event) {
    const { id } = event.currentTarget.dataset
    this.reviewEnrollment(id, false)
  },

  reviewEnrollment(id, approved) {
    request({
      url: `/activities/records/${id}/review-enrollment`,
      method: 'POST',
      data: {
        approved
      }
    }).then(() => {
      wx.showToast({
        title: approved ? '报名已通过' : '报名已驳回',
        icon: 'success'
      })
      this.loadPageData()
    }).catch((error) => {
      wx.showToast({
        title: error.message || '报名审核失败',
        icon: 'none'
      })
    })
  },

  evaluateRecord(event) {
    const { activityId, recordId } = event.currentTarget.dataset
    wx.showActionSheet({
      itemList: RATING_OPTIONS.map((item) => item.label),
      success: (res) => {
        const selected = RATING_OPTIONS[res.tapIndex]
        request({
          url: `/activities/${activityId}/records/${recordId}/evaluate`,
          method: 'POST',
          data: {
            serviceRating: selected.score,
            serviceComment: selected.comment
          }
        }).then(() => {
          wx.showToast({
            title: '服务评价已完成',
            icon: 'success'
          })
          this.loadRecords({ currentTarget: { dataset: { id: activityId } } })
          this.loadPageData()
        }).catch((error) => {
          wx.showToast({
            title: error.message || '服务评价失败',
            icon: 'none'
          })
        })
      }
    })
  },

  confirmRecord(event) {
    const { activityId, recordId } = event.currentTarget.dataset
    request({
      url: `/activities/${activityId}/records/${recordId}/confirm`,
      method: 'POST'
    }).then(() => {
      wx.showToast({
        title: '服务确认已完成',
        icon: 'success'
      })
      this.loadRecords({ currentTarget: { dataset: { id: activityId } } })
      this.loadPageData()
    }).catch((error) => {
      wx.showToast({
        title: error.message || '服务确认失败',
        icon: 'none'
      })
    })
  },

  approveSupplement(event) {
    const { id } = event.currentTarget.dataset
    const reviewComment = this.data.systemSettings.supplementApproveComment || '审核通过，系统已自动补签。'
    this.reviewSupplement(id, true, reviewComment)
  },

  rejectSupplement(event) {
    const { id } = event.currentTarget.dataset
    this.reviewSupplement(id, false, '审核驳回，请补充更清晰的现场证明材料。')
  },

  reviewSupplement(id, approved, reviewComment) {
    request({
      url: `/workflow/admin/supplements/${id}/review`,
      method: 'POST',
      data: {
        approved,
        reviewComment
      }
    }).then(() => {
      wx.showToast({
        title: '补签审核完成',
        icon: 'success'
      })
      this.loadPageData()
    }).catch((error) => {
      wx.showToast({
        title: error.message || '补签审核失败',
        icon: 'none'
      })
    })
  },

  copyAdminWebUrl() {
    const adminWebUrl = (this.data.systemSettings.adminWebUrl || '').trim()
    if (!adminWebUrl) {
      wx.showModal({
        title: '后台地址未配置',
        content: '请先在超级管理员后台的系统设置中填写正式的 admin-web PC 端地址。',
        showCancel: false
      })
      return
    }
    wx.setClipboardData({
      data: adminWebUrl,
      success: () => {
        wx.showModal({
          title: 'PC 端后台地址已复制',
          content: '请在电脑浏览器中粘贴打开，并登录 admin-web 后台完成全局配置。',
          showCancel: false
        })
      }
    })
  },

  openActivitySquare() {
    wx.reLaunch({
      url: '/pages/index/index'
    })
  },

  openDashboard() {
    wx.navigateTo({
      url: '/pages/dashboard/dashboard'
    })
  },

  openMessages() {
    wx.navigateTo({
      url: '/pages/messages/messages'
    })
  },

  openAdminAnnualReport() {
    wx.navigateTo({
      url: '/pages/admin-annual-report/admin-annual-report'
    })
  },

  openAdminLeaderboard() {
    wx.navigateTo({
      url: '/pages/admin-leaderboard/admin-leaderboard'
    })
  },

  openAdminCommunityInsights() {
    wx.navigateTo({
      url: '/pages/admin-community-insights/admin-community-insights'
    })
  },

  openCertificateProof(event) {
    const { recordId } = event.currentTarget.dataset
    if (!recordId) {
      wx.showToast({
        title: '未找到证明记录',
        icon: 'none'
      })
      return
    }
    wx.navigateTo({
      url: `/pages/certificate-proof/certificate-proof?recordId=${recordId}&scope=admin`
    })
  },

  logout() {
    app.clearSession()
    wx.showToast({
      title: '已退出登录',
      icon: 'success'
    })
    setTimeout(() => {
      wx.reLaunch({
        url: '/pages/index/index'
      })
    }, 300)
  }
})

