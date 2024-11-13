<script setup>
import { ref } from 'vue'

const mic = ref(true)
const video = ref(true)
</script>

<template>
  <v-icon v-show="mic" icon="mdi-microphone" size="x-large"></v-icon>
  <v-icon v-show="!mic" icon="mdi-microphoneOff" size="x-large"></v-icon>
  <v-icon v-show="video" icon="mdi-video" size="x-large"></v-icon>
  <v-icon v-show="!video" icon="mdi-videoOff" size="x-large"></v-icon>
</template>

<style scoped>
</style>
