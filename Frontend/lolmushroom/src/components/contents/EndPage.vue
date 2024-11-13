<script setup>
import { onMounted } from 'vue'

function firework() {
  var duration = 100 * 100
  var animationEnd = Date.now() + duration
  var defaults = { startVelocity: 25, spread: 360, ticks: 50, zIndex: 0 }

  function randomInRange(min, max) {
    return Math.random() * (max - min) + min
  }

  var interval = setInterval(function () {
    var timeLeft = animationEnd - Date.now()

    if (timeLeft <= 0) {
      return clearInterval(interval)
    }

    var particleCount = 50 * (timeLeft / duration)

    confetti(
      Object.assign({}, defaults, {
        particleCount,
        origin: { x: randomInRange(0.1, 0.3), y: Math.random() - 0.2 }
      })
    )
    confetti(
      Object.assign({}, defaults, {
        particleCount,
        origin: { x: randomInRange(0.7, 0.9), y: Math.random() - 0.2 }
      })
    )
  }, 100)
}

onMounted(() => {
  const script = document.createElement('script')
  script.src = 'https://cdn.jsdelivr.net/npm/canvas-confetti@1.5.1/dist/confetti.browser.min.js'
  script.onload = () => {
    firework()
  }
  document.body.appendChild(script)
})





</script>
<template>
    <main class="session-end-screen">
      <section class="content-wrapper">
        <div class="blank-div"></div>
        <h1 class="main-quote">"Well begun is half done"</h1>
        <footer class="team-info">
          <p><span class="team-name">made by. Team 사형수</span></p>
          <p class="team-leader">팀장 오화랑</p>
          <p class="team-members">김경호 김윤홍</p>
          <p class="team-members">변재호 송인범</p>
          <p class="team-members">이규석</p>
        </footer>
      </section>
    </main>
  </template>
  
  <style scoped>
  .blank-div{
    height: 200px;
  }
  .session-end-screen {
    width: 100vw;
    height: 100vh;
    background-color: #e7ffde;
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .content-wrapper {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
  }
  
  .main-quote {
    font-size: 72px;
    font-weight: 700;
  }
  
  .team-info {
    font-size: 24px;
    font-weight: 500;
    margin-top: 141px;
  }
  
  .team-name {
    font-size: 40px;
  }
  
  @media (max-width: 991px) {
    .session-end-screen {
      padding: 100px 20px 0;
    }
  
    .main-quote {
      max-width: 100%;
      font-size: 40px;
      line-height: 1.2;
    }
  
    .team-info {
      max-width: 100%;
      margin-top: 40px;
    }
  }
  </style>