import axios from 'axios';



export default class AuthApi {
   // loginEndpoint = '/oauth/token';
   //  validateTokenApi = '/oauth/check-token';

    login(username, password) {
        console.log('user:',username)
        console.log('password:',password)
        // if using vue-resource
        // const promise = http.post('/oauth/token', {
        //   username,
        //   password
        // });

        // if using axios
        const promise = axios.post('/oauth/token', 'grant_type=password&username='+username+'&password='+password+'&scope=read write'
        //     {
        //     username: username,
        //     password: password,
        //     'grant_type': 'password',
        //     scope: 'read write'
        // }
        ,{
            auth: {
                username: 'client',
                password: 'secret'
            }
        });

        return promise;
    }

    validateToken(token) {

        // const promise = this.$http.post(this.validateTokenApi, {
        //   token
        // });

        const promise = axios.post('/oauth/check-token', {
            token
        });

        return promise;
    }
}