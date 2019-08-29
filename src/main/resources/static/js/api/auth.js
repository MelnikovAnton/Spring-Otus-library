import axios from 'axios';



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
}