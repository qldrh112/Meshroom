<script setup>
  import { onMounted } from 'vue';

  // props 선언
  const props = defineProps({
    streamManager: Object,
  });

  onMounted(() => {
    props.streamManager.addVideoElement(document.querySelector('video'));
  });
</script>

<template>
  <video autoplay />
</template>

<style scoped>
</style>