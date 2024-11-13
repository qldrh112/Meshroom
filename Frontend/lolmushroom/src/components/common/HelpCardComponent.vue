<template>
  <v-row>
    <!-- 카드 생성하는 첫 번째 방식 -->
    <v-col cols="12" md="4">
      <v-card
        subtitle="No.1"
        text="클릭하면 참여됩니다."
        title="참여하기"
      ></v-card>

      <div class="text-center text-caption">Using Props Only</div>
    </v-col>

    <!-- 카드 생성하는 두 번째 방식 -->
    <v-col cols="12" md="4">
      <v-card>
        <template v-slot:title>
          대기하기
        </template>

        <template v-slot:subtitle>
          No.2
        </template>

        <template v-slot:text>
          다른 사람이 올 때까지 기다리세요.
        </template>
      </v-card>

      <div class="text-center text-caption">Using Slots Only</div>
    </v-col>
    
    <!-- 카드 생성하는 세 번째 방식 -->
    <v-col cols="12" md="4">
      <v-card>
        <v-card-item>
          <v-card-title>시작하기</v-card-title>

          <v-card-subtitle>No.3</v-card-subtitle>
        </v-card-item>

        <v-card-text>
          시작 버튼을 눌러서 콘텐츠에 빠져보세요.
        </v-card-text>
      </v-card>

      <div class="text-center text-caption">Using Markup Only</div>
    </v-col>
  </v-row>
</template>

<script setup>

</script>

<style scoped>

</style>