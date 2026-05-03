const { request } = require('../../utils/request')
const { formatProfile } = require('../../utils/display')

Page({
  data: {
    loading: true,
    profile: null,
    form: {
      realName: '',
      idCardNo: ''
    },
    visibility: {
      realName: false,
      idCardNo: false
    }
  },

  onShow() {
    this.loadIdentity()
  },

  loadIdentity() {
    this.setData({ loading: true })
    request({ url: '/users/identity' })
      .then((profile) => {
        const formattedProfile = formatProfile(profile)
        this.setData({
          loading: false,
          profile: formattedProfile,
          form: {
            realName: profile.realName || '',
            idCardNo: profile.idCardNo || ''
          },
          visibility: {
            realName: false,
            idCardNo: false
          }
        })
      })
      .catch((error) => {
        this.setData({ loading: false })
        wx.showToast({
          title: error.message || '加载认证信息失败',
          icon: 'none'
        })
      })
  },

  onInput(event) {
    const { field } = event.currentTarget.dataset
    if (!field) {
      return
    }
    this.setData({
      [`form.${field}`]: event.detail.value
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

  submit() {
    const realName = (this.data.form.realName || '').trim()
    const idCardNo = (this.data.form.idCardNo || '').trim().toUpperCase()

    if (!realName) {
      wx.showToast({
        title: '请先填写真实姓名',
        icon: 'none'
      })
      return
    }
    if (!idCardNo) {
      wx.showToast({
        title: '请先填写身份证号',
        icon: 'none'
      })
      return
    }

    wx.showLoading({
      title: '提交中'
    })
    request({
      url: '/users/verify',
      method: 'POST',
      data: {
        realName,
        idCardNo
      }
    }).then((profile) => {
      wx.hideLoading()
      this.setData({
        profile: formatProfile(profile),
        form: {
          realName,
          idCardNo
        },
        visibility: {
          realName: false,
          idCardNo: false
        }
      })
      wx.showToast({
        title: '实名认证成功',
        icon: 'success'
      })
      setTimeout(() => {
        wx.navigateBack({
          delta: 1
        })
      }, 600)
    }).catch((error) => {
      wx.hideLoading()
      wx.showToast({
        title: error.message || '实名认证失败',
        icon: 'none'
      })
    })
  }
})
