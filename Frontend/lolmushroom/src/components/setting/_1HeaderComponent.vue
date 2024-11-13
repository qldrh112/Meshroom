<script setup></script>

<template>
  <div class="custom-button">
    <div class="left-part">컨텐츠 목록</div>
    <div class="right-part">컨텐츠의 +버튼을 클릭해서 큐레이션을 구성해보세요!</div>
  </div>
</template>

<style scoped>
.custom-button {
  display: inline-flex;
  align-items: center;
  border-radius: 20px;
  background-color: #ffffff;
  box-shadow: 2px 2px 6px rgba(0, 0, 0, 0.2);
}

.left-part {
  background-color: #e7ffde;
  padding: 10px 20px;
  text-align: center;
  font-weight: bolder;
  border-radius: 20px; /* Round only the left side */
  max-width: 100px; /* 텍스트를 줄바꿈할 수 있도록 최대 너비를 지정 */
  white-space: normal; /* 텍스트 줄바꿈 허용 */
  word-break: keep-all; /* 단어 단위로 줄바꿈 방지 */
}

.right-part {
  color: black;
  padding: 10px 20px;
  font-size: small;
  font-weight: bolder;
  width: 100%;
}
</style>
