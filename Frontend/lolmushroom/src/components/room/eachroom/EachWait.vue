<script setup>
import { computed } from 'vue'

const props = defineProps({
  room: {
    type: Object,
    required: true
  }
})

// room.isReady 값을 기반으로 클래스 및 텍스트를 동적으로 설정
const statusClass = computed(() => {
  return props.room.isReady ? 'ready' : 'preparing'
})

const statusText = computed(() => {
  return props.room.isReady ? '준비 완료' : '준비 중'
})
</script>

<template>
  <div class="group-members">
    <div class="user-item" v-for="(user, i) in room.users.slice(0, 6)" :key="i">
      {{ user }}
    </div>
  </div>
  <div class="group-status" :class="statusClass">
    <div class="text">
      {{ statusText }}
    </div>
  </div>
</template>

<style scoped>
.group-status {
  color: white;
  height: 40px;
  font-size: 18px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
  transition: background-color 0.3s ease; /* 배경색 전환 시 애니메이션 추가 */
}

/* 준비 완료 상태 */
.group-status.ready {
  background-color: #226e14; /* 준비 완료일 때 배경색 */
  padding: 10px;
}

/* 준비 중 상태 */
.group-status.preparing {
  background-color: #00d200; /* 준비 중일 때 배경색 */
  border-radius: 10%;
}

.group-members {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 5px;
  margin-top: 10px;
  flex-grow: 1;
}

.user-item {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  background-color: #f0f0f0;
  padding: 5px;
  border-radius: 5px;
}
</style>
