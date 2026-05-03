import * as VueRuntime from './node_modules/@vue/runtime-dom/dist/runtime-dom.esm-browser.js?v=20260419-12';
import { compile } from './node_modules/@vue/compiler-dom/dist/compiler-dom.esm-browser.js?v=20260419-12';

const { createApp, reactive, ref, computed, onMounted, nextTick, watch } = VueRuntime;

const API = 'http://127.0.0.1:8082/api';
const TOKEN_KEY = 'volunteer_admin_web_token';
const BASE_KEY = 'volunteer_admin_web_base_url';

const UI = {
  appTitle: '\u7389\u5c4f\u5fd7\u613f\u5e73\u53f0\u540e\u53f0',
  systemName: '\u7389\u5c4f\u5fd7\u613f\u5e73\u53f0',
  systemAdminName: '\u7cfb\u7edf\u8d85\u7ea7\u7ba1\u7406\u5458',
  superAdmin: '\u8d85\u7ea7\u7ba1\u7406\u5458',
  restoringTitle: '\u6b63\u5728\u6062\u590d\u4f1a\u8bdd',
  restoringDesc: '\u6b63\u5728\u68c0\u67e5\u767b\u5f55\u72b6\u6001\u5e76\u52a0\u8f7d\u6838\u5fc3\u6570\u636e\u3002',
  loginTitle: '\u7389\u5c4f\u5fd7\u613f\u5e73\u53f0\u540e\u53f0',
  loginDesc: '\u767b\u5f55\u540e\u53ef\u8fdb\u5165\u5e73\u53f0\u7684\u6838\u5fc3\u7ba1\u7406\u4e0e\u5ba1\u6838\u6a21\u5757\u3002',
  loginApi: '\u63a5\u53e3\u5730\u5740',
  loginUser: '\u8d26\u53f7',
  loginPassword: '\u5bc6\u7801',
  loginButton: '\u767b\u5f55\u540e\u53f0',
  navTitle: '\u5bfc\u822a\u83dc\u5355',
  rolePrefix: '\u89d2\u8272\uff1a',
  userPrefix: '\u5f53\u524d\u7528\u6237\uff1a',
  refresh: '\u5237\u65b0\u6570\u636e',
  logout: '\u9000\u51fa\u767b\u5f55',
  headerTip: '\u6838\u5fc3\u6570\u636e\u3001\u5ba1\u6838\u4efb\u52a1\u4e0e\u8fd0\u8425\u8bbe\u7f6e',
  profileTitle: '\u5f53\u524d\u8eab\u4efd',
  profileEmpty: '\u7ba1\u7406',
};

const TEXT = {
  backendOffline: '\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u786e\u8ba4\u540e\u7aef\u670d\u52a1\u5df2\u542f\u52a8\u3002',
  requestFailed: '\u8bf7\u6c42\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5\u3002',
  refreshSuccess: '\u6570\u636e\u5df2\u5237\u65b0\u3002',
  sessionInvalid: '\u5f53\u524d\u767b\u5f55\u6001\u4e0d\u662f\u8d85\u7ea7\u7ba1\u7406\u5458\u3002',
  loginResponseInvalid: '\u767b\u5f55\u54cd\u5e94\u65e0\u6548\u3002',
  loginSuccess: '\u767b\u5f55\u6210\u529f\u3002',
  logoutSuccess: '\u5df2\u9000\u51fa\u767b\u5f55\u3002',
  roleUpdated: '\u89d2\u8272\u66f4\u65b0\u6210\u529f\u3002',
  applicationApproved: '\u7533\u8bf7\u5df2\u901a\u8fc7\u3002',
  applicationRejected: '\u7533\u8bf7\u5df2\u9a73\u56de\u3002',
  activityCreated: '\u6d3b\u52a8\u521b\u5efa\u6210\u529f\u3002',
  codeRefreshed: '\u7b7e\u5230\u7801\u5df2\u5237\u65b0\u3002',
  settingsSaved: '\u7cfb\u7edf\u8bbe\u7f6e\u5df2\u4fdd\u5b58\u3002',
  demoInitialized: '\u6f14\u793a\u6570\u636e\u5df2\u521d\u59cb\u5316\u3002',
  demoReset: '\u6f14\u793a\u6570\u636e\u5df2\u91cd\u7f6e\u3002',
  copySuccess: '\u5730\u5740\u5df2\u590d\u5236\u3002',
  copyFallback: '\u8bf7\u624b\u52a8\u590d\u5236\u4ee5\u4e0b\u5730\u5740\uff1a',
};

const MENUS = [
  { key: 'overview', label: '\u6982\u89c8\u53f0' },
  { key: 'users', label: '\u7528\u6237\u7ba1\u7406' },
  { key: 'analytics', label: '\u6570\u636e\u5206\u6790' },
  { key: 'leaderboard', label: '\u6392\u884c\u699c' },
  { key: 'community-insights', label: '\u793e\u533a\u70ed\u529b' },
  { key: 'store', label: '\u79ef\u5206\u5546\u57ce' },
  { key: 'my-center', label: '\u6211\u7684\u4e2d\u5fc3' },
  { key: 'notifications', label: '\u6d88\u606f\u4e2d\u5fc3' },
  { key: 'activities', label: '\u6d3b\u52a8\u7ba1\u7406' },
  { key: 'service-sites', label: '\u4f4d\u7f6e\u7ba1\u7406' },
  { key: 'service-records', label: '\u670d\u52a1\u8bb0\u5f55' },
  { key: 'supplement-review', label: '\u8865\u7b7e\u5ba1\u6838' },
  { key: 'evidence-center', label: '\u8bc1\u636e\u4e2d\u5fc3' },
  { key: 'anomalies', label: '\u5f02\u5e38\u8bb0\u5f55' },
  { key: 'settings', label: '\u7cfb\u7edf\u8bbe\u7f6e' },
];

const ROLE_TEXT = {
  VOLUNTEER: '\u5fd7\u613f\u8005',
  ADMIN: '\u6d3b\u52a8\u7ba1\u7406\u5458',
  SUPER_ADMIN: '\u8d85\u7ea7\u7ba1\u7406\u5458',
};

const ROLE_CLASS = {
  VOLUNTEER: 'theme-primary',
  ADMIN: 'theme-success',
  SUPER_ADMIN: 'theme-warning',
};

const APP_STATUS_TEXT = {
  PENDING: '\u5f85\u5ba1\u6838',
  APPROVED: '\u5df2\u901a\u8fc7',
  REJECTED: '\u5df2\u9a73\u56de',
};

const APP_STATUS_CLASS = {
  PENDING: 'theme-warning',
  APPROVED: 'theme-success',
  REJECTED: 'theme-danger',
};

const PARTICIPATION_STATUS_TEXT = {
  PENDING: '\u5f85\u5ba1\u6838',
  APPROVED: '\u5df2\u62a5\u540d',
  CHECKED_IN: '\u670d\u52a1\u4e2d',
  COMPLETED: '\u5df2\u5b8c\u6210',
  REJECTED: '\u5df2\u9a73\u56de',
};
const ANOMALY_STATUS_TEXT = {
  PENDING: '\u5f85\u5904\u7406',
  CONFIRMED: '\u5df2\u786e\u8ba4',
  IGNORED: '\u5df2\u5ffd\u7565',
};
const TRACK_POINT_TYPE_TEXT = {
  START: '\u8d77\u70b9\u8f68\u8ff9',
  MIDDLE: '\u4e2d\u9014\u8f68\u8ff9',
  END: '\u7ec8\u70b9\u8f68\u8ff9',
  STAY: '\u505c\u7559\u70b9',
};
const BLOCKCHAIN_EVENT_TYPE_TEXT = {
  CHECK_IN: '\u7b7e\u5230\u5b58\u8bc1',
  CHECK_OUT: '\u7b7e\u9000\u5b58\u8bc1',
  SNAPSHOT_UPLOAD: '\u5feb\u7167\u5b58\u8bc1',
  TRACK_SUMMARY: '\u8f68\u8ff9\u6c47\u603b\u5b58\u8bc1',
  ORGANIZER_CONFIRM: '\u7ec4\u7ec7\u8005\u786e\u8ba4\u5b58\u8bc1',
  REWARD_FINALIZE: '\u7ed3\u7b97\u5b58\u8bc1',
};
const BLOCKCHAIN_PROOF_STATUS_TEXT = {
  PENDING: '\u5f85\u4e0a\u94fe',
  MOCK_CHAINED: 'Mock \u5df2\u5b58\u8bc1',
  FAILED: '\u5b58\u8bc1\u5931\u8d25',
};
const ACTIVITY_CATEGORIES = [
  '\u793e\u533a\u670d\u52a1',
  '\u73af\u4fdd\u884c\u52a8',
  '\u52a9\u8001\u670d\u52a1',
  '\u52a9\u6b8b\u5e2e\u6276',
  '\u516c\u76ca\u5ba3\u4f20',
  '\u8d5b\u4e8b\u4fdd\u969c',
  '\u5e94\u6025\u670d\u52a1',
  '\u6559\u80b2\u652f\u6301',
  '\u533b\u7597\u534f\u52a9',
  '\u7efc\u5408\u670d\u52a1',
];
const STORE_CATEGORIES = [
  '\u65e5\u5e38\u7528\u54c1',
  '\u865a\u62df\u6743\u76ca',
  '\u516c\u76ca\u6743\u76ca',
  '\u5b66\u4e60\u6210\u957f',
  '\u6587\u521b\u5468\u8fb9',
  '\u5b9e\u4f53\u793c\u54c1',
];
const ACTIVITY_STATUS_TEXT = {
  DRAFT: '\u8349\u7a3f',
  PUBLISHED: '\u5df2\u53d1\u5e03',
  FINISHED: '\u5df2\u7ed3\u675f',
};

const NOTIFICATION_TYPE_TEXT = {
  ENROLLMENT_SUBMITTED: '\u62a5\u540d\u5df2\u63d0\u4ea4',
  ENROLLMENT_PENDING_REVIEW: '\u6709\u65b0\u7684\u62a5\u540d\u5f85\u5ba1\u6838',
  ENROLLMENT_APPROVED: '\u62a5\u540d\u5ba1\u6838\u901a\u8fc7',
  ENROLLMENT_REJECTED: '\u62a5\u540d\u5ba1\u6838\u9a73\u56de',
  ENROLLMENT_REVIEW_PROCESSED: '\u62a5\u540d\u5ba1\u6838\u56de\u6267',
  SUPPLEMENT_SUBMITTED: '\u8865\u7b7e\u7533\u8bf7\u5df2\u63d0\u4ea4',
  SUPPLEMENT_PENDING_REVIEW: '\u6709\u65b0\u7684\u8865\u7b7e\u5f85\u5ba1\u6838',
  SUPPLEMENT_APPROVED: '\u8865\u7b7e\u5ba1\u6838\u901a\u8fc7',
  SUPPLEMENT_REJECTED: '\u8865\u7b7e\u5ba1\u6838\u9a73\u56de',
  SUPPLEMENT_REVIEW_PROCESSED: '\u8865\u7b7e\u5ba1\u6838\u56de\u6267',
  ACTIVITY_REMINDER: '\u6d3b\u52a8\u5f00\u59cb\u63d0\u9192',
  TASK_COMPLETED: '\u5fd7\u613f\u4efb\u52a1\u5b8c\u6210',
  SERVICE_CONFIRM_PENDING: '\u670d\u52a1\u5b8c\u6210\u5f85\u786e\u8ba4',
  SERVICE_RATING_RECEIVED: '\u83b7\u5f97\u670d\u52a1\u8bc4\u5206',
  REWARD_FINALIZED: '\u670d\u52a1\u7ed3\u7b97\u901a\u77e5',
  REWARD_FINALIZED_PROCESSED: '\u670d\u52a1\u7ed3\u7b97\u56de\u6267',
  REDEMPTION_CREATED: '\u5151\u6362\u6210\u529f',
  REDEMPTION_DELIVERED: '\u5151\u6362\u53d1\u653e\u901a\u77e5',
  REDEMPTION_CREATED_OPERATOR: '\u6709\u65b0\u7684\u5151\u6362\u8bb0\u5f55',
  ADMIN_APPLICATION_SUBMITTED: '\u6709\u65b0\u7684\u7ba1\u7406\u5458\u7533\u8bf7',
  ADMIN_APPLICATION_APPROVED: '\u7ba1\u7406\u5458\u7533\u8bf7\u901a\u8fc7',
  ADMIN_APPLICATION_REJECTED: '\u7ba1\u7406\u5458\u7533\u8bf7\u9a73\u56de',
  ADMIN_APPLICATION_REVIEW_PROCESSED: '\u7ba1\u7406\u5458\u5ba1\u6838\u56de\u6267',
};

const NOTIFICATION_STATUS_TEXT = {
  PENDING: '\u5f85\u53d1\u9001',
  SENT: '\u5df2\u53d1\u9001',
  FAILED: '\u53d1\u9001\u5931\u8d25',
};

function emptyOverview() {
  return {
    totalActivities: 0,
    totalVolunteers: 0,
    completedRecords: 0,
    totalPointsIssued: 0,
    pendingSupplements: 0,
    pendingAnomalies: 0,
    pendingRedemptions: 0,
    lowStockItems: 0,
  };
}

function emptyAnnualOverview() {
  return {
    year: new Date().getFullYear(),
    totalActivities: 0,
    totalServiceHours: 0,
    activeVolunteers: 0,
    activeAdmins: 0,
    totalServiceCount: 0,
    hotAreas: [],
    categoryStats: [],
    monthlyActivityTrends: [],
    monthlyServiceHourTrends: [],
    communityHeatmap: [],
    activityHeatmap: [],
    adminLeaderboard: [],
    volunteerLeaderboard: [],
  };
}

function formatStarLeaderboardItem(item) {
  return {
    ...item,
    displayPeriod: item.period === 'month' ? '\u6708\u699c' : '\u5468\u699c',
    displayStarScore: Number(item.starScore || 0).toFixed(1),
    displayPositiveRatingRate: `${Number(item.positiveRatingRate || 0).toFixed(1)}%`,
    displayTrackCoverageScore: Number(item.trackCoverageScore || 0).toFixed(1),
    displayTotalHours: Number(item.totalHours || 0).toFixed(1),
    displayShowcaseText: item.showcaseText || '\u6301\u7eed\u670d\u52a1\u793e\u533a\uff0c\u5c55\u73b0\u7a33\u5b9a\u7684\u5fd7\u613f\u670d\u52a1\u8868\u73b0\u3002',
  };
}

function formatCommunityInsightItem(item) {
  return {
    ...item,
    displayDemandIndex: Number(item.demandIndex || 0),
    displayVitalityIndex: Number(item.vitalityIndex || 0),
    displayGapCount: Number(item.gapCount || 0),
    displayTrackCoverageScore: Number(item.trackCoverageScore || 0).toFixed(1),
    displayLocation: item.location || item.communityName || '\u672a\u6807\u6ce8\u5730\u70b9',
    displayAreaTag: item.areaTag || '\u5747\u8861\u670d\u52a1\u533a',
    displayGuidanceText: item.guidanceText || '\u5f53\u524d\u533a\u57df\u4f9b\u9700\u76f8\u5bf9\u5e73\u8861\uff0c\u53ef\u7ee7\u7eed\u89c2\u5bdf\u3002',
  };
}

function emptyAnalyticsUserDetail() {
  return {
    userId: null,
    userName: '',
    avatarUrl: '',
    role: '',
    verified: false,
    realName: '',
    maskedIdCardNo: '',
    volunteerCardNo: '',
    badgeName: '',
    totalHours: 0,
    totalPoints: 0,
    creditScore: 0,
    createdAt: '',
    leaderboardRank: null,
    annualReport: {
      year: new Date().getFullYear(),
      totalServiceHours: 0,
      totalActivities: 0,
      totalPoints: 0,
      totalCertificates: 0,
      bestActivityTitle: '',
      bestActivityRating: null,
      topCategory: '',
      summaryText: '',
      coveredAreas: [],
      footprints: [],
      representativeRecords: [],
    },
    participationRecords: [],
    relatedHeatmapActivities: [],
    evidenceSummary: {
      snapshotCount: 0,
      trackPointCount: 0,
      blockchainProofCount: 0,
      anomalyCount: 0,
    },
  };
}

function emptySettings() {
  return {
    systemName: UI.systemName,
    defaultGeofenceRadiusMeters: 300,
    minTrackPoints: 3,
    minSnapshotCount: 1,
    minServiceHours: 0.5,
    frequentSupplementLimit30Days: 3,
    certificateTitle: '\u5fd7\u613f\u670d\u52a1\u8bc1\u4e66',
    supplementApproveComment: '\u5ba1\u6838\u901a\u8fc7',
    adminWebUrl: '',
  };
}

function emptyForm(radius = 300) {
  return {
    serviceSiteId: '',
    title: '',
    category: ACTIVITY_CATEGORIES[0],
    description: '',
    location: '',
    capacity: 20,
    demandLevel: 3,
    difficultyLevel: 1,
    difficultyCoefficient: 1,
    geofenceRadiusMeters: radius,
    latitude: 30.5728,
    longitude: 104.0668,
    startTime: '',
    endTime: '',
  };
}

function emptySiteForm() {
  return {
    id: '',
    name: '',
    streetName: '',
    communityName: '',
    address: '',
    latitude: 30.5728,
    longitude: 104.0668,
    recommendedRadiusMeters: 300,
    enabled: true,
  };
}

function emptyStoreForm() {
  return {
    id: '',
    name: '',
    category: STORE_CATEGORIES[0],
    description: '',
    pointsCost: 0,
    stock: 0,
    active: true,
    deliveryType: 'VIRTUAL',
  };
}

function normalizeCategory(value, categories) {
  const text = String(value || '').trim();
  return categories.includes(text) ? text : categories[0];
}

function normalizeBase(value) {
  const text = String(value || '').trim();
  if (!text) return API;
  const normalized = text.endsWith('/') ? text.slice(0, -1) : text;
  return normalized.endsWith('/api') ? normalized : `${normalized}/api`;
}

function buildCheckInQrValue(activityId, checkInCode) {
  if (!activityId || !checkInCode) return '';
  return `volunteer-checkin://check-in?activityId=${activityId}&checkInCode=${encodeURIComponent(checkInCode)}`;
}

function buildCheckInQrImageUrl(activity) {
  const qrValue = buildCheckInQrValue(activity && activity.id, activity && activity.checkInCode);
  if (!qrValue) return '';
  return `https://api.qrserver.com/v1/create-qr-code/?size=120x120&margin=8&data=${encodeURIComponent(qrValue)}`;
}

function formatTime(value) {
  if (!value) return '-';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return String(value).replace('T', ' ');
  const pad = (n) => String(n).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}

function maskIdCardNo(value) {
  const text = String(value || '').trim();
  if (!text) return '\u672a\u586b\u5199';
  if (text.length <= 5) return text;
  return `${text.slice(0, 3)}${'*'.repeat(Math.max(1, text.length - 5))}${text.slice(-2)}`;
}

function maskRealName(value) {
  const text = String(value || '').trim();
  if (!text) return '\u672a\u8ba4\u8bc1';
  return `${text.slice(0, 1)}*`;
}

function maskVolunteerCardNo(value) {
  const text = String(value || '').trim();
  if (!text) return '\u672a\u586b\u5199';
  if (text.length <= 6) return text;
  return `${text.slice(0, 3)}${'*'.repeat(Math.max(1, text.length - 6))}${text.slice(-3)}`;
}

function toTimestamp(value) {
  if (!value) return 0;
  const timestamp = new Date(String(value).replace(' ', 'T')).getTime();
  return Number.isNaN(timestamp) ? 0 : timestamp;
}

function escapeHtml(value) {
  return String(value ?? '')
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

function formatProofDate(value) {
  if (!value) return '-';
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return String(value).split(' ')[0] || String(value);
  }
  const pad = (n) => String(n).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
}

