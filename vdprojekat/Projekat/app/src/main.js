import { createApp } from 'vue'
import { createI18n } from 'vue-i18n/dist/vue-i18n.cjs.js';
import App from './App.vue'
import router from './router'


import 'bootstrap/dist/js/bootstrap.min.js';


const i18n = createI18n({
    locale: 'srb', // Inicijalni jezik
    fallbackLocale: 'srb', // Jezik koji se koristi kada nije pronađen prevod
    formatFallbackMessages: true,
    messages: {
    en: require('../locales/en.json'), // Učitajte prevode za engleski jezik
    srb: require('../locales/srb.json') // Učitajte prevode za srpski jezik
    }
});




createApp(App).use(router).use(i18n).mount('#app')
