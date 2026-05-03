const app = getApp()
const { request } = require('../../utils/request')
const { maskVolunteerCardNo } = require('../../utils/display')

function formatDateTime(value, fallback = '暂无') {
  if (!value) {
    return fallback
  }
  return String(value).replace('T', ' ').replace(/\.\d+$/, '')
}

function formatProofStatus(status) {
  if (status === 'MOCK_CHAINED') {
    return '已上链（模拟）'
  }
  if (status === 'PENDING') {
    return '待存证'
  }
  if (status === 'FAILED') {
    return '存证失败'
  }
  return status || '暂无'
}

function normalizeSnapshots(list = []) {
  return list.map((item) => ({
    ...item,
    fullImageUrl: app.buildFileUrl(item.imageUrl),
    displayCreatedAt: formatDateTime(item.createdAt),
    displayNote: item.note || '未填写说明'
  }))
}

function formatProof(proof) {
  const snapshots = normalizeSnapshots(proof.snapshots || [])
  return {
    ...proof,
    displayActivityStartTime: formatDateTime(proof.activityStartTime),
    displayActivityEndTime: formatDateTime(proof.activityEndTime),
    displayCheckInTime: formatDateTime(proof.checkInTime, '未签到'),
    displayCheckOutTime: formatDateTime(proof.checkOutTime, '未签退'),
    displayOrganizerConfirmedAt: formatDateTime(proof.organizerConfirmedAt, '待确认'),
    displayRewardFinalizedAt: formatDateTime(proof.rewardFinalizedAt, '待结算'),
    displayIssuedAt: formatDateTime(proof.issuedAt),
    displayBlockchainAnchoredAt: formatDateTime(proof.blockchainAnchoredAt, '待写入'),
    displayBlockchainProofStatus: formatProofStatus(proof.blockchainProofStatus),
    displayBlockchainTransactionNo: proof.blockchainTransactionNo || '尚未生成',
    displayBlockchainProviderName: proof.blockchainProviderName || 'mock-chain',
    displayEvidenceHash: proof.evidenceHash || '暂无哈希',
    displayServiceRating: proof.serviceRating ? `${proof.serviceRating} 分` : '待评价',
    displayServiceComment: proof.serviceComment || '暂无评价意见',
    displayOrganizerConfirmComment: proof.organizerConfirmComment || '暂无确认意见',
    displayVolunteerCardNo: proof.volunteerCardNo ? maskVolunteerCardNo(proof.volunteerCardNo) : '认证后可查看',
    displayMaskedIdCardNo: proof.maskedIdCardNo || '未提供',
    displayCertificateUrl: app.buildFileUrl(proof.certificateUrl),
    snapshots
  }
}

Page({
  data: {
    loading: true,
    exportingPdf: false,
    recordId: '',
    scope: 'self',
    proof: null
  },

  onLoad(options) {
    const recordId = options.recordId
    const scope = options.scope === 'admin' ? 'admin' : 'self'
    if (!recordId) {
      wx.showToast({
        title: '未找到证明记录',
        icon: 'none'
      })
      setTimeout(() => {
        wx.navigateBack({ delta: 1 })
      }, 300)
      return
    }
    this.setData({
      recordId,
      scope
    })
    this.loadProof()
  },

  loadProof() {
    const { recordId, scope } = this.data
    const url = scope === 'admin'
      ? `/activities/records/${recordId}/certificate-proof`
      : `/users/records/${recordId}/certificate-proof`
    this.setData({ loading: true })
    request({ url })
      .then((proof) => {
        this.setData({
          proof: formatProof(proof),
          loading: false
        })
      })
      .catch((error) => {
        this.setData({ loading: false })
        wx.showToast({
          title: error.message || '加载证明失败',
          icon: 'none'
        })
      })
  },

  previewCertificate() {
    const { proof } = this.data
    if (!proof || !proof.displayCertificateUrl) {
      wx.showToast({
        title: '暂无电子证书',
        icon: 'none'
      })
      return
    }
    wx.previewImage({
      urls: [proof.displayCertificateUrl],
      current: proof.displayCertificateUrl
    })
  },

  exportPdf() {
    if (!app.hasLogin()) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      return
    }
    const { recordId, scope, exportingPdf } = this.data
    if (!recordId || exportingPdf) {
      return
    }
    const url = scope === 'admin'
      ? `${app.globalData.baseUrl}/activities/records/${recordId}/certificate-proof.pdf`
      : `${app.globalData.baseUrl}/users/records/${recordId}/certificate-proof.pdf`
    this.setData({ exportingPdf: true })
    wx.showLoading({
      title: '正在导出PDF'
    })
    wx.downloadFile({
      url,
      header: {
        Authorization: `Bearer ${app.globalData.token}`
      },
      success: (res) => {
        if (res.statusCode !== 200 || !res.tempFilePath) {
          wx.showToast({
            title: '导出PDF失败',
            icon: 'none'
          })
          return
        }
        wx.openDocument({
          filePath: res.tempFilePath,
          fileType: 'pdf',
          showMenu: true,
          fail: () => {
            wx.showToast({
              title: '打开PDF失败',
              icon: 'none'
            })
          }
        })
      },
      fail: () => {
        wx.showToast({
          title: '导出PDF失败',
          icon: 'none'
        })
      },
      complete: () => {
        wx.hideLoading()
        this.setData({ exportingPdf: false })
      }
    })
  },

  previewSnapshot(event) {
    const { index } = event.currentTarget.dataset
    const snapshots = (this.data.proof && this.data.proof.snapshots) || []
    if (!snapshots.length) {
      return
    }
    const urls = snapshots.map((item) => item.fullImageUrl).filter(Boolean)
    const current = snapshots[index] ? snapshots[index].fullImageUrl : urls[0]
    if (!urls.length) {
      wx.showToast({
        title: '暂无快照图片',
        icon: 'none'
      })
      return
    }
    wx.previewImage({
      urls,
      current
    })
  },

  goBack() {
    wx.navigateBack({
      delta: 1
    })
  }
})
