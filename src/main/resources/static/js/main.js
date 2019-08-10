import Vue from 'vue'
import 'api/resource'
import router from 'router/router'
import '@babel/polyfill'
import store from 'store/store'
import i18n from "i18n/i18n";

import App from 'viewes/App.vue'

new Vue({
    render: a => a(App),
    store,
    router,
    i18n
}).$mount('#app');

