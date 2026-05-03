const app = getApp()
const { request, uploadFile } = require('../../utils/request')
const { requestNotificationSubscription } = require('../../utils/subscribe')

function formatDateTime(value, fallback = '暂无') {
  if (!value) {
    return fallback
  }
  return String(value).replace('T', ' ').replace(/\.\d+$/, '')
}

function toDifficultyLabel(level) {
  const value = Number(level || 0)
  if (value <= 1) {
    return '1 级·轻量'
  }
  if (value === 2) {
    return '2 级·基础'
  }
  if (value === 3) {
    return '3 级·常规'
  }
  if (value === 4) {
    return '4 级·较高'
  }
  return '5 级·重点'
}

function toDemandLabel(level) {
  const value = Number(level || 0)
  if (value <= 1) {
    return '1 级·常规'
  }
  if (value === 2) {
    return '2 级·一般'
  }
  if (value === 3) {
    return '3 级·关注'
  }
  if (value === 4) {
    return '4 级·紧缺'
  }
  return '5 级·急需'
}

function formatSubmitDateTime(date = new Date()) {
  const year = date.getFullYear()
  const month = `${date.getMonth() + 1}`.padStart(2, '0')
  const day = `${date.getDate()}`.padStart(2, '0')
  const hour = `${date.getHours()}`.padStart(2, '0')
  const minute = `${date.getMinutes()}`.padStart(2, '0')
  const second = `${date.getSeconds()}`.padStart(2, '0')
  return `${year}-${month}-${day}T${hour}:${minute}:${second}`
}

function normalizeCode(value = '') {
  return String(value).trim().replace(/\s+/g, '')
}

function parseQueryString(query = '') {
  return String(query)
    .split('&')
    .filter(Boolean)
    .reduce((result, pair) => {
      const [rawKey, rawValue = ''] = pair.split('=')
      const key = decodeURIComponent(rawKey || '')
      const value = decodeURIComponent(rawValue || '')
      if (key) {
        result[key] = value
      }
      return result
    }, {})
}

function parseCheckInQrResult(result = '') {
  const text = String(result || '').trim()
  if (!text) {
    return {}
  }

  const questionIndex = text.indexOf('?')
  if (questionIndex >= 0) {
    const query = text.slice(questionIndex + 1)
    const parsed = parseQueryString(query)
    if (parsed.checkInCode || parsed.activityId) {
      return {
        activityId: parsed.activityId ? Number(parsed.activityId) : null,
        checkInCode: normalizeCode(parsed.checkInCode || '')
      }
    }
  }

  return {
    activityId: null,
    checkInCode: normalizeCode(text)
  }
}

function toTimestamp(value) {
  if (!value) {
    return 0
  }
  const timestamp = new Date(String(value).replace(' ', 'T')).getTime()
  return Number.isFinite(timestamp) ? timestamp : 0
}

function calculateDistanceMeters(first, second) {
  if (!first || !second) {
    return 0
  }
  const toRadians = (value) => (Number(value) * Math.PI) / 180
  const lat1 = toRadians(first.latitude)
  const lng1 = toRadians(first.longitude)
  const lat2 = toRadians(second.latitude)
  const lng2 = toRadians(second.longitude)
  const earthRadius = 6371000
  const dLat = lat2 - lat1
  const dLng = lng2 - lng1
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLng / 2) * Math.sin(dLng / 2)
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
  return earthRadius * c
}

function buildParticipationState(record, loggedIn) {
  if (!loggedIn) {
    return {
      currentStatusText: '游客浏览中',
      enrollButtonText: '登录后报名',
      canEnroll: true
    }
  }

  if (!record) {
    return {
      currentStatusText: '未报名',
      enrollButtonText: '立即报名',
      canEnroll: true
    }
  }

  if (record.status === 'PENDING') {
    return {
      currentStatusText: '待审核',
      enrollButtonText: '待审核',
      canEnroll: false
    }
  }

  if (record.status === 'CHECKED_IN') {
    return {
      currentStatusText: '服务中',
      enrollButtonText: '服务中',
      canEnroll: false
    }
  }

  if (record.status === 'COMPLETED') {
    return {
      currentStatusText: '已完成',
      enrollButtonText: '已完成',
      canEnroll: false
    }
  }

  return {
    currentStatusText: '已报名',
    enrollButtonText: '已报名',
    canEnroll: false
  }
}

