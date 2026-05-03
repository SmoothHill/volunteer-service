const LOCAL_DEVTOOLS_ORIGIN = 'http://127.0.0.1:8082'
const LOCAL_DEVICE_ORIGIN = 'http://192.168.1.126:8082'
const CLOUD_ORIGIN = 'https://replace-with-your-cloud-host'

function resolveMiniProgramEnvVersion() {
  const accountInfo = wx.getAccountInfoSync ? wx.getAccountInfoSync() : null
  const miniProgram = accountInfo && accountInfo.miniProgram ? accountInfo.miniProgram : null
  return (miniProgram && miniProgram.envVersion) || 'develop'
}

const MINI_PROGRAM_ENV_VERSION = resolveMiniProgramEnvVersion()
const ENABLE_LOCAL_TEST_LOGIN = MINI_PROGRAM_ENV_VERSION === 'develop'

function resolveOrigin() {
  if (MINI_PROGRAM_ENV_VERSION === 'trial' || MINI_PROGRAM_ENV_VERSION === 'release') {
    return CLOUD_ORIGIN
  }
  const deviceInfo = wx.getDeviceInfo ? wx.getDeviceInfo() : null
  const platform = deviceInfo && deviceInfo.platform ? deviceInfo.platform.toLowerCase() : ''
  if (platform === 'devtools') {
    return LOCAL_DEVTOOLS_ORIGIN
  }
  return LOCAL_DEVICE_ORIGIN
}

function normalizeRequestError(error, timeoutMessage, fallbackMessage) {
  const message = (error && error.errMsg) || ''
  if (message.includes('timeout')) {
    return new Error(timeoutMessage)
  }
  return new Error(fallbackMessage)
}

App({
  globalData: {
    origin: resolveOrigin(),
    baseUrl: `${resolveOrigin()}/api`,
    token: '',
    profile: null,
    guestMode: false,
    localTestLoginEnabled: ENABLE_LOCAL_TEST_LOGIN
  },

  onLaunch() {
    this.globalData.token = wx.getStorageSync('token') || ''
    this.globalData.profile = wx.getStorageSync('profile') || null
    this.globalData.guestMode = Boolean(wx.getStorageSync('guestMode'))
  },

  isLocalTestLoginEnabled() {
    return Boolean(this.globalData.localTestLoginEnabled)
  },

  setSession(payload) {
    this.globalData.token = payload.token
    this.globalData.profile = payload.profile
    wx.setStorageSync('token', payload.token)
    wx.setStorageSync('profile', payload.profile)
    this.setGuestMode(false)
  },

  setProfile(profile) {
    this.globalData.profile = profile
    wx.setStorageSync('profile', profile)
  },

  setGuestMode(enabled) {
    this.globalData.guestMode = Boolean(enabled)
    if (enabled) {
      wx.setStorageSync('guestMode', true)
      return
    }
    wx.removeStorageSync('guestMode')
  },

  clearSession() {
    this.globalData.token = ''
    this.globalData.profile = null
    wx.removeStorageSync('token')
    wx.removeStorageSync('profile')
    this.setGuestMode(false)
  },

  hasLogin() {
    return Boolean(this.globalData.token)
  },

  sendLoginRequest(url, data = {}) {
    return new Promise((resolve, reject) => {
      wx.request({
        url,
        method: 'POST',
        data,
        header: {
          'content-type': 'application/json'
        },
        timeout: 10000,
        success: (res) => {
          const payload = res.data || {}
          if (!payload.success) {
            reject(new Error(payload.message || '登录失败'))
            return
          }
          this.setSession(payload.data)
          resolve(payload.data)
        },
        fail: (error) => {
          reject(normalizeRequestError(error, '连接登录服务超时，请稍后重试', '连接登录服务失败，请确认后端已启动'))
        }
      })
    })
  },

  loginWithWechatStart(extraProfile = {}) {
    return new Promise((resolve, reject) => {
      wx.login({
        success: (loginRes) => {
          if (!loginRes.code) {
            reject(new Error('获取微信登录凭证失败'))
            return
          }

          wx.request({
            url: `${this.globalData.baseUrl}/auth/wechat-login-start`,
            method: 'POST',
            data: {
              code: loginRes.code,
              nickname: extraProfile.nickname || '',
              avatarUrl: extraProfile.avatarUrl || ''
            },
            header: {
              'content-type': 'application/json'
            },
            timeout: 10000,
            success: (res) => {
              const payload = res.data || {}
              if (!payload.success) {
                reject(new Error(payload.message || '登录失败'))
                return
              }
              const result = payload.data || {}
              if (result.loginResult && result.loginResult.token) {
                this.setSession(result.loginResult)
              }
              resolve(result)
            },
            fail: (error) => {
              reject(normalizeRequestError(error, '连接登录服务超时，请稍后重试', '连接登录服务失败，请确认后端已启动'))
            }
          })
        },
        fail: () => {
          reject(new Error('微信登录能力暂时不可用'))
        }
      })
    })
  },

  sendLoginCode(loginTicket, phoneNo) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: `${this.globalData.baseUrl}/auth/send-login-code`,
        method: 'POST',
        data: {
          loginTicket,
          phoneNo
        },
        header: {
          'content-type': 'application/json'
        },
        timeout: 10000,
        success: (res) => {
          const payload = res.data || {}
          if (!payload.success) {
            reject(new Error(payload.message || '发送验证码失败'))
            return
          }
          resolve(payload.data || {})
        },
        fail: (error) => {
          reject(normalizeRequestError(error, '验证码请求超时，请稍后重试', '验证码请求失败，请确认后端已启动'))
        }
      })
    })
  },

  verifyLoginCode(loginTicket, phoneNo, code) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: `${this.globalData.baseUrl}/auth/verify-login-code`,
        method: 'POST',
        data: {
          loginTicket,
          phoneNo,
          code
        },
        header: {
          'content-type': 'application/json'
        },
        timeout: 10000,
        success: (res) => {
          const payload = res.data || {}
          if (!payload.success) {
            reject(new Error(payload.message || '验证码校验失败'))
            return
          }
          this.setSession(payload.data)
          resolve(payload.data)
        },
        fail: (error) => {
          reject(normalizeRequestError(error, '验证码校验超时，请稍后重试', '验证码校验失败，请确认后端已启动'))
        }
      })
    })
  },

  loginWithLocalVolunteer() {
    return this.sendLoginRequest(`${this.globalData.baseUrl}/auth/local-volunteer-login`)
  },

  ensureLogin() {
    if (this.hasLogin()) {
      return Promise.resolve({
        token: this.globalData.token,
        profile: this.globalData.profile
      })
    }
    return Promise.reject(new Error('请先在首页授权登录或注册'))
  },

  requireLogin(options = {}) {
    if (this.hasLogin()) {
      return true
    }

    wx.showModal({
      title: '请先登录',
      content: options.content || '当前操作需要先授权登录或注册。',
      confirmText: '去首页',
      success: (res) => {
        if (res.confirm) {
          wx.reLaunch({
            url: '/pages/index/index'
          })
        }
      }
    })
    return false
  },

  buildFileUrl(path) {
    if (!path) {
      return ''
    }
    if (path.startsWith('http')) {
      return path
    }
    return `${this.globalData.origin}${path}`
  }
})
