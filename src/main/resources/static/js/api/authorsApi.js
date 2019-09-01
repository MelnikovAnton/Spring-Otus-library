import Vue from 'vue'
import axios from "axios";
import jwt_decode from "jwt-decode";

const authors = Vue.resource('/authors{/id}')

export default {
    add: author => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return  authors.save({}, author)
            })
        } else return  authors.save({}, author)

    },
    update: author => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return  authors.update({id: author.id}, author)
            })
        } else return  authors.update({id: author.id}, author)

    },
    remove: id => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return  authors.remove({id})
            })
        } else return  authors.remove({id})

    },
    get: id => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return authors.get({id})
            })
        } else return authors.get({id})
    }
}

async function refresh() {
    return axios.post('/oauth/token',
        'grant_type=refresh_token' +
        '&refresh_token=' + localStorage.getItem('refreshToken') +
        '&scope=read write'
        , {
            auth: {
                username: 'client',
                password: 'secret'
            }
        });
}