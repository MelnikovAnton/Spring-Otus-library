import Vue from 'vue'
import VueI18n from 'vue-i18n'
import ru from 'i18n/ru.js'
import en from 'i18n/en.js'

Vue.use(VueI18n)

const messages = {ru,en}

export default new VueI18n({
    locale: 'ru',
    fallbackLocale: 'en',
    messages
})