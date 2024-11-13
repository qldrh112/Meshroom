<script setup>
import { computed } from 'vue'
import { useBallStore } from '@/stores/ballStore'
import BallContent from './BallContent.vue'

const ballStore = useBallStore()
const totalBalls = computed(() => ballStore.getTotalBalls)

const rows = computed(() => {
  return totalBalls.value.reduce((acc, item, index) => {
    if (index % 2 === 0) acc.push([])
    acc[acc.length - 1].push({ ...item, rank: index + 1 })
    return acc
  }, [])
})

// 공 변경 하기
const onChangeClick = (sessionId) => {
  ballStore.onChangeClick(sessionId)
}
</script>

<template>
  <div class="ball-group">
    <transition-group name="slide" tag="div">
    <div v-for="(row, index) in rows" :key="index" class="row">
      <div v-for="ball in row" :key="ball.sessionId" class="ball-item">
        <BallContent
          :groupId="ball.sessionId"
          :rank="ball.rank" 
          :isMain="false"
          @click="onChangeClick(ball.sessionId)"
        />
      </div>
    </div>
  </transition-group>
  </div>
</template>

<style scoped>
.ball-group {
  max-height: 700px;
  overflow-y: auto; /* 내용이 넘칠 때 스크롤바가 나타나도록 설정 */
  margin-right: 100px;
  margin-top: 30px;
}

.row {
  display: flex;
  margin-bottom: 20px;
}

.ball-item {
  margin-right: 30px;
}

.row:last-child .ball-item:last-child {
  margin-right: 0;
}

/* 슬라이딩 효과 */
.slide-enter-active, .slide-leave-active {
  transition: all 1s ease;
}

.slide-enter-from, .slide-leave-to {
  transform: translateX(20px);
  opacity: 0;
}
</style>
