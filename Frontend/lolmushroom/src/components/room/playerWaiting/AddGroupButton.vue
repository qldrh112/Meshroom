<script setup>
const emits = defineEmits(['createGroup'])
const handleCreateGroupClick = () => {
  emits('createGroup')
}
</script>

<template>
  <v-btn
    varient="elevated"
    class="add-group-button"
    @click="handleCreateGroupClick"
  >
    <div class="add_container">
      <div class="add_btn">+</div>
      <div class="add_text">그룹 만들기</div>
    </div>
  </v-btn>
</template>

<style scoped>
.add-group-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 120px;
  height: 60px;
  background-color: #D9D9D9;
  padding: 10px;
  margin-top: 35px;
}
.add_container{
  display: flex;
  flex-direction: column;
  align-items: center;
}
.add_btn {
  font-size: 2em;
  font-weight: bold;
}
.add_text {
  margin-bottom: 3px;
}

</style>
