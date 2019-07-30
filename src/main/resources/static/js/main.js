import Vue from 'vue'
import VueResource from 'vue-resource'
import BootstrapVue from 'bootstrap-vue'
import Router from 'vue-router'

import Wrapper from 'viewes/Wrapper.vue'
import App from 'viewes/App.vue'
import EditBook from 'viewes/EditBook.vue'

Vue.use(VueResource);
Vue.use(BootstrapVue);
Vue.use(Router);


const router = new Router({
    routes: [
        {
            path: '/',
            name:'home',
            component: App,
            props: false
        },
        {
            path: '/edit/:id',
            name:'edit',
            component: EditBook,
            props: true
        }
    ]
});

new Vue({
    render: a => a(Wrapper),
    router
}).$mount('#app');

