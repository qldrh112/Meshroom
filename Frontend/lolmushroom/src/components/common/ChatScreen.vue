<script setup>
import { ref, computed } from 'vue'
import { useSessionStore } from '@/stores/sessionStore'
import { useChatStore } from '@/stores/chatStore'
import avatarImg from '@/assets/image/chatIcon.png'
import chat from '@/assets/image/chat.jpg'
import webSocketAPI from '@/api/webSocket'
import { useRoomStore } from '@/stores/roomStore'

const menu = ref(false)
const text = ref('')
const showModal = ref(false)
const chatStore = useChatStore()
const sessionStore = useSessionStore()
const roomStore = useRoomStore()

const subSessionId = computed(() => sessionStore.getSubSessionId)
const subSessionInfo = computed(() => roomStore.getGroupInfoBySessionId(subSessionId.value))
/**
 * * 1. 사용자에게 보이는 채팅의 내용은 currentMode에 따라 다르다.
 * IMP 1.1 'All' Mode : mainSessionMessages
 * IMP 1.2 'Group' Mode : subSessionMessages
 */
const isMainMode = computed(() => chatStore.currentMode === 'All')
const currentMessages = computed(() => {
  return isMainMode.value ? chatStore.mainSessionMessages : chatStore.subSessionMessages
})
const currentTitle = computed(() =>
  isMainMode.value ? '전체 그룹의 재잘재잘' : `${subSessionInfo.value.groupName} 그룹의 재잘재잘`
)
const currentSubtitle = computed(() =>
  isMainMode.value ? roomStore.getTotalUserCount + '명' : subSessionInfo.value.occupants + '명'
)

/**
 * * 2. CurrentMode를 Toggle한다.
 * IMP 'All' <-> 'Group'
 */

const toggleMode = () => {
  if (chatStore.currentMode === 'All') {
    if (subSessionId.value) {
      chatStore.setCurrentMode('Group')
    } else {
      showModal.value = true // Show modal if subSession info is not found
    }
  } else {
    chatStore.setCurrentMode('All')
  }
}

/**
 * * 3. Message를 WebSocket을 통해 Send한다.
 * IMP 'All' Mode -> 'sessionStore.sessionId'로 설정하고 Send
 * IMP 'Group' Mode -> 'sessionStore.subSessionId'로 설정하고 Send
 */

const sendMessage = () => {
  if (text.value.trim() === '') return
  const sessionId = isMainMode.value ? sessionStore.sessionId : sessionStore.subSessionId
  const chatMessage = {
    sessionId: sessionId,
    content: text.value,
    timestamp: new Date().toISOString()
  }
  webSocketAPI.sendMessage('/publish/chat/message', chatMessage)
  text.value = ''
}
</script>

<template>
  <div class="text-center">
    <v-menu v-model="menu" :close-on-content-click="false" location="top">
      <template v-slot:activator="{ props }">
        <v-icon icon="custom:chatButton" v-bind="props" class="icon-size"> </v-icon>
      </template>

      <v-card min-width="300" min-height="300" max-height="500" overflow-y: auto>
        <v-list>
          <v-list-item
            :prepend-avatar="chat"
            :subtitle="currentSubtitle"
            :title="currentTitle"
          >
            <template v-slot:append>
              <v-btn class="toggle-button" @click="toggleMode" icon>
                <div class="toggle-circle" :class="{ 'active-circle': isMainMode }"></div>
                <span class="toggle-text left" :class="{ visible: isMainMode }">모두</span>
                <span class="toggle-text" :class="{ visible: !isMainMode }">우리</span>
              </v-btn>
              <v-btn
                style="color: red"
                icon="mdi-minus-box"
                variant="text"
                @click="menu = false"
              ></v-btn>
            </template>
          </v-list-item>
        </v-list>

        <v-divider></v-divider>

        <v-list max-height="300px" style="overflow-y: auto">
          <v-list-item v-for="(message, index) in currentMessages" :key="index">
            <v-banner :avatar="avatarImg" :text="message.userName" color="black" :stacked="false">
              <template v-slot:actions>
                <div>{{ message.content }}</div>
              </template>
            </v-banner>
          </v-list-item>
        </v-list>

        <v-card-actions>
          <v-text-field
            label="메시지를 입력하세요."
            variant="underlined"
            v-model="text"
            @keyup.enter="sendMessage"
          ></v-text-field>
          <v-btn color="primary" variant="text" @click="sendMessage"> Send </v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>

    <!-- Modal for No Group Chat Info -->
    <v-dialog v-model="showModal" max-width="500">
      <v-card class="text-center">
        <v-card-title class="headline">그룹 채팅 불가</v-card-title>
        <v-card-text>
          아직 그룹에 소속되지 않아, 우리 그룹의 채팅이 준비되지 않았어요. 대신 전체 채팅에서 모두와
          이야기 나눠보세요! 금방 새로운 대화가 열릴지도 몰라요 🌱
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn color="primary" @click="showModal = false">알겠어요!</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<style scoped>
.icon-size {
  width: 50px;
  height: 50px;
}

.toggle-button {
  background-color: #2a3b59;
  border-radius: 20px;
  width: 90px;
  height: 30px;
  position: relative;
  display: inline-flex;
  align-items: center;
  padding: 0;
  transition: background-color 0.3s;
  overflow: hidden;
  cursor: pointer;
}

.toggle-circle {
  width: 25px;
  height: 25px;
  background-color: #52d1ff;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 5px;
  transform: translateY(-50%);
  transition: transform 0.3s;
}

.active-circle {
  transform: translate(56px, -50%);
}

.toggle-text {
  color: white;
  font-weight: bold;
  position: absolute;
  left: 50%;
  transition:
    opacity 0.3s,
    visibility 0.3s;
  pointer-events: none;
  opacity: 0;
  visibility: hidden;
}
.toggle-text.left {
  left: 10%;
}

.toggle-text.visible {
  opacity: 1;
  visibility: visible;
}
</style>
