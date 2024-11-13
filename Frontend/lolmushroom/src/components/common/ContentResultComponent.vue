<script setup>
  import { ref } from 'vue'
  import ButtonComponent from '@/components/common/ButtonComponent.vue';

  const overlay = ref()
</script>

<template>
    <v-overlay
      v-model="overlay"
      class="justify-center align-center"
      opacity="0.7"
      contained
    >
    <!-- ContentReusltComponent 호출할 때 사이에 넣고 싶은 요소 껴주면 된다. -->
      <slot />
    </v-overlay>

  <!-- 색상은 임시로 설정함-->
  <ButtonComponent
    text="결과 확인하기"
    color="white"
    @click="overlay= !overlay"
  />
</template>

<style scoped>

</style>