function buildCertificatePrintHtml(proof, resolveFileUrl) {
  const snapshots = Array.isArray(proof && proof.snapshots) ? proof.snapshots : [];
  const snapshotHtml = snapshots.length
    ? snapshots.map((item) => `
        <div class="snapshot-item">
          <img src="${escapeHtml(resolveFileUrl(item.imageUrl))}" alt="服务快照">
          <div class="snapshot-note">${escapeHtml(item.note || '服务快照')}</div>
          <div class="snapshot-time">${escapeHtml(formatTime(item.createdAt))}</div>
        </div>
      `).join('')
    : '<div class="snapshot-empty">暂无服务快照</div>';

  const certificateLink = proof && proof.certificateUrl
    ? `<a class="certificate-link" href="${escapeHtml(resolveFileUrl(proof.certificateUrl))}" target="_blank">查看电子证书</a>`
    : '<span class="certificate-link muted">暂无电子证书图片</span>';

  return `<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <title>${escapeHtml((proof && proof.certificateTitle) || '志愿服务证明')}</title>
  <style>
    * { box-sizing: border-box; }
    body { margin: 0; padding: 32px; font-family: "Microsoft YaHei", "PingFang SC", sans-serif; color: #0f172a; background: #f8fafc; }
    .sheet { max-width: 960px; margin: 0 auto; padding: 36px 40px; background: #fff; border: 1px solid #dbe5f2; }
    .title { text-align: center; font-size: 28px; font-weight: 700; letter-spacing: 2px; }
    .subtitle { margin-top: 8px; text-align: center; color: #64748b; font-size: 14px; }
    .section { margin-top: 28px; }
    .section-title { margin-bottom: 12px; font-size: 18px; font-weight: 700; }
    .grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 14px 18px; }
    .item { min-height: 72px; padding: 14px 16px; border: 1px solid #dbe5f2; background: #f8fbff; }
    .label { color: #64748b; font-size: 13px; }
    .value { margin-top: 8px; font-size: 16px; font-weight: 600; word-break: break-all; }
    .full { grid-column: 1 / -1; }
    .summary { min-height: 110px; white-space: pre-wrap; }
    .snapshots { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 16px; }
    .snapshot-item { border: 1px solid #dbe5f2; padding: 12px; background: #f8fbff; }
    .snapshot-item img { width: 100%; height: 180px; object-fit: cover; display: block; background: #e2e8f0; }
    .snapshot-note { margin-top: 10px; font-size: 14px; font-weight: 600; }
    .snapshot-time { margin-top: 6px; color: #64748b; font-size: 12px; }
    .snapshot-empty { padding: 18px; border: 1px dashed #cbd5e1; color: #94a3b8; text-align: center; }
    .certificate-link { color: #0052d9; text-decoration: none; }
    .muted { color: #94a3b8; }
    .footer { margin-top: 40px; display: flex; justify-content: space-between; gap: 24px; }
    .seal-box { width: 220px; min-height: 120px; border: 1px dashed #94a3b8; display: flex; align-items: center; justify-content: center; color: #94a3b8; }
    .footer-meta { flex: 1; }
    .footer-line { margin-top: 10px; font-size: 14px; line-height: 1.8; }
    @media print {
      body { padding: 0; background: #fff; }
      .sheet { border: none; max-width: none; }
    }
  </style>
</head>
<body>
  <div class="sheet">
    <div class="title">${escapeHtml((proof && proof.certificateTitle) || '志愿服务证明')}</div>
    <div class="subtitle">${escapeHtml((proof && proof.systemName) || '玉屏志愿平台')}</div>

    <div class="section">
      <div class="section-title">志愿者信息</div>
      <div class="grid">
        <div class="item"><div class="label">志愿者姓名</div><div class="value">${escapeHtml(proof && proof.volunteerName)}</div></div>
        <div class="item"><div class="label">志愿者号</div><div class="value">${escapeHtml((proof && proof.volunteerCardNo) || '-')}</div></div>
        <div class="item full"><div class="label">身份证号（脱敏）</div><div class="value">${escapeHtml((proof && proof.maskedIdCardNo) || '-')}</div></div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">服务信息</div>
      <div class="grid">
        <div class="item"><div class="label">活动名称</div><div class="value">${escapeHtml(proof && proof.activityTitle)}</div></div>
        <div class="item"><div class="label">服务地点</div><div class="value">${escapeHtml((proof && proof.activityLocation) || '-')}</div></div>
        <div class="item"><div class="label">活动时间</div><div class="value">${escapeHtml(formatTime(proof && proof.activityStartTime))} 至 ${escapeHtml(formatTime(proof && proof.activityEndTime))}</div></div>
        <div class="item"><div class="label">签到签退</div><div class="value">${escapeHtml(formatTime(proof && proof.checkInTime))} 至 ${escapeHtml(formatTime(proof && proof.checkOutTime))}</div></div>
        <div class="item"><div class="label">服务时长</div><div class="value">${escapeHtml((proof && proof.serviceHours) || 0)} 小时</div></div>
        <div class="item"><div class="label">服务评分 / 积分</div><div class="value">${escapeHtml((proof && proof.serviceRating) || '-')} 分 / ${escapeHtml((proof && proof.earnedPoints) || 0)} 积分</div></div>
        <div class="item"><div class="label">组织者确认时间</div><div class="value">${escapeHtml(formatTime(proof && proof.organizerConfirmedAt))}</div></div>
        <div class="item"><div class="label">出具日期</div><div class="value">${escapeHtml(formatProofDate(proof && proof.issuedAt))}</div></div>
        <div class="item full summary"><div class="label">服务评价 / 备注</div><div class="value">${escapeHtml((proof && (proof.serviceComment || proof.organizerConfirmComment)) || '无')}</div></div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">证明与存证</div>
      <div class="grid">
        <div class="item"><div class="label">证书编号</div><div class="value">${escapeHtml((proof && proof.certificateNo) || '-')}</div></div>
        <div class="item"><div class="label">电子证书</div><div class="value">${certificateLink}</div></div>
        <div class="item full"><div class="label">原始哈希</div><div class="value">${escapeHtml((proof && proof.evidenceHash) || '-')}</div></div>
        <div class="item"><div class="label">区块链存证状态</div><div class="value">${escapeHtml((proof && proof.blockchainProofStatus) || '-')}</div></div>
        <div class="item"><div class="label">存证流水号</div><div class="value">${escapeHtml((proof && proof.blockchainTransactionNo) || '-')}</div></div>
      </div>
    </div>

    <div class="section">
      <div class="section-title">服务快照</div>
      <div class="snapshots">${snapshotHtml}</div>
    </div>

    <div class="footer">
      <div class="footer-meta">
        <div class="footer-line">出具平台：${escapeHtml((proof && proof.systemName) || '玉屏志愿平台')}</div>
        <div class="footer-line">证明依据服务记录、快照与确认信息生成。</div>
      </div>
      <div class="seal-box">盖章区</div>
    </div>
  </div>
</body>
</html>`;
}

function distanceMeters(first, second) {
  if (!first || !second) return 0;
  const earthRadius = 6371000;
  const toRad = (degree) => degree * Math.PI / 180;
  const lat1 = toRad(Number(first.latitude));
  const lat2 = toRad(Number(second.latitude));
  const deltaLat = toRad(Number(second.latitude) - Number(first.latitude));
  const deltaLng = toRad(Number(second.longitude) - Number(first.longitude));
  const a =
    Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
    Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return earthRadius * c;
}

function buildUserNameMap(records) {
  return (Array.isArray(records) ? records : []).reduce((result, item) => {
    result[item.userId] = item.userName || '\u7528\u6237';
    return result;
  }, {});
}
function buildEvidenceTimeline(records, snapshots, trackPoints) {
  const events = [];
  const userNameMap = buildUserNameMap(records);
  const sortedTrackPoints = Array.isArray(trackPoints)
    ? [...trackPoints].sort((a, b) => toTimestamp(a.recordedAt) - toTimestamp(b.recordedAt))
    : [];

  (Array.isArray(records) ? records : []).forEach((record) => {
    const userName = record.userName || '\u7528\u6237';
    if (record.checkInTime) {
      events.push({
        id: `record-check-in-${record.recordId}`,
        time: formatTime(record.checkInTime),
        timestamp: toTimestamp(record.checkInTime),
        title: '\u7b7e\u5230',
        userName,
        detail: `\u5f53\u524d\u72b6\u6001\uff1a${formatParticipationStatus(record.status)}`,
        recordId: record.recordId,
      });
    }
    if (record.checkOutTime) {
      events.push({
        id: `record-check-out-${record.recordId}`,
        time: formatTime(record.checkOutTime),
        timestamp: toTimestamp(record.checkOutTime),
        title: '\u7b7e\u9000',
        userName,
        detail: `\u670d\u52a1\u65f6\u957f\uff1a${record.serviceHours ?? 0} \u5c0f\u65f6`,
        recordId: record.recordId,
      });
    }
    if (record.rewardFinalizedAt) {
      events.push({
        id: `record-reward-${record.recordId}`,
        time: formatTime(record.rewardFinalizedAt),
        timestamp: toTimestamp(record.rewardFinalizedAt),
        title: '\u79ef\u5206\u7ed3\u7b97',
        userName,
        detail: record.pointBreakdown || `\u83b7\u5f97\u79ef\u5206\uff1a${record.points ?? 0} \u5206`,
        recordId: record.recordId,
      });
    }
    if (record.anomalyReason) {
      events.push({
        id: `record-anomaly-${record.recordId}`,
        time: formatTime(record.rewardFinalizedAt || record.checkOutTime || record.checkInTime),
        timestamp: toTimestamp(record.rewardFinalizedAt || record.checkOutTime || record.checkInTime),
        title: '\u5f02\u5e38\u547d\u4e2d',
        userName,
        detail: record.anomalyReason + (record.anomalyRemark ? `\uff1b\u5904\u7f6e\u5907\u6ce8\uff1a${record.anomalyRemark}` : ''),
        recordId: record.recordId,
        isAnomaly: true,
      });
    }
  });

  (Array.isArray(snapshots) ? snapshots : []).forEach((snapshot, index) => {
    events.push({
      id: `snapshot-${snapshot.snapshotId || index}`,
      time: formatTime(snapshot.createdAt),
      timestamp: toTimestamp(snapshot.createdAt),
      title: '\u670d\u52a1\u5feb\u7167',
      userName: userNameMap[snapshot.userId] || '\u7528\u6237',
      detail: snapshot.note || '\u4e0a\u4f20\u4e86\u670d\u52a1\u73b0\u573a\u5feb\u7167',
      recordId: snapshot.recordId || null,
    });
  });

  sortedTrackPoints.forEach((point, index) => {
    const previous = index > 0 ? sortedTrackPoints[index - 1] : null;
    let title = '\u8f68\u8ff9\u70b9';
    if (point.pointType === 'START') {
      title = '\u8d77\u70b9\u8f68\u8ff9';
    } else if (point.pointType === 'END') {
      title = '\u7ec8\u70b9\u8f68\u8ff9';
    } else if (previous && distanceMeters(previous, point) < 30) {
      title = '\u505c\u7559\u70b9';
    } else if (point.pointType === 'MIDDLE') {
      title = '\u4e2d\u9014\u8f68\u8ff9';
    }
    events.push({
      id: `track-${point.trackPointId || index}`,
      time: formatTime(point.recordedAt),
      timestamp: toTimestamp(point.recordedAt),
      title,
      userName: userNameMap[point.userId] || '\u7528\u6237',
      detail: `\u4f4d\u7f6e\uff1a${point.latitude}, ${point.longitude}`,
      recordId: point.recordId || null,
    });
  });

  return events
    .filter((item) => item.timestamp > 0)
    .sort((first, second) => first.timestamp - second.timestamp);
}
function normalizeTrackPoints(trackPoints) {
  return (Array.isArray(trackPoints) ? trackPoints : [])
    .filter((item) => item && item.latitude != null && item.longitude != null)
    .map((item) => ({
      ...item,
      latitude: Number(item.latitude),
      longitude: Number(item.longitude),
    }))
    .sort((first, second) => toTimestamp(first.recordedAt) - toTimestamp(second.recordedAt));
}

function buildEvidenceUserNameMap(records) {
  return (Array.isArray(records) ? records : []).reduce((result, item) => {
    result[item.userId] = item.userName || '\u7528\u6237';
    return result;
  }, {});
}
function formatParticipationStatus(status) {
  return PARTICIPATION_STATUS_TEXT[status] || status || '-';
}
function formatAnomalyStatus(status) {
  return ANOMALY_STATUS_TEXT[status] || status || '-';
}
function formatTrackPointType(pointType) {
  return TRACK_POINT_TYPE_TEXT[pointType] || pointType || '\u4e2d\u9014\u8f68\u8ff9';
}
function formatBlockchainEventType(eventType) {
  return BLOCKCHAIN_EVENT_TYPE_TEXT[eventType] || eventType || '\u5b58\u8bc1\u4e8b\u4ef6';
}
function formatBlockchainProofStatus(status) {
  return BLOCKCHAIN_PROOF_STATUS_TEXT[status] || status || '-';
}
function formatNotificationType(type) {
  return NOTIFICATION_TYPE_TEXT[type] || '\u7cfb\u7edf\u6d88\u606f';
}
function formatNotificationStatus(status) {
  return NOTIFICATION_STATUS_TEXT[status] || status || '-';
}
function buildEvidenceTimelineView(records, snapshots, trackPoints, proofs) {
  const events = [];
  const userNameMap = buildEvidenceUserNameMap(records);
  const sortedTrackPoints = normalizeTrackPoints(trackPoints);

  (Array.isArray(records) ? records : []).forEach((record) => {
    const userName = record.userName || '\u7528\u6237';
    if (record.checkInTime) {
      events.push({
        id: `record-check-in-${record.recordId}`,
        time: formatTime(record.checkInTime),
        timestamp: toTimestamp(record.checkInTime),
        title: '\u7b7e\u5230',
        userName,
        detail: `\u5f53\u524d\u72b6\u6001\uff1a${formatParticipationStatus(record.status)}`,
        recordId: record.recordId,
      });
    }
    if (record.checkOutTime) {
      events.push({
        id: `record-check-out-${record.recordId}`,
        time: formatTime(record.checkOutTime),
        timestamp: toTimestamp(record.checkOutTime),
        title: '\u7b7e\u9000',
        userName,
        detail: `\u670d\u52a1\u65f6\u957f\uff1a${record.serviceHours ?? 0} \u5c0f\u65f6`,
        recordId: record.recordId,
      });
    }
    if (record.rewardFinalizedAt) {
      events.push({
        id: `record-reward-${record.recordId}`,
        time: formatTime(record.rewardFinalizedAt),
        timestamp: toTimestamp(record.rewardFinalizedAt),
        title: '\u79ef\u5206\u7ed3\u7b97',
        userName,
        detail: record.pointBreakdown || `\u83b7\u5f97\u79ef\u5206\uff1a${record.earnedPoints ?? 0} \u5206`,
        recordId: record.recordId,
      });
    }
    if (record.anomalyReason) {
      events.push({
        id: `record-anomaly-${record.recordId}`,
        time: formatTime(record.rewardFinalizedAt || record.checkOutTime || record.checkInTime),
        timestamp: toTimestamp(record.rewardFinalizedAt || record.checkOutTime || record.checkInTime),
        title: '\u5f02\u5e38\u547d\u4e2d',
        userName,
        detail: record.anomalyReason + (record.anomalyRemark ? `\uff1b\u5904\u7f6e\u5907\u6ce8\uff1a${record.anomalyRemark}` : ''),
        recordId: record.recordId,
        isAnomaly: true,
      });
    }
  });

  (Array.isArray(snapshots) ? snapshots : []).forEach((snapshot, index) => {
    events.push({
      id: `snapshot-${snapshot.snapshotId || index}`,
      time: formatTime(snapshot.createdAt),
      timestamp: toTimestamp(snapshot.createdAt),
      title: '\u670d\u52a1\u5feb\u7167',
      userName: userNameMap[snapshot.userId] || '\u7528\u6237',
      detail: snapshot.note || '\u4e0a\u4f20\u4e86\u670d\u52a1\u73b0\u573a\u5feb\u7167',
      recordId: snapshot.recordId || null,
    });
  });

  sortedTrackPoints.forEach((point, index) => {
    const previous = index > 0 ? sortedTrackPoints[index - 1] : null;
    let title = '\u8f68\u8ff9\u70b9';
    if (point.pointType === 'START') {
      title = '\u8d77\u70b9\u8f68\u8ff9';
    } else if (point.pointType === 'END') {
      title = '\u7ec8\u70b9\u8f68\u8ff9';
    } else if (previous && distanceMeters(previous, point) < 30) {
      title = '\u505c\u7559\u70b9';
    } else if (point.pointType === 'MIDDLE') {
      title = '\u4e2d\u9014\u8f68\u8ff9';
    }
    events.push({
      id: `track-${point.trackPointId || index}`,
      time: formatTime(point.recordedAt),
      timestamp: toTimestamp(point.recordedAt),
      title,
      userName: userNameMap[point.userId] || '\u7528\u6237',
      detail: `\u4f4d\u7f6e\uff1a${point.latitude}, ${point.longitude}`,
      recordId: point.recordId || null,
    });
  });

  (Array.isArray(proofs) ? proofs : []).forEach((proof, index) => {
    const userName = proof.userName || userNameMap[proof.userId] || '\u7528\u6237';
    events.push({
      id: `proof-${proof.id || index}`,
      time: formatTime(proof.anchoredAt || proof.eventTime),
      timestamp: toTimestamp(proof.anchoredAt || proof.eventTime),
      title: formatBlockchainEventType(proof.eventType),
      userName,
      detail: `${formatBlockchainProofStatus(proof.proofStatus)} / \u6d41\u6c34\u53f7\uff1a${proof.transactionNo || '-'} / Hash\uff1a${proof.evidenceHash || '-'}`,
      recordId: proof.recordId || null,
    });
  });

  return events
    .filter((item) => item.timestamp > 0)
    .sort((first, second) => first.timestamp - second.timestamp);
}
function buildTrackSummary(trackPoints) {
  const sorted = normalizeTrackPoints(trackPoints);
  if (!sorted.length) {
    return {
      pointCount: 0,
      stayCount: 0,
      startText: '-',
      endText: '-',
    };
  }

  let stayCount = 0;
  for (let index = 1; index < sorted.length - 1; index += 1) {
    if (distanceMeters(sorted[index - 1], sorted[index]) < 30) {
      stayCount += 1;
    }
  }

  const start = sorted[0];
  const end = sorted[sorted.length - 1];
  return {
    pointCount: sorted.length,
    stayCount,
    startText: `${start.latitude}, ${start.longitude}`,
    endText: `${end.latitude}, ${end.longitude}`,
  };
}

