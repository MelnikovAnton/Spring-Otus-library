<template>
    <tr>
        <td>{{ genre.id }}</td>
        <td>
            <input class="form-control" name="name" type="text" v-model="genre.name"/>
        </td>
        <td>
            <a class="d-inline btn btn-primary" @click="saveGenre">{{ $t('action.add')}}</a>
        </td>
    </tr>
</template>

<script>

    import {mapActions} from 'vuex'

    export default {
        data() {
            return {
                genre: {}
            }
        },
        methods: {
            ...mapActions(['addGenreAction']),
            saveGenre: function () {
                this.addGenreAction(this.genre)
                this.genre = {}

            }
        }
    }
</script>

<style scoped>

</style>