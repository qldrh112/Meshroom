<script setup>
import { computed, onMounted } from 'vue'
import { useSessionStore } from '@/stores/sessionStore'
import QrcodeVue from 'qrcode.vue'

const sessionStore = useSessionStore()
const sessionURL = computed(() => sessionStore.getSessionUrl)

const resizeWindow = () => {
  const width = document.querySelector('.qrcode-container').offsetWidth;
  const height = document.querySelector('.qrcode-container').offsetHeight;
  window.resizeTo(width + 180, height + 180);
}

onMounted(() => {
  resizeWindow();
});


const copyToClipboard = () => {
  navigator.clipboard
    .writeText(sessionURL.value)
    .then(() => {
      alert('🔗 링크 복사 완료! 이제 붙여넣기만 하면 마법이 일어납니다! ✨')
    })
    .catch(() => {
      alert('😅 어이쿠! 복사에 실패했어요. 다시 시도해 주세요.')
    })
}

const closeWindow = () => {
  window.close()
}
</script>

<template>
  <div class="code-page">
    <!-- Pink Top Half -->
    <div class="pink-section">
      <div class="qrcode-container">
        <h3 class="title">QR코드를 공유하세요</h3>
        <qrcode-vue :value="sessionURL" :size="150" level="H" style="margin-bottom: 8px;" />
        <div class="text-over-line">
          <p class="subtext">링크도 공유할 수 있어요!</p>
        </div>
        <div class="link-container">
          <input v-model="sessionURL" class="link-input" readonly />
          <button class="copy-button" @click="copyToClipboard">
            <v-icon>mdi-content-copy</v-icon>
          </button>
        </div>
        <!-- <div class="white-section">
          <button class="close-button" @click="closeWindow">
            코드 창 닫기
            <v-icon class="close-icon">mdi-close</v-icon>
          </button>
        </div> -->
      </div>
    </div>
  </div>
</template>

<style scoped>
.qrcode-container {
  background-color: #e7ffde;
  width: 90%; /* 뷰포트 너비의 90%로 설정 */
  max-width: 400px; /* 최대 너비 설정 */
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 5px 5px 10px #c0e6b6; /* 음영 색상 */
}

.pink-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%; /* 부모 요소의 높이를 100%로 설정하여 모달 창이 뷰포트에 맞게 중앙에 위치하도록 함 */
  background-color: #fff;
}

.code-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #ffffff;
  padding: 20px; /* 작은 화면에서 여백을 추가 */
}


.title {
  font-size: 1.2em;
  margin-bottom: 10px;
}

.text-over-line {
  position: relative;
  margin-bottom: 15px;
}

.text-over-line:before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  width: 100%;
  height: 1px;
  background-color: #ccc;
  z-index: 1;
}

.subtext {
  font-size: 0.9em;
  color: #000000;
  background-color: #e7ffde;
  padding: 0 10px;
  position: relative;
  z-index: 2;
  display: inline-block;
  font-weight: bold;
}

.link-container {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.link-input {
  flex: 1;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 5px;
}

.copy-button {
  background-color: transparent;
  border: none;
  cursor: pointer;
  margin-left: 5px;
}

.white-section {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

.close-button {
  background-color: #73dc71;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1em;
  width: 50%;
  position: relative;
}

.close-icon {
  position: absolute;
  right: 10px;
}
</style>
