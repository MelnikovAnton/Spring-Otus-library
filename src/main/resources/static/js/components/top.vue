<template>
    <div class="container-fluid">
        <div class="row">
            <h2 class="col">{{ $t('title') }}</h2>
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
    export default {
        data() {
            return {locale: 'ru'}
        },
        watch: {
            locale(val) {
                this.$i18n.locale = val
            }
        }
    }
</script>