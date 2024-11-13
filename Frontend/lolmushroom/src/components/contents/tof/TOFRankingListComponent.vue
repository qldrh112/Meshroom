<script setup>
// 여기서 그룹의 전체 유저 목록을 요청해서 아이콘이랑 닉네임 가져와야 함
const users = [
  {
    name: '나이스한 강아지',
    src: 'public/favicon.ico',
  },
  {
    name: '재빠른 날다람쥐',
    src: 'public/favicon.ico',
  },
  {
    name: '영민한 뱅갈호랑이',
    src: 'public/favicon.ico',
  },
  {
    name: '얍삽한 치타',
    src: 'public/favicon.ico',
  },
  {
    name: '여유로운 오징어',
    src: 'public/favicon.ico',
  },
  {
    name: '거대한 사막여우',
    src: 'public/favicon.ico',
  },
]
</script>

<template>
  <v-container class="overlay-container">
    <v-list>
      <v-list-subheader>선택 결과</v-list-subheader>
  
      <v-list-item
        v-for="(user, i) in users"
        :key="i"
        :value="user"
        :prepend-avatar=users.src
      >
        <template v-slot:prepend>
          <v-avatar :image="user.src" />
        </template>
  
        <v-list-item-title v-text="user.name" />
      </v-list-item>
    </v-list>
  </v-container>
</template>

<style scoped>
  .overlay-container {
    width: 1000px
  }
</style>