function buildTrackTimelineEvents(trackPoints = []) {
  const sorted = [...trackPoints].sort((left, right) => toTimestamp(left.recordedAt) - toTimestamp(right.recordedAt))
  return sorted.map((point, index) => {
    const previous = sorted[index - 1]
    const distance = previous ? calculateDistanceMeters(previous, point) : 0
    let title = '轨迹点记录'
    let description = `位置：${point.latitude}, ${point.longitude}`
    let typeClass = 'timeline-track'

    if (point.pointType === 'START') {
      title = '服务起点'
      description = '已记录签到位置'
      typeClass = 'timeline-start'
    } else if (point.pointType === 'END') {
      title = '服务终点'
      description = '已记录签退位置'
      typeClass = 'timeline-end'
    } else if (distance > 0 && distance < 30) {
      title = '停留点'
      description = `停留位置：${point.latitude}, ${point.longitude}`
      typeClass = 'timeline-stay'
    }

    return {
      id: `track-${point.id || index}`,
      time: formatDateTime(point.recordedAt),
      timestamp: toTimestamp(point.recordedAt),
      title,
      description,
      typeClass
    }
  })
}

function buildVolunteerTimeline(record, snapshots = [], trackPoints = []) {
  const events = []

  if (record && record.checkInTime) {
    events.push({
      id: `check-in-${record.id || 'record'}`,
      time: formatDateTime(record.checkInTime),
      timestamp: toTimestamp(record.checkInTime),
      title: '签到成功',
      description: '服务时段已开始',
      typeClass: 'timeline-start'
    })
  }

  buildTrackTimelineEvents(trackPoints).forEach((item) => events.push(item))

  ;(snapshots || []).forEach((snapshot, index) => {
    events.push({
      id: `snapshot-${snapshot.id || index}`,
      time: formatDateTime(snapshot.createdAt),
      timestamp: toTimestamp(snapshot.createdAt),
      title: '上传服务快照',
      description: snapshot.note || '已补充服务现场图片',
      typeClass: 'timeline-snapshot'
    })
  })

  if (record && record.checkOutTime) {
    events.push({
      id: `check-out-${record.id || 'record'}`,
      time: formatDateTime(record.checkOutTime),
      timestamp: toTimestamp(record.checkOutTime),
      title: '签退完成',
      description: '本次服务已结束，等待后续评价结算',
      typeClass: 'timeline-end'
    })
  }

  if (record && record.rewardFinalizedAt) {
    events.push({
      id: `reward-${record.id || 'record'}`,
      time: formatDateTime(record.rewardFinalizedAt),
      timestamp: toTimestamp(record.rewardFinalizedAt),
      title: '积分结算',
      description: `本次服务已结算 ${record.earnedPoints || 0} 积分`,
      typeClass: 'timeline-reward'
    })
  }

  return events.sort((left, right) => left.timestamp - right.timestamp)
}

function buildTrackMap(trackPoints = [], activity) {
  const sorted = [...trackPoints]
    .filter((item) => item && item.latitude != null && item.longitude != null)
    .sort((left, right) => toTimestamp(left.recordedAt) - toTimestamp(right.recordedAt))

  if (!sorted.length) {
    return {
      hasTrackMap: false,
      mapLatitude: Number(activity && activity.latitude) || 0,
      mapLongitude: Number(activity && activity.longitude) || 0,
      mapScale: 15,
      mapPolyline: [],
      mapCircles: [],
      mapIncludePoints: []
    }
  }

  const points = sorted.map((item) => ({
    latitude: Number(item.latitude),
    longitude: Number(item.longitude)
  }))

  const start = points[0]
  const end = points[points.length - 1]
  const stayCircles = []

  for (let index = 1; index < sorted.length - 1; index += 1) {
    const current = sorted[index]
    const previous = sorted[index - 1]
    const distance = calculateDistanceMeters(previous, current)
    if (distance > 0 && distance < 30) {
      stayCircles.push({
        latitude: Number(current.latitude),
        longitude: Number(current.longitude),
        color: '#f59e0b',
        fillColor: 'rgba(245, 158, 11, 0.28)',
        radius: 12,
        strokeWidth: 2
      })
    }
  }

  return {
    hasTrackMap: true,
    mapLatitude: start.latitude,
    mapLongitude: start.longitude,
    mapScale: 15,
    mapPolyline: [
      {
        points,
        color: '#2563eb',
        width: 8,
        arrowLine: true,
        borderWidth: 2,
        borderColor: '#93c5fd'
      }
    ],
    mapCircles: [
      {
        latitude: start.latitude,
        longitude: start.longitude,
        color: '#16a34a',
        fillColor: 'rgba(22, 163, 74, 0.28)',
        radius: 18,
        strokeWidth: 3
      },
      {
        latitude: end.latitude,
        longitude: end.longitude,
        color: '#ef4444',
        fillColor: 'rgba(239, 68, 68, 0.28)',
        radius: 18,
        strokeWidth: 3
      },
      ...stayCircles
    ],
    mapIncludePoints: points
  }
}

