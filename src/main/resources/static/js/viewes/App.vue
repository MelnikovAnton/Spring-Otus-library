<template>
    <div>
        <books-list :books="books"/>
    </div>
</template>

<script>

    import BooksList from 'components/book/BookList.vue'

    export default {
        components: {
            BooksList
        },
        data() {
            return {
                books: []
            }
        }
    }
</script>

<style>

</style>