<!-- eslint-disable no-unused-vars -->
<script setup>

const props = defineProps({
  text: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: '#3ABF38' // Default color if not provided
  },
  size: {
    type: String,
    default: 'medium' // Default size if not provided
  },
  variant: {
    type: String,
    default: 'tonal' // Default variant if not provided
  },
  // size: {
  //   type: String,
  //   default: 'default'
  // }
  width: {
    type: String,
    default: "100"
  },
  height: {
    type: String,
    default: "50"
  }
})
</script>

<template>
  <v-btn :variant="variant" :color="color" :size="size" rounded="lg" :text="text" :width="width" :height="height"/>
</template>

<style scoped></style>