function resolveUploadUrl(uploadResult) {
  if (!uploadResult) {
    return ''
  }
  if (typeof uploadResult === 'string') {
    return uploadResult
  }
  return uploadResult.url || uploadResult.path || ''
}

function formatCoordinate(value) {
  const number = Number(value)
  if (!Number.isFinite(number)) {
    return '暂未获取'
  }
  return number.toFixed(6)
}

function buildLocationDebugState(activity, location) {
  const activityLatitude = Number(activity && activity.latitude)
  const activityLongitude = Number(activity && activity.longitude)
  const radius = Number(activity && activity.geofenceRadiusMeters) || 0
  const baseState = {
    activityCenterText:
      Number.isFinite(activityLatitude) && Number.isFinite(activityLongitude)
        ? `${formatCoordinate(activityLatitude)}, ${formatCoordinate(activityLongitude)}`
        : '暂未配置',
    currentLocationText: '暂未获取',
    distanceToCenterText: '待获取',
    geofenceStatusText: radius > 0 ? `范围半径 ${radius} 米` : '未配置围栏半径',
    geofenceStatusClass: '',
    currentLatitude: null,
    currentLongitude: null,
    distanceToCenterMeters: null,
    outOfRangeMeters: 0
  }

  if (!location || !Number.isFinite(Number(location.latitude)) || !Number.isFinite(Number(location.longitude))) {
    return baseState
  }

  const current = {
    latitude: Number(location.latitude),
    longitude: Number(location.longitude)
  }
  const center =
    Number.isFinite(activityLatitude) && Number.isFinite(activityLongitude)
      ? { latitude: activityLatitude, longitude: activityLongitude }
      : null
  const distance = center ? calculateDistanceMeters(current, center) : null
  const inRange = distance != null && radius > 0 ? distance <= radius : false
  const outOfRangeMeters = distance != null && radius > 0 ? Math.max(0, Math.round(distance - radius)) : 0

  return {
    activityCenterText: baseState.activityCenterText,
    currentLocationText: `${formatCoordinate(current.latitude)}, ${formatCoordinate(current.longitude)}`,
    distanceToCenterText: distance != null ? `${Math.round(distance)} 米` : '待获取',
    geofenceStatusText:
      distance == null || radius <= 0
        ? baseState.geofenceStatusText
        : inRange
          ? `在服务范围内（半径 ${radius} 米）`
          : `超出服务范围 ${outOfRangeMeters} 米`,
    geofenceStatusClass: distance == null || radius <= 0 ? '' : (inRange ? 'debug-status-success' : 'debug-status-danger'),
    currentLatitude: current.latitude,
    currentLongitude: current.longitude,
    distanceToCenterMeters: distance != null ? Math.round(distance) : null,
    outOfRangeMeters
  }
}

