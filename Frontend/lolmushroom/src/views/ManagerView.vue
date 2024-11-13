<script setup>
import { onMounted } from 'vue'

import { useRoomStore } from '@/stores/roomStore'
import { useSessionStore } from '@/stores/sessionStore'
import { useContentsStore } from '@/stores/contentsStore'

import sessionAPI from '@/api/session'
import webSocketAPI from '@/api/webSocket'

const sessionStore = useSessionStore()
const contentsStore = useContentsStore()
const roomStore = useRoomStore()

/**
 * IMP 1. Manager는 getSessionInfo()를 통해 Session에 대한 정보를 가져온다.
 * * Manager 역시, Connection을 만들고 Socket 연결을 진행한다.
 * * 이와 같은 이유로, 처음 ManagerView에 들어오면 API를 통해 Group 정보를 가져오게 된다.
 * ! 근데 참여자들이 들어오는 것 자체가 Trigger가 되므로, 굳이 사용하지 않아도 되는 함수일 수도 있다.
 * ! 하지만, 진행자가 Last에 들어갈 수도 있으므로, 정의한느 것이 안전하다.
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
 * IMP 2. Manager가 Server에 대해 필요한 Socket을 연결한다.
 * IMP webSocket에 정의한 함수 Interface에 구현체 함수를 만드는 과정이라고 생각하면 된다.
 * * 1. Session 정보에 대한 Socket
 * * 2. Contents 진행에 대한 Socket
 * @param message
 * TODO : Manager도 여기서, Main Chat / Other Contents에 대한 구독을 진행해야 할 수도 있다.
 */
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
 * IMP 3. Manager가 ManagerView에 들어왔을 때 호출되어야 하는 CallBack에 대한 정리 CallBack()
 * * 1. SessionInfo에 대한 호출 API
 * * 2. Connection을 기반으로 SessionInfo에 대한 Socket을 연결한다.
 * * 3. Connection을 기반으로 Meshroom의 Progress에 대한 Socket을 연결한다.
 */
const managerFlowHandler = async () => {
  await getSessionInfo(sessionStore.sessionId)
  webSocketAPI.connect({
    sessionId: sessionStore.sessionId,
    onEventReceived: onSessionEventReceived,
    onProgressReceived: onProgressEventReceived,
    onFinishReceived: onFinishEventReceived,
    subscriptions: ['session', 'progress', 'finish']
  })
}

/**
 * IMP 4. ManagerView에 진입한 순간 ( onMounted )에 대한 호출 -> mangerFlowHandler()
 */
onMounted(() => {
  managerFlowHandler()
})
</script>

<template>
  <RouterView />
</template>
