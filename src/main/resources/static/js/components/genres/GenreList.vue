<template>
    <div>
        <ul class="list-group" v-for="genre in genres" :genre="genre" v-bind:key="genre.id">
            <li class="list-group-item d-flex">
                <span class="mr-auto">{{ genre.name }}</span>
                <button class=" btn-sm btn-danger ml-auto" v-if="isEdit || isAdd"
                     @click="deleteGenreFromBook(genre)">{{ $t('action.delete')}}
                </button>
            </li>
        </ul>
        <add-genre v-if="isEdit || isAdd" :genres="genres"></add-genre>
    </div>
</template>

<script>

    import AddGenre from 'components/genres/AddGenre.vue'

    export default {
        props: ['genres'],
        data() {
            return {
                isEdit: false,
                isAdd: false
            }
        },
        components: {
            AddGenre
        },
        methods: {
            deleteGenreFromBook: function (genre) {
                const deletionIndex = this.genres.findIndex(item => item.id === genre.id)
                this.genres.splice(deletionIndex, 1)
            }
        },
        created() {
            this.isEdit = this.$route.name === 'edit'
            this.isAdd = this.$route.name === 'addBook'
        }
    }

</script>

<style>

</style>