<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useContentsStore } from '@/stores/contentsStore'
import { useChatStore } from '@/stores/chatStore'
import { useUserStore } from '@/stores/userStore'
import { useRoomStore } from '@/stores/roomStore'
import { useSessionStore } from '@/stores/sessionStore'
import sessionAPI from '@/api/session'
import contentsAPI from '@/api/contents'
import webSocketAPI from '@/api/webSocket'
import SetName from '@/components/room/playerWaiting/SetName.vue'
import ChatScreen from '@/components/common/ChatScreen.vue'

const route = useRoute()
const router = useRouter()
const contentsStore = useContentsStore()
const chatStore = useChatStore()
const userStore = useUserStore()
const roomStore = useRoomStore()
const sessionStore = useSessionStore()
const showNameModal = ref(false)

/**
 * IMP 1. MainSession에 대한 Connection을 생성하는 CallBack()
 * REQ
 * @param `sessionId`
 * @param userName
 *
 * RES openvide_token, user_token
 * * 1.1 : Connection Token은 OpenVidu에서 주는 Token
 * TODO -> OpenVidu Token가 저장되는 지 확인해야 한다.
 * * 1.2 : Server와 인증에 사용되는 User JWT Token은 Cookie에 저장됨
 */
const getSessionConnection = async (sessionId, userName) => {
  try {
    const response = await sessionAPI.getSessionConnection(sessionId, userName)
    if (response.data.isSuccess) {
      console.log('Session Connected')
      userStore.setUserId(response.data.result.userId)
      userStore.userOvToken = response.data.result['ovToken']
    }
  } catch (error) {
    console.error('Error Getting Session Connection', error)
  }
}

/**
 * IMP 2. Player가 상호작용하는 데 필요한 모든 Socket을 연결하는 CallBack()
 * IMP webSocket에 정의한 함수 Interface에 구현체 함수를 만드는 과정이라고 생각하면 된다.
 * * onMainSessionMessageReceived : MainSession Message에 대한 Socket
 * * onSessionEventReceived : Session Info에 대한 변경 Event에 대한 Socket
 * * onProgressEventReceived : Contents의 진행 상황 변경 Event에 대한 Socket
 * TODO : 추가적으로 더 많은 Socket에 연결해야 할 수도 있다. ( Group Chat, Other Game에 대한 Socket )
 */
const onMainSessionMessageReceived = (message) => {
  chatStore.addMainSessionMessage(message)
}
const onSubSessionMessageReceived = (message) => {
  chatStore.addSubSessionMessage(message)
}
const onSessionEventReceived = (message) => {
  roomStore.setSessionData(message)
}
const onProgressEventReceived = (message) => {
  contentsStore.setCurrentContentsState(message)
}
const onFinishEventReceived = (message) => {
  contentsStore.setContentsFinish(message)
}

/**
 * IMP 3. PlayerView에 처음 들어왔을 때, Session의 상태를 가져오는 getSessionInfo API 호출
 */
const getSessionInfo = async (sessionId) => {
  try {
    const response = await sessionAPI.getSessionInfo(sessionId)
    if (response.data.isSuccess) {
      roomStore.setSessionData(response.data.result)
    }
  } catch (error) {
    console.error('Error Getting Session Information', error)
  }
}

/**
 * IMP 4. Player가 Main Session에 입장했을 때, 동작해야 하는 Flow를 관리하는 함수
 * * 4.1. getSessionConnection() API 호출 : Main Session과 Connection을 만들어 준다.
 * * 4.2. Connection을 바탕으로 Global Chatting에 대한 Socket 연결을 진행한다.
 * * 4.3. Connection을 바탕으로 Session Info에 대한 Socket 연결을 진행한다.
 * * 4.4  Connection을 바탕으로 MeshRoom의 Contents Progress에 대한 Socket 연결을 진행한다.
 */
const userFlowHandler = async () => {
  const sessionId = route.params.sessionId
  sessionStore.setSessionId(sessionId)
  await getSessionConnection(sessionId, { userName: userStore.userName })
  await getSessionInfo(sessionId)
  webSocketAPI.connect({
    sessionId: sessionStore.sessionId,
    onMessageReceived: onMainSessionMessageReceived,
    onEventReceived: onSessionEventReceived,
    onProgressReceived: onProgressEventReceived,
    onFinishReceived: onFinishEventReceived,
    subscriptions: ['chat', 'session', 'progress', 'finish']
  })
}

/**
 * TODO : Team Leader는 Contents가 종료되면, 종료 Signal을 진행자에게 보낸다.
 * @param sessionId
 */
