<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useBallStore } from '@/stores/ballStore'
import { useSessionStore } from '@/stores/sessionStore'
import { useRoomStore } from '@/stores/roomStore'
import BallContent from './BallContent.vue'
import spark from '@/assets/image/spark.png'
import swooshSound from '@/assets/swoosh.mp3'
import hitSound from '@/assets/hit.mp3'

const props = defineProps({
  timeOut: Number
})
const ballStore = useBallStore()
const sessionStore = useSessionStore()
const currentGroup = computed(() => ballStore.getCurrentGroup)
const isMine = computed(() => ballStore.getIsMyBall(currentGroup.value))

/**
 * TODO Time에 대한 Value를 관리해야 한다.
 */
const RemainTime = ref(props.timeOut)

/**
 * IMP Ball에 대한 Effect 관리를 해야 한다.
 */
const clickEffects = ref([])
const addClickEffect = (event) => {
  const effect = {
    id: Date.now(),
    x: event.clientX,
    y: event.clientY
  }
  clickEffects.value.push(effect)
  setTimeout(() => {
    clickEffects.value = clickEffects.value.filter((e) => e.id !== effect.id)
  }, 500)
}

// ballClick을 할 시 ballStore로 데이터를 전송한다.
const onBallClick = (event) => {
  addClickEffect(event)
  ballStore.onBallClick(sessionStore.sessionId, currentGroup.value)
}

// 다시 자신의 공으로 돌아온다.
const onReturnClick = () => {
  ballStore.onReturnClick()
}

// 키보드 입력시에도 효과를 준다.
const handleKeyup = (event) => {
  if (event.code === 'Space') {
    console.log(isMine)
    if (isMine.value == true) {
      const audio = new Audio(hitSound)
      audio.play()
    } else {
      const audio = new Audio(swooshSound)
      audio.play()
    }
    onBallClick(event)
  }
}

onMounted(() => {
  let intervalId = setInterval(() => {
    if (RemainTime.value > 0) {
      RemainTime.value -= 1
    } else {
      clearInterval(intervalId)
    }
  }, 1000)
  window.addEventListener('keyup', handleKeyup)
})

onBeforeUnmount(() => {
  window.removeEventListener('keyup', handleKeyup)
})
</script>

<template>
  <div class="center-wrapper">
    <div class="remainTime">남은 시간 : {{ RemainTime }} 초</div>
    <div class="main-container">
      <BallContent :isMain="true" :groupId="currentGroup" @click="onBallClick" />
      <v-btn v-if="!isMine" class="backToMyGroup" @click="onReturnClick">내 그룹으로 돌아가기</v-btn>
    </div>
    <div
      v-for="effect in clickEffects"
      :key="effect.id"
      class="click-effect"
      :style="{ left: `${effect.x - 50}px`, top: `${effect.y - 50}px` }"
    >
      <v-img :src="spark" @click="onBallClick" />
    </div>
  </div>
</template>

<style>
/* 전역 스타일로 이동 */
.click-effect {
  position: fixed;
  width: 100px;
  height: 100px;
  background-color: rgba(224, 107, 34, 0.5);
  border-radius: 50%;
  pointer-events: none;
  transform: scale(0);
  animation: click-animation 0.5s forwards;
  z-index: 100000;
}

@keyframes click-animation {
  to {
    transform: scale(1);
    opacity: 0;
  }
}
</style>

<style scoped>
.center-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  box-sizing: border-box;
  padding: 20px;
}

.remainTime {
  background-color: #C5DDD6;
  width: 300px;
  height: 60px;
  margin-bottom: 10%;
  border-radius: 20px;
  border: 1px black solid;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px; /* 글씨 크기 조정 */
}

.main-container {
  position: relative; /* 버튼 위치 기준 */
  width: 600px;
  height: 400px;
  background-color: #fff2f7;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 20px;
  border: 1px solid black;
}

.backToMyGroup {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background-color: #24a319;
  color: white;
  width: 200px;
  height: 50px;
  border-radius: 10px;
}

/* 이미지의 클릭 이벤트만 허용하고 부모 div는 무시하게 설정 */
.v-img {
  pointer-events: auto;
}
</style>
