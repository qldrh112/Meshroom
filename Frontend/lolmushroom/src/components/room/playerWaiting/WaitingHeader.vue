<!-- eslint-disable no-unused-vars -->
<script setup>
import { defineProps } from 'vue'
import SmileyFace from '@/assets/image/Smileyface.svg'

const props = defineProps({
  firstDescription: {
    type: String,
    required: true
  },
  secondDescription: {
    type: String,
    required: true
  },
  thirdDescription: {
    type: String
  }
})
</script>
<template>
  <v-container class="d-flex flex-row align-center justify-space-between box-layout">
    <div class="box first">
      <div class="content-wrapper">
        <v-img :src="SmileyFace" class="smile-image"></v-img>
        <span v-html="firstDescription"></span>
      </div>
    </div>
    <div class="box second" v-html="secondDescription"></div>
    <div class="box third" v-html="thirdDescription"></div>
  </v-container>
</template>

<style scoped>
.box {
  flex: 1;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.first {
  border-radius: 5px 0px 0px 5px;
  background-color: #90ff77;
  font-size: x-large;
  font-weight: bold;
}
.second {
  background-color: #ceffbc;
  font-size: large;
}
.third {
  border-radius: 0px 5px 5px 0px;
  background-color: #e7ffde;
  font-size: small;
}
.content-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}
.smile-image {
  width: 30px;
  height: 30px;
  margin-right: 10px;
}
/* 반응형 폰트 크기 조정 */
@media (max-width: 1200px) {
  .first,
  .second {
    font-size: large;
  }

  .third {
    font-size: small;
  }
}

@media (max-width: 768px) {
  .first,
  .second {
    font-size: medium;
  }

  .third {
    font-size: x-small;
  }
}

@media (max-width: 480px) {
  .first,
  .second {
    font-size: small;
  }

  .third {
    font-size: xx-small;
  }
}
</style>
