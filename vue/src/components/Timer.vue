<template>
  <span>
    <slot>
      {{ content }}
    </slot>
  </span>
</template>
<script>
export default {
  name: 'Timer',
  data() {
    return {
      timer: "",
      content: "00:00",
      minutes: 0,
      seconds: 0,
    }
  },
  props: {
    flag: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    flag: {
      immediate: true,
      handler(val) {
        if (val) {
          this.timer = setInterval(this.startTimer, 1000);
        } else clearInterval(this.timer);
      }
    }
  },
  destroyed() { clearInterval(this.timer); },

  methods: {
    startTimer() {
      this.seconds += 1;
      if (this.seconds >= 60) {
        this.seconds = 0;
        this.minutes = this.minutes + 1;
      }
      //   if (this.minute >= 60) {
      //     this.minute = 0;
      //     this.hour = this.hour + 1;
      //   }
      this.content = (this.minutes < 10 ? '0' + this.minutes : this.minutes) + ':' + (this.seconds < 10 ? '0' + this.seconds : this.seconds);
      this.$emit('jishi', this.content)
    }
  },
}
</script>
