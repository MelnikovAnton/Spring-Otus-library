import axios from 'axios';
import jwt_decode from "jwt-decode";



export default class AuthApi {
   // loginEndpoint = '/oauth/token';
   //  validateTokenApi = '/oauth/check-token';

    login(username, password) {

        // if using axios
        const promise = axios.post('/oauth/token', 'grant_type=password&username='+username+'&password='+password+'&scope=read write'
        ,{
            auth: {
                username: 'client',
                password: 'secret'
            }
        });

        return promise;
    }

    validateToken(token) {

        const promise = axios.post('/oauth/check-token', {
            token
        });

        return promise;
    }

    beforeRequest() {

        const accessToken = jwt_decode(localStorage.getItem('accessToken'))
        console.log(accessToken.exp * 1000)
        console.log(new Date().getTime())
        if (accessToken.exp * 1000 - 1000 > new Date().getTime()) {
          return
        } else {
            console.log('refresh token')
            const promise = axios.post('/oauth/token',
                'grant_type=refresh_token' +
                '&refresh_token=' + localStorage.getItem(refreshToken) +
                '&scope=read write'
                , {
                    auth: {
                        username: 'client',
                        password: 'secret'
                    }
                });
            promise.then((response) => {
                console.log("getted new token")
                localStorage.setItem('accessToken', response.data.access_token)
                localStorage.setItem('refreshToken', response.data.refresh_token)

            })

        }
    }
}