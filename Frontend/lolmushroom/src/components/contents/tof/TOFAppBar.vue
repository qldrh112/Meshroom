<script setup>
</script>

<template>
  <!-- TOF 전용 헤더 -->
  <header class="d-flex flex-row align-center justify-space-between box-layout">
    <div class="box first elevation-8">
      <div class="content-wrapper">
        <v-img src="../../../../src/assets/image/Smillyface.svg" class="smile-image"/>
        <strong>진실 혹은 거짓</strong>
      </div>
    </div>
    <div class="box second elevation-8">
      <p>
        <span style="color: blue;">진실 3개</span>, <span style="color: red;">거짓 1개</span>로 나를 소개해 보세요!</p>
    </div>
    <div class="box third elevation-8">
      <p>당신의 얼굴이 카메라에 보이면 당신의 차례입니다.</p>
    </div>
  </header>
</template>

<style scoped>
  .box-layout {
      padding: 10px;
    }
  .box {
    flex: 1;
    text-align: center;
    font-weight: bold;
    padding: 20px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 70px; 
  }
  .first {
    background-color: #90FF77;
  }
  .second {
    background-color: #CEFFBC;
  }
  .third {
    background-color: #E7FFDE;
  }
  .content-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .smile-image {
    width: 30px;
    height: 30px;
    margin-right: 10px;
  }
</style>