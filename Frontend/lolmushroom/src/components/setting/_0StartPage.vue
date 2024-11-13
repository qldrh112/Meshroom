<script setup>
import { useRouter } from 'vue-router'
import TitleComponent from './_0TitleComponent.vue'
import SessionStartCard from './_0SessionStartCard.vue'

const router = useRouter()
const onNextClick = () => {
  router.push('curation')
}
</script>

<template>
  <div class="start-page">
    <!-- Pink Top Half -->
    <div class="pink-section">
      <div class="title-container">
        <TitleComponent />
      </div>
    </div>
    <div class="card-container" @click="onNextClick">
      <SessionStartCard />
    </div>
    <!-- White Bottom Half -->
    <div class="white-section"></div>
  </div>
</template>

<style scoped>
.start-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.pink-section {
  background-color: #e7ffde;
  height: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.title-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
}

.white-section {
  height: 50%;
}

.card-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  cursor: pointer; /* Add cursor pointer to indicate the card is clickable */
}
</style>
