const app = getApp()
const { request } = require('../../utils/request')

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

Page({
  data: {
    loading: true,
    isSuperAdmin: false,
    period: 'week',
    adminInsights: [],
    platformInsights: []
  },

  onShow() {
    if (!app.requireLogin({
      content: '进入社区热力前，请先完成登录。'
    })) {
      return
    }
    this.loadData()
  },

  switchPeriod(event) {
    const { period } = event.currentTarget.dataset
    if (!period || period === this.data.period) {
      return
    }
    this.setData({
      period
    })
    this.loadData()
  },

  loadData() {
    const period = this.data.period || 'week'
    this.setData({ loading: true })
    request({ url: '/auth/me' })
      .then((profile) => {
        const requests = [
          request({ url: `/analytics/admin-self/community-insights?period=${period}` })
        ]
        const isSuperAdmin = profile.role === 'SUPER_ADMIN'
        if (isSuperAdmin) {
          requests.push(request({ url: `/analytics/admin/community-insights?period=${period}` }))
        }
        return Promise.all(requests).then((payload) => ({
          isSuperAdmin,
          payload
        }))
      })
      .then(({ isSuperAdmin, payload }) => {
        const [adminInsights, platformInsights] = payload
        this.setData({
          loading: false,
          isSuperAdmin,
          adminInsights: (adminInsights || []).map(formatCommunityInsightItem),
          platformInsights: (platformInsights || []).map(formatCommunityInsightItem)
        })
      })
      .catch((error) => {
        this.setData({ loading: false })
        wx.showToast({
          title: error.message || '加载社区数据失败',
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
