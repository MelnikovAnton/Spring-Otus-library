<template>
    <tr>
        <td>{{ genre.id }}</td>
        <td>
            <input class="form-control" name="name" type="text" v-model="genre.name"/>
        </td>
        <td>
            <a class="d-inline btn btn-primary" @click="saveGenre">{{ $t('action.save')}}</a>
        </td>
        <td>
            <a class="d-inline btn btn-danger"
               role="button" @click="deleteGenre">{{ $t('action.delete')}}</a>
        </td>
    </tr>
</template>

<script>

    import {mapActions} from 'vuex'

    export default {
        props: ['genre'],
        methods: {
            ...mapActions(['removeGenreAction', 'updateGenreAction']),
            deleteGenre: function () {
                this.removeGenreAction(this.genre)
            },
            saveGenre: function () {
                this.updateGenreAction(this.genre)
            }
        }
    }
</script>

<style scoped>

</style>