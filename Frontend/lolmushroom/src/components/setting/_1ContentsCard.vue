<script setup>
import { computed } from 'vue'
import { useContentsStore } from '@/stores/contentsStore'

const contentsStore = useContentsStore()
const content = computed(() => contentsStore.getPickedContents)
</script>

<template>
  <div v-if="content" class="contents-card-shadow contents-card">
    <!-- 이미지 섹션 -->
    <div class="image-section">
      <v-img :src="content.imgUrl"  max-width="100%" max-height="100%" style="border: 2px solid black; border-radius: 10%;"></v-img>
    </div>

    <!-- 제목 섹션 -->
    <div class="title-section">
      <h1>{{ content.contentTitle }}</h1>
    </div>
    
    <!-- Green Section (설명 섹션) -->
    <div class="green-section" style="white-space: pre-line;">
        {{ content.description }}
    </div>
  </div>
  <div v-else>
    <p>No content selected.</p>
  </div>
</template>

<style scoped>

.contents-card {
  display: flex;
  flex-direction: column;
  height: 100%;
  margin-left: 10px;
  justify-content: center;
  align-items: center;
}

.contents-card-shadow {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.image-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 300px;
  height: 220px;
  margin-top: 2%;
  margin-bottom: 2%;
}

.title-section {
  flex: 0.1;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 2.5%;
}

.green-section {
  flex: 1;
  background-color: #e7ffde;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 15%;
  font-size: medium;
  font-weight: bold;
}

</style>
