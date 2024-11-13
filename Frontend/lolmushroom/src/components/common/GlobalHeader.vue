<!-- eslint-disable no-unused-vars -->
<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'
import { useContentsStore } from '@/stores/contentsStore'
import NicknameModal from '@/components/common/NicknameModal.vue'
import FullNotice from '@/components/info/FullNotice.vue'
import contentsAPI from '@/api/contents'
import ContentsDictonary from '@/components/info/ContentsDictonary.vue'

const showNicknameModal = ref(false)
const noticeInfo = ref(false)
const dictionaryInfo = ref(false)
const userStore = useUserStore()
const contentsStore = useContentsStore()

const openNotice = () => {
  noticeInfo.value = true
}

const closeDictionary = () => {
  dictionaryInfo.value = false
}

const openDictionary = () => {
  dictionaryInfo.value = true
}

const closeNotice = () => {
  noticeInfo.value = false
}

onMounted(() => {})
</script>

<template>
  <header>
    <v-app-bar :height="75" flat>
      <v-col cols="4">
        <v-row>
          <v-btn @click="openDictionary">컨텐츠 도감</v-btn>
          <v-btn @click="openNotice">소식</v-btn>
        </v-row>
      </v-col>
      <v-col cols="1"></v-col>

      <v-col cols="2" class="center-content">
        <div>
          <h1 class="c-name">Meshroom</h1>
        </div>
      </v-col>

      <v-col cols="1"></v-col>
      <v-col cols="4" class="right-content">
        <div class="grid-container">
          <v-btn density="comfortable" icon="mdi-download-circle-outline" class="mr-2"></v-btn>
          <v-btn density="comfortable" icon="mdi-cog" class="mr-2"></v-btn>
          <v-img
            :height="50"
            :width="50"
            aspect-ratio="1"
            cover
            src="https://i.ibb.co/yVbJVG6/image.png"
            class="profile-img"
          ></v-img>
          <span class="nickname">{{ userStore.userNickname }}</span>
        </div>
      </v-col>
    </v-app-bar>
  </header>
  <nickname-modal :show="showNicknameModal" @update:show="showNicknameModal = $event" />
  <v-dialog v-model="noticeInfo" max-width="800px">
    <FullNotice @close="closeNotice" />
  </v-dialog>
  <v-dialog v-model="dictionaryInfo" max-width="1200px">
    <ContentsDictonary :contents="contentsStore.getContents" @close="closeDictionary" />
  </v-dialog>
</template>
<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Honk&display=swap');

@import url('https://fonts.googleapis.com/css2?family=Honk&display=swap');

.c-name {
  font-family: 'Honk', system-ui;
  font-optical-sizing: auto;
  font-weight: 00;
  font-style: normal;
  font-variation-settings:
    'MORF' 15,
    'SHLN' 50;
}
.center-content {
  display: flex;
  justify-content: center;
  align-items: center;
}

.right-content {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.grid-container {
  display: grid;
  grid-template-columns: auto auto auto auto;
  gap: 10px;
  align-items: center;
}

.nickname {
  margin-left: 10px;
}
</style>
