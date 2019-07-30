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
                Edit
            </router-link>
        </td>
        <td>
            <input type="button" class="d-inline btn btn-danger"
                   role="button" @click="deleteBook" value="delete"/>
        </td>
    </tr>

</template>

<script>
    import AuthorList from 'components/AuthorList.vue'
    import GenreList from 'components/GenreList.vue'

    export default {
        components: {
            AuthorList,
            GenreList
        },
        props: ['book', 'books'],
        methods: {
            deleteBook: function () {
                this.$resource('/bookApi{/id}').remove({id: this.book.id}).then(result => {
                    if (result.ok) {
                        this.books.splice(this.books.indexOf(this.book), 1)
                    }
                })
            }
        }
    }

</script>

<style>

</style>