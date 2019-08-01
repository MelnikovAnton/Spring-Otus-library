import Vue from 'vue'
import 'api/resource'
import router from 'router/router'
import '@babel/polyfill'
import store from 'store/store'

import Wrapper from 'viewes/Wrapper.vue'

new Vue({
    render: a => a(Wrapper),
    store,
    router
}).$mount('#app');

