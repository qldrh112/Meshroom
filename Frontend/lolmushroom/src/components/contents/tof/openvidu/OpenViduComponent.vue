<script setup>
  import axios from 'axios'
  import { ref, onMounted, onBeforeUnmount, reactive, watch } from 'vue'
  import { OpenVidu } from 'openvidu-browser'
  import { useUserStore } from '@/stores/userStore'
  import { useSessionStore } from '@/stores/sessionStore'
  import { useTOFStore } from '@/stores/tofStore'
  import UserVideo from './UserVideo.vue'

  axios.defaults.headers.post['Content-Type'] = 'application/json'

  const { VITE_OPENVIDU_URL } = import.meta.env;
  const { VITE_OPENVIDU_SECRET } = import.meta.env;

  const mic = ref(true)
  const video = ref(true)
  const videoContainer = ref(null)
  const userStore = useUserStore()
  const sessionStore = useSessionStore()
  const store = useTOFStore()
  const ovToken = ref(null)

  const state = reactive({
    // OpenVidu 관련 상태 관리
    OV: null,
    session: sessionStore.subSessionId,
    mainStreamManager: null,
    publisher: null,
    subscribers: [],
  })

  const joinSession = async () => {
    // --- 1) OpenVidu 객체 얻기 ---
    state.OV = new OpenVidu(VITE_OPENVIDU_URL, VITE_OPENVIDU_SECRET)

    // 통신 과정에서 많은 log를 남기게 되는데 필요하지 않은 log를 띄우지 않게 하는 모드
    state.OV.enableProdMode()

    // --- 2) 세션 초기화 ---
    state.session = state.OV.initSession()

    // --- 3) 세션에서 이벤트가 발생할 때 작업 ---

    // 스트림이 받아질 때..
    state.session.on('streamCreated', ({ stream }) => {
      console.log('Stream created:', stream);  // 스트림 생성 로그
      // 비디오는 이 때 렌더링
      const subscriber = state.session.subscribe(stream, 'video-container');
      ovToken.value = stream.connection.data
      console.log('Subscriber added:', subscriber);  // 구독자 추가 로그
      state.subscribers.push(subscriber);

      // assignVideoId(subscriber);
    })

    // 스트림이 파괴될 때...
    state.session.on('streamDestroyed', ({ stream }) => {
      console.log('Stream destroyed:', stream);  // 스트림 파괴 로그
      const index = state.subscribers.indexOf(stream.streamManager, 0);
      if (index >= 0) {
        state.subscribers.splice(index, 1);
        console.log('Subscriber removed at index:', index);  // 구독자 제거 로그
        }
      })

    // 모든 비동기 에러에 대해...
    state.session.on('exception', ({ exception }) => {
      console.warn(exception)
    })

    // --- 4) 유효한 토큰으로 세션에 연결함 ---
    // const token = await getToken(sessionStore.subSessionId)
    const token = userStore.userOvToken
    state.session.connect(token, { clientData: userStore.userOvToken })
      .then(() => {
        console.log('Session connected successfully');
  
        // --- 5) 속성과 함께 카메라 정의 ---
        const pub = state.OV.initPublisher('undefinded', {
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: undefined, // The source of video. If undefined default webcam
          publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
          publishVideo: true, // Whether you want to start publishing with your video enabled or not
          resolution: '320x240', // The resolution of your video
          frameRate: 30, // The frame rate of your video
          insertMode: 'APPEND', // How the video is inserted in the target element 'video-container'
          mirror: false, // Whether to mirror your local video or not
        })

        // 웹캠을 보여주고 퍼블리셔를 저장하는 메인 비디오를 설정
        state.mainStreamManager = pub
        state.publisher = pub
        
        ovToken.value = userStore.userOvToken
        
        // --- 6) 스트림 발행 ---
        state.session.publish(state.publisher)
      })
      .catch((error) => {
        console.log('There was an error connecting to the session:', error.code, error.message)
      })
  }

  const leaveSession = () => {
    // --- 7) Leave the session by calling 'disconnect' method over the Session object ---
    if (state.session) state.session.disconnect()

    // Empty all properties...
    state.session = null
    state.mainStreamManager = null
    state.publisher = null
    state.subscribers = []
    state.OV = null

    // Remove beforeunload listener
    window.removeEventListener('beforeunload', leaveSession)
  }

  watch(() => store.targetUserToken, (newToken) => {
    const videos = videoContainer.value.querySelectorAll('video');
    videos.forEach((video) => {
      if (video.id === newToken) {
        video.style.display = 'block';  // 해당 video를 표시
      } else {
        video.style.display = 'none';  // 다른 video는 숨김
      }
    });
  });

  const toggleMic = () => {
    mic.value = !mic.value
    state.publisher.publishAudio(mic.value)

    state.subscribers.forEach(subscriber => {
      subscriber.subscribeToAudio(mic.value)
    })
  }

  const toggleVideo = () => {
    video.value = !video.value
    // 여기에 내 비디오만 변화하도록 수정한다.
    state.publisher.publishVideo(video.value)

    state.subscribers.forEach(subscriber => {
      subscriber.subscribeToVideo(video.value)
    })
  }

const extractWebSocketURL = (data) => {
  const regex = /wss:\/\/[^\s"]+/;
  const match = data.match(regex);
  return match ? match[0] : null;
}

  // 컴포넌트가 마운트될 때 실행
  onMounted(() => {
    
    const observer = new MutationObserver((mutations) => {
      mutations.forEach((mutation) => {
        if (mutation.type === 'childList') {
          const newVideos = mutation.addedNodes;
          newVideos.forEach((node) => {
            if (node.tagName === 'VIDEO') {
              node.id = extractWebSocketURL(ovToken.value)
              ovToken.value = ''
              
              console.log(node.id, '이게 비디오의 ovtoken')
              console.log(node.id, '이게 발표자의 ovtoken')
              // 렌더링 완료 후 store.targetUserToken 값과 id가 일치하는 비디오만 표시
              if (node.id === store.targetUserToken) {
                node.style.display = 'block';  // 해당 video를 표시
              } else {
                node.style.display = 'none';  // 다른 video는 숨김
              }
            }
          });
        }
      });
    });

  observer.observe(videoContainer.value, { childList: true });

  window.addEventListener('beforeunload', leaveSession)
  joinSession()

  })

  // 컴포넌트가 언마운트될 때 실행
  onBeforeUnmount(() => {
    leaveSession()
  })
</script>

<template>
  <div id="session">
    <div id="main-video" class="col-md-6">
      <div class="d-flex">
        <div v-if="store.targetUserToken === userStore.userOvToken"> 
          <v-icon v-show="video" icon="mdi-video" size="x-large" @click="toggleVideo()"/>
          <v-icon v-show="!video" icon="mdi-video-off" size="x-large" @click="toggleVideo()"/>
        </div>
        <v-icon v-show="mic===true" icon="mdi-microphone" size="x-large" @click="toggleMic()"/>
        <v-icon v-show="mic===false" icon="mdi-microphone-off" size="x-large" @click="toggleMic()"/>
      </div>
    </div>
    <div id="video-container" class="col-md-6" ref="videoContainer">
      <user-video :stream-manager="state.mainStreamManager"/>
    </div>
  </div>
</template>