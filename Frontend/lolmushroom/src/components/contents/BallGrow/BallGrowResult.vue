<!-- eslint-disable no-undef -->
<script setup>
import { computed, onMounted } from 'vue'
import { useBallStore } from '@/stores/ballStore'
import BallContent from './BallContent.vue'

const ballStore = useBallStore()
const allBalls = computed(() => ballStore.getTotalBalls)
const winnerGroup = computed(() => {
  console.log(allBalls.value)
  return allBalls.value[0]
})

function firework() {
  var duration = 100 * 100
  var animationEnd = Date.now() + duration
  var defaults = { startVelocity: 25, spread: 360, ticks: 50, zIndex: 0 }

  function randomInRange(min, max) {
    return Math.random() * (max - min) + min
  }

  var interval = setInterval(function () {
    var timeLeft = animationEnd - Date.now()

    if (timeLeft <= 0) {
      return clearInterval(interval)
    }

    var particleCount = 50 * (timeLeft / duration)

    confetti(
      Object.assign({}, defaults, {
        particleCount,
        origin: { x: randomInRange(0.1, 0.3), y: Math.random() - 0.2 }
      })
    )
    confetti(
      Object.assign({}, defaults, {
        particleCount,
        origin: { x: randomInRange(0.7, 0.9), y: Math.random() - 0.2 }
      })
    )
  }, 100)
}

onMounted(() => {
  const script = document.createElement('script')
  script.src = 'https://cdn.jsdelivr.net/npm/canvas-confetti@1.5.1/dist/confetti.browser.min.js'
  script.onload = () => {
    firework()
  }
  document.body.appendChild(script)
})
</script>

<template>
  <div class="results-container">
    <div class="result-header"><h1>우승 그룹!</h1></div>
    <BallContent :isMain="true" :groupId="winnerGroup.sessionId" />
  </div>
</template>

<style scoped>
.results-container {
  background-color: #e7ffde;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  box-sizing: border-box;
  padding: 20px;
}

.result-header{
  margin-bottom: 2%;
}
</style>
