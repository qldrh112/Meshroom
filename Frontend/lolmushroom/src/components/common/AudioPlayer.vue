<script setup>
import { ref } from 'vue'
import MeshroomBgm from '@/assets/MeshroomBgm.mp3'

const audio = ref(null)
const isPlaying = ref(false)
const bgmIcon = ref('custom:bgmOn')

// 초기 볼륨 설정 (0.5는 50% 볼륨)
const volume = ref(0.2)

const toggleSound = () => {
  if (audio.value) {
    audio.value.volume = volume.value // 볼륨 설정
    if (isPlaying.value) {
      bgmIcon.value = 'custom:bgmOff'
      audio.value.pause()
    } else {
      audio.value.play()
      bgmIcon.value = 'custom:bgmOn'
    }
    isPlaying.value = !isPlaying.value
  }
}
</script>

<template>
  <div>
    <audio :src="MeshroomBgm" preload="auto" loop ref="audio" id="audio"></audio>
    <v-icon :icon="bgmIcon" class="icon-size" @click="toggleSound"> </v-icon>
  </div>
</template>

<style scoped>
.icon-size {
  width: 34px; /* Adjust icon size */
  height: 34px; /* Adjust icon size */
}
</style>
