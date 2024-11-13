<script setup>

  const props = defineProps({
    users: {
      type: Array,
      required: true,
    },
    target: {
      type: String,
      required: true
    }
  })

</script>

<template>
  <v-container
    v-for="(avatar, i) in props.users"
    :key="i"
    class="card-container"
    :class="{ highlighted: avatar.name === target }"
  >
    <v-avatar
      class="my-3"  
      size="60"
    >
    <v-img
      :src="avatar.src"
      cover
    />
    </v-avatar>
    <v-list-item
      class="text-black"
      :title="avatar.name"
    />
  </v-container>
</template>


<style scoped>

</style>