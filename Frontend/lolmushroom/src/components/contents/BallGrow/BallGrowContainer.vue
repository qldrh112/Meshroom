<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useBallStore } from '@/stores/ballStore'
import { useContentsStore } from '@/stores/contentsStore'
import { useSessionStore } from '@/stores/sessionStore'
import { useRoomStore } from '@/stores/roomStore'
import BallGrow_Content from '@/assets/BallGrow_Contents.png'
import WaitingHeader from '@/components/room/playerWaiting/WaitingHeader.vue'
import ContentsLoading from '@/components/contents/ContentsLoading.vue'
import BallMain from './BallMain.vue'
import BallGroup from './BallGroup.vue'

const router = useRouter()
const roomStore = useRoomStore()
const ballStore = useBallStore()
const contentsStore = useContentsStore()
const sessionStore = useSessionStore()

/**
 * IMP : Loading & Header Info
 */
const firstDescription = '공 키우기'
const secondDescription = '우리 그룹의 공을 최대한 크게 만드세요!'
const thirdDescription = 'Tip: 클릭 대신 스페이스바를 누를 수 있답니다 !'
const contentsInfo = contentsStore.contents[6]
const instructionsText =
  '1. 내 그룹의 공을 클릭하면 커집니다.<br>2.상대 그룹 공을 클릭하면 작아집니다<br>3.그룹원들과 협의해서 공격/수비 전략을 세워보세요 !'
const showLoading = ref(false)

/**
 * IMP Session Socket을 통해 활성화된 Group을 받아오고 Ball 개수 결정
 */
const activeGroups = computed(() => {
  return roomStore.getActiveRooms.map((room) => ({
    sessionId: room.sessionId,
    groupName: room.groupName
  }))
})

// socket 연결
onMounted(() => {
  ballStore.initSocketConnection(
    sessionStore.sessionId,
    sessionStore.subSessionId,
    activeGroups.value
  )

  showLoading.value = true
  setTimeout(() => {
    showLoading.value = false
  }, 5000) // 5초 동안 모달을 표시
})
</script>

<template>
  <v-dialog v-model="showLoading" persistent max-width="1200">
    <ContentsLoading
      :contentsInfo="contentsInfo"
      :contentsImage="BallGrow_Content"
      :instructionsText="instructionsText"
      :time="'5'"
      :countText="'초 후에 시작합니다!'"
    />
  </v-dialog>
  <div class="main-container">
    <WaitingHeader
      :first-description="firstDescription"
      :second-description="secondDescription"
      :third-description="thirdDescription"
    />
    <div class="content-container">
      <div class="left-container">
        <BallMain :timeOut="contentsInfo.timeOut" />
      </div>
      <div class="right-container">
        <BallGroup />
      </div>
    </div>
  </div>
</template>

<style scoped>
.main-container {
  background-color: #D8F3DC;
  align-items: center;
  height: 100%; /* 화면 전체 높이를 차지 */
  /* 드래그블 방지 */
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

.content-container {
  display: flex;
  width: 100%;
  height: 88%;
}

.left-container {
  flex: 2;
  display: flex;
  justify-content: center; /* 수평 가운데 정렬 */
  align-items: center; /* 수직 가운데 정렬 */
}

.right-container {
  flex: 1;
  display: flex;
}
</style>
