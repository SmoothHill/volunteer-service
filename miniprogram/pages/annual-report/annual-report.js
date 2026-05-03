const { request } = require('../../utils/request')
const { formatParticipationRecord } = require('../../utils/display')

function buildYearOptions() {
  const currentYear = new Date().getFullYear()
  return Array.from({ length: 5 }, (_, index) => currentYear - index)
}

function formatDateTime(value, fallback = '暂无') {
  if (!value) {
    return fallback
  }
  return String(value).replace('T', ' ').replace(/\.\d+$/, '')
}

function formatPointType(pointType) {
  if (pointType === 'START') return '起点'
  if (pointType === 'MIDDLE') return '途中'
  if (pointType === 'END') return '终点'
  if (pointType === 'SITE') return '服务地点'
  if (pointType === 'STAY') return '停留点'
  return pointType || '足迹点'
}

function normalizeRepresentativeRecord(record) {
  const formatted = formatParticipationRecord(record)
  return {
    ...formatted,
    displayCreatedAt: formatDateTime(record.createdAt),
    displayCheckInTime: formatDateTime(record.checkInTime, '未签到'),
    displayCheckOutTime: formatDateTime(record.checkOutTime, '未签退')
  }
}

function buildMapData(footprints = []) {
  const points = footprints
    .filter((item) => Number.isFinite(Number(item.latitude)) && Number.isFinite(Number(item.longitude)))
    .map((item) => ({
      ...item,
      latitude: Number(item.latitude),
      longitude: Number(item.longitude),
      displayRecordedAt: formatDateTime(item.recordedAt),
      displayPointType: formatPointType(item.pointType)
    }))

  if (!points.length) {
    return {
      hasMap: false,
      markers: [],
      polyline: [],
      includePoints: [],
      mapLatitude: 30.5728,
      mapLongitude: 104.0668,
      mapScale: 11,
      footprintList: []
    }
  }

  const uniqueMarkers = []
  const usedKeys = new Set()
  points.forEach((item, index) => {
    const key = `${item.activityId}-${item.latitude.toFixed(4)}-${item.longitude.toFixed(4)}`
    if (usedKeys.has(key) || uniqueMarkers.length >= 12) {
      return
    }
    usedKeys.add(key)
    uniqueMarkers.push({
      id: index + 1,
      latitude: item.latitude,
      longitude: item.longitude,
      width: 28,
      height: 28,
      callout: {
        content: item.activityTitle || item.displayPointType,
        display: 'BYCLICK',
        padding: 8,
        borderRadius: 8,
        bgColor: '#ffffff',
        color: '#102a43'
      }
    })
  })

  return {
    hasMap: true,
    markers: uniqueMarkers,
    polyline: [{
      points: points.map((item) => ({
        latitude: item.latitude,
        longitude: item.longitude
      })),
      color: '#0052D9',
      width: 4,
      dottedLine: false,
      arrowLine: true,
      borderColor: '#7fb3ff',
      borderWidth: 1
    }],
    includePoints: points.map((item) => ({
      latitude: item.latitude,
      longitude: item.longitude
    })),
    mapLatitude: points[0].latitude,
    mapLongitude: points[0].longitude,
    mapScale: 12,
    footprintList: points.slice(0, 10)
  }
}

function normalizeReport(report) {
  const mapData = buildMapData(report.footprints || [])
  return {
    ...report,
    displaySummaryText: report.summaryText || '这里汇总了你本年度的服务记录。',
    displayTopCategory: report.topCategory || '综合服务',
    displayCoveredAreas: (report.coveredAreas || []).length ? report.coveredAreas.join('、') : '暂无',
    displayBestActivityTitle: report.bestActivityTitle || '暂无',
    displayBestActivityRating: report.bestActivityRating ? `${report.bestActivityRating} 分` : '暂无',
    representativeRecords: (report.representativeRecords || []).map(normalizeRepresentativeRecord),
    ...mapData
  }
}

Page({
  data: {
    loading: true,
    yearOptions: buildYearOptions(),
    selectedYearIndex: 0,
    report: null
  },

  onShow() {
    this.loadAnnualReport()
  },

  getSelectedYear() {
    return this.data.yearOptions[this.data.selectedYearIndex]
  },

  onYearChange(event) {
    const selectedYearIndex = Number(event.detail.value || 0)
    this.setData({
      selectedYearIndex
    })
    this.loadAnnualReport()
  },

  loadAnnualReport() {
    this.setData({ loading: true })
    request({
      url: `/users/annual-report?year=${this.getSelectedYear()}`
    }).then((report) => {
      this.setData({
        loading: false,
        report: normalizeReport(report || {})
      })
    }).catch((error) => {
      this.setData({ loading: false })
      wx.showToast({
        title: error.message || '加载年报失败',
        icon: 'none'
      })
    })
  },

  openRepresentativeActivity(event) {
    const { activityId } = event.currentTarget.dataset
    if (!activityId) {
      return
    }
    wx.navigateTo({
      url: `/pages/activity-detail/activity-detail?id=${activityId}`
    })
  },

  goBack() {
    wx.navigateBack({
      delta: 1,
      fail: () => {
        wx.reLaunch({
          url: '/pages/dashboard/dashboard'
        })
      }
    })
  }
})
