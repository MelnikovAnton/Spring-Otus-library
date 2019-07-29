import Vue from 'vue'
import VueResource from 'vue-resource'
import BootstrapVue from 'bootstrap-vue'
import Router from 'vue-router'

import Wrapper from 'components/Wrapper.vue'
import App from 'components/App.vue'
import EditBook from 'components/EditBook.vue'

Vue.use(VueResource);
Vue.use(BootstrapVue);
Vue.use(Router);


const router = new Router({
    routes: [
        {
            path: '/',
            name:'home',
            component: App,
        },
        {
            path: '/edit/:id',
            name:'edit',
            component: EditBook,
        }
    ]
});

new Vue({
    render: a => a(Wrapper),
    router
}).$mount('#app');

