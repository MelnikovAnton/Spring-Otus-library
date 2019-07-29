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
            <input type="button" class="d-inline btn btn-primary" role="button" @click="editBook(book.id)"
                   value="Edit"/>
        </td>
        <td>
            <input type="button" class="d-inline btn btn-danger"
                   role="button" @click="deleteBook" value="delete"/>
        </td>
        <td>
            <router-link :to="{name: 'edit', params: {id: book.id}}">Edit Book</router-link>
        </td>
        <td>
            <router-view></router-view>
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
            },
            editBook: function (id) {
                document.location.href = '/edit/' + id
            }
        }
    }

</script>

<style>

</style>