function buildMarkerIcon(color) {
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 32 32">
      <circle cx="16" cy="16" r="12" fill="${color}" fill-opacity="0.92" stroke="#ffffff" stroke-width="4" />
    </svg>
  `;
  return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
}

document.title = UI.appTitle;

const template = `
<div class="admin-root">
  <div v-if="toast.show" class="toast" :class="toast.type">{{ toast.text }}</div>

  <div v-if="state.restoring" class="screen-shell">
    <div class="screen-panel">
      <div class="screen-badge">${UI.superAdmin}</div>
      <h1 class="screen-title">${UI.restoringTitle}</h1>
      <p class="screen-subtitle">${UI.restoringDesc}</p>
    </div>
  </div>

  <div v-else-if="!state.token || !state.profile" class="login-shell">
    <div class="login-background"></div>
    <div class="login-wrapper">
      <div class="card login-card">
        <div class="hero-tag-row">
          <span class="tag theme-primary">Vue 3</span>
          <span class="tag theme-success">TDesign</span>
          <span class="tag theme-warning">${UI.superAdmin}</span>
        </div>
        <h1 class="login-title">${UI.loginTitle}</h1>
        <p class="login-subtitle">${UI.loginDesc}</p>
        <div class="login-form">
          <div class="field-block">
            <label class="field-label">${UI.loginApi}</label>
            <input v-model="login.baseUrl" class="form-input" placeholder="https://api.example.com/api">
          </div>
          <div class="field-block">
            <label class="field-label">${UI.loginUser}</label>
            <input v-model="login.username" class="form-input" placeholder="请输入管理员账号">
          </div>
          <div class="field-block">
            <label class="field-label">${UI.loginPassword}</label>
            <input v-model="login.password" class="form-input" type="password" placeholder="请输入密码" @keyup.enter="doLogin">
          </div>
        </div>
        <div class="login-actions">
          <button class="primary-button block-button" type="button" @click="doLogin">${UI.loginButton}</button>
        </div>
      </div>
    </div>
  </div>

  <div v-else class="shell-layout">
    <aside class="shell-sidebar">
      <div class="brand-card">
        <div class="screen-badge">${UI.superAdmin}</div>
        <h2 class="brand-title">{{ settings.systemName || '${UI.systemName}' }}</h2>
        <p class="brand-text">${UI.headerTip}</p>
      </div>
      <div class="menu-group">
        <div class="menu-group-title">${UI.navTitle}</div>
        <button
          v-for="item in menus"
          :key="item.key"
          class="menu-item"
          :class="{ active: activeTab === item.key }"
          type="button"
          @click="activeTab = item.key"
        >{{ item.label }}</button>
      </div>
    </aside>

    <div class="shell-main">
      <header class="shell-header card">
        <div class="header-top">
          <div class="header-copy">
            <div class="page-eyebrow">${UI.superAdmin}</div>
            <h1 class="page-title">{{ currentMenu.label }}</h1>
            <p class="page-description">${UI.rolePrefix}{{ roleLabel(state.profile.role) }} | ${UI.userPrefix}{{ profileName }}</p>
          </div>
          <div class="header-profile">
            <div class="profile-avatar">
              <img v-if="profileAvatarUrl" :src="profileAvatarUrl" alt="avatar">
              <span v-else>{{ profileInitial }}</span>
            </div>
            <div class="profile-info">
              <div class="profile-label">${UI.profileTitle}</div>
              <div class="profile-name">{{ profileName }}</div>
              <div class="profile-role">{{ roleLabel(state.profile.role) }}</div>
            </div>
          </div>
        </div>
        <div class="header-bottom">
          <div class="header-note">${UI.headerTip}</div>
          <div class="header-actions">
            <button class="ghost-button" type="button" @click="loadAll(true)">${UI.refresh}</button>
            <button class="danger-button" type="button" @click="logout">${UI.logout}</button>
          </div>
        </div>
      </header>

      <main class="shell-content">
        <section v-if="activeTab === 'overview'" class="page-section">
          <div class="stats-grid">
            <div v-for="item in stats" :key="item.key" class="stat-card">
              <div class="stat-label">{{ item.label }}</div>
              <div class="stat-value">{{ item.value }}</div>
            </div>
          </div>
          <div class="todo-grid">
            <button v-for="item in todos" :key="item.key" class="todo-card" type="button" @click="activeTab = item.target">
              <div class="todo-label">{{ item.label }}</div>
              <div class="todo-value">{{ item.value }}</div>
            </button>
          </div>
          <div class="card page-card overview-annual-card">
            <div class="card-header">
              <div>
                <div class="card-title">年度概览</div>
                <div class="card-subtitle">{{ annualOverview.year }} 年平台服务概览</div>
              </div>
            </div>
            <div class="stats-grid overview-annual-stats">
              <div class="summary-item">
                <div class="mini-label">年度总活动数</div>
                <div class="summary-value">{{ annualOverview.totalActivities }}</div>
              </div>
              <div class="summary-item">
                <div class="mini-label">年度总服务时长</div>
                <div class="summary-value">{{ annualOverview.totalServiceHours }}</div>
              </div>
              <div class="summary-item">
                <div class="mini-label">年度志愿者活跃数</div>
                <div class="summary-value">{{ annualOverview.activeVolunteers }}</div>
              </div>
              <div class="summary-item">
                <div class="mini-label">热门服务区域数</div>
                <div class="summary-value">{{ annualOverview.hotAreas.length }}</div>
              </div>
            </div>
            <div class="surface overview-annual-surface">
              <div class="section-title">热门服务区域</div>
              <div v-if="annualOverview.hotAreas.length" class="mini-list">
                <div v-for="item in annualOverview.hotAreas" :key="item.label" class="summary-item">
                  <div class="mini-label">{{ item.label }}</div>
                  <div class="summary-value">{{ item.value }} 场活动</div>
                </div>
              </div>
                <div v-else class="empty-panel">暂无热门服务区域</div>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'analytics'" class="page-section">
          <div class="card page-card analytics-hero-card">
            <div class="card-header analytics-hero-header">
              <div class="analytics-hero-copy">
                <div class="card-title">数据分析</div>
                <div class="card-subtitle">平台总览、运营分析与用户详情</div>
              </div>
              <div class="analytics-toolbar analytics-toolbar-compact">
                <div class="field-block inline-field">
                  <label class="field-label">统计年份</label>
                  <input v-model.number="analyticsYear" type="number" min="2020" max="2099" class="form-input short-input">
                </div>
                <button class="ghost-button small-button" type="button" @click="loadAnalyticsDashboard">刷新分析</button>
              </div>
            </div>
          </div>

          <div class="analytics-stack">
            <div class="card page-card analytics-overview-card">
              <div class="card-header">
                <div>
                  <div class="card-title">平台年度总览</div>
                  <div class="card-subtitle">{{ annualOverview.year }} 年核心数据概览</div>
                </div>
              </div>
              <div class="stats-grid analytics-overview-stats">
                <div class="summary-item">
                  <div class="mini-label">年度总活动数</div>
                  <div class="summary-value">{{ annualOverview.totalActivities }}</div>
                </div>
                <div class="summary-item">
                  <div class="mini-label">年度总服务时长</div>
                  <div class="summary-value">{{ annualOverview.totalServiceHours }}</div>
                </div>
                <div class="summary-item">
                  <div class="mini-label">年度志愿者活跃数</div>
                  <div class="summary-value">{{ annualOverview.activeVolunteers }}</div>
                </div>
                <div class="summary-item">
                  <div class="mini-label">年度管理员活跃数</div>
                  <div class="summary-value">{{ annualOverview.activeAdmins }}</div>
                </div>
                <div class="summary-item">
                  <div class="mini-label">年度服务总人次</div>
                  <div class="summary-value">{{ annualOverview.totalServiceCount }}</div>
                </div>
                <div class="summary-item">
                  <div class="mini-label">热门服务区域</div>
                  <div class="summary-value">{{ annualOverview.hotAreas.length }}</div>
                </div>
              </div>
              <div class="surface analytics-overview-surface">
                <div class="section-title">热门服务区域</div>
                <div v-if="annualOverview.hotAreas.length" class="analytics-pill-list">
                  <div v-for="item in annualOverview.hotAreas" :key="'hot-area-' + item.label" class="analytics-pill">
                    <span>{{ item.label }}</span>
                    <strong>{{ item.value }} 场</strong>
                  </div>
                </div>
                <div v-else class="empty-panel analytics-empty-panel">暂无热门服务区域</div>
              </div>
            </div>

            <div class="card page-card analytics-operations-card">
              <div class="card-header">
                <div>
                  <div class="card-title">平台运营分析</div>
                  <div class="card-subtitle">社区、排行、类别与趋势数据</div>
                </div>
              </div>
              <div class="analytics-operations-grid">
                <div class="surface analytics-surface-card">
                  <div class="section-title">社区热力</div>
                  <div v-if="analyticsHeatmap.length" class="mini-list">
                    <div v-for="item in analyticsHeatmap.slice(0, 6)" :key="'heatmap-' + item.activityId" class="mini-item">
                      <div class="mini-item-head">
                        <div class="mini-item-title">{{ item.activityTitle }}</div>
                        <span class="tag theme-warning">{{ item.intensity }}</span>
                      </div>
                      <div class="mini-item-sub">{{ item.location || '未标注地点' }}</div>
                      <div class="mini-item-sub">{{ item.category || '综合服务' }} / 缺口 {{ item.gapCount || 0 }} / {{ item.areaTag || '普通区域' }}</div>
                    </div>
                  </div>
                  <div v-else class="empty-panel analytics-empty-panel">暂无社区热力</div>
                </div>

                <div class="surface analytics-surface-card">
                  <div class="section-title">活动举办热力</div>
                  <div v-if="annualOverview.activityHeatmap.length" class="mini-list">
                    <div v-for="item in annualOverview.activityHeatmap.slice(0, 6)" :key="'activity-heatmap-' + item.activityId" class="mini-item">
                      <div class="mini-item-head">
                        <div class="mini-item-title">{{ item.activityTitle }}</div>
                        <span class="tag theme-warning">{{ item.intensity }}</span>
                      </div>
                      <div class="mini-item-sub">{{ item.location || '未标注地点' }}</div>
                      <div class="mini-item-sub">{{ item.category || '综合服务' }} / 缺口 {{ item.gapCount || 0 }} / {{ item.areaTag || '常规区域' }}</div>
                    </div>
                  </div>
                  <div v-else class="empty-panel analytics-empty-panel">暂无活动热力</div>
                </div>

                <div class="surface analytics-surface-card">
                  <div class="section-title">排行榜</div>
                  <div v-if="analyticsLeaderboard.length" class="mini-list">
                    <div v-for="(item, index) in analyticsLeaderboard.slice(0, 8)" :key="'leaderboard-' + item.userId" class="mini-item">
                      <div class="mini-item-head">
                        <div class="mini-item-title">第 {{ index + 1 }} 名 · {{ item.userName }}</div>
                        <span class="tag theme-primary">{{ item.totalPoints }} 分</span>
                      </div>
                      <div class="mini-item-sub">{{ item.badgeName || '成长中' }} / {{ item.totalHours || 0 }} 小时</div>
                    </div>
                  </div>
                  <div v-else class="empty-panel analytics-empty-panel">暂无排行数据</div>
                </div>

                <div class="surface analytics-surface-card">
                  <div class="section-title">管理员排行榜</div>
                  <div v-if="annualOverview.adminLeaderboard.length" class="mini-list">
                    <div v-for="(item, index) in annualOverview.adminLeaderboard.slice(0, 8)" :key="'admin-leaderboard-' + item.userId" class="mini-item">
                      <div class="mini-item-head">
                        <div class="mini-item-title">第 {{ index + 1 }} 名 · {{ item.userName }}</div>
                        <span class="tag theme-primary">{{ item.totalActivities }} 场</span>
                      </div>
                      <div class="mini-item-sub">服务人次 {{ item.totalServiceCount || 0 }} / 时长 {{ Number(item.totalServiceHours || 0).toFixed(1) }} 小时</div>
                    </div>
                  </div>
                  <div v-else class="empty-panel analytics-empty-panel">暂无管理员排行</div>
                </div>

                <div class="surface analytics-surface-card analytics-panel-span-2">
                  <div class="section-title">活动类别分布</div>
                  <div v-if="annualOverview.categoryStats.length" class="analytics-pill-list">
                    <div v-for="item in annualOverview.categoryStats" :key="'category-' + item.label" class="analytics-pill">
                      <span>{{ item.label }}</span>
                      <strong>{{ item.value }} 场</strong>
                    </div>
                  </div>
                  <div v-else class="empty-panel analytics-empty-panel">暂无活动类别分布</div>
                </div>

                <div class="surface analytics-surface-card">
                  <div class="section-title">月度活动趋势</div>
                  <div v-if="annualOverview.monthlyActivityTrends.length" class="analytics-trend-list">
                    <div v-for="item in annualOverview.monthlyActivityTrends" :key="'activity-trend-' + item.label" class="analytics-trend-item">
                      <span>{{ item.label }}</span>
                      <strong>{{ Number(item.value || 0) }} 场</strong>
                    </div>
                  </div>
                  <div v-else class="empty-panel analytics-empty-panel">暂无月度活动趋势</div>
                </div>

                <div class="surface analytics-surface-card">
                  <div class="section-title">月度服务时长趋势</div>
                  <div v-if="annualOverview.monthlyServiceHourTrends.length" class="analytics-trend-list">
                    <div v-for="item in annualOverview.monthlyServiceHourTrends" :key="'hours-trend-' + item.label" class="analytics-trend-item">
                      <span>{{ item.label }}</span>
                      <strong>{{ Number(item.value || 0).toFixed(1) }} 小时</strong>
                    </div>
                  </div>
                  <div v-else class="empty-panel analytics-empty-panel">暂无月度服务时长趋势</div>
                </div>
              </div>
            </div>

            <div class="card page-card analytics-user-browser-panel">
              <div class="card-header">
                <div>
                  <div class="card-title">用户分析</div>
                  <div class="card-subtitle">用户筛选与明细查看</div>
                </div>
              </div>
              <div class="analytics-toolbar analytics-user-toolbar">
                <select v-model="analyticsRoleFilter" class="native-field short-select">
                  <option value="ALL">全部角色</option>
                  <option value="VOLUNTEER">志愿者</option>
                  <option value="ADMIN">管理员</option>
                  <option value="SUPER_ADMIN">超级管理员</option>
                </select>
                <input v-model="analyticsUserKeyword" class="form-input" placeholder="输入姓名、志愿者号或用户ID">
              </div>
              <div v-if="filteredAnalyticsUsers.length" class="analytics-user-list">
                <button
                  v-for="item in filteredAnalyticsUsers"
                  :key="'analytics-user-' + item.id"
                  type="button"
                  class="analytics-user-item"
                  :class="{ active: String(item.id) === String(selectedAnalyticsUserId) }"
                  @click="selectedAnalyticsUserId = String(item.id)"
                >
                  <div class="analytics-user-item-head">
                    <div class="analytics-user-name">{{ item.name || ('用户 ' + item.id) }}</div>
                    <span class="tag" :class="roleClass(item.role)">{{ roleLabel(item.role) }}</span>
                  </div>
                  <div class="mini-item-sub">志愿者号：{{ maskVolunteerCardNo(item.volunteerCardNo) }}</div>
                  <div class="mini-item-sub">积分 {{ item.totalPoints || 0 }} / 时长 {{ item.totalHours || 0 }} / 活动 {{ item.activityCount || 0 }}</div>
                </button>
              </div>
              <div v-else class="empty-panel analytics-empty-panel">暂无用户数据</div>
            </div>

            <div class="card page-card analytics-user-detail-panel">
              <div class="card-header">
                <div>
                  <div class="card-title">用户详情</div>
                  <div class="card-subtitle">用户服务记录与证据概况</div>
                </div>
              </div>
              <div v-if="analyticsUserDetail.userId" class="analytics-user-detail">
                <div class="analytics-detail-grid analytics-detail-summary-grid">
                  <div class="summary-item">
                    <div class="mini-label">用户姓名</div>
                    <div class="summary-value">{{ analyticsUserDetail.userName }}</div>
                  </div>
                  <div class="summary-item">
                    <div class="mini-label">当前角色</div>
                    <div class="summary-value">{{ roleLabel(analyticsUserDetail.role) }}</div>
                  </div>
                  <div class="summary-item">
                    <div class="mini-label">认证状态</div>
                    <div class="summary-value">{{ analyticsUserDetail.verified ? '已认证' : '未认证' }}</div>
                  </div>
                  <div class="summary-item">
                    <div class="mini-label">排行榜位置</div>
                    <div class="summary-value">{{ analyticsUserDetail.leaderboardRank || '-' }}</div>
                  </div>
                  <div class="summary-item">
                    <div class="mini-label">志愿者号</div>
                    <div class="summary-value">{{ analyticsUserDetail.volunteerCardNo || '-' }}</div>
                  </div>
                  <div class="summary-item">
                    <div class="mini-label">身份证号</div>
                    <div class="summary-value">{{ analyticsUserDetail.maskedIdCardNo || '-' }}</div>
                  </div>
                </div>

                <div class="surface analytics-surface-card">
                  <div class="section-title">年度服务报告</div>
                  <div class="analytics-detail-grid analytics-annual-grid">
                    <div class="summary-item">
                      <div class="mini-label">年度总服务时长</div>
                      <div class="summary-value">{{ analyticsUserDetail.annualReport.totalServiceHours || 0 }}</div>
                    </div>
                    <div class="summary-item">
                      <div class="mini-label">年度参与活动数</div>
                      <div class="summary-value">{{ analyticsUserDetail.annualReport.totalActivities || 0 }}</div>
                    </div>
                    <div class="summary-item">
                      <div class="mini-label">年度累计积分</div>
                      <div class="summary-value">{{ analyticsUserDetail.annualReport.totalPoints || 0 }}</div>
                    </div>
                    <div class="summary-item">
                      <div class="mini-label">年度证书数</div>
                      <div class="summary-value">{{ analyticsUserDetail.annualReport.totalCertificates || 0 }}</div>
                    </div>
                    <div class="summary-item">
                      <div class="mini-label">最高评分活动</div>
                      <div class="summary-value">{{ analyticsUserDetail.annualReport.bestActivityTitle || '-' }}</div>
                    </div>
                    <div class="summary-item">
                      <div class="mini-label">主要服务类别</div>
                      <div class="summary-value">{{ analyticsUserDetail.annualReport.topCategory || '-' }}</div>
                    </div>
                  </div>
                  <div class="mini-item-sub analytics-summary-text">{{ analyticsUserDetail.annualReport.summaryText || '暂无年度服务报告摘要' }}</div>
                </div>

                <div class="surface analytics-surface-card analytics-footprint-surface">
                  <div class="section-title">足迹地图</div>
                  <div id="analytics-footprint-map" class="track-map-web analytics-footprint-map"></div>
                  <div class="analytics-pill-list analytics-footprint-areas">
                    <div v-for="item in analyticsUserDetail.annualReport.coveredAreas" :key="'covered-area-' + item" class="analytics-pill">
                      <span>{{ item }}</span>
                    </div>
                  </div>
                </div>

                <div class="analytics-detail-dual-grid">
                  <div class="surface analytics-surface-card">
                    <div class="section-title">社区热力关联活动</div>
                    <div v-if="analyticsUserDetail.relatedHeatmapActivities.length" class="mini-list">
                      <div v-for="item in analyticsUserDetail.relatedHeatmapActivities" :key="'related-heatmap-' + item.activityId" class="mini-item">
                        <div class="mini-item-title">{{ item.activityTitle }}</div>
                        <div class="mini-item-sub">{{ item.location || '未标注地点' }}</div>
                        <div class="mini-item-sub">{{ item.category || '综合服务' }} / 热力值 {{ item.intensity }}</div>
                      </div>
                    </div>
                    <div v-else class="empty-panel analytics-empty-panel">暂无关联活动</div>
                  </div>

                  <div class="surface analytics-surface-card">
                    <div class="section-title">证据 / 异常简况</div>
                    <div class="analytics-detail-grid compact">
                      <div class="summary-item">
                        <div class="mini-label">服务快照</div>
                        <div class="summary-value">{{ analyticsUserDetail.evidenceSummary.snapshotCount || 0 }}</div>
                      </div>
                      <div class="summary-item">
                        <div class="mini-label">轨迹点</div>
                        <div class="summary-value">{{ analyticsUserDetail.evidenceSummary.trackPointCount || 0 }}</div>
                      </div>
                      <div class="summary-item">
                        <div class="mini-label">区块链存证</div>
                        <div class="summary-value">{{ analyticsUserDetail.evidenceSummary.blockchainProofCount || 0 }}</div>
                      </div>
                      <div class="summary-item">
                        <div class="mini-label">异常记录</div>
                        <div class="summary-value">{{ analyticsUserDetail.evidenceSummary.anomalyCount || 0 }}</div>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="surface analytics-surface-card">
                  <div class="section-title">参与活动记录</div>
                  <div v-if="analyticsUserDetail.participationRecords.length" class="mini-list">
                    <div v-for="item in analyticsUserDetail.participationRecords" :key="'analytics-record-' + item.id" class="mini-item">
                      <div class="mini-item-head">
                        <div class="mini-item-title">{{ item.activityTitle || ('活动 ' + item.activityId) }}</div>
                        <span class="tag theme-primary">{{ item.activityCategory || '综合服务' }}</span>
                      </div>
                      <div class="mini-item-sub">状态：{{ formatParticipationStatus(item.status) }} / 时长 {{ item.serviceHours || 0 }} 小时 / 积分 {{ item.earnedPoints || 0 }}</div>
                      <div class="mini-item-sub">签到：{{ formatTime(item.checkInTime) }} / 签退：{{ formatTime(item.checkOutTime) }}</div>
                    </div>
                  </div>
                  <div v-else class="empty-panel analytics-empty-panel">暂无参与活动记录</div>
                </div>
              </div>
              <div v-else class="empty-panel analytics-empty-panel">请选择用户</div>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'leaderboard'" class="page-section">
          <div class="card page-card analytics-hero-card">
            <div class="card-header analytics-hero-header">
              <div class="analytics-hero-copy">
                <div class="card-title">排行榜</div>
                <div class="card-subtitle">按周期查看平台服务排行</div>
              </div>
              <div class="analytics-toolbar analytics-toolbar-compact">
                <button class="sub-module-tab" :class="{ active: insightPeriod === 'week' }" type="button" @click="insightPeriod = 'week'">周榜</button>
                <button class="sub-module-tab" :class="{ active: insightPeriod === 'month' }" type="button" @click="insightPeriod = 'month'">月榜</button>
              </div>
            </div>
          </div>

          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">平台服务排行</div>
                <div class="card-subtitle">平台服务排行明细</div>
              </div>
            </div>
            <div v-if="platformStarLeaderboard.length" class="mini-list">
              <div v-for="(item, index) in platformStarLeaderboard" :key="'platform-star-' + item.userId" class="mini-item">
                <div class="mini-item-head">
                  <div class="mini-item-title">第 {{ index + 1 }} 名 · {{ item.userName }}</div>
                  <span class="tag theme-warning">星值 {{ item.displayStarScore }}</span>
                </div>
                <div class="mini-item-sub">{{ item.displayPeriod }} / 好评率 {{ item.displayPositiveRatingRate }} / 轨迹 {{ item.displayTrackCoverageScore }}</div>
                <div class="mini-item-sub">服务时长 {{ item.displayTotalHours }} 小时 / 完成 {{ item.completionCount }} 次 / 社区 {{ item.topArea || '未标注社区' }}</div>
                <div class="mini-item-sub">{{ item.displayShowcaseText }}</div>
              </div>
            </div>
            <div v-else class="empty-panel analytics-empty-panel">暂无平台排行数据</div>
          </div>
        </section>

        <section v-else-if="activeTab === 'community-insights'" class="page-section">
          <div class="card page-card analytics-hero-card">
            <div class="card-header analytics-hero-header">
              <div class="analytics-hero-copy">
                <div class="card-title">社区热力</div>
                <div class="card-subtitle">社区需求与服务活跃度</div>
              </div>
              <div class="analytics-toolbar analytics-toolbar-compact">
                <button class="sub-module-tab" :class="{ active: insightPeriod === 'week' }" type="button" @click="insightPeriod = 'week'">本周</button>
                <button class="sub-module-tab" :class="{ active: insightPeriod === 'month' }" type="button" @click="insightPeriod = 'month'">本月</button>
              </div>
            </div>
          </div>

          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">平台社区数据</div>
                <div class="card-subtitle">平台社区数据明细</div>
              </div>
            </div>
            <div v-if="platformCommunityInsights.length" class="mini-list">
              <div v-for="item in platformCommunityInsights" :key="'platform-community-' + item.communityName + '-' + item.streetName" class="mini-item">
                <div class="mini-item-head">
                  <div class="mini-item-title">{{ item.communityName }}</div>
                  <span class="tag" :class="item.displayAreaTag === '需求洼地' ? 'theme-warning' : item.displayAreaTag === '服务活力区' ? 'theme-success' : 'theme-default'">{{ item.displayAreaTag }}</span>
                </div>
                <div class="mini-item-sub">{{ item.streetName }} / {{ item.displayLocation }}</div>
                <div class="mini-item-sub">需求 {{ item.displayDemandIndex }} / 活力 {{ item.displayVitalityIndex }} / 缺口 {{ item.displayGapCount }} / 轨迹 {{ item.displayTrackCoverageScore }}</div>
                <div class="mini-item-sub">{{ item.displayGuidanceText }}</div>
              </div>
            </div>
            <div v-else class="empty-panel analytics-empty-panel">暂无平台社区数据</div>
          </div>
        </section>

        <section v-else-if="activeTab === 'store'" class="page-section panel-grid two-column">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">商品管理</div>
                <div class="card-subtitle">积分商品维护</div>
              </div>
            </div>
            <div class="form-grid two-column">
              <div class="field-block"><label class="field-label">商品名称</label><input v-model="storeForm.name" class="form-input"></div>
              <div class="field-block">
                <label class="field-label">商品分类</label>
                <select v-model="storeForm.category" class="native-field">
                  <option v-for="item in storeCategories" :key="'store-category-' + item" :value="item">{{ item }}</option>
                </select>
              </div>
              <div class="field-block span-2"><label class="field-label">商品描述</label><textarea v-model="storeForm.description" class="form-textarea" rows="4"></textarea></div>
              <div class="field-block"><label class="field-label">所需积分</label><input v-model="storeForm.pointsCost" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">库存</label><input v-model="storeForm.stock" class="form-input" type="number"></div>
              <div class="field-block">
                <label class="field-label">商品类型</label>
                <select v-model="storeForm.deliveryType" class="native-field">
                  <option value="VIRTUAL">虚拟商品</option>
                  <option value="PHYSICAL">实体商品</option>
                </select>
              </div>
              <div class="field-block">
                <label class="field-label">上架状态</label>
                <label class="switch-row"><input v-model="storeForm.active" type="checkbox"> <span>{{ storeForm.active ? '上架中' : '已下架' }}</span></label>
              </div>
            </div>
            <div class="card-actions">
              <button class="primary-button" type="button" @click="saveStoreItem">{{ storeForm.id ? '保存商品' : '新增商品' }}</button>
              <button class="ghost-button" type="button" @click="resetStoreForm">重置表单</button>
            </div>
          </div>

          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">商品列表</div>
                <div class="card-subtitle">库存、类型与编辑入口</div>
              </div>
            </div>
            <div v-if="storeItems.length" class="table-wrapper">
              <table class="admin-table">
                <thead>
                  <tr>
                    <th>名称</th>
                    <th>分类</th>
                    <th>类型</th>
                    <th>积分</th>
                    <th>库存</th>
                    <th>状态</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in storeItems" :key="'store-item-' + item.id">
                    <td>{{ item.name }}</td>
                    <td>{{ item.category }}</td>
                    <td>{{ item.deliveryType === 'PHYSICAL' ? '实体商品' : '虚拟商品' }}</td>
                    <td>{{ item.pointsCost }}</td>
                    <td>{{ item.stock }}</td>
                    <td>{{ item.active ? '上架中' : '已下架' }}</td>
                    <td><button class="ghost-button small-button" type="button" @click="editStoreItem(item)">编辑</button></td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div v-else class="empty-panel">暂无商城商品</div>
          </div>

          <div class="card page-card span-2">
            <div class="card-header">
              <div>
                <div class="card-title">兑换发放</div>
                <div class="card-subtitle">兑换记录与发放处理</div>
              </div>
            </div>
            <div v-if="redemptionRecords.length" class="table-wrapper">
              <table class="admin-table">
                <thead>
                  <tr>
                    <th>商品</th>
                    <th>用户</th>
                    <th>类型</th>
                    <th>数量</th>
                    <th>状态</th>
                    <th>收货信息</th>
                    <th>备注</th>
                    <th>时间</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in redemptionRecords" :key="'redemption-' + item.id">
                    <td>{{ item.itemName }}</td>
                    <td>{{ item.userName }}</td>
                    <td>{{ item.deliveryType === 'PHYSICAL' ? '实体商品' : '虚拟商品' }}</td>
                    <td>{{ item.quantity }}</td>
                    <td>{{ item.status === 'DELIVERED' ? '已发放' : item.status === 'CANCELLED' ? '已取消' : '待处理' }}</td>
                    <td>
                      <div>{{ item.recipientName || '-' }}</div>
                      <div>{{ item.maskedRecipientPhone || '-' }}</div>
                      <div>{{ item.recipientAddress || '-' }}</div>
                    </td>
                    <td>{{ item.deliveryRemark || '-' }}</td>
                    <td>{{ formatTime(item.createdAt) }}</td>
                    <td>
                      <div class="inline-actions" v-if="item.status === 'CREATED'">
                        <button class="primary-button small-button" type="button" @click="deliverRedemption(item)">标记发放</button>
                        <button class="danger-button small-button" type="button" @click="cancelRedemption(item)">取消</button>
                      </div>
                      <span v-else>-</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div v-else class="empty-panel">暂无兑换记录</div>
          </div>
        </section>

        <section v-else-if="activeTab === 'users'" class="page-section panel-grid two-column">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u7528\u6237\u5217\u8868</div>
                <div class="card-subtitle">\u7528\u6237\u89d2\u8272\u7ba1\u7406</div>
              </div>
            </div>
            <div class="table-wrapper">
              <table class="admin-table">
                <thead>
                  <tr>
                    <th>\u7528\u6237</th>
                    <th>\u89d2\u8272</th>
                    <th>\u670d\u52a1\u65f6\u957f</th>
                    <th>\u79ef\u5206</th>
                    <th>\u521b\u5efa\u65f6\u95f4</th>
                    <th>\u64cd\u4f5c</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="user in users" :key="user.id">
                    <td><div class="cell-title">{{ user.name || ('ID ' + user.id) }}</div></td>
                    <td><span class="tag" :class="roleClass(user.role)">{{ roleLabel(user.role) }}</span></td>
                    <td>{{ user.totalHours || 0 }}</td>
                    <td>{{ user.totalPoints || 0 }}</td>
                    <td>{{ formatTime(user.createdAt) }}</td>
                    <td>
                      <div class="table-actions">
                        <button class="ghost-button small-button" type="button" :disabled="user.role === 'VOLUNTEER'" @click="setRole(user.id, 'VOLUNTEER')">\u8bbe\u4e3a\u5fd7\u613f\u8005</button>
                        <button class="success-button small-button" type="button" :disabled="user.role === 'ADMIN'" @click="setRole(user.id, 'ADMIN')">\u8bbe\u4e3a\u7ba1\u7406\u5458</button>
                      </div>
                    </td>
                  </tr>
                  <tr v-if="!users.length"><td colspan="6" class="empty-cell">\u6682\u65e0\u7528\u6237\u6570\u636e</td></tr>
                </tbody>
              </table>
            </div>
          </div>

          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u7ba1\u7406\u5458\u7533\u8bf7</div>
                <div class="card-subtitle">\u5f85\u5ba1\u6838\u7ba1\u7406\u5458\u7533\u8bf7</div>
              </div>
            </div>
            <div v-if="applications.length" class="application-list">
              <div v-for="item in applications" :key="item.id" class="application-item">
                <div class="application-head">
                  <div>
                    <div class="application-name">{{ item.userName || ('ID ' + item.userId) }}</div>
                    <div class="application-meta">
                      <span>\u521b\u5efa\u65f6\u95f4\uff1a{{ formatTime(item.createdAt) }}</span>
                      <span>\u5f53\u524d\u89d2\u8272\uff1a{{ roleLabel(item.currentRole) }}</span>
                    </div>
                  </div>
                  <span class="tag" :class="applicationClass(item.status)">{{ applicationLabel(item.status) }}</span>
                </div>
                <div class="application-block">
                  <div class="mini-label">\u7533\u8bf7\u539f\u56e0</div>
                  <div>{{ item.reason || '-' }}</div>
                </div>
                <div class="application-block">
                  <div class="mini-label">\u5ba1\u6838\u610f\u89c1</div>
                  <textarea v-model="reviews[item.id]" class="form-textarea" rows="3" placeholder="\u8bf7\u8f93\u5165\u5ba1\u6838\u610f\u89c1"></textarea>
                </div>
                <div class="table-actions">
                  <button class="success-button" type="button" @click="review(item.id, true)">\u901a\u8fc7</button>
                  <button class="danger-button" type="button" @click="review(item.id, false)">\u9a73\u56de</button>
                </div>
              </div>
            </div>
            <div v-else class="empty-panel">\u6682\u65e0\u5f85\u5ba1\u6838\u7ba1\u7406\u5458\u7533\u8bf7</div>
          </div>
        </section>

        <section v-else-if="activeTab === 'my-center'" class="page-section panel-grid two-column">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u6211\u7684\u8eab\u4efd</div>
                <div class="card-subtitle">\u5b9e\u540d\u8ba4\u8bc1\u4e0e\u8eab\u4efd\u4fe1\u606f</div>
              </div>
            </div>
            <div class="profile-summary-grid">
              <div class="summary-item">
                <div class="mini-label">\u5f53\u524d\u8d26\u53f7</div>
                <div class="summary-value">{{ profileName }}</div>
              </div>
              <div class="summary-item">
                <div class="mini-label">\u5f53\u524d\u89d2\u8272</div>
                <div class="summary-value">{{ roleLabel(state.profile.role) }}</div>
              </div>
              <div class="summary-item">
                <div class="mini-label">\u8ba4\u8bc1\u72b6\u6001</div>
                <div class="summary-value">
                  <span class="tag" :class="identity.verified ? 'theme-success' : 'theme-warning'">
                    {{ identity.verified ? '\u5df2\u8ba4\u8bc1' : '\u672a\u8ba4\u8bc1' }}
                  </span>
                </div>
              </div>
        <div class="summary-item">
          <div class="mini-label">\u771f\u5b9e\u59d3\u540d</div>
          <div class="summary-value">{{ identityVisibility.realName ? (identity.realName || '\u672a\u8ba4\u8bc1') : maskRealName(identity.realName) }}</div>
          <button class="ghost-button small-button sensitive-toggle-button" type="button" @click="identityVisibility.realName = !identityVisibility.realName">
            {{ identityVisibility.realName ? '\u9690\u85cf' : '\u663e\u793a' }}
          </button>
        </div>
        <div class="summary-item">
          <div class="mini-label">\u8eab\u4efd\u8bc1\u53f7</div>
          <div class="summary-value">{{ identityVisibility.idCardNo ? (identity.idCardNo || '\u672a\u586b\u5199') : maskIdCardNo(identity.idCardNo) }}</div>
          <button class="ghost-button small-button sensitive-toggle-button" type="button" @click="identityVisibility.idCardNo = !identityVisibility.idCardNo">
            {{ identityVisibility.idCardNo ? '\u9690\u85cf' : '\u663e\u793a' }}
          </button>
        </div>
        <div class="summary-item">
          <div class="mini-label">\u5fd7\u613f\u8005\u53f7</div>
          <div class="summary-value">{{ identityVisibility.volunteerCardNo ? (identity.volunteerCardNo || '\u8ba4\u8bc1\u540e\u81ea\u52a8\u751f\u6210') : maskVolunteerCardNo(identity.volunteerCardNo) }}</div>
          <button class="ghost-button small-button sensitive-toggle-button" type="button" @click="identityVisibility.volunteerCardNo = !identityVisibility.volunteerCardNo">
            {{ identityVisibility.volunteerCardNo ? '\u9690\u85cf' : '\u663e\u793a' }}
          </button>
        </div>
            </div>
          </div>

          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u5b9e\u540d\u8ba4\u8bc1</div>
                <div class="card-subtitle">\u5b9e\u540d\u4fe1\u606f\u7ef4\u62a4</div>
              </div>
            </div>
            <div class="form-grid">
              <div class="field-block">
                <label class="field-label">\u771f\u5b9e\u59d3\u540d</label>
                <div class="inline-sensitive-field">
                  <input
                    :value="identityVisibility.realName ? identityForm.realName : maskRealName(identity.realName)"
                    class="form-input"
                    :type="identityVisibility.realName ? 'text' : 'password'"
                    :readonly="!identityVisibility.realName"
                    placeholder="\u8bf7\u8f93\u5165\u771f\u5b9e\u59d3\u540d"
                    @input="identityForm.realName = $event.target.value"
                  >
                  <button class="ghost-button small-button sensitive-toggle-button" type="button" @click="identityVisibility.realName = !identityVisibility.realName">
                    {{ identityVisibility.realName ? '\u9690\u85cf' : '\u663e\u793a' }}
                  </button>
                </div>
              </div>
              <div class="field-block">
                <label class="field-label">\u8eab\u4efd\u8bc1\u53f7</label>
                <div class="inline-sensitive-field">
                  <input
                    :value="identityVisibility.idCardNo ? identityForm.idCardNo : maskIdCardNo(identity.idCardNo)"
                    class="form-input"
                    :type="identityVisibility.idCardNo ? 'text' : 'password'"
                    :readonly="!identityVisibility.idCardNo"
                    placeholder="\u8bf7\u8f93\u5165\u8eab\u4efd\u8bc1\u53f7"
                    @input="identityForm.idCardNo = $event.target.value"
                  >
                  <button class="ghost-button small-button sensitive-toggle-button" type="button" @click="identityVisibility.idCardNo = !identityVisibility.idCardNo">
                    {{ identityVisibility.idCardNo ? '\u9690\u85cf' : '\u663e\u793a' }}
                  </button>
                </div>
              </div>
              <div class="field-block span-2">
                <label class="field-label">\u5fd7\u613f\u8005\u53f7</label>
                <div class="inline-sensitive-field">
                  <input
                    :value="identityVisibility.volunteerCardNo ? (identity.volunteerCardNo || '\u8ba4\u8bc1\u540e\u81ea\u52a8\u751f\u6210') : maskVolunteerCardNo(identity.volunteerCardNo)"
                    class="form-input"
                    :type="identityVisibility.volunteerCardNo ? 'text' : 'password'"
                    readonly
                  >
                  <button class="ghost-button small-button sensitive-toggle-button" type="button" @click="identityVisibility.volunteerCardNo = !identityVisibility.volunteerCardNo">
                    {{ identityVisibility.volunteerCardNo ? '\u9690\u85cf' : '\u663e\u793a' }}
                  </button>
                </div>
              </div>
            </div>
            <div class="card-actions">
              <button class="primary-button" type="button" @click="saveIdentity">{{ identity.verified ? '\u66f4\u65b0\u8ba4\u8bc1\u4fe1\u606f' : '\u5b8c\u6210\u5b9e\u540d\u8ba4\u8bc1' }}</button>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'notifications'" class="page-section">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u6d88\u606f\u4e2d\u5fc3</div>
                <div class="card-subtitle">\u5ba1\u6838\u3001\u6d3b\u52a8\u4e0e\u7ed3\u7b97\u901a\u77e5</div>
              </div>
            </div>
            <div v-if="notifications.length" class="mini-list">
              <div v-for="item in notifications" :key="'notification-' + item.id" class="mini-item">
                <div class="mini-item-head">
                  <div class="mini-item-title">{{ formatNotificationType(item.notificationType) }}</div>
                  <span class="tag" :class="item.sendStatus === 'FAILED' ? 'theme-danger' : item.sendStatus === 'SENT' ? 'theme-success' : 'theme-warning'">
                    {{ formatNotificationStatus(item.sendStatus) }}
                  </span>
                </div>
                <div class="mini-item-sub">{{ item.content || '\u6682\u65e0\u901a\u77e5\u5185\u5bb9' }}</div>
                <div class="mini-item-sub">\u521b\u5efa\u65f6\u95f4\uff1a{{ formatTime(item.createdAt) }}</div>
                <div class="mini-item-sub">\u53d1\u9001\u65f6\u95f4\uff1a{{ formatTime(item.sentAt) }}</div>
                <div v-if="item.failureReason" class="mini-item-sub">\u5931\u8d25\u539f\u56e0\uff1a{{ item.failureReason }}</div>
              </div>
            </div>
            <div v-else class="empty-panel">\u6682\u65e0\u6d88\u606f\u8bb0\u5f55</div>
          </div>
        </section>

        <section v-else-if="activeTab === 'activities'" class="page-section panel-grid two-column">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u53d1\u5e03\u6d3b\u52a8</div>
                <div class="card-subtitle">\u6d3b\u52a8\u57fa\u7840\u4fe1\u606f</div>
              </div>
            </div>
            <div class="form-grid two-column">
              <div class="field-block span-2">
                <label class="field-label">\u670d\u52a1\u7ad9\u70b9</label>
                <select v-model="form.serviceSiteId" class="native-field">
                  <option value="">\u4e0d\u7ed1\u5b9a\u7ad9\u70b9\uff0c\u76f4\u63a5\u624b\u52a8\u914d\u7f6e</option>
                  <option v-for="item in activeServiceSites" :key="'site-option-' + item.id" :value="String(item.id)">
                    {{ item.name }} / {{ item.streetName }} / {{ item.communityName }}
                  </option>
                </select>
                <div class="field-tip">\u9009\u62e9\u7ad9\u70b9\u540e\uff0c\u4f1a\u81ea\u52a8\u5e26\u51fa\u5730\u70b9\u3001\u7ecf\u7eac\u5ea6\u548c\u63a8\u8350\u56f4\u680f\u534a\u5f84\uff0c\u4f60\u4ecd\u7136\u53ef\u4ee5\u7ee7\u7eed\u5fae\u8c03\u3002</div>
              </div>
              <div class="field-block"><label class="field-label">\u6d3b\u52a8\u6807\u9898</label><input v-model="form.title" class="form-input"></div>
              <div class="field-block">
                <label class="field-label">\u6d3b\u52a8\u7c7b\u522b</label>
                <select v-model="form.category" class="native-field">
                  <option v-for="item in activityCategories" :key="'activity-category-' + item" :value="item">{{ item }}</option>
                </select>
              </div>
              <div class="field-block span-2"><label class="field-label">\u6d3b\u52a8\u7b80\u4ecb</label><textarea v-model="form.description" class="form-textarea" rows="4"></textarea></div>
              <div class="field-block"><label class="field-label">\u6d3b\u52a8\u5730\u70b9</label><input v-model="form.location" class="form-input"></div>
              <div class="field-block span-2">
                <label class="field-label">\u5730\u70b9\u641c\u7d22</label>
                <div class="inline-search-row">
                  <input v-model="publishSearchKeyword" class="form-input" placeholder="\u8f93\u5165\u793e\u533a\u3001\u5b66\u6821\u3001\u670d\u52a1\u7ad9\u540d\u79f0">
                  <button class="primary-button small-button" type="button" @click="searchPublishLocation">\u641c\u7d22\u5730\u70b9</button>
                </div>
                <div class="field-tip">\u70b9\u51fb\u5019\u9009\u5730\u70b9\u540e\uff0c\u4f1a\u81ea\u52a8\u56de\u586b\u5730\u70b9\u3001\u7eac\u5ea6\u548c\u7ecf\u5ea6\u3002</div>
                <div v-if="publishSearchLoading" class="field-tip">\u6b63\u5728\u641c\u7d22\u5730\u70b9...</div>
                <div v-if="publishSearchResults.length" class="search-result-list web-search-results">
                  <button
                    v-for="item in publishSearchResults"
                    :key="item.id"
                    class="search-result-item web-search-item"
                    type="button"
                    @click="choosePublishLocation(item)"
                  >
                    <div class="search-result-title">{{ item.title }}</div>
                    <div class="search-result-address">{{ item.address }}</div>
                    <div class="search-result-meta">{{ item.latitude }}, {{ item.longitude }}</div>
                  </button>
                </div>
                <div v-else-if="publishSearchAttempted && !publishSearchLoading" class="field-tip">\u6682\u65e0\u5339\u914d\u5730\u70b9\uff0c\u53ef\u5c1d\u8bd5\u8f93\u5165\u66f4\u5b8c\u6574\u7684\u5730\u5740\u540d\u79f0\u3002</div>
              </div>
              <div class="field-block"><label class="field-label">\u4eba\u6570\u4e0a\u9650</label><input v-model="form.capacity" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">\u9700\u6c42\u7b49\u7ea7</label><input v-model="form.demandLevel" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">\u96be\u5ea6\u7b49\u7ea7</label><input v-model="form.difficultyLevel" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">\u96be\u5ea6\u7cfb\u6570</label><input v-model="form.difficultyCoefficient" class="form-input" type="number" step="0.1"></div>
              <div class="field-block"><label class="field-label">\u56f4\u680f\u534a\u5f84</label><input v-model="form.geofenceRadiusMeters" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">\u7eac\u5ea6</label><input v-model="form.latitude" class="form-input" type="number" step="0.0001"></div>
              <div class="field-block"><label class="field-label">\u7ecf\u5ea6</label><input v-model="form.longitude" class="form-input" type="number" step="0.0001"></div>
              <div class="field-block"><label class="field-label">\u5f00\u59cb\u65f6\u95f4</label><input v-model="form.startTime" class="form-input" type="datetime-local"></div>
              <div class="field-block"><label class="field-label">\u7ed3\u675f\u65f6\u95f4</label><input v-model="form.endTime" class="form-input" type="datetime-local"></div>
            </div>
            <div class="card-actions">
              <button class="primary-button" type="button" @click="createActivity">\u521b\u5efa\u6d3b\u52a8</button>
              <button class="ghost-button" type="button" @click="resetForm">\u91cd\u7f6e\u8868\u5355</button>
            </div>
          </div>

          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u6d3b\u52a8\u5217\u8868</div>
                <div class="card-subtitle">\u6d3b\u52a8\u5217\u8868\u4e0e\u7b7e\u5230\u7801</div>
              </div>
            </div>
            <div class="table-wrapper">
              <table class="admin-table">
                <thead>
                  <tr>
                    <th>\u6d3b\u52a8</th>
                    <th>\u65f6\u95f4</th>
                    <th>\u5730\u70b9</th>
                    <th>\u72b6\u6001</th>
                    <th>\u7b7e\u5230\u7801 / \u4e8c\u7ef4\u7801</th>
                    <th>\u64cd\u4f5c</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in activities" :key="item.id">
                    <td>
                      <div class="cell-title">{{ item.title }}</div>
                      <div class="cell-subtitle">{{ item.category || '\u672a\u5206\u7c7b' }}</div>
                    </td>
                    <td>
                      <div>{{ formatTime(item.startTime) }}</div>
                      <div class="cell-subtitle">{{ formatTime(item.endTime) }}</div>
                    </td>
                    <td>{{ item.location || '-' }}</td>
                    <td>{{ activityLabel(item.status) }}</td>
                    <td>
                      <div v-if="item.checkInCode" class="code-qr-cell">
                        <div class="cell-title">{{ item.checkInCode }}</div>
                        <div class="cell-subtitle">\u5fd7\u613f\u8005\u53ef\u626b\u63cf\u4e8c\u7ef4\u7801\u76f4\u63a5\u7b7e\u5230</div>
                        <img
                          class="activity-qr-image"
                          :src="buildCheckInQrImageUrl(item)"
                          :alt="item.title + ' \u7b7e\u5230\u4e8c\u7ef4\u7801'"
                        >
                      </div>
                      <span v-else>-</span>
                    </td>
                    <td>
                      <div class="table-actions">
                        <button class="ghost-button small-button" type="button" @click="refreshCode(item.id)">\u5237\u65b0\u7b7e\u5230\u7801</button>
                      </div>
                    </td>
                  </tr>
                  <tr v-if="!activities.length"><td colspan="6" class="empty-cell">\u6682\u65e0\u6d3b\u52a8\u6570\u636e</td></tr>
                </tbody>
              </table>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'service-sites'" class="page-section panel-grid two-column">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u670d\u52a1\u7ad9\u70b9</div>
                <div class="card-subtitle">\u8857\u9053\u3001\u793e\u533a\u4e0e\u7ad9\u70b9\u7ef4\u62a4</div>
              </div>
            </div>
            <div class="form-grid two-column">
              <div class="field-block"><label class="field-label">\u7ad9\u70b9\u540d\u79f0</label><input v-model="siteForm.name" class="form-input"></div>
              <div class="field-block"><label class="field-label">\u6240\u5c5e\u8857\u9053</label><input v-model="siteForm.streetName" class="form-input"></div>
              <div class="field-block"><label class="field-label">\u6240\u5c5e\u793e\u533a</label><input v-model="siteForm.communityName" class="form-input"></div>
              <div class="field-block"><label class="field-label">\u63a8\u8350\u534a\u5f84\uff08\u7c73\uff09</label><input v-model="siteForm.recommendedRadiusMeters" class="form-input" type="number"></div>
              <div class="field-block span-2"><label class="field-label">\u8be6\u7ec6\u5730\u5740</label><input v-model="siteForm.address" class="form-input"></div>
              <div class="field-block span-2">
                <label class="field-label">\u5730\u70b9\u641c\u7d22</label>
                <div class="inline-search-row">
                  <input v-model="siteSearchKeyword" class="form-input" placeholder="\u641c\u7d22\u5730\u70b9\u540d\u79f0\uff0c\u81ea\u52a8\u56de\u586b\u5730\u5740\u548c\u7ecf\u7eac\u5ea6">
                  <button class="primary-button" type="button" @click="searchSiteLocation">\u641c\u7d22\u5730\u70b9</button>
                </div>
                <div class="field-tip">\u53ef\u76f4\u63a5\u641c\u7d22\u793e\u533a\u3001\u5b66\u6821\u3001\u517b\u8001\u9662\u3001\u670d\u52a1\u7ad9\u7b49\u5730\u70b9\uff0c\u9009\u4e2d\u540e\u81ea\u52a8\u56de\u586b\u8be6\u7ec6\u5730\u5740\u548c\u7ecf\u7eac\u5ea6\u3002</div>
                <div v-if="siteSearchLoading" class="field-tip">\u6b63\u5728\u641c\u7d22\u5730\u70b9...</div>
                <div v-if="siteSearchResults.length" class="web-search-results">
                  <button
                    v-for="item in siteSearchResults"
                    :key="'site-search-' + item.id"
                    class="web-search-item"
                    type="button"
                    @click="chooseSiteLocation(item)"
                  >
                    <div class="result-title">{{ item.title }}</div>
                    <div class="result-subtitle">{{ item.address }}</div>
                    <div class="result-meta">{{ item.latitude }}, {{ item.longitude }}</div>
                  </button>
                </div>
                <div v-else-if="siteSearchAttempted && siteSearchKeyword && !siteSearchLoading" class="field-tip">\u6682\u65e0\u5339\u914d\u5730\u70b9\uff0c\u53ef\u5c1d\u8bd5\u8f93\u5165\u66f4\u5b8c\u6574\u7684\u5730\u5740\u540d\u79f0\u3002</div>
              </div>
              <div class="field-block"><label class="field-label">\u7eac\u5ea6</label><input v-model="siteForm.latitude" class="form-input" type="number" step="0.000001"></div>
              <div class="field-block"><label class="field-label">\u7ecf\u5ea6</label><input v-model="siteForm.longitude" class="form-input" type="number" step="0.000001"></div>
              <div class="field-block span-2">
                <label class="field-label">\u542f\u7528\u72b6\u6001</label>
                <label class="inline-check">
                  <input v-model="siteForm.enabled" type="checkbox">
                  <span>\u542f\u7528\u8be5\u7ad9\u70b9</span>
                </label>
              </div>
            </div>
            <div class="card-actions">
              <button class="primary-button" type="button" @click="saveSite">{{ siteForm.id ? '\u4fdd\u5b58\u7ad9\u70b9' : '\u65b0\u589e\u7ad9\u70b9' }}</button>
              <button class="ghost-button" type="button" @click="resetSiteForm">\u91cd\u7f6e\u8868\u5355</button>
            </div>
          </div>

          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u7ad9\u70b9\u5217\u8868</div>
                <div class="card-subtitle">\u7ad9\u70b9\u5217\u8868\u4e0e\u72b6\u6001\u7ba1\u7406</div>
              </div>
            </div>
            <div class="table-wrapper">
              <table class="admin-table">
                <thead>
                  <tr>
                    <th>\u7ad9\u70b9</th>
                    <th>\u6240\u5c5e\u533a\u57df</th>
                    <th>\u5730\u5740 / \u534a\u5f84</th>
                    <th>\u72b6\u6001</th>
                    <th>\u64cd\u4f5c</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in serviceSites" :key="'site-' + item.id">
                    <td>
                      <div class="cell-title">{{ item.name }}</div>
                      <div class="cell-subtitle">{{ item.latitude }}, {{ item.longitude }}</div>
                    </td>
                    <td>
                      <div>{{ item.streetName }}</div>
                      <div class="cell-subtitle">{{ item.communityName }}</div>
                    </td>
                    <td>
                      <div>{{ item.address }}</div>
                      <div class="cell-subtitle">\u63a8\u8350\u534a\u5f84\uff1a{{ item.recommendedRadiusMeters }} \u7c73</div>
                    </td>
                    <td>
                      <span class="tag" :class="item.enabled ? 'theme-success' : 'theme-default'">
                        {{ item.enabled ? '\u5df2\u542f\u7528' : '\u5df2\u505c\u7528' }}
                      </span>
                    </td>
                    <td>
                      <div class="table-actions">
                        <button class="ghost-button small-button" type="button" @click="editSite(item)">\u7f16\u8f91</button>
                        <button class="ghost-button small-button" type="button" @click="toggleSite(item)">
                          {{ item.enabled ? '\u505c\u7528' : '\u542f\u7528' }}
                        </button>
                      </div>
                    </td>
                  </tr>
                  <tr v-if="!serviceSites.length"><td colspan="5" class="empty-cell">\u6682\u65e0\u670d\u52a1\u7ad9\u70b9</td></tr>
                </tbody>
              </table>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'settings'" class="page-section panel-grid two-column">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u7cfb\u7edf\u8bbe\u7f6e</div>
                <div class="card-subtitle">\u5e73\u53f0\u5168\u5c40\u914d\u7f6e</div>
              </div>
            </div>
            <div class="form-grid">
              <div class="field-block"><label class="field-label">\u7cfb\u7edf\u540d\u79f0</label><input v-model="settings.systemName" class="form-input"></div>
              <div class="field-block"><label class="field-label">\u9ed8\u8ba4\u56f4\u680f\u534a\u5f84</label><input v-model="settings.defaultGeofenceRadiusMeters" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">\u6700\u5c11\u8f68\u8ff9\u70b9\u6570</label><input v-model="settings.minTrackPoints" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">\u6700\u5c11\u5feb\u7167\u6570\u91cf</label><input v-model="settings.minSnapshotCount" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">\u6700\u77ed\u670d\u52a1\u65f6\u957f\uff08\u5c0f\u65f6\uff09</label><input v-model="settings.minServiceHours" class="form-input" type="number" step="0.1"></div>
              <div class="field-block"><label class="field-label">30 \u5929\u5185\u8865\u7b7e\u9884\u8b66\u9608\u503c</label><input v-model="settings.frequentSupplementLimit30Days" class="form-input" type="number"></div>
              <div class="field-block"><label class="field-label">\u8bc1\u4e66\u6807\u9898</label><input v-model="settings.certificateTitle" class="form-input"></div>
              <div class="field-block"><label class="field-label">\u540e\u53f0\u5730\u5740</label><input v-model="settings.adminWebUrl" class="form-input" placeholder="https://admin.example.com"></div>
              <div class="field-block"><label class="field-label">\u8865\u7b7e\u9ed8\u8ba4\u610f\u89c1</label><textarea v-model="settings.supplementApproveComment" class="form-textarea" rows="4"></textarea></div>
            </div>
            <div class="card-actions">
              <button class="primary-button" type="button" @click="saveSettings">\u4fdd\u5b58\u8bbe\u7f6e</button>
              <button class="ghost-button" type="button" @click="copyUrl">\u590d\u5236\u540e\u53f0\u5730\u5740</button>
            </div>
          </div>

          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u6f14\u793a\u6570\u636e</div>
                <div class="card-subtitle">\u6f14\u793a\u6570\u636e\u521d\u59cb\u5316\u4e0e\u91cd\u7f6e</div>
              </div>
            </div>
            <div class="hint-list">
              <div>\u521d\u59cb\u5316\u6f14\u793a\u6570\u636e\uff1a\u8865\u5145\u793a\u4f8b\u6d3b\u52a8\u548c\u793a\u4f8b\u5546\u54c1\uff0c\u4e0d\u6e05\u7a7a\u73b0\u6709\u4e1a\u52a1\u8bb0\u5f55\u3002</div>
              <div>\u91cd\u7f6e\u6f14\u793a\u6570\u636e\uff1a\u4fdd\u7559\u7528\u6237\u3001\u89d2\u8272\u548c\u7cfb\u7edf\u8bbe\u7f6e\uff0c\u518d\u91cd\u7f6e\u4e1a\u52a1\u6570\u636e\u3002</div>
            </div>
            <div class="card-actions">
              <button class="ghost-button" type="button" @click="initDemo">\u521d\u59cb\u5316\u6f14\u793a\u6570\u636e</button>
              <button class="danger-button" type="button" @click="resetDemo">\u91cd\u7f6e\u6f14\u793a\u6570\u636e</button>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'service-records'" class="page-section">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u670d\u52a1\u8bb0\u5f55</div>
                <div class="card-subtitle">\u670d\u52a1\u8bb0\u5f55\u4e0e\u8bc4\u4ef7</div>
              </div>
            </div>
            <div class="module-stack">
              <div class="inline-toolbar">
                <select v-model="selectedRecordActivityId" class="native-field">
                  <option value="">\u8bf7\u9009\u62e9\u6d3b\u52a8</option>
                  <option v-for="item in activities" :key="'record-' + item.id" :value="String(item.id)">{{ item.title }}</option>
                </select>
              </div>
              <div class="table-wrapper">
                <table class="admin-table">
                  <thead>
                    <tr>
                      <th>\u7528\u6237</th>
                      <th>\u72b6\u6001</th>
                      <th>\u670d\u52a1\u65f6\u957f</th>
                      <th>\u79ef\u5206</th>
                      <th>\u8bc4\u5206</th>
                      <th>\u64cd\u4f5c</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="record in serviceRecords" :key="record.recordId">
                      <td>
                        <div class="cell-title">{{ record.userName || ('ID ' + record.userId) }}</div>
                        <div class="cell-subtitle">{{ formatTime(record.checkInTime) }}</div>
                      </td>
                      <td>{{ formatParticipationStatus(record.status) }}</td>
                      <td>{{ record.serviceHours || 0 }}</td>
                      <td>{{ record.earnedPoints || 0 }}</td>
                      <td>{{ record.serviceRating || '\u672a\u8bc4\u4ef7' }}</td>
                      <td>
                        <div class="action-stack">
                          <input v-model="recordReviewForms[record.recordId].rating" class="native-field small-field" type="number" min="1" max="5" placeholder="1-5">
                          <textarea v-model="recordReviewForms[record.recordId].comment" class="form-textarea compact-area" rows="2" placeholder="\u8f93\u5165\u670d\u52a1\u8bc4\u4ef7"></textarea>
                          <button class="ghost-button small-button" type="button" :disabled="!canExportCertificate(record)" @click="openCertificatePreview(record)">\u5bfc\u51fa\u8bc1\u660e</button>
                          <button class="success-button small-button" type="button" @click="evaluateRecord(record)">\u63d0\u4ea4\u8bc4\u4ef7</button>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="!serviceRecords.length"><td colspan="6" class="empty-cell">\u6682\u65e0\u670d\u52a1\u8bb0\u5f55</td></tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-if="certificatePreviewVisible && certificatePreview" class="proof-overlay" @click.self="closeCertificatePreview">
              <div class="proof-dialog card">
                <div class="proof-header">
                  <div>
                    <div class="card-title">\u5fd7\u613f\u670d\u52a1\u8bc1\u660e\u9884\u89c8</div>
                    <div class="card-subtitle">\u652f\u6301\u6253\u5370\u4e0e PDF \u5bfc\u51fa</div>
                  </div>
                  <div class="proof-actions">
                    <button class="primary-button small-button" type="button" @click="downloadCertificatePdf">\u5bfc\u51fa PDF</button>
                    <button class="ghost-button small-button" type="button" @click="printCertificatePreview">\u6253\u5370\u9884\u89c8</button>
                    <button class="ghost-button small-button" type="button" @click="closeCertificatePreview">\u5173\u95ed</button>
                  </div>
                </div>
                <div class="certificate-sheet">
                  <div class="certificate-title">{{ certificatePreview.certificateTitle || '志愿服务证明' }}</div>
                  <div class="certificate-subtitle">{{ certificatePreview.systemName || '玉屏志愿平台' }}</div>

                  <div class="certificate-section">
                    <div class="certificate-section-title">\u5fd7\u613f\u8005\u4fe1\u606f</div>
                    <div class="certificate-grid">
                      <div class="certificate-item">
                        <div class="certificate-label">\u5fd7\u613f\u8005\u59d3\u540d</div>
                        <div class="certificate-value">{{ certificatePreview.volunteerName || '-' }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u5fd7\u613f\u8005\u53f7</div>
                        <div class="certificate-value">{{ maskVolunteerCardNo(certificatePreview.volunteerCardNo) || '-' }}</div>
                      </div>
                      <div class="certificate-item certificate-item-full">
                        <div class="certificate-label">\u8eab\u4efd\u8bc1\u53f7\uff08\u8131\u654f\uff09</div>
                        <div class="certificate-value">{{ certificatePreview.maskedIdCardNo || '-' }}</div>
                      </div>
                    </div>
                  </div>

                  <div class="certificate-section">
                    <div class="certificate-section-title">\u670d\u52a1\u4fe1\u606f</div>
                    <div class="certificate-grid">
                      <div class="certificate-item">
                        <div class="certificate-label">\u6d3b\u52a8\u540d\u79f0</div>
                        <div class="certificate-value">{{ certificatePreview.activityTitle || '-' }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u670d\u52a1\u5730\u70b9</div>
                        <div class="certificate-value">{{ certificatePreview.activityLocation || '-' }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u6d3b\u52a8\u65f6\u95f4</div>
                        <div class="certificate-value">{{ formatTime(certificatePreview.activityStartTime) }} \u81f3 {{ formatTime(certificatePreview.activityEndTime) }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u7b7e\u5230\u7b7e\u9000</div>
                        <div class="certificate-value">{{ formatTime(certificatePreview.checkInTime) }} \u81f3 {{ formatTime(certificatePreview.checkOutTime) }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u670d\u52a1\u65f6\u957f</div>
                        <div class="certificate-value">{{ certificatePreview.serviceHours || 0 }} \u5c0f\u65f6</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u670d\u52a1\u8bc4\u5206 / \u79ef\u5206</div>
                        <div class="certificate-value">{{ certificatePreview.serviceRating || '-' }} \u5206 / {{ certificatePreview.earnedPoints || 0 }} \u79ef\u5206</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u7ec4\u7ec7\u8005\u786e\u8ba4\u65f6\u95f4</div>
                        <div class="certificate-value">{{ formatTime(certificatePreview.organizerConfirmedAt) }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u51fa\u5177\u65e5\u671f</div>
                        <div class="certificate-value">{{ formatProofDate(certificatePreview.issuedAt) }}</div>
                      </div>
                      <div class="certificate-item certificate-item-full">
                        <div class="certificate-label">\u670d\u52a1\u8bc4\u4ef7 / \u5907\u6ce8</div>
                        <div class="certificate-value certificate-summary">{{ certificatePreview.serviceComment || certificatePreview.organizerConfirmComment || '\u65e0' }}</div>
                      </div>
                    </div>
                  </div>

                  <div class="certificate-section">
                    <div class="certificate-section-title">\u5b58\u8bc1\u4e0e\u8bc1\u4e66</div>
                    <div class="certificate-grid">
                      <div class="certificate-item">
                        <div class="certificate-label">\u8bc1\u4e66\u7f16\u53f7</div>
                        <div class="certificate-value">{{ certificatePreview.certificateNo || '-' }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u533a\u5757\u94fe\u5b58\u8bc1</div>
                        <div class="certificate-value">{{ formatBlockchainProofStatus(certificatePreview.blockchainProofStatus) }}</div>
                      </div>
                      <div class="certificate-item certificate-item-full">
                        <div class="certificate-label">\u539f\u59cb\u54c8\u5e0c</div>
                        <div class="certificate-value certificate-hash">{{ certificatePreview.evidenceHash || '-' }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u5b58\u8bc1\u6d41\u6c34\u53f7</div>
                        <div class="certificate-value">{{ certificatePreview.blockchainTransactionNo || '-' }}</div>
                      </div>
                      <div class="certificate-item">
                        <div class="certificate-label">\u7535\u5b50\u8bc1\u4e66</div>
                        <div class="certificate-value">
                          <a v-if="certificatePreview.certificateUrl" class="inline-link" :href="toFileUrl(certificatePreview.certificateUrl)" target="_blank">\u67e5\u770b\u7535\u5b50\u8bc1\u4e66</a>
                          <span v-else>-</span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="certificate-section">
                    <div class="certificate-section-title">\u670d\u52a1\u5feb\u7167</div>
                    <div v-if="certificatePreview.snapshots && certificatePreview.snapshots.length" class="certificate-snapshots">
                      <div v-for="(snapshot, index) in certificatePreview.snapshots" :key="'proof-snapshot-' + index" class="certificate-snapshot">
                        <img :src="toFileUrl(snapshot.imageUrl)" :alt="snapshot.note || '服务快照'">
                        <div class="certificate-snapshot-note">{{ snapshot.note || '\u670d\u52a1\u5feb\u7167' }}</div>
                        <div class="certificate-snapshot-time">{{ formatTime(snapshot.createdAt) }}</div>
                      </div>
                    </div>
                    <div v-else class="empty-panel">\u6682\u65e0\u670d\u52a1\u5feb\u7167</div>
                  </div>

                  <div class="certificate-footer">
                    <div class="certificate-footer-meta">
                      <div>\u51fa\u5177\u5e73\u53f0\uff1a{{ certificatePreview.systemName || '玉屏志愿平台' }}</div>
                      <div>\u8be5\u8bc1\u660e\u4f9d\u636e\u670d\u52a1\u8bb0\u5f55\u3001\u5feb\u7167\u3001\u8f68\u8ff9\u4e0e\u7ec4\u7ec7\u8005\u786e\u8ba4\u4fe1\u606f\u751f\u6210\u3002</div>
                    </div>
                    <div class="certificate-seal-box">\u76d6\u7ae0\u533a</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'supplement-review'" class="page-section">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u8865\u7b7e\u5ba1\u6838</div>
                <div class="card-subtitle">\u7b7e\u5230\u4e0e\u7b7e\u9000\u8865\u7b7e\u5ba1\u6838</div>
              </div>
            </div>
            <div class="module-stack">
              <div class="inline-toolbar">
                <div class="toolbar-tip">\u663e\u793a\u5f85\u5ba1\u6838\u8865\u7b7e\u7533\u8bf7</div>
              </div>
              <div class="application-list">
                <div v-for="item in supplements" :key="item.id" class="application-item">
                  <div class="application-head">
                    <div>
                      <div class="application-name">\u6d3b\u52a8 {{ item.activityId }} / \u7528\u6237 {{ item.userId }}</div>
                      <div class="application-meta">
                        <span>\u8865\u7b7e\u7c7b\u578b\uff1a{{ item.type || '-' }}</span>
                        <span>\u7533\u8bf7\u65f6\u95f4\uff1a{{ formatTime(item.requestedTime || item.createdAt) }}</span>
                      </div>
                    </div>
                    <span class="tag theme-warning">{{ item.status || '\u5f85\u5ba1\u6838' }}</span>
                  </div>
                  <div class="application-block">
                    <div class="mini-label">\u7533\u8bf7\u539f\u56e0</div>
                    <div>{{ item.reason || '-' }}</div>
                  </div>
                  <div class="application-block" v-if="item.evidenceImageUrl">
                    <div class="mini-label">\u8865\u7b7e\u51ed\u8bc1</div>
                    <a class="inline-link" :href="toFileUrl(item.evidenceImageUrl)" target="_blank">{{ item.evidenceImageUrl }}</a>
                  </div>
                  <div class="application-block">
                    <div class="mini-label">\u5ba1\u6838\u610f\u89c1</div>
                    <textarea v-model="supplementReviewForms[item.id]" class="form-textarea" rows="3" placeholder="\u8bf7\u8f93\u5165\u5ba1\u6838\u610f\u89c1"></textarea>
                  </div>
                  <div class="table-actions">
                    <button class="success-button" type="button" @click="reviewSupplement(item, true)">\u901a\u8fc7</button>
                    <button class="danger-button" type="button" @click="reviewSupplement(item, false)">\u9a73\u56de</button>
                  </div>
                </div>
                <div v-if="!supplements.length" class="empty-panel">\u6682\u65e0\u5f85\u5ba1\u6838\u8865\u7b7e</div>
              </div>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'evidence-center'" class="page-section">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u8bc1\u636e\u4e2d\u5fc3</div>
                <div class="card-subtitle">\u670d\u52a1\u8bb0\u5f55\u3001\u5feb\u7167\u4e0e\u8f68\u8ff9\u8bc1\u636e</div>
              </div>
            </div>
            <div class="module-stack">
              <div class="inline-toolbar">
                <select v-model="selectedEvidenceActivityId" class="native-field">
                  <option value="">\u8bf7\u9009\u62e9\u6d3b\u52a8</option>
                  <option v-for="item in activities" :key="'evidence-' + item.id" :value="String(item.id)">{{ item.title }}</option>
                </select>
                <button class="ghost-button small-button" type="button" @click="backfillEvidenceProofs">\u8865\u5f55\u5386\u53f2\u5b58\u8bc1</button>
              </div>
              <div class="panel-grid two-column">
                <div class="card soft-card">
                  <div class="card-title soft-title">\u670d\u52a1\u8bb0\u5f55</div>
                  <div class="mini-list">
                    <div v-for="item in evidenceRecords" :key="'ev-record-' + item.recordId" class="mini-item" :class="{ 'mini-item-highlight': highlightedEvidenceRecordId === item.recordId }">
                      <div class="mini-item-head">
                        <div class="mini-item-title">{{ item.userName || ('ID ' + item.userId) }}</div>
                        <span v-if="item.anomalyReason" class="tag theme-warning">\u5f02\u5e38</span>
                      </div>
                      <div class="mini-item-sub">\u5feb\u7167 {{ item.snapshotCount }} / \u8f68\u8ff9 {{ item.trackPointCount }} / \u6821\u9a8c {{ item.evidenceValid ? '\u901a\u8fc7' : '\u672a\u901a\u8fc7' }}</div>
                      <div v-if="item.anomalyReason" class="mini-item-sub">{{ item.anomalyReason }}</div>
                    </div>
                    <div v-if="!evidenceRecords.length" class="empty-panel">\u6682\u65e0\u8bc1\u636e\u8bb0\u5f55</div>
                  </div>
                </div>
                <div class="card soft-card">
                  <div class="card-title soft-title">\u670d\u52a1\u5feb\u7167</div>
                  <div class="mini-list">
                    <div v-for="item in evidenceSnapshots" :key="'snapshot-' + item.id" class="mini-item">
                      <a class="inline-link" :href="toFileUrl(item.imageUrl)" target="_blank">{{ item.note || item.imageUrl }}</a>
                      <div class="mini-item-sub">{{ formatTime(item.createdAt) }}</div>
                    </div>
                    <div v-if="!evidenceSnapshots.length" class="empty-panel">\u6682\u65e0\u5feb\u7167</div>
                  </div>
                </div>
                <div class="card soft-card">
                  <div class="card-title soft-title">\u8f68\u8ff9\u70b9</div>
                  <div class="mini-list">
                    <div v-for="item in evidenceTrackPoints" :key="'track-' + item.id" class="mini-item">
                      <div class="mini-item-title">{{ item.pointType || 'MIDDLE' }} 路 {{ item.latitude }}, {{ item.longitude }}</div>
                      <div class="mini-item-sub">{{ formatTime(item.recordedAt) }}</div>
                    </div>
                    <div v-if="!evidenceTrackPoints.length" class="empty-panel">\u6682\u65e0\u8f68\u8ff9\u70b9</div>
                  </div>
                </div>
                <div class="card soft-card">
                  <div class="card-title soft-title">\u533a\u5757\u94fe\u5b58\u8bc1</div>
                  <div class="mini-list">
                    <div v-for="item in blockchainProofs" :key="'proof-' + item.id" class="mini-item">
                      <div class="mini-item-head">
                        <div class="mini-item-title">{{ formatBlockchainEventType(item.eventType) }}</div>
                        <span class="tag" :class="item.proofStatus === 'FAILED' ? 'theme-danger' : 'theme-success'">{{ formatBlockchainProofStatus(item.proofStatus) }}</span>
                      </div>
                      <div class="mini-item-sub">{{ item.userName || ('ID ' + item.userId) }} / {{ formatTime(item.anchoredAt || item.eventTime) }}</div>
                      <div class="mini-item-sub">\u6d41\u6c34\u53f7\uff1a{{ item.transactionNo || '-' }}</div>
                      <div class="mini-item-sub proof-hash">Hash\uff1a{{ item.evidenceHash || '-' }}</div>
                      <div v-if="item.payloadSummary" class="mini-item-sub proof-summary">{{ item.payloadSummary }}</div>
                      <div v-if="item.failureReason" class="mini-item-sub">{{ item.failureReason }}</div>
                    </div>
                    <div v-if="!blockchainProofs.length" class="empty-panel">\u6682\u65e0\u533a\u5757\u94fe\u5b58\u8bc1\u8bb0\u5f55</div>
                  </div>
                </div>
              </div>
              <div class="card soft-card">
                <div class="card-title soft-title">\u5730\u56fe\u8f68\u8ff9</div>
                <div v-if="evidenceTrackPoints.length" class="map-panel">
                  <div id="evidence-track-map" class="track-map-web"></div>
                  <div class="map-meta-grid">
                    <div class="map-meta-card">
                      <div class="mini-label">\u8d77\u70b9</div>
                      <div>{{ evidenceTrackSummary.startText }}</div>
                    </div>
                    <div class="map-meta-card">
                      <div class="mini-label">\u7ec8\u70b9</div>
                      <div>{{ evidenceTrackSummary.endText }}</div>
                    </div>
                    <div class="map-meta-card">
                      <div class="mini-label">\u8f68\u8ff9\u70b9</div>
                      <div>{{ evidenceTrackSummary.pointCount }}</div>
                    </div>
                    <div class="map-meta-card">
                      <div class="mini-label">\u505c\u7559\u70b9</div>
                      <div>{{ evidenceTrackSummary.stayCount }}</div>
                    </div>
                  </div>
                </div>
                <div v-else class="empty-panel">\u6682\u65e0\u5730\u56fe\u8f68\u8ff9</div>
              </div>
              <div class="card soft-card">
                <div class="card-title soft-title">\u8bc1\u636e\u65f6\u95f4\u8f74</div>
                <div class="timeline-list">
                  <div v-for="item in evidenceTimeline" :key="item.id" class="timeline-item" :class="{ 'timeline-item-anomaly': item.isAnomaly, 'timeline-item-highlight': highlightedEvidenceRecordId && highlightedEvidenceRecordId === item.recordId }">
                    <div class="timeline-marker"></div>
                    <div class="timeline-body">
                      <div class="timeline-head">
                        <div class="timeline-title">{{ item.title }}</div>
                        <div class="timeline-time">{{ item.time }}</div>
                      </div>
                      <div class="timeline-user">{{ item.userName }}</div>
                      <div class="timeline-detail">{{ item.detail }}</div>
                    </div>
                  </div>
                  <div v-if="!evidenceTimeline.length" class="empty-panel">\u6682\u65e0\u8bc1\u636e\u65f6\u95f4\u8f74</div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section v-else-if="activeTab === 'anomalies'" class="page-section">
          <div class="card page-card">
            <div class="card-header">
              <div>
                <div class="card-title">\u5f02\u5e38\u8bb0\u5f55</div>
                <div class="card-subtitle">\u5f02\u5e38\u8bb0\u5f55\u4e0e\u5904\u7f6e</div>
              </div>
            </div>
            <div class="module-stack">
              <div class="inline-toolbar">
                <div class="toolbar-tip">\u5f02\u5e38\u8bb0\u5f55\u81ea\u52a8\u5237\u65b0</div>
              </div>
              <div class="table-wrapper">
                <table class="admin-table">
                  <thead>
                    <tr>
                      <th>\u8bb0\u5f55</th>
                      <th>\u5f02\u5e38\u539f\u56e0</th>
                      <th>\u5904\u7f6e\u72b6\u6001</th>
                      <th>\u5904\u7f6e\u5907\u6ce8</th>
                      <th>\u64cd\u4f5c</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="item in anomalies" :key="'anomaly-' + item.recordId">
                      <td>
                        <div class="cell-title">{{ item.activityTitle || ('\u8bb0\u5f55 ' + item.recordId) }}</div>
                        <div class="cell-subtitle">{{ item.userName || ('\u7528\u6237 ' + item.userId) }}</div>
                      </td>
                      <td>{{ item.anomalyReason || '-' }}</td>
                      <td>{{ formatAnomalyStatus(item.anomalyStatus) }}</td>
                      <td>
                        <textarea v-model="anomalyRemarkForms[item.recordId]" class="form-textarea compact-area" rows="2" placeholder="\u8f93\u5165\u5904\u7f6e\u5907\u6ce8"></textarea>
                      </td>
                      <td>
                        <div class="table-actions">
                          <button class="ghost-button small-button" type="button" @click="openAnomalyEvidence(item)">\u67e5\u770b\u8bc1\u636e</button>
                          <button class="success-button small-button" type="button" @click="updateAnomaly(item, 'CONFIRMED')">\u6807\u8bb0\u786e\u8ba4</button>
                          <button class="ghost-button small-button" type="button" @click="updateAnomaly(item, 'IGNORED')">\u6807\u8bb0\u5ffd\u7565</button>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="!anomalies.length"><td colspan="5" class="empty-cell">\u6682\u65e0\u5f02\u5e38\u8bb0\u5f55</td></tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>
</div>
`;

const render = new Function('Vue', compile(template, { mode: 'function' }).code)(VueRuntime);

createApp({
  render,
  setup() {
    const state = reactive({ token: localStorage.getItem(TOKEN_KEY) || '', profile: null, restoring: true });
    const toast = reactive({ show: false, text: '', type: 'success', timer: null });
    const login = reactive({ baseUrl: localStorage.getItem(BASE_KEY) || API, username: 'superadmin', password: '123456' });
    const activeTab = ref('overview');
    const overview = reactive(emptyOverview());
    const annualOverview = reactive(emptyAnnualOverview());
    const analyticsYear = ref(new Date().getFullYear());
    const insightPeriod = ref('week');
    const analyticsUsers = ref([]);
    const analyticsLeaderboard = ref([]);
    const analyticsHeatmap = ref([]);
    const platformStarLeaderboard = ref([]);
    const platformCommunityInsights = ref([]);
    const analyticsRoleFilter = ref('ALL');
    const analyticsUserKeyword = ref('');
    const selectedAnalyticsUserId = ref('');
    const analyticsUserDetail = ref(emptyAnalyticsUserDetail());
    const settings = reactive(emptySettings());
    const form = reactive(emptyForm(settings.defaultGeofenceRadiusMeters));
    const siteForm = reactive(emptySiteForm());
    const storeForm = reactive(emptyStoreForm());
    const activityCategories = ACTIVITY_CATEGORIES;
    const storeCategories = STORE_CATEGORIES;
    const identity = reactive({
      verified: false,
      realName: '',
      idCardNo: '',
      volunteerCardNo: '',
    });
    const identityVisibility = reactive({
      realName: false,
      idCardNo: false,
      volunteerCardNo: false,
    });
    const identityForm = reactive({
      realName: '',
      idCardNo: '',
    });
    const publishSearchKeyword = ref('');
    const publishSearchResults = ref([]);
    const publishSearchLoading = ref(false);
    const publishSearchAttempted = ref(false);
    const siteSearchKeyword = ref('');
    const siteSearchResults = ref([]);
    const siteSearchLoading = ref(false);
    const siteSearchAttempted = ref(false);
    const users = ref([]);
    const applications = ref([]);
    const notifications = ref([]);
    const activities = ref([]);
    const serviceSites = ref([]);
    const storeItems = ref([]);
    const redemptionRecords = ref([]);
    const reviews = reactive({});
    const selectedRecordActivityId = ref('');
    const selectedEvidenceActivityId = ref('');
    const selectedEvidenceRecordId = ref('');
    const serviceRecords = ref([]);
    const certificatePreview = ref(null);
    const certificatePreviewVisible = ref(false);
    const supplements = ref([]);
    const anomalies = ref([]);
    const evidenceRecords = ref([]);
    const evidenceSnapshots = ref([]);
    const evidenceTrackPoints = ref([]);
    const evidenceTrackGeometryPoints = ref([]);
    const evidenceTimeline = ref([]);
    const blockchainProofs = ref([]);
    const supplementReviewForms = reactive({});
    const anomalyRemarkForms = reactive({});
    const recordReviewForms = reactive({});
    let evidenceMap = null;
    let analyticsMap = null;

    const currentMenu = computed(() => MENUS.find((item) => item.key === activeTab.value) || MENUS[0]);
    const profileName = computed(() => (state.profile && state.profile.name) || UI.systemAdminName);
    const profileAvatarUrl = computed(() => (state.profile && state.profile.avatarUrl) || '');
    const profileInitial = computed(() => {
      const clean = String(profileName.value || '').replace(/\s+/g, '');
      return clean ? clean.slice(-2) : UI.profileEmpty;
    });
    const activeServiceSites = computed(() => serviceSites.value.filter((item) => item && item.enabled));
    const evidenceTrackSummary = computed(() => buildTrackSummary(evidenceTrackGeometryPoints.value));
    const highlightedEvidenceRecordId = computed(() => Number(selectedEvidenceRecordId.value || 0));
    const filteredAnalyticsUsers = computed(() => {
      const keyword = String(analyticsUserKeyword.value || '').trim().toLowerCase();
      return analyticsUsers.value.filter((item) => {
        const roleMatched = analyticsRoleFilter.value === 'ALL' || item.role === analyticsRoleFilter.value;
        if (!roleMatched) {
          return false;
        }
        if (!keyword) {
          return true;
        }
        const name = String(item.name || '').toLowerCase();
        const cardNo = String(item.volunteerCardNo || '').toLowerCase();
        return name.includes(keyword) || cardNo.includes(keyword) || String(item.id || '').includes(keyword);
      });
    });
    const stats = computed(() => [
      { key: 'activities', label: '\u6d3b\u52a8\u603b\u6570', value: overview.totalActivities },
      { key: 'volunteers', label: '\u5fd7\u613f\u8005\u4eba\u6570', value: overview.totalVolunteers },
      { key: 'records', label: '\u5b8c\u6210\u8bb0\u5f55', value: overview.completedRecords },
      { key: 'points', label: '\u79ef\u5206\u53d1\u653e', value: overview.totalPointsIssued },
    ]);
    const todos = computed(() => [
      { key: 'applications', target: 'users', label: '\u5f85\u5ba1\u6838\u7533\u8bf7', value: applications.value.length },
      { key: 'supplements', target: 'supplement-review', label: '\u5f85\u5904\u7406\u8865\u7b7e', value: overview.pendingSupplements },
      { key: 'anomalies', target: 'anomalies', label: '\u5f85\u5904\u7406\u5f02\u5e38', value: overview.pendingAnomalies },
      { key: 'stock', target: 'activities', label: '\u4f4e\u5e93\u5b58\u5546\u54c1', value: overview.lowStockItems },
    ]);

    function showToast(text, type = 'success') {
      toast.text = text;
      toast.type = type;
      toast.show = true;
      if (toast.timer) clearTimeout(toast.timer);
      toast.timer = setTimeout(() => {
        toast.show = false;
      }, 2200);
    }

    function persistBase() {
      login.baseUrl = normalizeBase(login.baseUrl);
      localStorage.setItem(BASE_KEY, login.baseUrl);
    }

    function clearSession() {
      state.token = '';
      state.profile = null;
      localStorage.removeItem(TOKEN_KEY);
    }

    function syncIdentity(user) {
      identity.verified = !!(user && user.verified);
      identity.realName = (user && user.realName) || '';
      identity.idCardNo = (user && user.idCardNo) || '';
      identity.volunteerCardNo = (user && user.volunteerCardNo) || '';
      identityVisibility.realName = false;
      identityVisibility.idCardNo = false;
      identityVisibility.volunteerCardNo = false;
      identityForm.realName = identity.realName;
      identityForm.idCardNo = identity.idCardNo;
      if (state.profile) {
        state.profile.verified = identity.verified;
        state.profile.realName = identity.realName;
        state.profile.idCardNo = identity.idCardNo;
        state.profile.volunteerCardNo = identity.volunteerCardNo;
      }
    }

    function roleLabel(role) {
      return ROLE_TEXT[role] || role || '-';
    }

    function roleClass(role) {
      return ROLE_CLASS[role] || 'theme-default';
    }

    function applicationLabel(status) {
      return APP_STATUS_TEXT[status] || status || '-';
    }

    function applicationClass(status) {
      return APP_STATUS_CLASS[status] || 'theme-default';
    }

    function activityLabel(status) {
      return ACTIVITY_STATUS_TEXT[status] || status || '-';
    }

    async function loadIdentity() {
      const data = await request('GET', '/users/identity');
      syncIdentity(data || {});
    }

    async function saveIdentity() {
        try {
          const data = await request('POST', '/users/verify', {
            realName: identityForm.realName,
            idCardNo: identityForm.idCardNo,
          });
        syncIdentity(data || {});
        showToast('\u5b9e\u540d\u4fe1\u606f\u5df2\u66f4\u65b0\u3002');
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    function resetForm() {
      Object.assign(form, emptyForm(settings.defaultGeofenceRadiusMeters));
      publishSearchKeyword.value = '';
      publishSearchResults.value = [];
      publishSearchLoading.value = false;
      publishSearchAttempted.value = false;
    }

    function resetSiteForm() {
      Object.assign(siteForm, emptySiteForm());
      siteSearchKeyword.value = '';
      siteSearchResults.value = [];
      siteSearchLoading.value = false;
      siteSearchAttempted.value = false;
    }

    function resetStoreForm() {
      Object.assign(storeForm, emptyStoreForm());
    }

    function applyServiceSiteToForm(site) {
      if (!site) return;
      form.serviceSiteId = String(site.id);
      form.location = site.address || site.name || form.location;
      form.latitude = Number(site.latitude);
      form.longitude = Number(site.longitude);
      form.geofenceRadiusMeters = Number(site.recommendedRadiusMeters || settings.defaultGeofenceRadiusMeters);
      publishSearchKeyword.value = site.name || '';
      publishSearchResults.value = [];
      publishSearchAttempted.value = false;
    }

    async function searchPublishLocation() {
      const keyword = String(publishSearchKeyword.value || '').trim();
      if (keyword.length < 2) {
        publishSearchAttempted.value = false;
        publishSearchResults.value = [];
        showToast('请至少输入两个字', 'warning');
        return;
      }

      publishSearchAttempted.value = true;
      publishSearchLoading.value = true;
      try {
        const data = await request('GET', `/workflow/admin/map/search?keyword=${encodeURIComponent(keyword)}&latitude=${encodeURIComponent(form.latitude)}&longitude=${encodeURIComponent(form.longitude)}`);
        publishSearchResults.value = Array.isArray(data) ? data : [];
        if (!publishSearchResults.value.length) {
          showToast('未搜索到地点，请换更完整的地址', 'warning');
        }
      } catch (error) {
        publishSearchResults.value = [];
        showToast(error.message || TEXT.requestFailed, 'error');
      } finally {
        publishSearchLoading.value = false;
      }
    }

    function choosePublishLocation(item) {
      form.serviceSiteId = '';
      form.location = item.title || item.address || form.location;
      form.latitude = Number(item.latitude);
      form.longitude = Number(item.longitude);
      publishSearchKeyword.value = item.title || '';
      publishSearchResults.value = [];
      publishSearchAttempted.value = false;
    }

    function editSite(item) {
      Object.assign(siteForm, {
        id: item.id,
        name: item.name || '',
        streetName: item.streetName || '',
        communityName: item.communityName || '',
        address: item.address || '',
        latitude: Number(item.latitude),
        longitude: Number(item.longitude),
        recommendedRadiusMeters: Number(item.recommendedRadiusMeters || 300),
        enabled: !!item.enabled,
      });
      siteSearchKeyword.value = item.address || item.name || '';
      siteSearchResults.value = [];
      siteSearchLoading.value = false;
      siteSearchAttempted.value = false;
      activeTab.value = 'service-sites';
    }

    async function searchSiteLocation() {
      const keyword = String(siteSearchKeyword.value || '').trim();
      if (keyword.length < 2) {
        siteSearchAttempted.value = false;
        siteSearchResults.value = [];
        showToast('请至少输入两个字', 'warning');
        return;
      }

      siteSearchAttempted.value = true;
      siteSearchLoading.value = true;
      try {
        const data = await request(
          'GET',
          `/workflow/admin/map/search?keyword=${encodeURIComponent(keyword)}&latitude=${encodeURIComponent(siteForm.latitude)}&longitude=${encodeURIComponent(siteForm.longitude)}`
        );
        siteSearchResults.value = Array.isArray(data) ? data : [];
        if (!siteSearchResults.value.length) {
          showToast('未搜索到地点，请换更完整的地址', 'warning');
        }
      } catch (error) {
        siteSearchResults.value = [];
        showToast(error.message || TEXT.requestFailed, 'error');
      } finally {
        siteSearchLoading.value = false;
      }
    }

    function chooseSiteLocation(item) {
      siteForm.address = item.address || item.title || '';
      siteForm.latitude = Number(item.latitude);
      siteForm.longitude = Number(item.longitude);
      if (!siteForm.name) {
        siteForm.name = item.title || '';
      }
      siteSearchKeyword.value = item.title || '';
      siteSearchResults.value = [];
      siteSearchAttempted.value = false;
    }

    function ensureRecordReviewForm(recordId) {
      if (!recordReviewForms[recordId]) {
        recordReviewForms[recordId] = { rating: 5, comment: '' };
      }
    }

    function canExportCertificate(record) {
      return !!(record
        && record.status === 'COMPLETED'
        && record.organizerConfirmed
        && !record.rewardPending);
    }

    function toFileUrl(path) {
      if (!path) return '';
      if (/^https?:\/\//i.test(path)) return path;
      return `${normalizeBase(login.baseUrl).replace(/\/api$/, '')}${path}`;
    }

    function buildCertificateFileName(proof) {
      const title = (proof && proof.activityTitle) || '\u5fd7\u613f\u670d\u52a1\u8bc1\u660e';
      const safeTitle = String(title).replace(/[\\/:*?"<>|]/g, '-');
      const suffix = proof && proof.recordId ? `-${proof.recordId}` : '';
      return `${safeTitle}${suffix}.pdf`;
    }

    function renderEvidenceMap() {
      const container = document.getElementById('evidence-track-map');
      if (!container) return;

      const sorted = normalizeTrackPoints(evidenceTrackGeometryPoints.value);
      if (!sorted.length) {
        container.innerHTML = '<div class="map-inline-empty">\u5730\u56fe\u672a\u52a0\u8f7d\uff0c\u8bf7\u68c0\u67e5 Key \u4e0e\u767d\u540d\u5355\u914d\u7f6e</div>';
        return;
      }

      const TMapSdk = window.TMap;
      const missingSdkParts = !TMapSdk
        || typeof TMapSdk.Map !== 'function'
        || typeof TMapSdk.LatLng !== 'function'
        || typeof TMapSdk.MultiPolyline !== 'function'
        || typeof TMapSdk.PolylineStyle !== 'function';

      if (missingSdkParts) {
        container.innerHTML = '<div class="map-inline-empty">\u5730\u56fe\u670d\u52a1\u672a\u5c31\u7eea\uff0c\u8bf7\u68c0\u67e5 Key \u4e0e\u6743\u9650\u914d\u7f6e</div>';
        return;
      }

      if (evidenceMap && typeof evidenceMap.destroy === 'function') {
        evidenceMap.destroy();
      }

      const latLngPoints = sorted.map((item) => new TMapSdk.LatLng(item.latitude, item.longitude));
      const centerPoint = latLngPoints[Math.floor(latLngPoints.length / 2)];

      evidenceMap = new TMapSdk.Map(container, {
        center: centerPoint,
        zoom: 15,
        pitch: 0,
        rotation: 0,
      });

      if (typeof evidenceMap.fitBounds === 'function' && typeof TMapSdk.LatLngBounds === 'function') {
        const bounds = new TMapSdk.LatLngBounds();
        latLngPoints.forEach((point) => bounds.extend(point));
        evidenceMap.fitBounds(bounds, { padding: 60 });
      }

      new TMapSdk.MultiPolyline({
        id: 'evidence-track-line',
        map: evidenceMap,
        styles: {
          track: new TMapSdk.PolylineStyle({
            color: '#2563eb',
            width: 6,
            borderWidth: 2,
            borderColor: '#93c5fd',
            lineCap: 'round',
          }),
        },
        geometries: [
          {
            id: 'track',
            styleId: 'track',
            paths: latLngPoints,
          },
        ],
      });

      if (typeof TMapSdk.MultiMarker === 'function' && typeof TMapSdk.MarkerStyle === 'function') {
        const startPoint = sorted[0];
        const endPoint = sorted[sorted.length - 1];
        const stayPoints = [];

        for (let index = 1; index < sorted.length - 1; index += 1) {
          if (distanceMeters(sorted[index - 1], sorted[index]) < 30) {
            stayPoints.push(sorted[index]);
          }
        }

        const geometries = [
          {
            id: 'start',
            styleId: 'start',
            position: new TMapSdk.LatLng(startPoint.latitude, startPoint.longitude),
          },
          {
            id: 'end',
            styleId: 'end',
            position: new TMapSdk.LatLng(endPoint.latitude, endPoint.longitude),
          },
          ...stayPoints.map((item, index) => ({
            id: `stay-${index}`,
            styleId: 'stay',
            position: new TMapSdk.LatLng(item.latitude, item.longitude),
          })),
        ];

        new TMapSdk.MultiMarker({
          id: 'evidence-track-markers',
          map: evidenceMap,
          styles: {
            start: new TMapSdk.MarkerStyle({
              width: 28,
              height: 28,
              anchor: { x: 14, y: 14 },
              src: buildMarkerIcon('#16a34a'),
            }),
            end: new TMapSdk.MarkerStyle({
              width: 28,
              height: 28,
              anchor: { x: 14, y: 14 },
              src: buildMarkerIcon('#ef4444'),
            }),
            stay: new TMapSdk.MarkerStyle({
              width: 24,
              height: 24,
              anchor: { x: 12, y: 12 },
              src: buildMarkerIcon('#f59e0b'),
            }),
          },
          geometries,
        });
      }
    }

    function renderAnalyticsMap() {
      const container = document.getElementById('analytics-footprint-map');
      if (!container) return;

      const footprints = Array.isArray(analyticsUserDetail.value?.annualReport?.footprints)
        ? analyticsUserDetail.value.annualReport.footprints.filter((item) => Number.isFinite(Number(item.latitude)) && Number.isFinite(Number(item.longitude)))
        : [];

      if (!footprints.length) {
        container.innerHTML = '<div class="map-inline-empty">暂无年度足迹点</div>';
        return;
      }

      const TMapSdk = window.TMap;
      const missingSdkParts = !TMapSdk
        || typeof TMapSdk.Map !== 'function'
        || typeof TMapSdk.LatLng !== 'function'
        || typeof TMapSdk.MultiPolyline !== 'function'
        || typeof TMapSdk.PolylineStyle !== 'function';

      if (missingSdkParts) {
        container.innerHTML = '<div class="map-inline-empty">\u5730\u56fe\u670d\u52a1\u672a\u5c31\u7eea\uff0c\u8bf7\u68c0\u67e5 Key \u4e0e\u6743\u9650\u914d\u7f6e</div>';
        return;
      }

      if (analyticsMap && typeof analyticsMap.destroy === 'function') {
        analyticsMap.destroy();
      }

      const sorted = [...footprints].sort((a, b) => new Date(a.recordedAt || 0) - new Date(b.recordedAt || 0));
      const latLngPoints = sorted.map((item) => new TMapSdk.LatLng(Number(item.latitude), Number(item.longitude)));
      const centerPoint = latLngPoints[Math.floor(latLngPoints.length / 2)];

      analyticsMap = new TMapSdk.Map(container, {
        center: centerPoint,
        zoom: 12,
        pitch: 0,
        rotation: 0,
      });

      if (typeof analyticsMap.fitBounds === 'function' && typeof TMapSdk.LatLngBounds === 'function') {
        const bounds = new TMapSdk.LatLngBounds();
        latLngPoints.forEach((point) => bounds.extend(point));
        analyticsMap.fitBounds(bounds, { padding: 60 });
      }

      if (latLngPoints.length > 1) {
        new TMapSdk.MultiPolyline({
          id: 'analytics-footprint-line',
          map: analyticsMap,
          styles: {
            track: new TMapSdk.PolylineStyle({
              color: '#2563eb',
              width: 5,
              borderWidth: 2,
              borderColor: '#93c5fd',
              lineCap: 'round',
            }),
          },
          geometries: [
            {
              id: 'track',
              styleId: 'track',
              paths: latLngPoints,
            },
          ],
        });
      }

      if (typeof TMapSdk.MultiMarker === 'function' && typeof TMapSdk.MarkerStyle === 'function') {
        const geometries = sorted.map((item, index) => ({
          id: `annual-footprint-${index}`,
          styleId: index === 0 ? 'start' : index === sorted.length - 1 ? 'end' : 'middle',
          position: new TMapSdk.LatLng(Number(item.latitude), Number(item.longitude)),
        }));

        new TMapSdk.MultiMarker({
          id: 'analytics-footprint-markers',
          map: analyticsMap,
          styles: {
            start: new TMapSdk.MarkerStyle({
              width: 26,
              height: 26,
              anchor: { x: 13, y: 13 },
              src: buildMarkerIcon('#16a34a'),
            }),
            middle: new TMapSdk.MarkerStyle({
              width: 22,
              height: 22,
              anchor: { x: 11, y: 11 },
              src: buildMarkerIcon('#3b82f6'),
            }),
            end: new TMapSdk.MarkerStyle({
              width: 26,
              height: 26,
              anchor: { x: 13, y: 13 },
              src: buildMarkerIcon('#ef4444'),
            }),
          },
          geometries,
        });
      }
    }

    async function request(method, path, body, customBase) {
      const base = normalizeBase(customBase || login.baseUrl);
      const headers = { Accept: 'application/json' };
      if (body !== undefined) headers['Content-Type'] = 'application/json';
      if (state.token) headers.Authorization = `Bearer ${state.token}`;

      let response;
      try {
        response = await fetch(`${base}${path}`, {
          method,
          headers,
          body: body === undefined ? undefined : JSON.stringify(body),
        });
      } catch (error) {
        throw new Error(TEXT.backendOffline);
      }

      let payload = null;
      try {
        payload = await response.json();
      } catch (error) {
        payload = null;
      }

      if (!response.ok || !payload || payload.success === false) {
        if (response.status === 401 || response.status === 403) clearSession();
        throw new Error((payload && payload.message) || TEXT.requestFailed);
      }
      return payload.data;
    }

    async function loadOverview() {
      Object.assign(overview, emptyOverview(), await request('GET', '/admin/overview'));
    }

    async function loadAnnualOverview() {
      Object.assign(annualOverview, emptyAnnualOverview(), await request('GET', `/analytics/admin/annual-overview?year=${encodeURIComponent(analyticsYear.value)}`));
    }

    async function loadAnalyticsUsers() {
      const data = await request('GET', '/analytics/admin/users');
      analyticsUsers.value = Array.isArray(data) ? data : [];
      if (!selectedAnalyticsUserId.value && analyticsUsers.value.length) {
        selectedAnalyticsUserId.value = String(analyticsUsers.value[0].id);
      }
    }

    async function loadAnalyticsLeaderboard() {
      const data = await request('GET', '/analytics/admin/leaderboard');
      analyticsLeaderboard.value = Array.isArray(data) ? data : [];
    }

    async function loadAnalyticsHeatmap() {
      const data = await request('GET', '/analytics/admin/heatmap');
      analyticsHeatmap.value = Array.isArray(data) ? data : [];
    }

    async function loadPlatformStarLeaderboard() {
      const data = await request('GET', `/analytics/admin/star-leaderboard?period=${encodeURIComponent(insightPeriod.value)}`);
      platformStarLeaderboard.value = Array.isArray(data) ? data.map(formatStarLeaderboardItem) : [];
    }

    async function loadPlatformCommunityInsights() {
      const data = await request('GET', `/analytics/admin/community-insights?period=${encodeURIComponent(insightPeriod.value)}`);
      platformCommunityInsights.value = Array.isArray(data) ? data.map(formatCommunityInsightItem) : [];
    }

    async function loadSelectedAnalyticsUser() {
      if (!selectedAnalyticsUserId.value) {
        analyticsUserDetail.value = emptyAnalyticsUserDetail();
        await nextTick();
        renderAnalyticsMap();
        return;
      }
      const data = await request('GET', `/analytics/admin/users/${selectedAnalyticsUserId.value}?year=${encodeURIComponent(analyticsYear.value)}`);
      analyticsUserDetail.value = Object.assign(emptyAnalyticsUserDetail(), data || {});
      await nextTick();
      renderAnalyticsMap();
    }

    async function loadAnalyticsDashboard() {
      await Promise.all([
        loadAnnualOverview(),
        loadAnalyticsUsers(),
        loadAnalyticsLeaderboard(),
        loadAnalyticsHeatmap(),
        loadPlatformStarLeaderboard(),
        loadPlatformCommunityInsights(),
      ]);
      await loadSelectedAnalyticsUser();
    }

    async function loadUsers() {
      const data = await request('GET', '/admin/users');
      users.value = Array.isArray(data) ? data : [];
    }

    async function loadApplications() {
      const data = await request('GET', '/admin/admin-applications/pending');
      applications.value = Array.isArray(data) ? data : [];
      applications.value.forEach((item) => {
        if (reviews[item.id] === undefined) reviews[item.id] = '';
      });
    }

    async function loadNotifications() {
      const data = await request('GET', '/users/notifications');
      notifications.value = Array.isArray(data) ? data : [];
    }

    async function loadActivities() {
      const data = await request('GET', '/activities');
      activities.value = Array.isArray(data) ? data : [];
      if (!selectedRecordActivityId.value && activities.value.length) {
        selectedRecordActivityId.value = String(activities.value[0].id);
      }
      if (!selectedEvidenceActivityId.value && activities.value.length) {
        selectedEvidenceActivityId.value = String(activities.value[0].id);
      }
    }

    async function loadServiceSites() {
      const data = await request('GET', '/admin/service-sites');
      serviceSites.value = Array.isArray(data) ? data : [];
    }

    async function loadStoreItems() {
      const data = await request('GET', '/store/admin/items');
      storeItems.value = Array.isArray(data) ? data : [];
    }

    async function loadRedemptionRecords() {
      const data = await request('GET', '/store/admin/redemptions');
      redemptionRecords.value = Array.isArray(data) ? data : [];
    }

    async function loadSettings() {
      Object.assign(settings, emptySettings(), await request('GET', '/admin/system-settings'));
    }

    async function loadSupplements() {
      const data = await request('GET', '/workflow/admin/supplements/pending');
      supplements.value = Array.isArray(data) ? data : [];
      supplements.value.forEach((item) => {
        if (supplementReviewForms[item.id] === undefined) {
          supplementReviewForms[item.id] = settings.supplementApproveComment || '';
        }
      });
    }

    async function loadAnomalies() {
      const data = await request('GET', '/evidence/anomalies');
      anomalies.value = Array.isArray(data) ? data : [];
      anomalies.value.forEach((item) => {
        if (anomalyRemarkForms[item.recordId] === undefined) {
          anomalyRemarkForms[item.recordId] = item.anomalyRemark || '';
        }
      });
    }

    async function loadSelectedRecords() {
      if (!selectedRecordActivityId.value) {
        serviceRecords.value = [];
        return;
      }
      const data = await request('GET', `/activities/${selectedRecordActivityId.value}/records`);
      serviceRecords.value = Array.isArray(data) ? data : [];
      serviceRecords.value.forEach((item) => {
        ensureRecordReviewForm(item.recordId);
        if (item.serviceRating) {
          recordReviewForms[item.recordId].rating = item.serviceRating;
        }
        if (item.serviceComment) {
          recordReviewForms[item.recordId].comment = item.serviceComment;
        }
      });
    }

    async function loadEvidenceBundle() {
      if (!selectedEvidenceActivityId.value) {
        evidenceRecords.value = [];
        evidenceSnapshots.value = [];
        evidenceTrackPoints.value = [];
        evidenceTrackGeometryPoints.value = [];
        evidenceTimeline.value = [];
        blockchainProofs.value = [];
        selectedEvidenceRecordId.value = '';
        await nextTick();
        renderEvidenceMap();
        return;
      }
      const [records, snapshots, trackPoints, proofs] = await Promise.all([
        request('GET', `/evidence/activities/${selectedEvidenceActivityId.value}/records`),
        request('GET', `/evidence/activities/${selectedEvidenceActivityId.value}/snapshots`),
        request('GET', `/evidence/activities/${selectedEvidenceActivityId.value}/track-points`),
        request('GET', `/evidence/activities/${selectedEvidenceActivityId.value}/blockchain-proofs`),
      ]);
      evidenceRecords.value = Array.isArray(records) ? records : [];
      evidenceSnapshots.value = Array.isArray(snapshots) ? snapshots : [];
      evidenceTrackGeometryPoints.value = Array.isArray(trackPoints) ? trackPoints : [];
      blockchainProofs.value = Array.isArray(proofs) ? proofs : [];
      evidenceTrackPoints.value = evidenceTrackGeometryPoints.value.map((item) => ({
        ...item,
        pointType: formatTrackPointType(item.pointType),
      }));
      evidenceTimeline.value = buildEvidenceTimelineView(
        evidenceRecords.value,
        evidenceSnapshots.value,
        evidenceTrackGeometryPoints.value,
        blockchainProofs.value,
      );
      await nextTick();
      renderEvidenceMap();
    }

    async function backfillEvidenceProofs() {
      if (!selectedEvidenceActivityId.value) {
        showToast('\u8bf7\u5148\u9009\u62e9\u6d3b\u52a8\u3002', 'warning');
        return;
      }
      try {
        const createdCount = await request('POST', `/evidence/activities/${selectedEvidenceActivityId.value}/blockchain-proofs/backfill`);
        await loadEvidenceBundle();
        showToast(`\u5df2\u8865\u5f55 ${Number(createdCount) || 0} \u6761\u533a\u5757\u94fe\u5b58\u8bc1\u3002`);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function loadAll(showSuccess = false) {
      try {
        persistBase();
        await Promise.all([
          loadOverview(),
          loadAnnualOverview(),
          loadUsers(),
          loadApplications(),
          loadActivities(),
          loadServiceSites(),
          loadStoreItems(),
          loadRedemptionRecords(),
          loadSettings(),
          loadSupplements(),
          loadAnomalies(),
        ]);
        await Promise.all([loadSelectedRecords(), loadEvidenceBundle()]);
        if (showSuccess) showToast(TEXT.refreshSuccess);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function restore() {
      if (!state.token) {
        state.restoring = false;
        return;
      }
      try {
        persistBase();
        const profile = await request('GET', '/auth/me');
        if (!profile || profile.role !== 'SUPER_ADMIN') throw new Error(TEXT.sessionInvalid);
        state.profile = profile;
        await loadAll(false);
      } catch (error) {
        clearSession();
        showToast(error.message || TEXT.sessionInvalid, 'warning');
      } finally {
        state.restoring = false;
      }
    }

    async function doLogin() {
      try {
        persistBase();
        const data = await request(
          'POST',
          '/auth/super-admin-login',
          { username: login.username, password: login.password },
          login.baseUrl,
        );
        if (!data || !data.token || !data.profile) throw new Error(TEXT.loginResponseInvalid);
        state.token = data.token;
        state.profile = data.profile;
        localStorage.setItem(TOKEN_KEY, state.token);
        await loadAll(false);
        showToast(TEXT.loginSuccess);
      } catch (error) {
        clearSession();
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    function logout() {
      clearSession();
      activeTab.value = 'overview';
      showToast(TEXT.logoutSuccess);
    }

    async function setRole(userId, role) {
      try {
        await request('POST', `/admin/users/${userId}/role`, { role });
        await Promise.all([loadUsers(), loadApplications()]);
        showToast(TEXT.roleUpdated);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function review(id, approved) {
      try {
        await request('POST', `/admin/admin-applications/${id}/review`, {
          approved,
          reviewComment: reviews[id] || '',
        });
        await Promise.all([loadUsers(), loadApplications()]);
        showToast(approved ? TEXT.applicationApproved : TEXT.applicationRejected);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function evaluateRecord(record) {
      try {
        const formState = recordReviewForms[record.recordId];
        await request('POST', `/activities/${record.activityId}/records/${record.recordId}/evaluate`, {
          serviceRating: Number(formState.rating),
          serviceComment: formState.comment || '\u540e\u53f0\u5b8c\u6210\u670d\u52a1\u8bc4\u4ef7',
        });
        await Promise.all([loadSelectedRecords(), loadEvidenceBundle(), loadOverview(), loadAnomalies()]);
        showToast('\u670d\u52a1\u8bc4\u4ef7\u5df2\u63d0\u4ea4\u3002');
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function openCertificatePreview(record) {
      if (!canExportCertificate(record)) {
        showToast('完成确认与结算后可导出证明', 'warning');
        return;
      }
      try {
        const data = await request('GET', `/activities/records/${record.recordId}/certificate-proof`);
        certificatePreview.value = data || null;
        certificatePreviewVisible.value = !!data;
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    function closeCertificatePreview() {
      certificatePreviewVisible.value = false;
    }

    function printCertificatePreview() {
      if (!certificatePreview.value) {
        showToast('暂无可打印的证明内容', 'warning');
        return;
      }
      const printWindow = window.open('', '_blank', 'width=1080,height=760');
      if (!printWindow) {
        showToast('请允许打开打印窗口', 'warning');
        return;
      }
      printWindow.document.write(buildCertificatePrintHtml(certificatePreview.value, toFileUrl));
      printWindow.document.close();
      printWindow.focus();
      printWindow.onload = () => {
        printWindow.print();
      };
    }

    async function downloadCertificatePdf() {
      if (!certificatePreview.value) {
        showToast('暂无可导出的证明内容', 'warning');
        return;
      }

      const base = normalizeBase(login.baseUrl);
      const headers = {};
      if (state.token) {
        headers.Authorization = `Bearer ${state.token}`;
      }

      let response;
      try {
        response = await fetch(`${base}/activities/records/${certificatePreview.value.recordId}/certificate-proof.pdf`, {
          method: 'GET',
          headers,
        });
      } catch (error) {
        showToast(TEXT.backendOffline, 'error');
        return;
      }

      if (!response.ok) {
        showToast(TEXT.requestFailed, 'error');
        return;
      }

      const blob = await response.blob();
      const objectUrl = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = objectUrl;
      link.download = buildCertificateFileName(certificatePreview.value);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(objectUrl);
    }

    async function reviewSupplement(item, approved) {
      try {
        await request('POST', `/workflow/admin/supplements/${item.id}/review`, {
          approved,
          reviewComment: supplementReviewForms[item.id] || settings.supplementApproveComment || '',
        });
        await Promise.all([loadSupplements(), loadOverview()]);
        showToast(approved ? '\u8865\u7b7e\u5df2\u901a\u8fc7\u3002' : '\u8865\u7b7e\u5df2\u9a73\u56de\u3002');
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function openAnomalyEvidence(item) {
      selectedEvidenceActivityId.value = String(item.activityId || '');
      selectedEvidenceRecordId.value = String(item.recordId || '');
      activeTab.value = 'evidence-center';
      await nextTick();
      await loadEvidenceBundle();
      showToast('\u5df2\u5207\u6362\u5230\u5bf9\u5e94\u8bc1\u636e\u3002');
    }

    async function updateAnomaly(item, status) {
      try {
        await request('POST', `/evidence/anomalies/${item.recordId}/status`, {
          status,
          remark: anomalyRemarkForms[item.recordId] || '\u540e\u53f0\u5df2\u5b8c\u6210\u5f02\u5e38\u5904\u7f6e',
        });
        await Promise.all([loadAnomalies(), loadOverview(), loadSelectedRecords(), loadEvidenceBundle()]);
        showToast('\u5f02\u5e38\u8bb0\u5f55\u5df2\u5904\u7f6e\u3002');
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function createActivity() {
      try {
        await request('POST', '/activities', {
          serviceSiteId: form.serviceSiteId ? Number(form.serviceSiteId) : null,
          title: form.title,
          category: form.category,
          description: form.description,
          location: form.location,
          capacity: Number(form.capacity),
          demandLevel: Number(form.demandLevel),
          difficultyLevel: Number(form.difficultyLevel),
          difficultyCoefficient: Number(form.difficultyCoefficient),
          geofenceRadiusMeters: Number(form.geofenceRadiusMeters),
          latitude: Number(form.latitude),
          longitude: Number(form.longitude),
          startTime: form.startTime || null,
          endTime: form.endTime || null,
        });
        resetForm();
        await Promise.all([loadActivities(), loadOverview()]);
        showToast(TEXT.activityCreated);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function saveSite() {
      const payload = {
        name: siteForm.name,
        streetName: siteForm.streetName,
        communityName: siteForm.communityName,
        address: siteForm.address,
        latitude: Number(siteForm.latitude),
        longitude: Number(siteForm.longitude),
        recommendedRadiusMeters: Number(siteForm.recommendedRadiusMeters),
        enabled: !!siteForm.enabled,
      };

      try {
        if (siteForm.id) {
          await request('PUT', `/admin/service-sites/${siteForm.id}`, payload);
          showToast('\u670d\u52a1\u7ad9\u70b9\u5df2\u66f4\u65b0\u3002');
        } else {
          await request('POST', '/admin/service-sites', payload);
          showToast('\u670d\u52a1\u7ad9\u70b9\u5df2\u521b\u5efa\u3002');
        }
        resetSiteForm();
        await loadServiceSites();
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function toggleSite(item) {
      try {
        await request('POST', `/admin/service-sites/${item.id}/toggle`);
        await loadServiceSites();
        showToast(item.enabled ? '\u670d\u52a1\u7ad9\u70b9\u5df2\u505c\u7528\u3002' : '\u670d\u52a1\u7ad9\u70b9\u5df2\u542f\u7528\u3002');
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    function editStoreItem(item) {
      Object.assign(storeForm, {
        id: item.id || '',
        name: item.name || '',
        category: normalizeCategory(item.category, STORE_CATEGORIES),
        description: item.description || '',
        pointsCost: Number(item.pointsCost || 0),
        stock: Number(item.stock || 0),
        active: item.active !== false,
        deliveryType: item.deliveryType || 'VIRTUAL',
      });
      activeTab.value = 'store';
    }

    async function saveStoreItem() {
      const isEditing = !!storeForm.id;
      try {
        await request(
          'POST',
          isEditing ? `/store/admin/items/${storeForm.id}` : '/store/admin/items',
          {
            name: storeForm.name,
            category: storeForm.category,
            description: storeForm.description,
            pointsCost: Number(storeForm.pointsCost || 0),
            stock: Number(storeForm.stock || 0),
            active: !!storeForm.active,
            deliveryType: storeForm.deliveryType || 'VIRTUAL',
          },
        );
        resetStoreForm();
        await Promise.all([loadStoreItems(), loadOverview()]);
        showToast(isEditing ? '商品已更新。' : '商品已创建。');
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function deliverRedemption(item) {
      const remark = window.prompt('填写发放备注', item.deliveryRemark || '');
      if (remark === null) {
        return;
      }
      try {
        await request('POST', `/store/admin/redemptions/${item.id}/status`, {
          status: 'DELIVERED',
          deliveryRemark: remark,
        });
        await Promise.all([loadRedemptionRecords(), loadOverview()]);
        showToast('已标记为发放');
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function cancelRedemption(item) {
      try {
        await request('POST', `/store/admin/redemptions/${item.id}/status`, {
          status: 'CANCELLED',
          deliveryRemark: '超级管理员取消兑换',
        });
        await Promise.all([loadRedemptionRecords(), loadOverview()]);
        showToast('兑换记录已取消');
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function refreshCode(id) {
      try {
        await request('POST', `/activities/${id}/refresh-code`);
        await loadActivities();
        showToast(TEXT.codeRefreshed);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function saveSettings() {
      try {
        const data = await request('POST', '/admin/system-settings', {
          systemName: settings.systemName,
          defaultGeofenceRadiusMeters: Number(settings.defaultGeofenceRadiusMeters),
          minTrackPoints: Number(settings.minTrackPoints),
          minSnapshotCount: Number(settings.minSnapshotCount),
          minServiceHours: Number(settings.minServiceHours),
          frequentSupplementLimit30Days: Number(settings.frequentSupplementLimit30Days),
          certificateTitle: settings.certificateTitle,
          supplementApproveComment: settings.supplementApproveComment,
          adminWebUrl: settings.adminWebUrl,
        });
        Object.assign(settings, emptySettings(), data || {});
        showToast(TEXT.settingsSaved);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function initDemo() {
      try {
        await request('POST', '/admin/system-settings/demo-data/init');
        await Promise.all([loadOverview(), loadActivities()]);
        showToast(TEXT.demoInitialized);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function resetDemo() {
      try {
        await request('POST', '/admin/system-settings/demo-data/reset');
        await loadAll(false);
        showToast(TEXT.demoReset);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    }

    async function copyUrl() {
      const value = settings.adminWebUrl || window.location.href;
      try {
        if (navigator.clipboard && window.isSecureContext) {
          await navigator.clipboard.writeText(value);
          showToast(TEXT.copySuccess);
          return;
        }
      } catch (error) {
      }
      window.prompt(TEXT.copyFallback, value);
    }

    watch(selectedRecordActivityId, async () => {
      certificatePreviewVisible.value = false;
      if (!state.token || activeTab.value !== 'service-records') return;
      try {
        await loadSelectedRecords();
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    });

    watch(selectedEvidenceActivityId, async () => {
      if (!state.token || activeTab.value !== 'evidence-center') return;
      try {
        await loadEvidenceBundle();
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    });

    watch(selectedAnalyticsUserId, async () => {
      if (!state.token || activeTab.value !== 'analytics') return;
      try {
        await loadSelectedAnalyticsUser();
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    });

    watch(analyticsYear, async () => {
      if (!state.token || activeTab.value !== 'analytics') return;
      try {
        await Promise.all([loadAnnualOverview(), loadSelectedAnalyticsUser()]);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    });

    watch(insightPeriod, async () => {
      if (!state.token || (activeTab.value !== 'leaderboard' && activeTab.value !== 'community-insights')) return;
      try {
        await Promise.all([loadPlatformStarLeaderboard(), loadPlatformCommunityInsights()]);
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    });

    watch(() => form.serviceSiteId, (value) => {
      if (!value) return;
      const site = serviceSites.value.find((item) => String(item.id) === String(value));
      if (site) {
        applyServiceSiteToForm(site);
      }
    });

    watch(activeTab, async (tab) => {
      if (tab !== 'service-records') {
        certificatePreviewVisible.value = false;
      }
      if (!state.token) return;
      try {
        if (tab === 'service-records') {
          await loadSelectedRecords();
        } else if (tab === 'store') {
          await Promise.all([loadStoreItems(), loadRedemptionRecords()]);
        } else if (tab === 'my-center') {
          await loadIdentity();
        } else if (tab === 'notifications') {
          await loadNotifications();
        } else if (tab === 'analytics') {
          await loadAnalyticsDashboard();
        } else if (tab === 'leaderboard') {
          await loadPlatformStarLeaderboard();
        } else if (tab === 'community-insights') {
          await loadPlatformCommunityInsights();
        } else if (tab === 'service-sites') {
          await loadServiceSites();
        } else if (tab === 'supplement-review') {
          await loadSupplements();
        } else if (tab === 'evidence-center') {
          await loadEvidenceBundle();
        } else if (tab === 'anomalies') {
          await loadAnomalies();
        }
      } catch (error) {
        showToast(error.message || TEXT.requestFailed, 'error');
      }
    });

    onMounted(() => {
      restore();
    });

    return {
      menus: MENUS,
      activityCategories,
      storeCategories,
      state,
      toast,
      login,
        activeTab,
        overview,
        annualOverview,
        analyticsYear,
        insightPeriod,
        analyticsUsers,
        analyticsLeaderboard,
        analyticsHeatmap,
        platformStarLeaderboard,
        platformCommunityInsights,
        analyticsRoleFilter,
        analyticsUserKeyword,
        selectedAnalyticsUserId,
        analyticsUserDetail,
        settings,
      form,
      siteForm,
      storeForm,
      identity,
      identityVisibility,
      identityForm,
      publishSearchKeyword,
      publishSearchResults,
      publishSearchLoading,
      publishSearchAttempted,
      siteSearchKeyword,
      siteSearchResults,
      siteSearchLoading,
      siteSearchAttempted,
      users,
      applications,
      notifications,
      activities,
      serviceSites,
      storeItems,
      redemptionRecords,
      reviews,
      selectedRecordActivityId,
      selectedEvidenceActivityId,
      serviceRecords,
      certificatePreview,
      certificatePreviewVisible,
      supplements,
      anomalies,
      evidenceRecords,
      evidenceSnapshots,
      evidenceTrackPoints,
      evidenceTrackGeometryPoints,
      evidenceTimeline,
      blockchainProofs,
      supplementReviewForms,
      anomalyRemarkForms,
      recordReviewForms,
      currentMenu,
      profileName,
      profileAvatarUrl,
      profileInitial,
      activeServiceSites,
      filteredAnalyticsUsers,
      evidenceTrackSummary,
      highlightedEvidenceRecordId,
      stats,
      todos,
      roleLabel,
      roleClass,
      maskIdCardNo,
      maskRealName,
      maskVolunteerCardNo,
      applicationLabel,
      applicationClass,
      activityLabel,
      formatParticipationStatus,
      formatAnomalyStatus,
      formatTrackPointType,
      formatBlockchainEventType,
      formatBlockchainProofStatus,
      formatProofDate,
      formatNotificationType,
      formatNotificationStatus,
      buildCheckInQrImageUrl,
      formatTime,
      canExportCertificate,
      loadAll,
      loadAnalyticsDashboard,
      loadIdentity,
      loadNotifications,
      backfillEvidenceProofs,
      doLogin,
      logout,
      saveIdentity,
      setRole,
      review,
      evaluateRecord,
      openCertificatePreview,
      closeCertificatePreview,
      printCertificatePreview,
      downloadCertificatePdf,
      reviewSupplement,
      updateAnomaly,
      openAnomalyEvidence,
      resetForm,
      resetSiteForm,
      resetStoreForm,
      editSite,
      editStoreItem,
      searchPublishLocation,
      searchSiteLocation,
      choosePublishLocation,
      chooseSiteLocation,
      saveSite,
      saveStoreItem,
      deliverRedemption,
      cancelRedemption,
      toggleSite,
      createActivity,
      refreshCode,
      saveSettings,
      initDemo,
      resetDemo,
      copyUrl,
      loadSelectedRecords,
      loadSupplements,
      loadAnomalies,
      loadEvidenceBundle,
      toFileUrl,
    };
  },
}).mount('#app');




