const { request } = require('./request')

const TEMPLATE_ID_MAP = {
  ENROLLMENT_APPROVED: 'BFvieOX5I-hnmuxwbwAIBZlxaQu2gLMob6s1k46eS7M',
  ENROLLMENT_REJECTED: 'BFvieOX5I-hnmuxwbwAIBZlxaQu2gLMob6s1k46eS7M',
  ACTIVITY_REMINDER: 'XOf6S0B6sW5qYGIJsOSWV9oYgGi2akf5W8USDykcEgA',
  SUPPLEMENT_SUBMITTED: '4DBb2cqVGZQj2VwK0BBRIfV1c4WKHwqrzAVq-j1uPaE',
  SUPPLEMENT_APPROVED: 'dmA4JlVDTCaETTAqwz3pfFRYFOeNv2qPiAakUN3QSiU',
  SUPPLEMENT_REJECTED: '2OCAhg0IyAIs7gUbRunl4SARA6ZgWLvRB6WLiRnRE3w',
  TASK_COMPLETED: 'NoZQu5BlSSXB4qllfeO1wYiCIl76OqWiYfsPtE3goEI',
  SERVICE_RATING_RECEIVED: '0YOpNrUjKdL5n_eezhZw9X6izc7e_Ya-_jsOz19yDM8',
  REWARD_FINALIZED: '4FVJWo66-PwJu_PqVneDySEEdBc53reSKQ7dnp1Utxs',
  REDEMPTION_CREATED: 'k95LkKmKEY-wGIk2xKchqbyYQ1XwmpTqxhkO91UXh1Q',
  REDEMPTION_DELIVERED: 'k95LkKmKEY-wGIk2xKchqbyYQ1XwmpTqxhkO91UXh1Q',
  ENROLLMENT_PENDING_REVIEW: 'TdZc1aPXbA-QRIARa8Oqmfg396EwuW5ICbkZNGHakx0',
  SUPPLEMENT_PENDING_REVIEW: 'I-FjIerQLbyOO4L8050pbE_IWk9nqGWTimsyInBV2AQ',
  SERVICE_CONFIRM_PENDING: 'RzYHTf5luVonN3VtJqNY_HiaRjKIEpueoYH39wxwFH0'
}

function mapSubscribeStatus(value) {
  if (value === 'accept') {
    return 'ACCEPT'
  }
  if (value === 'ban') {
    return 'BAN'
  }
  if (value === 'reject') {
    return 'REJECT'
  }
  return ''
}

function uniqueTemplateIds(notificationTypes = []) {
  return Array.from(new Set(
    notificationTypes
      .map((type) => TEMPLATE_ID_MAP[type])
      .filter(Boolean)
  ))
}

function buildSubscriptionItems(notificationTypes = [], result = {}) {
  return notificationTypes
    .map((notificationType) => {
      const templateCode = TEMPLATE_ID_MAP[notificationType]
      const acceptStatus = mapSubscribeStatus(result[templateCode])
      if (!templateCode || !acceptStatus) {
        return null
      }
      return {
        notificationType,
        templateCode,
        acceptStatus
      }
    })
    .filter(Boolean)
}

function saveSubscriptionItems(items = []) {
  if (!items.length) {
    return Promise.resolve()
  }
  return request({
    url: '/users/notification-subscriptions',
    method: 'POST',
    data: {
      items
    }
  }).catch(() => null)
}

function requestNotificationSubscription(notificationTypes = []) {
  const tmplIds = uniqueTemplateIds(notificationTypes)
  if (!tmplIds.length) {
    return Promise.resolve({
      requested: false
    })
  }

  return new Promise((resolve) => {
    wx.requestSubscribeMessage({
      tmplIds,
      complete: (result) => {
        const items = buildSubscriptionItems(notificationTypes, result || {})
        saveSubscriptionItems(items).finally(() => {
          resolve({
            requested: true,
            items
          })
        })
      }
    })
  })
}

module.exports = {
  requestNotificationSubscription,
  TEMPLATE_ID_MAP
}
