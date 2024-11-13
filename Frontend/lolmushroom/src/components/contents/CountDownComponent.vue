<script setup>
const emit = defineEmits(['endCountDown'])

const props = defineProps({
  time: {
    type: String,
    required: true
  },
  text: {
    type: String,
    required: false
  }
})

const endCountDown = () => {
  emit('endCountDown', true)
}
</script>

<template>
  <vue-countdown :time="`${props.time}` * 1000" v-slot="{ seconds }" @end="endCountDown()">
    <h1 class="countdown-number">
      {{ seconds }}
    </h1>
  </vue-countdown>
  <br />
  <br />
  <div class="countdown-tooltip">
    <h2 style="color: white">{{ props.text }}</h2>
  </div>
</template>

<style scoped>
.countdown-number {
  font-size: 120px;
  animation: countdown-animation 1s linear infinite;
}

@keyframes countdown-animation {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.countdown-tooltip h1 {
  font-size: 40px;
  margin: 10px 0;
  color: '#FFFFFF';
}
</style>
