const app = getApp()
const { request } = require('../../utils/request')
const { requestNotificationSubscription } = require('../../utils/subscribe')
const {
  formatParticipationRecord,
  formatProfile,
  formatRedemption,
  formatSupplement
} = require('../../utils/display')

Page({
  data: {
    profile: null,
    records: [],
    storeItems: [],
    redemptions: [],
    supplements: [],
    currentTab: 'records',
    showServiceTabs: true,
    shippingDialogVisible: false,
    pendingRedeemItemId: null,
    shippingForm: {
      recipientName: '',
      recipientPhone: '',
      recipientAddress: ''
    },
    visibility: {
      realName: false,
      idCardNo: false,
      volunteerCardNo: false
    }
  },

  onShow() {
    if (!app.requireLogin({
      content: '进入我的中心前，请先完成登录。'
    })) {
      return
    }
    this.loadDashboard()
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

  loadDashboard() {
    request({ url: '/users/dashboard' })
      .then((dashboard) => {
        const profile = formatProfile(dashboard.profile)
        const showServiceTabs = profile.role !== 'SUPER_ADMIN'
        const nextData = {
          profile,
          records: (dashboard.participationRecords || []).map(formatParticipationRecord),
          currentTab: 'records',
          showServiceTabs,
          visibility: {
            realName: false,
            idCardNo: false,
            volunteerCardNo: false
          }
        }

        if (!showServiceTabs) {
          this.setData({
            ...nextData,
            storeItems: [],
            redemptions: [],
            supplements: []
          })
          return null
        }

        this.setData(nextData)
        return Promise.all([
          request({ url: '/store/items' }),
          request({ url: '/store/my-redemptions' }),
          request({ url: '/workflow/supplements' })
        ])
      })
      .then((payload) => {
        if (!payload) {
          return
        }
        const [storeItems, redemptions, supplements] = payload
        this.setData({
          storeItems: storeItems || [],
          redemptions: (redemptions || []).map(formatRedemption),
          supplements: (supplements || []).map(formatSupplement)
        })
      })
      .catch((error) => {
        wx.showToast({
          title: error.message || '加载个人中心失败',
          icon: 'none'
        })
      })
  },

  toggleSensitive(event) {
    const { field } = event.currentTarget.dataset
    if (!field) {
      return
    }
    this.setData({
      [`visibility.${field}`]: !this.data.visibility[field]
    })
  },

  goVerify() {
    wx.navigateTo({
      url: '/pages/verify/verify'
    })
  },

  goHome() {
    wx.reLaunch({
      url: '/pages/index/index'
    })
  },

  openMessages() {
    wx.navigateTo({
      url: '/pages/messages/messages'
    })
  },

  openAnnualReport() {
    wx.navigateTo({
      url: '/pages/annual-report/annual-report'
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

  submitRedeem(itemId, payload = {}) {
    request({
      url: '/store/redeem',
      method: 'POST',
      data: {
        itemId,
        quantity: 1,
        ...payload
      }
    }).then(() => {
      wx.showToast({
        title: '兑换成功',
        icon: 'success'
      })
      requestNotificationSubscription(['REDEMPTION_CREATED', 'REDEMPTION_DELIVERED'])
      this.resetShippingForm()
      this.loadDashboard()
    }).catch((error) => {
      wx.showToast({
        title: error.message || '兑换失败',
        icon: 'none'
      })
    })
  },

  redeem(event) {
    const { id } = event.currentTarget.dataset
    const targetItem = (this.data.storeItems || []).find((item) => item.id === id)
    const isPhysical = targetItem && targetItem.deliveryType === 'PHYSICAL'

    if (!isPhysical) {
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

  previewCertificate(event) {
    const { url } = event.currentTarget.dataset
    const fullUrl = app.buildFileUrl(url)
    wx.previewImage({
      urls: [fullUrl],
      current: fullUrl
    })
  },

  exportElectronicCertificate(event) {
    const { url } = event.currentTarget.dataset
    if (!url) {
      wx.showToast({
        title: '暂无电子证书',
        icon: 'none'
      })
      return
    }
    const fullUrl = app.buildFileUrl(url)
    wx.showLoading({
      title: '正在导出'
    })
    wx.downloadFile({
      url: fullUrl,
      success: (res) => {
        if (res.statusCode !== 200) {
          wx.showToast({
            title: '下载电子证书失败',
            icon: 'none'
          })
          return
        }
        wx.saveImageToPhotosAlbum({
          filePath: res.tempFilePath,
          success: () => {
            wx.showToast({
              title: '电子证书已保存',
              icon: 'success'
            })
          },
          fail: (error) => {
            const message = (error && error.errMsg) || ''
            if (message.includes('auth deny') || message.includes('auth denied')) {
              wx.showModal({
                title: '需要相册权限',
                content: '请先在设置中允许保存到相册，再重新尝试导出电子证书。',
                success: (modalRes) => {
                  if (modalRes.confirm) {
                    wx.openSetting()
                  }
                }
              })
              return
            }
            wx.showToast({
              title: '保存电子证书失败',
              icon: 'none'
            })
          },
          complete: () => {
            wx.hideLoading()
          }
        })
      },
      fail: () => {
        wx.hideLoading()
        wx.showToast({
          title: '下载电子证书失败',
          icon: 'none'
        })
      }
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
      url: `/pages/certificate-proof/certificate-proof?recordId=${recordId}&scope=self`
    })
  }
})
