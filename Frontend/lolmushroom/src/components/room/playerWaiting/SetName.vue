<script setup>
import { ref, computed, watch } from 'vue'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()
const props = defineProps({ show: Boolean })
const emits = defineEmits(['update:show', 'name-saved'])

const inputName = ref('')
const nameDialog = ref(props.show)
const saveName = () => {
  if (isFormValid.value) {
    userStore.setName(inputName.value)
    emits('name-saved')
    closeModal()
  }
}
const closeModal = () => {
  emits('update:show', false)
}

watch(
  () => props.show,
  (newValue) => {
    nameDialog.value = newValue
  }
)

// Validation Rule
const required = (value) => !!value
const isFormValid = computed(() => !!inputName.value.trim())
</script>

<template>
  <v-dialog v-model="nameDialog" max-width="500" persistent>
    <template v-slot:default="{}">
      <v-card rounded="lg">
        <v-card-title
          class="d-flex justify-space-between align-center pt-3 pb-3"
          style="background-color: #1ed013"
        >
          <div class="text-h6" style="color: white">이름을 설정하세요</div>
        </v-card-title>
        <v-divider class="mb-1"></v-divider>
        <v-card-text class="pb-0">
          <v-textarea
            v-model="inputName"
            :counter="10"
            rows="1"
            label="이름을 입력하시오."
            :rules="[required]"
            variant="outlined"
            color="green"
            @keydown.enter.prevent="saveName"
          ></v-textarea>
        </v-card-text>
        <v-card-actions class="pt-0 pr-6">
          <v-btn
            color="success"
            @click="saveName"
            :style="{ backgroundColor: '#CEFFBC', color: '#000' }"
            >완료</v-btn
          >
        </v-card-actions>
      </v-card>
    </template>
  </v-dialog>
</template>

<style scoped></style>
