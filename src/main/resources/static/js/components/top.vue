<template>
    <div class="container-fluid">
        <div class="row">
            <h2 class="col">{{ $t('title' , {msg: username } ) }}</h2>
            <div class="btn btn-link" @click="logout">logout</div>
            <div class="col_2">
                <div class="container text-center center-block">
                    <select v-model="locale">
                        <option>en</option>
                        <option>ru</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <router-link :to="{name: 'home'}">{{ $t('action.toHome') }}</router-link>
            </div>
            <div class="col">
                <router-link :to="{name: 'addBook'}">{{ $t('action.addBook') }}</router-link>
            </div>
            <div class="col">
                <router-link :to="{name: 'authorList'}">{{ $t('action.addAuthor') }}</router-link>
            </div>
            <div class="col">
                <router-link :to="{name: 'genreList'}">{{ $t('action.addGenre') }}</router-link>
            </div>
        </div>

    </div>
</template>

<script>

    import jwt_decode from "jwt-decode";

    export default {
        data() {
            return {locale: 'ru'}
        },
        watch: {
            locale(val) {
                this.$i18n.locale = val
            }
        },
        methods: {
            logout() {
                localStorage.removeItem('accessToken')
                localStorage.removeItem('refreshToken')
                this.$router.push('login')
            }
        },
        computed: {
            username() {
                const accessToken = jwt_decode(localStorage.getItem('accessToken'))
                return accessToken.user_name
            }
        }
    }
</script>