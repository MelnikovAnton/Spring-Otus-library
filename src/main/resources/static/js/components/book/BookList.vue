<template>
    <table class="table table-striped">
        <thead>
        <tr>
            <th scope="col">{{ $t('book.title') }}</th>
            <th scope="col">{{ $t('book.author') }}</th>
            <th scope="col">{{ $t('book.genre') }}</th>
        </tr>
        </thead>
        <tbody>
        <book-row v-for="book in books" :book="book"  v-bind:key="book.id"></book-row>
        </tbody>
    </table>
</template>

<script>
    import { mapState } from 'vuex'
    import BookRow from 'components/book/BookRow.vue'

    export default {
        components: {
            BookRow
        },
        data() {
            return {
                book: null
            }
        },
        computed: mapState(['books']),
        created() {
           this.$store.dispatch('getAllBookAction')
        }

    }
</script>

<style>

</style>