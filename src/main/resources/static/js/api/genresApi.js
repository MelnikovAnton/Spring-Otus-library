import Vue from 'vue'
import jwt_decode from "jwt-decode";
import axios from "axios";

const genres = Vue.resource('/genres{/id}')

export default {
    add: genre => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return genres.save({}, genre)
            })
        } else return genres.save({}, genre)

    },
    update: genre => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return genres.update({id: genre.id}, genre)
            })
        } else return genres.update({id: genre.id}, genre)

    },
    remove: id => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return genres.remove({id})
            })
        } else return genres.remove({id})

    },
    get: id => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return genres.get({id})
            })
        } else return genres.get({id})

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