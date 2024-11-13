<script setup>
import { useRouter } from 'vue-router'
import { useSessionStore } from '@/stores/sessionStore'

const router = useRouter()
const sessionStore = useSessionStore()

const shareSession = async () => {
  console.log('세션 링크 공유하기 버튼 클릭됨')
  sessionStore.setSessionUrl(sessionStore.getSessionId)
  const sessioncodeUrl = router.resolve({
    name: 'sessioncode',
    query: {
      sessionUrl: sessionStore.getSessionUrl
    }
  }).href
  window.open(sessioncodeUrl, '_blank', 'width=1200,height=800')
}
</script>

<template>
  <div class="share-session">
    <v-btn class="share-session-btn" @click="shareSession">
      <v-icon class="share-icon" icon="mdi-share" />
      공유
    </v-btn>
  </div>
</template>

<style scoped>
.share-session {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.share-session-btn {
  display: flex;
  align-items: center;
  background-color: #e7ffde;
  color: #000000;
  width: 120px;
  height: 60%;
  padding: 10px 20px;
  margin-bottom: 10px;
  border: none;
  border-radius: 50px;
  font-size: 1.2rem;
}

.share-icon {
  font-size: 1.5rem; /* Adjust size as needed */
  margin-right: 8px;
  color: #000000;
}

.share-info-text {
  font-size: 1rem;
  text-align: center;
}
</style>
