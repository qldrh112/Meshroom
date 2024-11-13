import { createVuetify } from 'vuetify'
import { customSVGs } from '@/assets/icons/customSvgs'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi',
    sets: {
      custom: customSVGs
    }
  }
})

export default vuetify
