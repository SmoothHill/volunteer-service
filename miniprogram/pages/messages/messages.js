const app = getApp()
const { request } = require('../../utils/request')
const { formatNotification, formatProfile } = require('../../utils/display')

Page({
  data: {
    profile: null,
    notifications: []
  },

  onShow() {
    if (!app.requireLogin({
      content: '进入消息中心前，请先完成登录。'
    })) {
      return
    }
    this.loadMessages()
  },

  loadMessages() {
    Promise.all([
      request({ url: '/users/dashboard' }),
      request({ url: '/users/notifications' })
    ]).then(([dashboard, notifications]) => {
      this.setData({
        profile: formatProfile(dashboard.profile),
        notifications: (notifications || []).map(formatNotification)
      })
    }).catch((error) => {
      wx.showToast({
        title: error.message || '加载消息失败',
        icon: 'none'
      })
    })
  },

  showSubscriptionTip() {
    wx.showModal({
      title: '订阅消息说明',
      content: '站内消息会一直保留。对已接入模板的业务事件，系统会在关键操作后申请微信订阅授权；只有你同意订阅后，后续才会收到微信外部提醒。',
      showCancel: false
    })
  },

  goBack() {
    wx.navigateBack({
      delta: 1,
      fail: () => {
        wx.reLaunch({
          url: '/pages/index/index'
        })
      }
    })
  }
})
