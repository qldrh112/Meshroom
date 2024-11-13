<script setup>
import { computed } from 'vue'
import { useContentsStore } from '@/stores/contentsStore'
import { useRoomStore } from '@/stores/roomStore'
import EachWait from './EachWait.vue'
import EachTOF from './EachTOF.vue'
import EachAlpha from './EachAlpha.vue'
import EachBall from './EachBall.vue'

const props = defineProps({
  currentContents: {
    type: String
  }
})

// Store access
const roomStore = useRoomStore()
const contentsStore = useContentsStore()
const rooms = computed(() => {
  return roomStore.getRooms.map((room, index) => {
    const groupState = contentsStore.currentGroupState[index]
    return {
      ...room,
      gameEnd: groupState && groupState.isFinish
    }
  })
})
</script>

<template>
  <div class="group-list">
    <div
      v-for="(room, index) in rooms"
      :key="index"
      :class="['group-card', { inactive: !room.isActive }]"
    >
      <div v-if="room.isActive">
        <div class="group-header">
          <div class="group-name">{{ room.groupName || `그룹${index + 1}` }}</div>
          <div class="group-count">{{ room.occupants }} / {{ room.capacity }}</div>
        </div>
        <EachWait v-if="!currentContents" :room="room" />
        <div v-if="room.gameEnd">게임 종료</div>
        <div v-else>
          <EachTOF v-if="currentContents === '1'" :room="room" />
          <EachAlpha v-if="currentContents === '4'" :room="room" />
          <EachBall v-if="currentContents === '7'" :room="room" />
        </div>
      </div>
      <div v-else class="inactive-content">
        <div class="inactive-icon">✖</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.group-list {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  margin-top: 20px;
  gap: 30px;
}

.group-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border: 1px solid #00ff00;
  border-radius: 15px;
  width: 230px;
  height: 200px;
  padding: 0px 10px 0px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.group-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.group-card.inactive {
  background-color: #f8f8f8;
  border: 1px dashed #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.inactive-content {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  font-size: 70px;
  color: #ff6363;
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #e0e0e0;
}

.group-name {
  font-size: 20px;
  font-weight: bold;
}

.group-count {
  font-size: 16px;
}
</style>
