import Vue from 'vue'
import 'api/resource'
import router from 'router/router'
import '@babel/polyfill'
import store from 'store/store'
import i18n from "i18n/i18n";

import App from 'viewes/App.vue'

Vue.http.interceptors.push(function (request, next) {
    //Add JWT to all requests
   request.headers.set('Authorization',`Bearer `+ localStorage.getItem('accessToken'))
    //Skip storing token refresh requests
    next()
}.bind(this));

router.afterEach((to, from) => {
    console.log(to.name)
    console.log(from.name)
})

new Vue({
    render: a => a(App),
    store,
    router,
    i18n
}).$mount('#app');

