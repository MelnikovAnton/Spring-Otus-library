import Vue from 'vue'
import jwt_decode from "jwt-decode";
import axios from "axios";

const comments = Vue.resource('/comments{/id}')

export default {
    add: comment => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return  comments.save({}, comment)
            })
        } else return  comments.save({}, comment)

    },
    update: comment => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return comments.update({id: comment.id}, comment)
            })
        } else return comments.update({id: comment.id}, comment)

    },
    remove: id => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return comments.remove({id})
            })
        } else return comments.remove({id})

    },
    get: id => {
        const accessToken = jwt_decode(localStorage.getItem('accessToken'))

        if (accessToken.exp * 1000 < new Date().getTime()) {
            return refresh().then((response) => {
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)
                return comments.get({id})
            })
        } else return comments.get({id})

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