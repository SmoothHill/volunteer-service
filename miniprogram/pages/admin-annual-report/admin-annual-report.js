const app = getApp()
const { request } = require('../../utils/request')

function buildYearOptions() {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, index) => currentYear - index)
}

function formatHeatmapItem(item) {
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

function formatAdminLeaderboard(item) {
  return {
    ...item,
    displayServiceHours: Number(item.totalServiceHours || 0).toFixed(1)
  }
}

function buildHeatmapMapData(items = []) {
  const points = (items || [])
    .filter((item) => Number.isFinite(Number(item.latitude)) && Number.isFinite(Number(item.longitude)))
    .slice(0, 12)
    .map((item, index) => ({
      ...item,
      markerId: index + 1,
      latitude: Number(item.latitude),
      longitude: Number(item.longitude)
    }))

  if (!points.length) {
    return {
      hasMap: false,
      latitude: 30.5728,
      longitude: 104.0668,
      scale: 11,
      markers: [],
      includePoints: []
    }
  }

  return {
    hasMap: true,
    latitude: points[0].latitude,
    longitude: points[0].longitude,
    scale: 11,
    markers: points.map((item) => ({
      id: item.markerId,
      latitude: item.latitude,
      longitude: item.longitude,
      width: 28,
      height: 28,
      callout: {
        content: `${item.activityTitle || '热力点'} ${item.displayIntensity}`,
        display: 'BYCLICK',
        padding: 8,
        borderRadius: 8,
        bgColor: '#ffffff',
        color: '#102a43'
      }
    })),
    includePoints: points.map((item) => ({
      latitude: item.latitude,
      longitude: item.longitude
    }))
  }
}

function normalizeAdminReport(report) {
  const communityHeatmap = (report.communityHeatmap || []).map(formatHeatmapItem)
  const activityHeatmap = (report.activityHeatmap || []).map(formatHeatmapItem)
  return {
    ...report,
    displaySummaryText: report.summaryText || '这里汇总了本年度的管理和服务数据。',
    displayHottestCommunity: report.hottestCommunity || '暂无',
    displayTopCategory: report.topCategory || '综合服务',
    coveredCommunities: report.coveredCommunities || [],
    categoryStats: report.categoryStats || [],
    monthlyActivityTrends: report.monthlyActivityTrends || [],
    monthlyServiceHourTrends: report.monthlyServiceHourTrends || [],
    communityHeatmap,
    activityHeatmap,
    communityMap: buildHeatmapMapData(communityHeatmap),
    activityMap: buildHeatmapMapData(activityHeatmap)
  }
}

function normalizePlatformOverview(overview) {
  if (!overview) {
    return null
  }
  const communityHeatmap = (overview.communityHeatmap || []).map(formatHeatmapItem)
  const activityHeatmap = (overview.activityHeatmap || []).map(formatHeatmapItem)
  return {
    ...overview,
    hotAreas: overview.hotAreas || [],
    categoryStats: overview.categoryStats || [],
    monthlyActivityTrends: overview.monthlyActivityTrends || [],
    monthlyServiceHourTrends: overview.monthlyServiceHourTrends || [],
    adminLeaderboard: (overview.adminLeaderboard || []).map(formatAdminLeaderboard),
    volunteerLeaderboard: overview.volunteerLeaderboard || [],
    communityHeatmap,
    activityHeatmap,
    communityMap: buildHeatmapMapData(communityHeatmap),
    activityMap: buildHeatmapMapData(activityHeatmap)
  }
}

Page({
  data: {
    loading: true,
    isSuperAdmin: false,
    yearOptions: buildYearOptions(),
    selectedYearIndex: 0,
    adminReport: null,
    adminLeaderboard: [],
    platformOverview: null
  },

  onShow() {
    if (!app.requireLogin({
      content: '进入管理年报前，请先完成登录。'
    })) {
      return
    }
    this.loadAnnualAnalytics()
  },

  getSelectedYear() {
    return this.data.yearOptions[this.data.selectedYearIndex]
  },

  onYearChange(event) {
    this.setData({
      selectedYearIndex: Number(event.detail.value || 0)
    })
    this.loadAnnualAnalytics()
  },

  loadAnnualAnalytics() {
    this.setData({ loading: true })
    const year = this.getSelectedYear()

    request({ url: '/auth/me' })
      .then((profile) => {
        const requests = [
          request({ url: `/analytics/admin-self/annual-report?year=${year}` }),
          request({ url: `/analytics/admin-self/leaderboard?year=${year}` })
        ]
        const isSuperAdmin = profile.role === 'SUPER_ADMIN'
        if (isSuperAdmin) {
          requests.push(request({ url: `/analytics/admin/annual-overview?year=${year}` }))
        }
        return Promise.all(requests).then((payload) => ({
          isSuperAdmin,
          payload
        }))
      })
      .then(({ isSuperAdmin, payload }) => {
        const [adminReport, adminLeaderboard, platformOverview] = payload
        this.setData({
          loading: false,
          isSuperAdmin,
          adminReport: normalizeAdminReport(adminReport || {}),
          adminLeaderboard: (adminLeaderboard || []).map(formatAdminLeaderboard),
          platformOverview: normalizePlatformOverview(platformOverview)
        })
      })
      .catch((error) => {
        this.setData({ loading: false })
        wx.showToast({
          title: error.message || '加载管理年报失败',
          icon: 'none'
        })
      })
  },

  goBack() {
    wx.navigateBack({
      delta: 1,
      fail: () => {
        wx.reLaunch({
          url: '/pages/admin/admin'
        })
      }
    })
  }
})
