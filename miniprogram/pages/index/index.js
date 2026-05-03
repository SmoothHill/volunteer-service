const app = getApp()
const { request } = require('../../utils/request')
const { requestNotificationSubscription } = require('../../utils/subscribe')
const {
  formatActivity,
  formatAdminApplication,
  formatCommunityInsightItem,
  formatProfile,
  formatStarLeaderboardItem
} = require('../../utils/display')

const EMPTY_ADMIN_APPLICATION = formatAdminApplication(null)

Page({
  data: {
    profile: null,
    guestMode: false,
    needAuthChoice: false,
    needRoleSelection: false,
    localTestLoginEnabled: false,
    adminApplication: EMPTY_ADMIN_APPLICATION,
    adminApplyReason: '',
    activities: [],
    recommendations: [],
    leaderboard: [],
    heatmap: [],
    starLeaderboard: [],
    communityInsights: [],
    leaderboardPeriod: 'week',
    heatmapPeriod: 'week',
    leaderboardViewMode: 'star',
    heatmapViewMode: 'insight',
    storeItems: [],
    currentTab: 'square',
    phoneVerificationVisible: false,
    phoneVerificationLoading: false,
    phoneVerificationTicket: '',
    phoneVerificationDebugCode: '',
    phoneVerificationCountdown: 0,
    phoneVerificationForm: {
      phoneNo: '',
      code: ''
    },
    shippingDialogVisible: false,
    pendingRedeemItemId: null,
    shippingForm: {
      recipientName: '',
      recipientPhone: '',
      recipientAddress: ''
    }
  },

  onShow() {
    this.setData({
      localTestLoginEnabled: app.isLocalTestLoginEnabled()
    })
    this.loadPageData()
  },

  onUnload() {
    this.clearPhoneVerificationTimer()
  },

  switchTab(event) {
    const { tab } = event.currentTarget.dataset
    if (!tab) {
      return
    }
    this.setData({
      currentTab: tab
    })
  },

  showError(error, fallback) {
    wx.showToast({
      title: (error && error.message) || fallback || '加载失败',
      icon: 'none'
    })
  },

  clearPhoneVerificationTimer() {
    if (this.phoneVerificationTimer) {
      clearInterval(this.phoneVerificationTimer)
      this.phoneVerificationTimer = null
    }
  },

  startPhoneVerificationCountdown(seconds = 60) {
    this.clearPhoneVerificationTimer()
    this.setData({
      phoneVerificationCountdown: seconds
    })
    this.phoneVerificationTimer = setInterval(() => {
      const next = this.data.phoneVerificationCountdown - 1
      if (next <= 0) {
        this.clearPhoneVerificationTimer()
        this.setData({
          phoneVerificationCountdown: 0
        })
        return
      }
      this.setData({
        phoneVerificationCountdown: next
      })
    }, 1000)
  },

  resetPhoneVerificationState(extra = {}) {
    this.clearPhoneVerificationTimer()
    this.setData({
      phoneVerificationVisible: false,
      phoneVerificationLoading: false,
      phoneVerificationTicket: '',
      phoneVerificationDebugCode: '',
      phoneVerificationCountdown: 0,
      phoneVerificationForm: {
        phoneNo: '',
        code: ''
      },
      ...extra
    })
  },

  resetShippingForm() {
    this.setData({
      shippingDialogVisible: false,
      pendingRedeemItemId: null,
      shippingForm: {
        recipientName: '',
        recipientPhone: '',
        recipientAddress: ''
      }
    })
  },

  resetPageData(extra = {}) {
    this.resetPhoneVerificationState()
    this.setData({
      profile: null,
      needRoleSelection: false,
      adminApplication: EMPTY_ADMIN_APPLICATION,
      adminApplyReason: '',
      activities: [],
      recommendations: [],
      leaderboard: [],
      heatmap: [],
      starLeaderboard: [],
      communityInsights: [],
      leaderboardViewMode: 'star',
      heatmapViewMode: 'insight',
      storeItems: [],
      currentTab: 'square',
      ...extra
    })
  },

  loadPublicActivities(extra = {}) {
    return request({ url: '/activities', skipAuth: true })
      .then((activities) => {
        this.resetPageData({
          activities: (activities || []).map(formatActivity),
          ...extra
        })
      })
      .catch((error) => {
        this.showError(error, '加载活动失败')
      })
  },

  loadParticipantData(profile) {
    return Promise.all([
      request({ url: '/activities', skipAuth: true }),
      request({ url: '/analytics/recommendations' }),
      request({ url: '/analytics/leaderboard' }),
      request({ url: '/analytics/heatmap' }),
      request({ url: '/analytics/star-leaderboard?period=week' }),
      request({ url: '/analytics/community-insights?period=week' }),
      request({ url: '/store/items' }),
      request({ url: '/auth/my-admin-application' })
    ]).then(([activities, recommendations, leaderboard, heatmap, starLeaderboard, communityInsights, storeItems, adminApplication]) => {
      this.setData({
        profile: formatProfile(profile),
        guestMode: false,
        needAuthChoice: false,
        needRoleSelection: false,
        adminApplication: formatAdminApplication(adminApplication),
        activities: (activities || []).map(formatActivity),
        recommendations: recommendations || [],
        leaderboard: leaderboard || [],
        heatmap: heatmap || [],
        starLeaderboard: (starLeaderboard || []).map(formatStarLeaderboardItem),
        communityInsights: (communityInsights || []).map(formatCommunityInsightItem),
        storeItems: storeItems || []
      })
    })
  },

  switchLeaderboardPeriod(event) {
    const { period } = event.currentTarget.dataset
    if (!period || period === this.data.leaderboardPeriod) {
      return
    }
    request({ url: `/analytics/star-leaderboard?period=${period}` })
      .then((starLeaderboard) => {
        this.setData({
          leaderboardPeriod: period,
          starLeaderboard: (starLeaderboard || []).map(formatStarLeaderboardItem)
        })
      })
      .catch((error) => {
        this.showError(error, '加载排行数据失败')
      })
  },

  switchLeaderboardView(event) {
    const { mode } = event.currentTarget.dataset
    if (!mode || mode === this.data.leaderboardViewMode) {
      return
    }
    this.setData({
      leaderboardViewMode: mode
    })
  },

  switchHeatmapPeriod(event) {
    const { period } = event.currentTarget.dataset
    if (!period || period === this.data.heatmapPeriod) {
      return
    }
    request({ url: `/analytics/community-insights?period=${period}` })
      .then((communityInsights) => {
        this.setData({
          heatmapPeriod: period,
          communityInsights: (communityInsights || []).map(formatCommunityInsightItem)
        })
      })
      .catch((error) => {
        this.showError(error, '加载社区数据失败')
      })
  },

  switchHeatmapView(event) {
    const { mode } = event.currentTarget.dataset
    if (!mode || mode === this.data.heatmapViewMode) {
      return
    }
    this.setData({
      heatmapViewMode: mode
    })
  },

  loadPageData() {
    const guestMode = app.globalData.guestMode
    const hasLogin = app.hasLogin()

    this.setData({
      guestMode,
      needAuthChoice: !guestMode && !hasLogin
    })

    if (!guestMode && !hasLogin) {
      this.resetPageData({
        guestMode: false,
        needAuthChoice: true
      })
      return
    }

    if (!hasLogin) {
      this.loadPublicActivities({
        guestMode: true,
        needAuthChoice: false
      })
      return
    }

    request({ url: '/auth/me' })
      .then((profile) => {
        app.setProfile(profile)

        if (profile.role === 'ADMIN' || profile.role === 'SUPER_ADMIN') {
          app.setGuestMode(false)
          wx.reLaunch({
            url: '/pages/admin/admin'
          })
          return null
        }

        if (!profile.role) {
          return Promise.all([
            Promise.resolve(profile),
            request({ url: '/auth/my-admin-application' }),
            request({ url: '/activities', skipAuth: true })
          ])
        }

        return Promise.all([
          Promise.resolve(profile),
          this.loadParticipantData(profile)
        ])
      })
      .then((result) => {
        if (!result) {
          return
        }

        if (result.length === 2 && result[1] === undefined) {
          return
        }

        const [profile, adminApplication, activities] = result
        this.resetPageData({
          profile: formatProfile(profile),
          guestMode: false,
          needAuthChoice: false,
          needRoleSelection: true,
          adminApplication: formatAdminApplication(adminApplication),
          activities: (activities || []).map(formatActivity)
        })
      })
      .catch((error) => {
        if (!app.hasLogin()) {
          this.loadPageData()
          return
        }
        this.showError(error, '加载首页数据失败')
      })
  },

  authorizeLogin() {
    wx.showModal({
      title: '授权登录',
      content: '是否使用当前微信账号完成登录或注册？首次绑定时还需要短信验证码验证手机号。',
      confirmText: '继续',
      cancelText: '取消',
      success: (res) => {
        if (!res.confirm) {
          wx.showToast({
            title: '已取消登录',
            icon: 'none'
          })
          return
        }

        wx.showLoading({
          title: '登录中'
        })
        app.loginWithWechatStart()
          .then((result = {}) => {
            wx.hideLoading()
            if (result.needPhoneVerification) {
              this.resetPhoneVerificationState({
                phoneVerificationVisible: true,
                phoneVerificationTicket: result.loginTicket || ''
              })
              wx.showToast({
                title: '请先完成手机号验证',
                icon: 'none'
              })
              return
            }
            this.loadPageData()
          })
          .catch((error) => {
            wx.hideLoading()
            this.showError(error, '登录失败')
          })
      }
    })
  },

  onPhoneVerificationInput(event) {
    const { field } = event.currentTarget.dataset
    if (!field) {
      return
    }
    this.setData({
      [`phoneVerificationForm.${field}`]: (event.detail.value || '').trim()
    })
  },

  sendPhoneVerificationCode() {
    const loginTicket = this.data.phoneVerificationTicket
    const phoneNo = String(this.data.phoneVerificationForm.phoneNo || '').trim()

    if (!loginTicket) {
      wx.showToast({
        title: '登录状态已失效',
        icon: 'none'
      })
      return
    }
    if (!/^1\d{10}$/.test(phoneNo)) {
      wx.showToast({
        title: '请输入正确手机号',
        icon: 'none'
      })
      return
    }
    if (this.data.phoneVerificationCountdown > 0 || this.data.phoneVerificationLoading) {
      return
    }

    this.setData({
      phoneVerificationLoading: true
    })
    app.sendLoginCode(loginTicket, phoneNo)
      .then((result = {}) => {
        this.setData({
          phoneVerificationLoading: false,
          phoneVerificationDebugCode: result.debugCode || ''
        })
        this.startPhoneVerificationCountdown(60)
        wx.showToast({
          title: result.mock ? '验证码已生成' : '验证码已发送',
          icon: 'none'
        })
      })
      .catch((error) => {
        this.setData({
          phoneVerificationLoading: false
        })
        this.showError(error, '发送验证码失败')
      })
  },

  submitPhoneVerification() {
    const loginTicket = this.data.phoneVerificationTicket
    const phoneNo = String(this.data.phoneVerificationForm.phoneNo || '').trim()
    const code = String(this.data.phoneVerificationForm.code || '').trim()

    if (!loginTicket) {
      wx.showToast({
        title: '登录状态已失效',
        icon: 'none'
      })
      return
    }
    if (!/^1\d{10}$/.test(phoneNo)) {
      wx.showToast({
        title: '请输入正确手机号',
        icon: 'none'
      })
      return
    }
    if (!/^\d{6}$/.test(code)) {
      wx.showToast({
        title: '请输入 6 位验证码',
        icon: 'none'
      })
      return
    }

    wx.showLoading({
      title: '验证中'
    })
    this.setData({
      phoneVerificationLoading: true
    })
    app.verifyLoginCode(loginTicket, phoneNo, code)
      .then(() => {
        wx.hideLoading()
        this.resetPhoneVerificationState()
        wx.showToast({
          title: '登录成功',
          icon: 'success'
        })
        this.loadPageData()
      })
      .catch((error) => {
        wx.hideLoading()
        this.setData({
          phoneVerificationLoading: false
        })
        this.showError(error, '验证码校验失败')
      })
  },

  cancelPhoneVerification() {
    this.resetPhoneVerificationState()
    wx.showToast({
      title: '已取消手机号验证',
      icon: 'none'
    })
  },

  loginLocalVolunteer() {
    wx.showLoading({
      title: '登录中'
    })
    app.loginWithLocalVolunteer()
      .then(() => {
        wx.hideLoading()
        wx.showToast({
          title: '已进入本地志愿者账号',
          icon: 'success'
        })
        this.loadPageData()
      })
      .catch((error) => {
        wx.hideLoading()
        this.showError(error, '本地测试登录失败')
      })
  },

  enterGuestMode() {
    this.resetPhoneVerificationState()
    app.setGuestMode(true)
    this.loadPageData()
  },

  resetAuthChoice() {
    this.resetPhoneVerificationState()
    app.setGuestMode(false)
    this.loadPageData()
  },

  registerVolunteer() {
    wx.showLoading({
      title: '提交中'
    })
    request({
      url: '/auth/register-volunteer',
      method: 'POST'
    })
      .then(() => {
        wx.hideLoading()
        wx.showToast({
          title: '已注册为志愿者',
          icon: 'success'
        })
        this.loadPageData()
      })
      .catch((error) => {
        wx.hideLoading()
        this.showError(error, '注册失败')
      })
  },

  onAdminReasonInput(event) {
    this.setData({
      adminApplyReason: event.detail.value
    })
  },

  submitAdminApplication() {
    const reason = (this.data.adminApplyReason || '').trim()
    if (!reason) {
      wx.showToast({
        title: '请先填写申请原因',
        icon: 'none'
      })
      return
    }

    wx.showLoading({
      title: '提交中'
    })
    request({
      url: '/auth/admin-application',
      method: 'POST',
      data: { reason }
    })
      .then(() => {
        wx.hideLoading()
        wx.showToast({
          title: '申请已提交',
          icon: 'success'
        })
        this.setData({
          adminApplyReason: ''
        })
        this.loadPageData()
      })
      .catch((error) => {
        wx.hideLoading()
        this.showError(error, '提交申请失败')
      })
  },

  openDetail(event) {
    const { id } = event.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/activity-detail/activity-detail?id=${id}`
    })
  },

  openDashboard() {
    if (!app.requireLogin({
      content: '进入我的中心前，请先完成登录并确认身份。'
    })) {
      return
    }
    wx.navigateTo({
      url: '/pages/dashboard/dashboard'
    })
  },

  openMessages() {
    if (!app.requireLogin({
      content: '进入消息提醒前，请先完成登录并确认身份。'
    })) {
      return
    }
    wx.navigateTo({
      url: '/pages/messages/messages'
    })
  },

  submitRedeem(itemId, payload = {}) {
    request({
      url: '/store/redeem',
      method: 'POST',
      data: {
        itemId,
        quantity: 1,
        ...payload
      }
    })
      .then(() => {
        wx.showToast({
          title: '兑换成功',
          icon: 'success'
        })
        requestNotificationSubscription(['REDEMPTION_CREATED', 'REDEMPTION_DELIVERED'])
        this.resetShippingForm()
        this.loadPageData()
      })
      .catch((error) => {
        this.showError(error, '兑换失败')
      })
  },

  redeemStoreItem(event) {
    const { id, deliveryType } = event.currentTarget.dataset
    if (deliveryType !== 'PHYSICAL') {
      this.submitRedeem(id)
      return
    }
    this.setData({
      shippingDialogVisible: true,
      pendingRedeemItemId: id,
      shippingForm: {
        recipientName: '',
        recipientPhone: '',
        recipientAddress: ''
      }
    })
  },

  onShippingFormChange(event) {
    const { field, value } = event.detail || {}
    if (!field) {
      return
    }
    this.setData({
      [`shippingForm.${field}`]: value
    })
  },

  onShippingDialogCancel() {
    this.resetShippingForm()
  },

  onShippingDialogConfirm() {
    const itemId = this.data.pendingRedeemItemId
    const form = this.data.shippingForm || {}
    const recipientName = String(form.recipientName || '').trim()
    const recipientPhone = String(form.recipientPhone || '').trim()
    const recipientAddress = String(form.recipientAddress || '').trim()

    if (!recipientName || !recipientPhone || !recipientAddress) {
      wx.showToast({
        title: '请完整填写收货信息',
        icon: 'none'
      })
      return
    }

    this.submitRedeem(itemId, {
      recipientName,
      recipientPhone,
      recipientAddress
    })
  },

  logout() {
    this.resetPhoneVerificationState()
    app.clearSession()
    this.resetPageData({
      guestMode: false,
      needAuthChoice: true
    })
    wx.showToast({
      title: '已退出登录',
      icon: 'success'
    })
  }
})
