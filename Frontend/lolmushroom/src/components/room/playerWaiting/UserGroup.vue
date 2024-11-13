<!-- UserGroup.vue -->
<script setup>
const props = defineProps({
  users: Array,
  groupName: String
})
</script>

<template>
  <div v-if="users.length > 0" class="user-list">
    <div v-for="user in users" :key="user" class="user-container" min-width="200">
      <div class="user-groupname">
        {{ groupName }}
      </div>
      <div class="user-name">
        {{ user }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.user-list {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 40px;
}

.user-container {
  display: flex;
  flex-direction: row;
  align-content: center;
  background-color: #d4d4d474;
  width: 90%;
  font-size: 12px;
  margin-bottom: 10px;
  border-radius: 20px;
}

.user-groupname {
  flex: 3;
  align-content: center;
  text-align: center;
  font-weight: bold;
  background-color: #fcee59d5;
  padding: 5px 10px;
  border-radius: 20px;
}

.user-name {
  flex: 2;
  text-align: center;
  font-size: 1.2em;
  font-weight: bold;
  padding: 5px 10px;
}

</style>
