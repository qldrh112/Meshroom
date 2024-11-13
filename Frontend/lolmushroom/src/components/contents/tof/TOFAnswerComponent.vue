<script setup>
  import { useTOFStore } from '@/stores/tofStore'
  import { ref, computed } from 'vue'

  const props = defineProps({
    selectedAnswer: Number,
    answer: Number,
    index: Number,
    allStatements: Array,
  })

  const emit = defineEmits(['cardSelected'])

  const handleCardClick = (key) => {
    emit('cardSelected', key);
  };

  const getCardColor = (key, isHovering) => {
    if (props.selectedAnswer === key) {
      return '#24A319'; // Color when selected
    } else if (isHovering) {
      return '#66FF4F'; // Hover color
    } else {
      return undefined;
    }
  };
</script>

<template>
  <v-container>
    <v-row>
      <v-col
        v-for="(statement, i) in allStatements[index].statements"
        :key="i"
        @click="handleCardClick(i + 1)"
        cols="6"
      >
        <v-hover>
          <template v-slot:default="{ isHovering, props }">
            <v-card
              class="card-border"
              v-bind="props"
              :color="getCardColor(i + 1, isHovering)"
              hover
            >
              <div class="statement">{{ statement }}</div>
            </v-card>
          </template>
        </v-hover>
      </v-col>
    </v-row>
  </v-container>

</template>

<style scoped>
ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

li {
  padding: 5px 0;
}

* {
  font-size: small;
}

.card-border {
  display: flex;
  flex-direction: row;
  min-height: 50px;
  justify-content: center;
  align-items: center;
  padding: 10px;
}

.card-border .statement {
  flex-grow: 1;
  height: 100%;
  font-size: 1.5em;
}

</style>
