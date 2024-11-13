<script setup>
import LoadingCountDown from './LoadingCountDown.vue'
import ContentsInfo from './ContentsInfo.vue'

const props = defineProps({
  contentsInfo: {
    type: Object,
    required: true
  },
  contentsImage: {
    type: String,
    required: true
  },
  instructionsText: {
    type: String,
    required: true
  },
  time: {
    type: String,
    required: true
  },
  countText: {
    type: String,
    required: true
  }
})
const title = props.contentsInfo.contentTitle
const buttonText = props.contentsInfo.category
</script>

<template>
  <ContentsInfo
    :title="title"
    :buttonText="buttonText"
    :contentsImage="contentsImage"
    :instructionsText="instructionsText"
  />
  <LoadingCountDown :time="time" :text="countText" />
</template>
