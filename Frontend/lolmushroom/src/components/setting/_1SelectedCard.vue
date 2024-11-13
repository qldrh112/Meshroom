<script setup>
const props = defineProps({
  item: Object,
  order: Number
})

const emit = defineEmits(['remove'])

const onRemoveClick = () => {
  emit('remove', props.item)
}
</script>

<template>
  <div class="content-card">
    <img :src="item.imgUrl" alt="Content Thumbnail" class="thumbnail" style="width: 100%; height: 100px;"/>
    <button class="delete-btn" @click="onRemoveClick">✖</button>
    <div class="content-info">
      <div>{{ item.contentTitle }}</div>
    </div>
  </div>
</template>

<style scoped>
.content-card {
  position: relative;
  width: 120px;
  background-color: #ffffff;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 1px solid rgb(96, 96, 96);
  transition: transform 0.2s;
}

.content-card:hover {
  transform: scale(1.02);
}

/* 기본적으로 삭제 버튼 숨김 */
.delete-btn {
  position: absolute;
  top: -7px;
  right: -7px;
  width: 24px;
  height: 24px;
  background-color: #fff2f7;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
  color: red;
  line-height: 24px;
  text-align: center;
  padding: 0;
  opacity: 0;
  transition: opacity 0.2s;
}

/* content-card가 hover 상태일 때 삭제 버튼 표시 */
.content-card:hover .delete-btn {
  opacity: 1;
}

.thumbnail {
  width: 90%;
  height: auto;
  border-radius: 5px;
}

.content-info {
  display: flex;
  font-size: 15px;
  justify-content: center;
  font-weight: 900;
  border-top: 1px solid black;
}
</style>
