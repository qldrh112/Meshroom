<script setup>
import { ref, onMounted } from 'vue'

const currentURL = ref('')

onMounted(() => {
  const url = new URL(window.location.href)
  const hostname = url.hostname === 'localhost' ? '127.0.0.1' : url.hostname
  currentURL.value = `${hostname}:${url.port}`
})
</script>

<template>
  <footer>
    <v-footer :height="30" app>
      <v-col class="text-right pr-4" cols="12" color="#333">
        {{ currentURL }}
      </v-col>
    </v-footer>
  </footer>
</template>

<style scoped>
.v-footer {
  background-color: #f0f0f0; /* Light grey background for the footer */
  border-top: 1px solid #e0e0e0; /* Optional border for the footer */
}

.text-right {
  text-align: right;
}
</style>
