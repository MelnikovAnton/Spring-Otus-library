<template>
    <table class="table table-striped">
        <thead>
        <tr>
            <th scope="col">Title</th>
            <th scope="col">Authors</th>
            <th scope="col">Genres</th>
        </tr>
        </thead>
        <tbody>
        <book-row v-for="book in books" :book="book" :books="books" v-bind:key="book.id"></book-row>
        </tbody>
    </table>
</template>

<script>

    import BookRow from 'components/BookRow.vue'

    export default {
        components: {
            BookRow
        },
        props: ['books'],
        data() {
            return {
                book: null
            }
        },
        created() {
            this.$resource('/bookApi{/id}').get().then(result =>
                result.json().then(data =>
                    data.forEach(book => this.books.push(book)
                    )
                )
            )
        }
    }
</script>

<style>

</style>