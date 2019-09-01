import Vue from 'vue'
import axios from 'axios';
import jwt_decode from "jwt-decode";

const books = Vue.resource('/books{/id}')

export default {
    add: book => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return books.save({}, book)
            })
        } else return books.save({}, book)

    },
    update: book => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return books.update({id: book.id}, book)
            })
        } else return books.update({id: book.id}, book)

    },
    remove: id => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return books.remove({id})
            })
        } else return books.remove({id})

    },
    get: id => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return books.get({id})
            })
        } else return books.get({id})

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