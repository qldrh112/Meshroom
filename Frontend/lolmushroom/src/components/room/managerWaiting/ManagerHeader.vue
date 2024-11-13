<script setup>
import { useRoomStore } from '@/stores/roomStore'
import { computed } from 'vue'
const roomStore = useRoomStore()
const currentCount = computed(() => roomStore.getTotalUserCount)
</script>

<template>
  <header class="room-waiting-header">
    <div class="room-waiting-title">
      <span>😀</span>
      우리들의 오리엔테이션 세션
    </div>
    <div class="room-waiting-count-info">
      현재 인원
      <span>{{ currentCount-1}} / 60</span>
    </div>
  </header>
</template>

<style scoped>
.room-waiting-header {
  min-width: 500px;
  width: 60%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-content: center;
  background-color: #b5ff9b;
  border-radius: 10px;
  margin-bottom: 30px;
}

.room-waiting-title {
  flex-basis: 60%;
  min-width: 200px; /* Set the minimum width to maintain layout on smaller screens */
  max-width: 60%; /* Ensures that it doesn't grow beyond 60% */
  min-height: 60px;
  background-color: #9ee682;
  border-radius: 10px;
  align-content: center;
  padding: 10px;
  font-size: 22px;
  font-weight: bold;
}

.room-waiting-count-info {
  flex-direction: row;
  justify-content: center;
  align-content: center;
  border-radius: 10px;
  width: 40%;
  font-size: 3vm;
}
</style>
