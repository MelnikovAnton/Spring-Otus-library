<template>
    <div class="container-fluid">
        <table class="table table-striped">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Name</th>
            </tr>
            </thead>
            <tbody>
            <author-row v-for="author in aauthors" :author="author" v-bind:key="author.id"></author-row>
            <create-author ></create-author>
            </tbody>
        </table>

    </div>
</template>

<script>
    import {mapState} from 'vuex'
    import AuthorRow from 'components/authors/AuthorRow.vue'
    import CreateAuthor from 'components/authors/CreateAuthor.vue'

    export default {
        components: {
            AuthorRow,
            CreateAuthor
        },
        data() {
            return {
                author: null
            }
        },
        computed: mapState(['aauthors']),
        created() {
            this.$store.dispatch('getAllAuthorsAction')
        }
    }
</script>

<style scoped>

</style>