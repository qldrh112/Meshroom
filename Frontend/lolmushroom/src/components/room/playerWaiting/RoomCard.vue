<script setup>
import { computed } from 'vue'
import { useSessionStore } from '@/stores/sessionStore'
import { useUserStore } from '@/stores/userStore'
import ReadyButton from './ReadyButton.vue'
import GroupName from './GroupName.vue'
import JoinButton from './JoinButton.vue'
import UserGroup from './UserGroup.vue'

const sessionStore = useSessionStore()
const userStore = useUserStore()
const isUserStarted = computed(() => userStore.isStarted)
const isGroupTeamLeader = computed(() => {
  return sessionStore.subSessionId === props.room.sessionId && userStore.isTeamLeader
})
const isGroupMember = computed(() => {
  return sessionStore.subSessionId === props.room.sessionId
})
const props = defineProps({
  room: Object
})
const emits = defineEmits(['onReady', 'onChangeName', 'onJoinOrLeave'])
const handleReadyClick = () => {
  emits('onReady')
}
const handleChangeNameClick = () => {
  emits('onChangeName')
}
const handleJoinOrLeaveClick = () => {
  emits('onJoinOrLeave')
  if (isGroupTeamLeader.value && props.room.isReady) {
    emits('onReady')
  }
}
</script>

<template>
  <div class="group-card-div">
    <div :class="['group-info', { 'group-info--strong-border': isGroupMember }]">
      <!-- ReadyButton과 JoinButton을 isUserStarted가 false일 때만 표시 -->
      <ReadyButton
        v-if="!isUserStarted"
        :isReady="room.isReady"
        :isGroupTeamLeader="isGroupTeamLeader"
        @onClick="handleReadyClick"
        class="group-ready-btn"
      />
      <GroupName
        :groupName="room.groupName"
        :isGroupMember="isGroupMember"
        @onClick="handleChangeNameClick"
        class="group-name-btn"
      />
      <JoinButton
        v-if="!isUserStarted"
        :isGroupMember="isGroupMember"
        @onClick="handleJoinOrLeaveClick"
        class="group-join-btn"
      />
    </div>
    <UserGroup :users="room.users" :groupName="room.groupName" />
  </div>
</template>

<style scoped>
.group-card-div {
  max-height: 200px;
  width: 100%;
}

.group-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}

.group-info--strong-border {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.5);
}

/* .group-ready-btn {
  min-height: 25px;
  flex: 1;
} */
/* .group-name-btn {
  max-height: 70px;
  flex: 3;
} */
/* .group-join-btn {
  min-height: 25px;
  flex: 1;
} */
</style>
