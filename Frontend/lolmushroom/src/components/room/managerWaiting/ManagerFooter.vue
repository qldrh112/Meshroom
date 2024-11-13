<template>
  <div class="footer-container">
    <div class="footer-section empty"></div>
    <div class="footer-section">
        <slot name="start"></slot>
    </div>
    <div class="footer-section share">
      <slot name="share"></slot>
    </div>
  </div>
</template>

<style scoped>
.footer-container {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin-top: 30px;
  margin-bottom: 50px;
  height: 80px;
}

.footer-section {
  flex: 1;
  text-align: center;
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.footer-section.empty {
  flex: 1;
}

</style>
