<script setup>
import { computed, watch } from 'vue'
import { useChatStore } from '@/stores/chatStore'
import { useUserStore } from '@/stores/userStore'
import { useSessionStore } from '@/stores/sessionStore'
import { useRoomStore } from '@/stores/roomStore'
import webSocketAPI from '@/api/webSocket'
import sessionAPI from '@/api/session'
import RoomCard from './RoomCard.vue'
import AddGroupButton from './AddGroupButton.vue'

const chatStore = useChatStore()
const roomStore = useRoomStore()
const sessionStore = useSessionStore()
const userStore = useUserStore()
const isUserStarted = computed(() => userStore.isStarted)
const rooms = computed(() => roomStore.getRooms)
const activeRooms = computed(() => roomStore.getActiveRooms)
const subSessionInfo = computed(() => roomStore.getGroupInfoBySessionId(sessionStore.subSessionId))

/**
 * IMP subSessionInfo를 Watch하고 있다가, 본인의 팀장 여부를 관찰한다.
 */
watch(subSessionInfo, (newValue) => {
  if (newValue && newValue.teamLeaderId === userStore.userId) {
    console.log('그룹장이 되었습니다!')
    userStore.setTeamLeader(true)
  } else {
    console.log('그룹장이 아닙니다.')
    userStore.setTeamLeader(false)
  }
})

/**
 * IMP SubSession을 들어가는 것에 대한 Handler
 * @param sessionId
 * @param userName
 */
const onSubSessionMessageReceived = (message) => {
  chatStore.addSubSessionMessage(message)
}
const getSessionConnection = async (sessionId, userName) => {
  try {
    const response = await sessionAPI.getSessionConnection(sessionId, userName)
    if (response.data.isSuccess) {
      console.log('subSession과 Connection을 성공적으로 만들어 냈습니다.')
      console.log('WebSocket 연결을 진행해 드릴게요:) Group Chatting을 즐기세요!')
      sessionStore.setSubSessionId(sessionId)
      userStore.userOvToken = response.data.result['ovToken']
      webSocketAPI.connect({
        sessionId: sessionStore.getSubSessionId,
        onMessageReceived: onSubSessionMessageReceived,
        subscriptions: ['chat']
      })
    }
  } catch (error) {
    console.error('Error Getting Session Connection', error)
  }
}

const createSubSessionHandler = async (sessionId) => {
  try {
    const response = await sessionAPI.createSubSession(sessionId)
    if (response.data.isSuccess) {
      console.log('하부 Session의 생성이 완료되었습니다:)')
    }
  } catch (error) {
    console.error('Error Creating SubSession', error)
  }
}

const handleLeaveSession = async (sessionId) => {
  try {
    await sessionAPI.getSubSessionQuit(sessionId)
    webSocketAPI.unsubscribeSession(sessionId) // subSession webSocket에도 구독을 제거함.
    userStore.setTeamLeader(false) // 팀장이 나갈 가능성에 대한 처리
    sessionStore.setSubSessionId(null) // sessionStore에서 SubSessionId가 없앤다.
    chatStore.setCurrentMode('All') // ChatStore의 Mode를 'All'로 변환한다.
    chatStore.resetSubSessionMessage() // subSession Message 내역을 초기화한다.
    console.log('Session에서 성공적으로 퇴장했습니다.')
  } catch (error) {
    console.error('Error Leaving Session', error)
  }
}

/**
 * IMP : subSession에서 발생하는 모든 상호작용에 대한 핸들러
 * @param sessionId
 */
const handleRoomClick = async (index) => {
  const room = rooms.value[index]
  if (!room.isActive) {
    console.log('하부 세션을 생성할 수 있어요! 지금 만들어 드릴게요:)')
    await createSubSessionHandler(sessionStore.sessionId)
  } else {
    if (sessionStore.subSessionId) {
      if (room.sessionId === sessionStore.subSessionId) {
        console.log('하부 세션에서 퇴장을 하시는군요! 하부 세션에 대한 연결을 끊어드릴게요:)')
        await handleLeaveSession(room.sessionId)
      } else {
        console.log('이미 소속된 하부 세션이 있어요:) 연결을 끊고 다른 세션으로 연결해 드릴게요:)')
        await handleLeaveSession(sessionStore.subSessionId)
        await getSessionConnection(room.sessionId, { userName: userStore.userName })
      }
    } else {
      console.log('하부 세션으로 입장을 하시는군요! 하부 세션에 대한 연결을 해드릴게요:)')
      await getSessionConnection(room.sessionId, { userName: userStore.userName })
    }
  }
}

const sendReadyMessage = async (index) => {
  const room = rooms.value[index]
  try {
    const response = await sessionAPI.getSubSessionReady(room.sessionId)
    if (response.data.isSuccess) {
      console.log('우리 Team은 모두 준비 완료입니다:)')
    }
  } catch (error) {
    console.error('Error Ready Message to Manager')
  }
}

const changeRoomName = async (index) => {
  const room = rooms.value[index]
  try {
    const response = await sessionAPI.changeSubSessionName(room.sessionId)
    if (response.data.isSuccess) {
      console.log('Group 이름을 성공적으로 변경했어요:)')
    }
  } catch (error) {
    console.error('Group 이름을 변경하지 못했어요:(')
  }
}
</script>

<template>
  <div class="room-list-container">
    <div class="room-list">
      <div
        v-for="(room, index) in activeRooms"
        :key="room.sessionId"
        class="group-container"
      >
        <RoomCard
          :room="room"
          @onReady="sendReadyMessage(index)"
          @onChangeName="changeRoomName(index)"
          @onJoinOrLeave="handleRoomClick(index)"
        />
      </div>
      <v-container
        v-if="activeRooms.length < roomStore.maxRoomCount && !isUserStarted"
        class="add-group-container"
      >
        <AddGroupButton @createGroup="handleRoomClick(activeRooms.length)" />
      </v-container>
    </div>
  </div>
</template>

<style scoped>
.room-list-container {
  width: 100%;
  height: 90%;
  overflow-x: auto;
  display: flex;
  flex-wrap: nowrap;
  padding: 20px;  
  scrollbar-color: #888; 
}

.room-list {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.group-container {
  min-width: 250px;
  padding: 16px;
  /* max-height: 200px; */
  /* flex-shrink: 0; */
}
</style>
