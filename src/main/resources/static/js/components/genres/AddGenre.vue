<template>
    <div class="row" style="margin-left: 0px">
        <select class="d-inline" v-model="genre">
            <option v-for="agenre in agenres" :value="agenre" v-if="isContains(agenre)">{{ agenre.name }}</option>
        </select>
        <button class="d-inline btn btn-primary" @click="addGenre" role="button">{{ $t('action.add')}}</button>
    </div>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        props: ['genres'],
        data() {
            return {
                isEdit: false,
                genre: {}
            }
        },
        computed: mapState(['agenres']),
        methods: {
            addGenre() {
                this.genres.push(this.genre)
            },
            isContains(agenre) {
                var agen = JSON.parse(JSON.stringify(agenre))
                var flag = true

                this.genres.forEach(v => {
                    var gen = JSON.parse(JSON.stringify(v))
                    if (gen.id === agen.id) flag = false
                })
                return flag
            }
        },
        created() {
            this.$store.dispatch('getAllGenresAction')
        }
    }
</script>