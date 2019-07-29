<template>
    <div>
        <books-list :books="books"/>
    </div>
</template>

<script>

    import BooksList from 'components/BookList.vue'

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