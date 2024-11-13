<script setup>
import { computed } from 'vue'
import { useBallStore } from '@/stores/ballStore'
import BlueBallComponent from '@/components/contents/BallGrow/BlueBallComponent.vue'
import GreenBallComponent from '@/components/contents/BallGrow/GreenBallComponent.vue'

const ballStore = useBallStore()
const props = defineProps({
  isMain: Boolean,
  groupId: String,
  rank: Number
})

const isMine = computed(() => ballStore.getIsMyBall(props.groupId))
const groupName = computed(() => ballStore.getCurrentGroupName(props.groupId))
const currentSize = computed(() => ballStore.getBallSize(props.groupId))
const containerClass = computed(() => (props.isMain ? 'main-container' : 'sub-container'))
</script>

<template>
  <div :class="containerClass">
    <div class="ball-name"> {{ groupName }} 그룹</div>
    <div class="ball-rank" v-if="!isMain">{{ props.rank }}등</div>
    <div class="ball-container">
      <GreenBallComponent v-if="isMine" :health="currentSize" :isMain="isMain" />
      <BlueBallComponent v-else :health="currentSize" :isMain="isMain" />
    </div>
  </div>
</template>

<style scoped>
.main-container,
.sub-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff2f7;
  border: 1px solid black;
  border-radius: 20px;
  flex-direction: column; /* 컨테이너 내부 요소들을 수직으로 정렬 */
}

.main-container {
  width: 600px;
  height: 400px;
}

.sub-container {
  width: 300px;
  height: 200px;
}

.ball-name {
  position : absolute;
  top : 10px;
  left : 10px;
  padding: 5px;
  font-size: x-large;
}

.ball-rank {
  position: absolute;
  top:10px;
  right: 10px;
  padding: 5px;
  font-size: larger;
  background-color: rgb(213, 79, 79);
  border-radius: 50%;
}

.ball-container {
  display: flex;
  align-items: center;
  justify-content: center;
}


.main-container .ball-name {
  font-size: x-large; /* Main container의 경우 큰 글씨 */
}

.sub-container .ball-name {
  font-size: small; /* Sub container의 경우 작은 글씨 */
}
</style>