const finishContents = async (sessionId) => {
  try {
    const response = await contentsAPI.finishContents(sessionId)
    if (response.data.isSuccess) {
      console.log('우리 Team은 Contents를 종료했어요!')
      console.log(response.data)
    }
  } catch (error) {
    console.error('Error Finishing Contents', error)
  }
}

/**
 * IMP : Finish Event를 처리하는 Watcher => TeamLeader는 Group 종료 Signal을 날린다.
 */
const isFinish = computed(() => contentsStore.getContentsFinish)
watch(isFinish, (newState) => {
  if (newState && userStore.isTeamLeader) {
    console.log('Contents를 종료합니다.')
    finishContents(sessionStore.subSessionId)
  }
})

/**
 * IMP 적절한 ContentsID에 맞게 Routing을 해주는 Watcher
 */
const routeMapping = contentsStore.getRouteMapping
const socketMapping = contentsStore.getSocketMapping
const currentContents = computed(() => contentsStore.getCurrentContentsId)
watch(currentContents, (newContentsId, oldContentsId) => {
  if (oldContentsId) {
    switch (oldContentsId) {
      case '1':
        webSocketAPI.unsubscribeInput('question')
        webSocketAPI.unsubscribeInput('answer')
        webSocketAPI.unsubscribeInput('next')
        break
      case '4':
        webSocketAPI.unsubscribeInput('word')
        webSocketAPI.unsubscribeInput('guess')
        break
      case '7':
        webSocketAPI.unsubscribeGame(socketMapping[oldContentsId])
        break
      default:
        console.warn(`Unknown subscription type: ${oldContentsId}`)
    }
  }
  if (newContentsId && routeMapping[newContentsId]) {
    userStore.setIsStarted()
    contentsStore.setContentsFinish(null)
    router.push({
      name: routeMapping[newContentsId],
      params: {
        sessionId: sessionStore.sessionId,
        subSessionId: sessionStore.subSessionId
      }
    })
  }
})

/**
 * IMP 적절한 Finish Signal에 맞게 RoomWaiting으로 보내주는 Watcher
 */
watch(
  () => contentsStore.currentGroupState,
  (newState) => {
    const group = newState.find((group) => group.sessionId === sessionStore.subSessionId)
    if (group && group.isFinish) {
      if (currentContents.value === '7') {
        router.push({
          name: 'BallGrowResult',
          params: { sessionId: sessionStore.sessionId, subSessionId: sessionStore.subSessionId }
        })
        return
      }
      router.push({
        name: 'mainSession',
        params: { sessionId: sessionStore.sessionId }
      })
    }
  },
  { deep: true } // Ensure the watcher detects nested changes within the array
)

/**
 * IMP 5. Player가 PlayerView에 들어온 순간에 호출되어야 하는 CallBack()
 * * 5.1 : 닉네임 설정 X => showNicknameModal -> userFlowHandler()
 * * 5.2 : 닉네임 설정 O => webSocketAPI를 통한 Socket 재연결
 */
onMounted(() => {
  if (!userStore.userName) {
    showNameModal.value = true
  } else {
    /**
     * IMP : 새로고침 한다면, Main Chat, SessionInfo, Contents Progress Socket을 재구독
     */
    webSocketAPI.connect({
      sessionId: sessionStore.sessionId,
      onMessageReceived: onMainSessionMessageReceived,
      onEventReceived: onSessionEventReceived,
      onProgressReceived: onProgressEventReceived,
      onFinishReceived: onFinishEventReceived,
      subscriptions: ['chat', 'session', 'progress', 'finish']
    })

    /**
     * IMP : 새로고침을 할 때, Session Storage에 subSessionId가 남아 있다면, Sub Chat을 재구독
     */
    if (sessionStore.subSessionId) {
      webSocketAPI.connect({
        sessionId: sessionStore.subSessionId,
        onMessageReceived: onSubSessionMessageReceived,
        subscriptions: ['chat']
      })
    }
  }
})
</script>

<template>
  <div class="contents-container">
    <div class="main-content">
      <RouterView />
    </div>
    <!-- IMP : NickName Modal -->
    <SetName
      :show="showNameModal"
      @update:show="showNameModal = $event"
      @name-saved="userFlowHandler"
    />
    <div class="sub-content">
      <ChatScreen />
    </div>
  </div>
</template>

<style scoped>
.contents-container {
  background-color: #fff2f7;
  height: 100vh; /* 전체 화면 높이 설정 */
  display: flex;
  flex-direction: column;
  position: relative; /* 자식 요소의 absolute 위치 설정을 위해 relative 설정 */
  overflow-y: hidden;
}

.main-content {
  height: 100%; /* 100% 높이 설정 */
  background: #e7ffde;
}

.sub-content {
  position: absolute;
  bottom: 0;
}
</style>
