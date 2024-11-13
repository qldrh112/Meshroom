<script setup>
import { useTOFStore } from '@/stores/tofStore'
import { computed } from 'vue'

const props = defineProps({
  room: {
    type: Object,
    required: true
  }
})
const tofStore = useTOFStore()
/**
 * TODO : Watch로 처리해야 정확한 반응성이 보장될려나?
 */
const currentInfo = computed(() => tofStore.getCurrentInfoBySubSessionId(props.room.sessionId))
const stepLabel = computed(() => (currentInfo.value.groupStep === 1 ? '진술서 제출중' : '발표 중'))
const progress = computed(() => `${currentInfo.value.groupStepInfo}/${currentInfo.value.groupSize}`)
</script>

<template>
  <div class="status-container">
    <span class="status-label">{{ stepLabel }}</span>
    <span class="status-progress">- {{ progress }}</span>
  </div>
</template>

<style scoped>
.status-container {
  font-size: 18px;
  font-weight: bold;
  color: #000;
}

.status-label {
  margin-right: 10px;
}

.status-progress {
  color: #666;
}
</style>
