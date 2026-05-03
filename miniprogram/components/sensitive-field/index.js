Component({
  properties: {
    label: {
      type: String,
      value: ''
    },
    value: {
      type: String,
      value: ''
    },
    maskedValue: {
      type: String,
      value: ''
    },
    visible: {
      type: Boolean,
      value: false
    },
    emptyText: {
      type: String,
      value: '未填写'
    }
  },

  methods: {
    onToggle() {
      this.triggerEvent('toggle')
    }
  }
})
