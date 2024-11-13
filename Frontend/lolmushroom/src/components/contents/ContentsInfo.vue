<script setup>
import { defineProps } from 'vue'

defineProps({
  title: {
    type: String,
    required: true
  },
  buttonText: {
    type: String,
    required: true
  },
  contentsImage: {
    type: String,
    required: true
  },
  instructionsText: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div class="content-loading-container">
    <div class="header">
      <div class="emoji">😊</div>
      <div class="title">{{ title }}</div>
    </div>
    <div class="content-wrapper">
      <img class="content-image" :src="contentsImage" />
      <div class="instructions">
        <button class="btn">{{ buttonText }}</button>
        <div class="instructions-text">
          <h3>플레이 방법</h3>
          <p v-html="instructionsText"></p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.content-loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #e8f5e9;
  width: 100%;
  height: 100vh;
  box-sizing: border-box;
}

.header {
  display: flex;
  align-items: center;
  background-color: #ccff90;
  padding: 10px;
  border-radius: 10px;
  width: 200px;
  max-width: 1000px;
  position: fixed;
  top: 20px;
  left: 9%;
  z-index: 1000;
  box-sizing: border-box;
}

.emoji {
  font-size: 24px;
  margin-right: 5px;
}

.title {
  font-size: 24px;
  font-weight: bold;
}

.content-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  width: 100%;
  max-width: 1000px;
  margin-top: 80px;
  padding: 10px;
  box-sizing: border-box;
}

.content-image {
  width: 65%;
  height: 100%;
  max-height: 350px;
  background-color: #e0e0e0;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  color: #555;
}

.instructions {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 30%;
  text-align: center;
}

.btn {
  width: 100%;
  padding: 10px;
  font-size: 18px;
  background-color: #76ff03;
  border: none;
  border-radius: 10px;
  margin-bottom: 20px;
}

.instructions-text {
  border-radius: 10px;
  color: white;
  font-size: 16px;
  display: inline-block;
  background-color: #66bb6a;
}

.instructions-text h3 {
  color: #ffffff;
  background-color: #24a319;
  padding: 10px;
  margin: 0;
  border-radius: 10px 10px 0 0;
}

.instructions-text p {
  color: #000000;
  background-color: #90ff77;
  padding: 10px;
  border-radius: 0 0 10px 10px;
  margin: 0;
  line-height: 1.5;
}
</style>
