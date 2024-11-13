<script setup>
import { computed } from 'vue'
import { useRoomStore } from '@/stores/roomStore'
import WaitingHeader from './WaitingHeader.vue'
import PlayerWaitng from './PlayerWaitng.vue'

const roomStore = useRoomStore()
const text1 = '우리들의 오리엔테이션 세션'
const text2 = computed(() => `현재 인원 ${roomStore.getTotalUserCount}/60`)
const text3 =
  '원하는 팀을 구성하고 컨텐츠를 함께 하세요 ! <br /> 먼저 들어간 사람이 그룹장이 됩니다!'
</script>

<template>
  <div class="background">
    <!-- 대기화면 소개 글 추가-->
    <WaitingHeader
      :first-description="text1"
      :second-description="text2"
      :third-description="text3"
    />
    <PlayerWaitng />
  </div>
</template>

<style scoped>
.background {
  background-color: #e7ffde;
  height: 100%;
  width: 100%;
}

.StartButton {
  margin-top: 40px;
  margin-left: 20px;
}
</style>
