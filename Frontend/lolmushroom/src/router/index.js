import { createRouter, createWebHistory } from 'vue-router'
import MakeSessionView from '@/views/MakeSessionView.vue'
import RoomWatching from '@/components/room/managerWaiting/RoomWatching.vue'
import PlayerMain from '@/components/room/playerWaiting/PlayerMain.vue'
import ManagerView from '@/views/ManagerView.vue'
import PlayerView from '@/views/PlayerView.vue'
import GroupSessionView from '@/views/GroupSessionView.vue'
import GroupFightSessionView from '@/views/GroupFightSessionView.vue'
import TOFMainComponent from '@/components/contents/tof/TOFMainComponent.vue'
import TOFInputComponent from '@/components/contents/tof/TOFInputComponent.vue'
import AlphabetSubmitComponent from '@/components/contents/alphabet/AlphabetSubmitComponent.vue'
import AlphabetMainComponent from '@/components/contents/alphabet/AlphabetMainComponent.vue'
import StartPage from '@/components/setting/_0StartPage.vue'
import CurationPage from '@/components/setting/_1CurationPage.vue'
import SessionCode from '@/components/setting/_2SessionCode.vue'
import BallGrowContainer from '@/components/contents/BallGrow/BallGrowContainer.vue'
import EndPage from '@/components/contents/EndPage.vue'

import BallGrowResult from '@/components/contents/BallGrow/BallGrowResult.vue'
const routes = [
  {
    path: '/',
    name: 'home',
    component: MakeSessionView,
    children: [
      { path: '', name: 'startpage', component: StartPage },
      { path: '/curation', name: 'curation', component: CurationPage },
      { path: '/sessioncode', name: 'sessioncode', component: SessionCode }
    ]
  },

  {
    path: '/admin/:sessionId',
    component: ManagerView,
    children: [{ path: 'roomwatching', name: 'roomwatching', component: RoomWatching }]
  },

  {
    path: '/:sessionId',
    component: PlayerView,
    children: [
      { path: '', name: 'mainSession', component: PlayerMain },
      {
        path: ':subSessionId/GroupSessionView',
        component: GroupSessionView,
        children: [
          { path: 'TOF', name: 'TOF', component: TOFInputComponent },
          { path: 'TOFContent', name: 'TOFContent', component: TOFMainComponent },
          { path: 'alphabet', name: 'alphabet', component: AlphabetSubmitComponent },
          { path: 'alphabetContent', name: 'alphabetContent', component: AlphabetMainComponent }
        ]
      },
      {
        path: ':subSessionId/GroupFightSessionView',
        name: 'GroupFightSessionView',
        component: GroupFightSessionView,
        children: [
          {
            path: '',
            name: 'BallGrowContainer',
            component: BallGrowContainer
          },
          {
            path: 'result',
            name: 'BallGrowResult',
            component: BallGrowResult
          }
        ]
      }
    ]
  },
  {
    path: '/Ending',
    name: 'Ending',
    component: EndPage
  }
]

const router = createRouter({
  history: createWebHistory('/'),
  routes
})

export default router
