<script setup>
import { computed } from 'vue'
import { useBallStore } from '@/stores/ballStore'

const props = defineProps({
  room: {
    type: Object,
    required: true
  }
})

// ballStore에서 데이터를 가져옴
const ballStore = useBallStore()

// sessionId, GroupName, score가 들어있는 Array => rank : ArrayIndex + 1
const totalBalls = computed(() => ballStore.getTotalBalls)
const userRankAndScore = computed(() => {
  // room.sessionId와 같은 sessionId를 찾음
  const userIndex = totalBalls.value.findIndex((ball) => ball.sessionId === props.room.sessionId)

  if (userIndex !== -1) {
    return {
      rank: userIndex + 1, // 등수는 index + 1
      score: totalBalls.value[userIndex].size // 점수는 해당 index의 score
    }
  } else {
    return {
      rank: 'N/A',
      score: 'N/A'
    }
  }
})
</script>

<template>
  <div class="room-info">
    <div class="rank">등수: {{ userRankAndScore.rank }}</div>
    <div class="score">점수: {{ userRankAndScore.score }}</div>
  </div>
</template>

<style scoped>
.room-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  color: #000;
}

.rank,
.score {
  margin: 5px 0;
}
</style>
