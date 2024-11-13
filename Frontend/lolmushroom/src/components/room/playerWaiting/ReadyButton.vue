<!-- ReadyButton.vue -->
<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  isReady: Boolean,
  isGroupTeamLeader: Boolean
})
const emit = defineEmits(['onClick'])

const handleClick = () => {
  if (!props.isGroupTeamLeader) {
    console.log('해당 Group장님이 아니면, 준비 완료를 누를 수 없습니다:)')
  } else {
    emit('onClick')
  }
}
</script>

<template>
  <div class="ready-btn-div">
    <v-btn
      :style="{
        backgroundColor: isReady ? '#000000' : '#D9D9D9',
        color: isReady ? '#FFFFFF' : '#000000'
      }"
      @click="handleClick"
      class="ready-btn"
    >
      <div>
        {{ isReady ? '준비완료' : '준비' }}
        <p v-if="!isGroupTeamLeader" class="warning">해당 팀장만 준비완료가 가능합니다!</p>
      </div>
    </v-btn>
  </div>
</template>

<style scoped>
.ready-btn-div {
  width: 100%;
}

.ready-btn {
  /* position: relative; */
  width: 100%;
}

/* 말풍선 팀장만 준비 가능*/
.warning {
  display: none;
  position: absolute;
  width: 300px;
  padding: 0px;
  left: 50%;
  transform: translateX(-50%);
  margin-bottom: 5px;
  border-radius: 8px;
  background: #333;
  color: #fff;
  font-size: 14px;
  z-index: 10;
}

.ready-btn:hover .warning {
  display: block;
}
</style>
