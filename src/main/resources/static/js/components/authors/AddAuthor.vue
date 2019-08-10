<template>
    <div class="row" style="margin-left: 0px">
        <select class="d-inline" v-model="auth">
            <option v-for="aauthor in aauthors" :value="aauthor" v-if="isContains(aauthor)">{{ aauthor.name }}</option>
        </select>
        <button class="d-inline btn btn-primary" @click="addAuthor" role="button">{{ $t('action.add')}}</button>
    </div>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        props: ['authors'],
        data() {
            return {
                isEdit: false,
                auth: {}
            }
        },
        computed: mapState(['aauthors']),
        methods: {
            addAuthor() {
                this.authors.push(this.auth)
            },
            isContains(author) {
                var aauth = JSON.parse(JSON.stringify(author))
                var flag = true
                if (this.authors === undefined) this.authors = []
                this.authors.forEach(v => {
                    var auth = JSON.parse(JSON.stringify(v))
                    if (auth.id === aauth.id) flag = false
                })
                return flag
            }
        },
        created() {
            this.$store.dispatch('getAllAuthorsAction')

        }
    }
</script>