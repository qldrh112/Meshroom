<script setup>
import { defineEmits } from 'vue'
const props = defineProps({
  buttonLabel: {
    type: String
  }
})
const emit = defineEmits(['start-game'])

const startGame = () => {
  emit('start-game')
}
</script>

<template>
  <button class="start-session-btn" @click="startGame">
    <span class="start-session-text">{{ buttonLabel }}</span>
    <v-icon class="start-session-icon" icon="mdi-chevron-right" />
  </button>
</template>

<style scoped>
.start-session-btn {
  width: 60%;
  height: 100%;
  background-color: #9ee682;
  color: #000000;
  padding: 20px;
  border: none;
  border-radius: 10px;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative /* This allows us to position the icon independently */
}

.start-session-text {
  flex-grow: 1;
  text-align: center;
}

.start-session-icon {
  position: absolute;
  right: 20px; /* Position the icon to the far right */
  font-size: 2rem;
  color: #ffffff;
}
</style>
