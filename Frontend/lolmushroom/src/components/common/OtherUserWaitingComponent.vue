<script setup>
  const props = defineProps({
    total: {
      type: Number,
      required: true
    },
    current: {
      type: Number,
      default: 0,
    },
  })
</script>


<template>
  <!-- 컨텐츠 진행 중 다른 사용자를 기다리는 컴포넌트 -->
  <img src="../../assets/joker.gif" alt="joker" class="waiting-icon">
  <h2 style="color: black">다른 참여자가 제출을 끝낼 때까지 잠시만 기다려 주세요!</h2>
  <v-progress-linear
    bg-color="#FFFFFF"
    color="#24A319"
    height="30"
    :max="props.total"
    min="0"
    :model-value="props.current"
    rounded
    style="border: #000000 2px solid"
    >
      <strong style="color: #000000;">{{ props.current }} / {{ props.total }}</strong>
  </v-progress-linear>
</template>


<style scoped>
  .waiting-icon {
    text-align: center
  }
</style>