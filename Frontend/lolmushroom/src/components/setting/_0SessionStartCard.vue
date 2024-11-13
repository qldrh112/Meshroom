<script setup>
import GamePadImage from '@/assets/ps-gamepad.svg'
</script>

<template>
  <v-card class="elevation-2 custom-card">
    <v-card-text>
      <v-container fluid>
        <v-row class="d-flex align-center">
          <!-- Image Column -->
          <v-col cols="3">
            <v-img class="custom-image" :src="GamePadImage" aspect-ratio="1"></v-img>
          </v-col>

          <!-- Text Column -->
          <v-col cols="8">
            <div>
              <h3>세션 만들기</h3>
              <p>오리엔테이션 콘텐츠를 구성하고 함께 즐겨보세요!</p>
            </div>
          </v-col>
          <v-col><v-icon icon="mdi-chevron-right" size="medium" color="#247719"></v-icon></v-col>
        </v-row>
      </v-container>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.custom-card {
  border-radius: 16px;
  width: 555px; /* Fixed width */
}

.custom-image {
  max-width: 80%; /* Ensure the image fits within the column */
  height: auto;
}
</style>
