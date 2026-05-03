const app = getApp()
const { request } = require('../../utils/request')

function formatStarLeaderboardItem(item) {
  return {
    ...item,
    displayPeriod: item.period === 'month' ? '月榜' : '周榜',
    displayStarScore: Number(item.starScore || 0).toFixed(1),
    displayPositiveRatingRate: `${Number(item.positiveRatingRate || 0).toFixed(1)}%`,
    displayTrackCoverageScore: Number(item.trackCoverageScore || 0).toFixed(1),
    displayTotalHours: Number(item.totalHours || 0).toFixed(1),
    displayShowcaseText: item.showcaseText || '持续服务社区，展现稳定的志愿服务表现。'
  }
}

Page({
  data: {
    loading: true,
    isSuperAdmin: false,
    period: 'week',
    adminStars: [],
    platformStars: []
  },

  onShow() {
    if (!app.requireLogin({
      content: '进入排行榜前，请先完成登录。'
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
          request({ url: `/analytics/admin-self/star-leaderboard?period=${period}` })
        ]
        const isSuperAdmin = profile.role === 'SUPER_ADMIN'
        if (isSuperAdmin) {
          requests.push(request({ url: `/analytics/admin/star-leaderboard?period=${period}` }))
        }
        return Promise.all(requests).then((payload) => ({
          isSuperAdmin,
          payload
        }))
      })
      .then(({ isSuperAdmin, payload }) => {
        const [adminStars, platformStars] = payload
        this.setData({
          loading: false,
          isSuperAdmin,
          adminStars: (adminStars || []).map(formatStarLeaderboardItem),
          platformStars: (platformStars || []).map(formatStarLeaderboardItem)
        })
      })
      .catch((error) => {
        this.setData({ loading: false })
        wx.showToast({
          title: error.message || '加载排行榜失败',
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
