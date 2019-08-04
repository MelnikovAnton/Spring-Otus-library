import Vue from 'vue'
import VueRouter from 'vue-router'
import App from 'viewes/App.vue'
import EditBook from 'viewes/EditBook.vue'

Vue.use(VueRouter)

const routes = [
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
        },
        {
            path: '/addBook/',
            name:'addBook',
            component: EditBook,
            props: true
        }
    ]

export default new VueRouter({
    mode: 'history',
    routes
})