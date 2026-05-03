Component({
  properties: {
    visible: {
      type: Boolean,
      value: false
    },
    form: {
      type: Object,
      value: null
    }
  },

  data: {
    currentTab: 'recipientName',
    tabList: [
      { value: 'recipientName', label: '收货人', placeholder: '请填写收货人姓名' },
      { value: 'recipientPhone', label: '手机号', placeholder: '请填写收货手机号' },
      { value: 'recipientAddress', label: '收货地址', placeholder: '请填写详细收货地址' }
    ]
  },

  observers: {
    visible(nextVisible) {
      if (nextVisible) {
        this.setData({
          currentTab: 'recipientName'
        })
      }
    }
  },

  methods: {
    onVisibleChange() {
      this.triggerEvent('cancel')
    },

    onTabChange(event) {
      const { value } = event.currentTarget.dataset
      if (!value) {
        return
      }
      this.setData({
        currentTab: value
      })
    },

    onInput(event) {
      const field = event.currentTarget.dataset.field
      const value = event.detail.value
      this.triggerEvent('change', {
        field,
        value
      })
    },

    onCancel() {
      this.triggerEvent('cancel')
    },

    onConfirm() {
      this.triggerEvent('confirm')
    }
  }
})
