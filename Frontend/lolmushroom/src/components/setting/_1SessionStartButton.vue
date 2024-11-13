<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useContentsStore } from '@/stores/contentsStore'
import { useSessionStore } from '@/stores/sessionStore'
import sessionAPI from '@/api/session'

const router = useRouter()
const sessionStore = useSessionStore()
const contentsStore = useContentsStore()
const selectedContents = computed(() => contentsStore.getSelectedContents)
const totalDuration = computed(() => contentsStore.getTotalDuration)

/**
 * IMP 1. MainSession에 대한 Connection을 생성한다.
 * REQ
 * @param sessionId
 * @param userName
 *
 * RES openvide_token, user_token
 */
const getSessionConnection = async (sessionId, userName) => {
  try {
    const response = await sessionAPI.getSessionConnection(sessionId, userName)
    if (response.data.isSuccess) {
      console.log('Connection을 성공적으로 만들어 냈습니다:)')
    }
  } catch (error) {
    console.error('Error Getting Session Connection', error)
  }
}

const createSessionHandler = async () => {
  try {
    const order = selectedContents.value.map((content) => content._id)
    const response = await sessionAPI.createSession({ contentsOrder: order })
    if (response.data.isSuccess) {
      const { sessionId } = response.data.result
      sessionStore.setSessionId(sessionId)
    }
  } catch (error) {
    console.error('진행자님! SessionID 생성을 실패했습니다. ', error)
  }
}

// 세션 시작하기 버튼 클릭 핸들러
const startSession = async () => {
  console.log('세션 시작하기 버튼 클릭됨')
  await createSessionHandler()
  await getSessionConnection(sessionStore.getSessionId, { userName: 'Manager' })
  router.replace({ name: 'roomwatching', params: { sessionId: sessionStore.getSessionId } })

  sessionStore.setSessionUrl(sessionStore.getSessionId)
  const sessioncodeUrl = router.resolve({
    name: 'sessioncode',
    query: {
      sessionUrl: sessionStore.getSessionUrl
    }
  }).href
  window.open(sessioncodeUrl, '_blank', 'width=1200,height=800')
}

const formattedDuration = computed(() => {
  return `예상소요시간: ${totalDuration.value}분`
})
</script>

<template>
  <div class="session-button-container">
    <button @click="startSession" class="session-button">OT 시작하기</button>
    <div class="duration-display">{{ formattedDuration }}</div>
  </div>
</template>

<style scoped>
.session-button-container {
  flex: 0 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 3%;
}

.session-button {
  background-color: #228b22;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 16px;
  margin-bottom: 10px;
}

.session-button:hover {
  background-color: #1e7a1e;
}

.duration-display {
  font-size: 13px;
  font-weight: bold;
  color: #000000;
}
</style>
