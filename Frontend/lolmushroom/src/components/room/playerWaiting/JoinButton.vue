<!-- JoinButton.vue -->
<script setup>
const props = defineProps({ isGroupMember: Boolean })
const emit = defineEmits(['onClick'])
const handleClick = () => {
  emit('onClick')
}
</script>

<template>
  <div class="join-btn-div">
    <v-btn
      :style="{ backgroundColor: isGroupMember ? '#FFCDD2' : '#E7FFDE' }"
      class="join-btn"
      @click="handleClick"
    >
      {{ isGroupMember ? '나가기' : '참가하기' }}
    </v-btn>
  </div>
</template>

<style scoped>
.join-btn-div {
  width: 100%;
  margin-top: 10px;
}
.join-btn {
  width: 100%;
}
</style>
