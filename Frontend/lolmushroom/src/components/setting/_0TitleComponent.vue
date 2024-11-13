<script setup>
import MeshroomImage from '@/assets/Meshroom.svg'
</script>

<template>
  <v-container fluid>
    <v-row class="align-center justify-center">
      <v-col cols="auto" class="d-flex align-center">
        <v-img :aspect-ratio="1" :src="MeshroomImage" width="150"></v-img>
        <div class="text-container">
          <h1 class="title">MESH-ROOM</h1>
          <h2 class="subtitle">당신만의 오리엔테이션을 구성해보세요!</h2>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.text-container {
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 2.5rem;
  font-weight: bold;
  margin: 0.5rem 0;
}

.subtitle {
  font-size: 1.2rem;
  color: #333;
}
</style>
