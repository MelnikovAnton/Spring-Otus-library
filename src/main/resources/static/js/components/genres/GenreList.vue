<template>
    <div>
        <ul class="list-group" v-for="genre in genres" :genre="genre" v-bind:key="genre.id">
            <li class="list-group-item d-flex">
                <span class="mr-auto">{{ genre.name }}</span>
                <button class=" btn-sm btn-danger ml-auto" v-if="isEdit"
                     @click="deleteGenreFromBook">delete
                </button>
            </li>
        </ul>
        <add-genre v-if="isEdit" :genres="genres"></add-genre>
    </div>
</template>

<script>

    import AddGenre from 'components/genres/AddGenre.vue'

    export default {
        props: ['genres'],
        data() {
            return {
                isEdit: false
            }
        },
        components: {
            AddGenre
        },
        methods: {
            deleteGenreFromBook: function () {
                this.genres.splice(this.genres.indexOf(this.genre), 1)
            }
        },
        created() {
            this.isEdit = this.$route.name === 'edit'
        }
    }

</script>

<style>

</style>