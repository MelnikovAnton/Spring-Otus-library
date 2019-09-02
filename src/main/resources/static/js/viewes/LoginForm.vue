<template>
    <div class="container">
        <div class="d-flex justify-content-center h-100">
            <div class="card">
                <div class="card-header">
                    <h3>Вход</h3>
<!--                    <div class="d-flex justify-content-end social_icon">-->
<!--                        <span><i class="fab fa-facebook-square"></i></span>-->
<!--                        <span><i class="fab fa-google-plus-square"></i></span>-->
<!--                        <span><i class="fab fa-twitter-square"></i></span>-->
<!--                    </div>-->
                </div>
                <div class="card-body">
                    <div>
                        <div class="input-group form-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text"><i class="fas fa-user"></i></span>
                            </div>
                            <input type="text" class="form-control" id="username"  name="username"
                                   v-model="credentials.username"
                                   placeholder="username">

                        </div>
                        <div class="input-group form-group">
                            <div class="input-group-prepend">
                                <span class="input-group-text"><i class="fas fa-key"></i></span>
                            </div>
                            <input type="password" id="password" name="password" class="form-control"
                                   v-model="credentials.password"
                                   placeholder="Пароль">
                        </div>
                        <div class="form-group">
                            <input type="submit" value="Войти" class="btn float-right login_btn" @click="login">
                        </div>
                    </div>
                </div>
<!--                <div class="card-footer">-->
<!--                    <div class="d-flex justify-content-center links">-->
<!--                        Don't have an account?<a href="#">Sign Up</a>-->
<!--                    </div>-->
<!--                    <div class="d-flex justify-content-center">-->
<!--                        <a href="#">Forgot your password?</a>-->
<!--                    </div>-->
<!--                </div>-->
            </div>
        </div>
    </div>
</template>

<script>

    import AuthApi from 'api/authApi.js';

    import jwt_decode from 'jwt-decode';

    export default {
        name: "loginForm",
        created() {
            this.authApi = new AuthApi()
        },
        data() {
            return {
                credentials: {
                    username: null,
                    password: null
                }
            }
        },
        methods: {
            login() {
                console.log('will submit', this.credentials);
                const promise = this.authApi.login(this.credentials.username, this.credentials.password);
                promise.then((response) => {
                    // success callback
                    localStorage.setItem('accessToken',response.data.access_token)
                    localStorage.setItem('refreshToken',response.data.refresh_token)
                    this.$router.push('/')

                    const accessToken=jwt_decode(response.data.access_token)
                    const refreshToken=jwt_decode(response.data.refresh_token)

                    console.log(accessToken)
                    console.log(refreshToken)

                    let date= new Date(accessToken.exp*1000)


                    console.log('expared: ',date)
                    console.log('current: ',new Date())

                }, response => {
                    // error callback
                    console.log('error', response);
                });
            }
        }
    }
</script>

<style scoped>

    .social_icon{
        position: absolute;
        right: 20px;
        top: -45px;
    }

    .input-group-prepend span{
        width: 50px;
        background-color: #FFC312;
        color: black;
        border:0 !important;
    }

    input:focus{
        outline: 0 0 0 0  !important;
        box-shadow: 0 0 0 0 !important;

    }

    .remember{
        color: white;
    }

    .remember input
    {
        width: 20px;
        height: 20px;
        margin-left: 15px;
        margin-right: 5px;
    }

    .login_btn{
        color: black;
        background-color: #FFC312;
        width: 100px;
    }

    .login_btn:hover{
        color: black;
        background-color: white;
    }

    .links{
        color: white;
    }

    .links a{
        margin-left: 4px;
    }
</style>