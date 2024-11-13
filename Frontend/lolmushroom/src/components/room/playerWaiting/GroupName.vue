<!-- GroupName.vue -->
<script setup>
import autorenew from '@/assets/image/autorenew.svg'
const props = defineProps({
  groupName: String,
  isGroupMember: Boolean
})
const emit = defineEmits(['onClick'])
const handleClick = () => {
  if (!props.isGroupMember) {
    console.log('Group Member가 아니라면, 함부로 다른 Group의 이름을 바꿀 수 없어요:)')
  } else {
    emit('onClick')
  }
}
</script>

<template>
  <div class="group-name-div">
    <v-btn class="group-name" @click="handleClick">
      <v-img :src="autorenew" class="icon" />
      {{ groupName }}
    </v-btn>
  </div>
</template>

<style scoped>
.group-name-div {
  width: 100%;
}
.group-name {
  background-color: #fff58a64;
  font-size: larger;
  font-weight: bold;
  /* height: 100%; */
  height: 70px;
  width: 100%;
}

.icon {
  width: 20px;
  margin-right: 10px;
}
</style>
