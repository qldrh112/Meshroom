import { h } from 'vue'
import type { IconSet, IconProps } from 'vuetify'
import cameraOn from './cameraOn.vue'
import cameraOff from './cameraOff.vue'
import micOn from './micOn.vue'
import micOff from './micOff.vue'
import bgmOn from './bgmOn.vue'
import bgmOff from './bgmOff.vue'
import chatButton from './ChatButton.vue'

const customSvgNameToComponent: any = {
  cameraOn,
  cameraOff,
  micOn,
  micOff,
  bgmOn,
  bgmOff,
  chatButton
}

// Define the aliases object with the necessary mappings
const customSVGs: IconSet = {
  component: (props: IconProps) => h(customSvgNameToComponent[props.icon])
}

export { customSVGs /* aliases */ }
