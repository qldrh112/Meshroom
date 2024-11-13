<script setup>
import { computed, ref, watch } from 'vue'
import { useContentsStore } from '@/stores/contentsStore'
import { VueDraggableNext } from 'vue-draggable-next'
import SelectedCard from './_1SelectedCard.vue'
import SessionStartButton from './_1SessionStartButton.vue'

const contentsStore = useContentsStore()
const selectedContents = computed(() => contentsStore.getSelectedContents)
const localSelectedContents = ref(selectedContents.value)

watch(
  selectedContents,
  (newVal) => {
    localSelectedContents.value = newVal
  },
  { deep: true }
)

const removeContent = (item) => {
  contentsStore.removeContent(item)
}
const onEnd = () => {
  contentsStore.setSelectedContents(localSelectedContents.value)
}
</script>

<template>
  <div v-if="localSelectedContents.length === 0" class="empty-container">
    <p class="empty-message">나만의 오리엔테이션을 구성해보자</p>
  </div>
  <div v-else class="bottom-list">
    <VueDraggableNext
      v-model="localSelectedContents"
      item-key="_id"
      class="draggable-container"
      @end="onEnd"
    >
      <div
        v-for="(each, index) in localSelectedContents"
        :key="each._id"
        class="draggable-item-container"
      >
        <SelectedCard :item="each" :order="index + 1" @remove="removeContent" />
        <div v-if="index < localSelectedContents.length - 1" class="arrow-container">▶️</div>
      </div>
    </VueDraggableNext>
    <SessionStartButton v-if="localSelectedContents.length > 0" />
  </div>
</template>

<style scoped>
.bottom-list {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  margin-left: 2%;
}

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background-color: #f7f7f7;
  border-radius: 10px;
}

.empty-message {
  font-size: 1.5em;
  color: #aaaaaa;
  text-align: center;
  font-weight: bolder;
  font-size: medium;
}

.draggable-container {
  display: flex;
  overflow-x: auto;
  width: 100%;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* Internet Explorer 10+ */
}

.draggable-container::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

.draggable-item-container {
  display: flex;
  align-items: center;
  padding-top: 5px;
}

.arrow-container {
  display: flex;
  font-size: 24px;
  color: #888;
  margin-left: 10px;
  margin-right: 10px;
}

.draggable-item {
  flex: 0 0 auto;
  cursor: move;
}

.session-button {
  background-color: #4caf50;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}
</style>
