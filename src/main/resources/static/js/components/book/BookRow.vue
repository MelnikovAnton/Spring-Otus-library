<template>
    <tr>
        <td>{{ book.title }}</td>
        <td>
            <author-list :authors="book.authors"/>
        </td>
        <td>
            <genre-list :genres="book.genres"/>
        </td>
        <td>
            <router-link :to="{name: 'edit', params: {id: book.id}, props: true}" v-bind:book="book" class="d-inline btn btn-primary" role="button">
                {{ $t('action.edit')}}
            </router-link>
        </td>
        <td>
            <div  class="d-inline btn btn-danger"
                 role="button" @click="deleteBook">{{ $t('action.delete')}}</div>
        </td>
    </tr>

</template>

<script>
    import AuthorList from 'components/authors/AuthorList.vue'
    import GenreList from 'components/genres/GenreList.vue'
    import { mapActions } from 'vuex'

    export default {
        components: {
            AuthorList,
            GenreList
        },
        props: ['book'],
        methods: {
            ...mapActions(['removeBookAction']),
            deleteBook: function () {
                this.removeBookAction(this.book)
            }
        }
    }

</script>

<style>

</style>