import Vue from 'vue'
import VueRouter from 'vue-router'
import App from 'viewes/App.vue'
import EditBook from 'viewes/EditBook.vue'
import AuthorList from 'viewes/AuthorViewe.vue'
import GenreList from "viewes/GenreViewe.vue"

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: App,
        props: false
    },
    {
        path: '/edit/:id',
        name: 'edit',
        component: EditBook,
        props: true
    },
    {
        path: '/addBook/',
        name: 'addBook',
        component: EditBook,
        props: true
    },
    {
        path: '/authorList/',
        name: 'authorList',
        component: AuthorList,
        props: true
    },
    {
        path: '/genreList/',
        name: 'genreList',
        component: GenreList,
        props: true
    }
]

export default new VueRouter({
    mode: 'history',
    routes
})