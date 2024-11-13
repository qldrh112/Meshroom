<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAlphabetStore } from '@/stores/alphabetStore'
import { useBallStore } from '@/stores/ballStore'
import { useContentsStore } from '@/stores/contentsStore'
import { useSessionStore } from '@/stores/sessionStore'
import { useUserStore } from '@/stores/userStore'
import { useRoomStore } from '@/stores/roomStore'
import { useTOFStore } from '@/stores/tofStore'
import Swal from 'sweetalert2'
import webSocketAPI from '@/api/webSocket'
import contentsAPI from '@/api/contents'
import adminWatchWaiting from '@/components/room/eachroom/adminWatchWaiting.vue'
import ManagerHeader from './ManagerHeader.vue'
import ManagerFooter from './ManagerFooter.vue'
import FooterStart from './FooterStart.vue'
import FooterShare from './FooterShare.vue'

const router = useRouter()
const alphabetStore = useAlphabetStore()
const ballStore = useBallStore()
const contentsStore = useContentsStore()
const sessionStore = useSessionStore()
const userStore = useUserStore()
const roomStore = useRoomStore()
const TOFStore = useTOFStore()
const showShareFooter = ref(true)

const currentContents = computed(() => contentsStore.getCurrentContentsId)
const groups = computed(() => {
  return roomStore.getActiveRooms.map((room) => ({
    sessionId: room.sessionId,
    groupName: room.groupName,
    occupants: room.occupants
  }))
})

/**
 * IMP 1. 진행자가 게임 시작을 누르면, Session Contents가 시작하는 첫 단계이므로, isStart : true
 * IMP 1.1. Game이 시작된 이후에는, NextContents를 호출한다.
 * * Contents API의 callNextContetns를 호출한다.
 */
const callNextContents = async (isStart) => {
  try {
    const response = await contentsAPI.callNextContents(isStart)
    if (response.data.isSuccess) {
      console.log(response.data)
    }
  } catch (error) {
    console.error('Error Call Next Contents', error)
  }
}

/**
 * IMP 2. 진행자는 현재 Contents의 상황을 Watch하면서, ContentsId에 따라 Rendering이 달라진다.
 * IMP 2.1 하위 Component는 v-if에 따라 보여주는 정보가 다르게 되고, 받는 정보가 달라진다.
 */
const socketMapping = computed(() => contentsStore.getSocketMapping)
watch(currentContents, (newContentsId, oldContentsId) => {
  if (oldContentsId) {
    webSocketAPI.unsubscribeGame(socketMapping.value[oldContentsId])
  }
  if (newContentsId) {
    switch (newContentsId) {
      case '0':
        router.push({ name: 'Ending' })
        break
      case '1':
        TOFStore.initSocketConnection(sessionStore.sessionId, groups.value)
        break
      case '4':
        alphabetStore.initSocketConnection(sessionStore.sessionId, groups.value)
        break
      case '7':
        ballStore.initSocketConnection(
          sessionStore.sessionId,
          sessionStore.subSessionId,
          groups.value
        )
        break
      default:
        console.warn(`Unknown subscription type: ${newContentsId}`)
    }
  }
})

// 게임 시작 알림
const startGame = () => {
  Swal.fire({
    title: '게임을 시작하시겠습니까?',
    text: '다시 돌아갈 수 없어요!',
    icon: 'question',
    color: 'black',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '시작',
    cancelButtonText: '취소'
  }).then((result) => {
    if (result.isConfirmed) {
      callNextContents(true)
      userStore.setIsStarted()
      showShareFooter.value = false
    }
  })
}

const startNextGame = () => {
  Swal.fire({
    title: '다음 게임을 시작하시겠습니까?',
    icon: 'question',
    color: 'black',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: '시작',
    cancelButtonText: '취소'
  }).then((result) => {
    if (result.isConfirmed) {
      callNextContents(false)
    }
  })
}
</script>

<template>
  <div class="room-waiting">
    <div class="header-container">
      <ManagerHeader />
    </div>
    <adminWatchWaiting :currentContents="currentContents" />
    <ManagerFooter>
      <template v-slot:start>
        <FooterStart
          v-if="userStore.getIsStarted"
          @start-game="startNextGame"
          :buttonLabel="'다음 컨텐츠로'"
        />
        <FooterStart v-else @start-game="startGame" :buttonLabel="'컨텐츠 시작하기'" />
      </template>
      <template v-slot:share v-if="showShareFooter">
        <FooterShare />
      </template>
    </ManagerFooter>
  </div>
</template>

<style scoped>
div {
  text-align: center;
  font-weight: bold;
}

.header-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 1%;
}
</style>