Page({
  data: {
    activityId: null,
    activity: null,
    loggedIn: false,
    currentRecord: null,
    currentStatusText: '未报名',
    enrollButtonText: '立即报名',
    canEnroll: true,
    checkInCode: '',
    displayCheckInCode: '登录后可见',
    displaySupplementEvidence: '未上传',
    snapshots: [],
    trackPoints: [],
    timelineEvents: [],
    supplementReason: '',
    supplementEvidenceUrl: '',
    trackSampling: false,
    mapLatitude: 0,
    mapLongitude: 0,
    mapScale: 15,
    mapPolyline: [],
    mapCircles: [],
    mapIncludePoints: [],
    hasTrackMap: false,
    activityCenterText: '暂未配置',
    currentLocationText: '暂未获取',
    distanceToCenterText: '待获取',
    geofenceStatusText: '待获取',
    geofenceStatusClass: '',
    currentLatitude: null,
    currentLongitude: null,
    distanceToCenterMeters: null,
    outOfRangeMeters: 0
  },

  onLoad(options) {
    const activityId = Number(options.id || options.activityId || 0)
    if (!activityId) {
      wx.showToast({
        title: '缺少活动信息',
        icon: 'none'
      })
      setTimeout(() => {
        wx.navigateBack({
          delta: 1
        })
      }, 300)
      return
    }

    this.setData({
      activityId
    })
  },

  onShow() {
    if (this.data.activityId) {
      this.loadPageData()
    }
  },

  loadPageData() {
    const activityId = this.data.activityId
    const loggedIn = app.hasLogin()

    const activityPromise = request({
      url: `/activities/${activityId}`,
      skipAuth: true
    })

    const snapshotsPromise = loggedIn
      ? request({ url: `/workflow/activities/${activityId}/snapshots` }).catch(() => [])
      : Promise.resolve([])

    const trackPointsPromise = loggedIn
      ? request({ url: `/workflow/activities/${activityId}/track-points` }).catch(() => [])
      : Promise.resolve([])

    const dashboardPromise = loggedIn
      ? request({ url: '/users/dashboard' }).catch(() => null)
      : Promise.resolve(null)

    Promise.all([activityPromise, snapshotsPromise, trackPointsPromise, dashboardPromise])
      .then(([activity, snapshots, trackPoints, dashboard]) => {
        const displayActivity = {
          ...activity,
          displayCategory: activity.category || '综合服务',
          displayDifficulty: toDifficultyLabel(activity.difficultyLevel),
          displayDemand: toDemandLabel(activity.demandLevel),
          displayStartTime: formatDateTime(activity.startTime),
          displayEndTime: formatDateTime(activity.endTime)
        }
        const currentRecord =
          (dashboard && dashboard.participationRecords || []).find(
            (item) => Number(item.activityId) === Number(activityId)
          ) || null

        const state = buildParticipationState(currentRecord, loggedIn)
        const safeSnapshots = (snapshots || []).map((item) => ({
          ...item,
          imageUrl: app.buildFileUrl(item.imageUrl),
          displayCreatedAt: formatDateTime(item.createdAt)
        }))
        const safeTrackPoints = (trackPoints || []).map((item) => ({
          ...item,
          latitude: Number(item.latitude),
          longitude: Number(item.longitude),
          displayRecordedAt: formatDateTime(item.recordedAt)
        }))
        const mapState = loggedIn ? buildTrackMap(safeTrackPoints, displayActivity) : buildTrackMap([], displayActivity)
        const latestLocation = safeTrackPoints.length ? safeTrackPoints[safeTrackPoints.length - 1] : null
        const locationDebugState = buildLocationDebugState(displayActivity, latestLocation)

        this.setData({
          activity: displayActivity,
          loggedIn,
          currentRecord,
          currentStatusText: state.currentStatusText,
          enrollButtonText: state.enrollButtonText,
          canEnroll: state.canEnroll,
          checkInCode: displayActivity.checkInCode || '',
          displayCheckInCode: loggedIn ? (displayActivity.checkInCode || '暂无') : '登录后可见',
          displaySupplementEvidence: this.data.supplementEvidenceUrl ? '已上传凭证' : '未上传',
          snapshots: safeSnapshots,
          trackPoints: safeTrackPoints,
          timelineEvents: loggedIn ? buildVolunteerTimeline(currentRecord, safeSnapshots, safeTrackPoints) : [],
          trackSampling: Boolean(currentRecord && currentRecord.status === 'CHECKED_IN'),
          ...locationDebugState,
          ...mapState
        })
      })
      .catch((error) => {
        wx.showToast({
          title: error.message || '加载活动详情失败',
          icon: 'none'
        })
      })
  },

  goLogin() {
    wx.reLaunch({
      url: '/pages/index/index'
    })
  },

  onCodeInput(event) {
    this.setData({
      checkInCode: normalizeCode(event.detail.value)
    })
  },

  onSupplementReasonInput(event) {
    this.setData({
      supplementReason: event.detail.value
    })
  },

  fillCurrentCode() {
    this.setData({
      checkInCode: normalizeCode(this.data.activity && this.data.activity.checkInCode)
    })
  },

  ensureLoggedIn() {
    return app.requireLogin({
      content: '该操作需要先登录后再进行。'
    })
  },

  enroll() {
    if (!this.ensureLoggedIn()) {
      return
    }

    if (!this.data.canEnroll) {
      wx.showToast({
        title: this.data.currentStatusText,
        icon: 'none'
      })
      return
    }

    request({
      url: `/activities/${this.data.activityId}/enroll`,
      method: 'POST',
      data: {}
    })
      .then(() => {
        wx.showToast({
          title: '报名成功',
          icon: 'success'
        })
        requestNotificationSubscription([
          'ENROLLMENT_APPROVED',
          'ENROLLMENT_REJECTED',
          'ACTIVITY_REMINDER'
        ])
        this.loadPageData()
      })
      .catch((error) => {
        wx.showToast({
          title: error.message || '报名失败',
          icon: 'none'
        })
      })
  },

  submitCodeCheckIn(code) {
    if (!this.ensureLoggedIn()) {
      return
    }

    if (this.data.currentRecord && this.data.currentRecord.status === 'PENDING') {
      wx.showToast({
        title: '报名待审核，暂不能签到',
        icon: 'none'
      })
      return
    }

    const checkInCode = normalizeCode(code || this.data.checkInCode || this.data.activity.checkInCode)
    if (!checkInCode) {
      wx.showToast({
        title: '请先获取签到码',
        icon: 'none'
      })
      return
    }

    request({
      url: `/activities/${this.data.activityId}/check-in`,
      method: 'POST',
      data: {
        method: 'QR_CODE',
        checkInCode
      }
    })
      .then(() => {
        wx.showToast({
          title: '签到成功',
          icon: 'success'
        })
        this.loadPageData()
      })
      .catch((error) => {
        wx.showToast({
          title: error.message || '签到失败',
          icon: 'none'
        })
      })
  },

  checkInByCode() {
    this.submitCodeCheckIn()
  },

  scanCodeCheckIn() {
    if (!this.ensureLoggedIn()) {
      return
    }

    wx.scanCode({
      onlyFromCamera: false,
      scanType: ['qrCode'],
      success: (res) => {
        const parsed = parseCheckInQrResult(res.result)
        if (parsed.activityId && Number(parsed.activityId) !== Number(this.data.activityId)) {
          wx.showToast({
            title: '二维码不属于当前活动',
            icon: 'none'
          })
          return
        }
        this.submitCodeCheckIn(parsed.checkInCode)
      },
      fail: () => {
        wx.showToast({
          title: '未识别到签到二维码',
          icon: 'none'
        })
      }
    })
  },

  requestLocation(success) {
    wx.getLocation({
      type: 'gcj02',
      success: (location) => {
        this.updateCurrentLocationDebug(location)
        if (typeof success === 'function') {
          success(location)
        }
      },
      fail: () => {
        wx.showModal({
          title: '无法获取定位',
          content: '请确认已授权位置信息，或使用动态二维码签到。',
          showCancel: false
        })
      }
    })
  },

  updateCurrentLocationDebug(location) {
    this.setData(buildLocationDebugState(this.data.activity, location))
  },

  refreshCurrentLocation() {
    this.requestLocation(() => {
      wx.showToast({
        title: '当前位置已刷新',
        icon: 'success'
      })
    })
  },

  checkInByLocation() {
    if (!this.ensureLoggedIn()) {
      return
    }

    if (this.data.currentRecord && this.data.currentRecord.status === 'PENDING') {
      wx.showToast({
        title: '报名待审核，暂不能签到',
        icon: 'none'
      })
      return
    }

    this.requestLocation((location) => {
      request({
        url: `/activities/${this.data.activityId}/check-in`,
        method: 'POST',
        data: {
          method: 'GEOFENCE',
          latitude: location.latitude,
          longitude: location.longitude
        }
      })
        .then(() => {
          wx.showToast({
            title: '围栏签到成功',
            icon: 'success'
          })
          this.loadPageData()
        })
        .catch((error) => {
          wx.showToast({
            title: error.message || '围栏签到失败',
            icon: 'none'
          })
        })
    })
  },

  checkOut() {
    if (!this.ensureLoggedIn()) {
      return
    }

    this.requestLocation((location) => {
      request({
        url: `/activities/${this.data.activityId}/check-out`,
        method: 'POST',
        data: {
          latitude: location.latitude,
          longitude: location.longitude
        }
      })
        .then(() => {
          wx.showToast({
            title: '签退成功',
            icon: 'success'
          })
          requestNotificationSubscription([
            'TASK_COMPLETED',
            'SERVICE_RATING_RECEIVED',
            'REWARD_FINALIZED'
          ])
          this.loadPageData()
        })
        .catch((error) => {
          wx.showToast({
            title: error.message || '签退失败',
            icon: 'none'
          })
        })
    })
  },

  uploadSnapshot() {
    if (!this.ensureLoggedIn()) {
      return
    }

    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (chooseRes) => {
        const filePath = chooseRes.tempFiles && chooseRes.tempFiles[0] && chooseRes.tempFiles[0].tempFilePath
        if (!filePath) {
          return
        }
        uploadFile({
          filePath,
          category: 'snapshot'
        })
          .then((uploadResult) => request({
            url: '/workflow/snapshots',
            method: 'POST',
            data: {
              activityId: this.data.activityId,
              imageUrl: resolveUploadUrl(uploadResult),
              note: '服务现场快照'
            }
          }))
          .then(() => {
            wx.showToast({
              title: '快照上传成功',
              icon: 'success'
            })
            this.loadPageData()
          })
          .catch((error) => {
            wx.showToast({
              title: error.message || '快照上传失败',
              icon: 'none'
            })
          })
      }
    })
  },

  recordTrackPoint() {
    if (!this.ensureLoggedIn()) {
      return
    }

    this.requestLocation((location) => {
      request({
        url: '/workflow/track-points',
        method: 'POST',
        data: {
          activityId: this.data.activityId,
          latitude: location.latitude,
          longitude: location.longitude,
          pointType: 'MIDDLE',
          recordedAt: formatSubmitDateTime()
        }
      })
        .then(() => {
          wx.showToast({
            title: '足迹已记录',
            icon: 'success'
          })
          this.loadPageData()
        })
        .catch((error) => {
          wx.showToast({
            title: error.message || '足迹记录失败',
            icon: 'none'
          })
        })
    })
  },

  previewImage(event) {
    const current = event.currentTarget.dataset.url
    if (!current) {
      return
    }
    wx.previewImage({
      current,
      urls: this.data.snapshots.map((item) => item.imageUrl)
    })
  },

  uploadSupplementEvidence() {
    if (!this.ensureLoggedIn()) {
      return
    }

    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (chooseRes) => {
        const filePath = chooseRes.tempFiles && chooseRes.tempFiles[0] && chooseRes.tempFiles[0].tempFilePath
        if (!filePath) {
          return
        }
        uploadFile({
          filePath,
          category: 'supplement'
        })
          .then((uploadResult) => {
            this.setData({
              supplementEvidenceUrl: resolveUploadUrl(uploadResult),
              displaySupplementEvidence: '已上传凭证'
            })
            wx.showToast({
              title: '补签凭证已上传',
              icon: 'success'
            })
          })
          .catch((error) => {
            wx.showToast({
              title: error.message || '补签凭证上传失败',
              icon: 'none'
            })
          })
      }
    })
  },

  submitSupplement(type) {
    if (!this.ensureLoggedIn()) {
      return
    }

    if (!this.data.supplementReason.trim()) {
      wx.showToast({
        title: '请先填写补签原因',
        icon: 'none'
      })
      return
    }

    if (!this.data.supplementEvidenceUrl) {
      wx.showToast({
        title: '请先上传补签凭证',
        icon: 'none'
      })
      return
    }

    request({
      url: '/workflow/supplements',
      method: 'POST',
      data: {
        activityId: this.data.activityId,
        type,
        requestedTime: formatSubmitDateTime(),
        reason: this.data.supplementReason.trim(),
        evidenceImageUrl: this.data.supplementEvidenceUrl
      }
    })
      .then(() => {
        wx.showToast({
          title: '补签申请已提交',
          icon: 'success'
        })
        requestNotificationSubscription([
          'SUPPLEMENT_SUBMITTED',
          'SUPPLEMENT_APPROVED',
          'SUPPLEMENT_REJECTED'
        ])
        this.setData({
          supplementReason: ''
        })
      })
      .catch((error) => {
        wx.showToast({
          title: error.message || '补签申请失败',
          icon: 'none'
        })
      })
  },

  applyCheckInSupplement() {
    this.submitSupplement('CHECK_IN')
  },

  applyCheckOutSupplement() {
    this.submitSupplement('CHECK_OUT')
  }
})
