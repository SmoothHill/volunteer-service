const app = getApp()

function normalizeNetworkError(error, fallback) {
  const message = (error && error.errMsg) || ''
  if (message.includes('timeout')) {
    return new Error('请求超时，请稍后重试')
  }
  if (message.includes('fail')) {
    return new Error('连接服务失败，请确认后端已启动')
  }
  return new Error(fallback || '请求失败')
}

function request({ url, method = 'GET', data, skipAuth = false }) {
  const doRequest = () => new Promise((resolve, reject) => {
    const header = {
      'content-type': 'application/json'
    }
    if (!skipAuth && app.globalData.token) {
      header.Authorization = `Bearer ${app.globalData.token}`
    }
    wx.request({
      url: `${app.globalData.baseUrl}${url}`,
      method,
      data,
      header,
      success(res) {
        const payload = res.data || {}
        if (res.statusCode === 401) {
          app.clearSession()
          reject(new Error(payload.message || '登录已过期，请重新登录'))
          return
        }
        if (payload.success) {
          resolve(payload.data)
          return
        }
        reject(new Error(payload.message || '请求失败'))
      },
      fail(error) {
        reject(normalizeNetworkError(error, '请求失败'))
      }
    })
  })

  if (skipAuth) {
    return doRequest()
  }

  if (!app.hasLogin()) {
    return Promise.reject(new Error('请先在首页授权登录或注册'))
  }

  return doRequest()
}

function uploadFile({ filePath, category = 'common' }) {
  if (!app.hasLogin()) {
    return Promise.reject(new Error('请先在首页授权登录或注册'))
  }

  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: `${app.globalData.baseUrl}/files/upload`,
      filePath,
      name: 'file',
      formData: {
        category
      },
      header: {
        Authorization: `Bearer ${app.globalData.token}`
      },
      success(res) {
        const payload = JSON.parse(res.data || '{}')
        if (payload.success) {
          resolve(payload.data)
          return
        }
        reject(new Error(payload.message || '上传失败'))
      },
      fail(error) {
        reject(normalizeNetworkError(error, '上传失败'))
      }
    })
  })
}

module.exports = {
  request,
  uploadFile
}
