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
            <genre-row v-for="genre in agenres" :genre="genre" v-bind:key="genre.id"></genre-row>
            <create-genre ></create-genre>
            </tbody>
        </table>

    </div>
</template>

<script>
    import {mapState} from 'vuex'
    import GenreRow from 'components/genres/GenreRow.vue'
    import CreateGenre from 'components/genres/CreateGenre.vue'

    export default {
        components: {
            GenreRow,
            CreateGenre
        },
        data() {
            return {
                genre: null
            }
        },
        computed: mapState(['agenres']),
        created() {
            this.$store.dispatch('getAllGenresAction')
        }
    }
</script>

<style scoped>

</style>