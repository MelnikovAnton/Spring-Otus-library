<template>
    <div>
        <ul class="list-group" v-for="genre in genres" :genre="genre" v-bind:key="genre.id">
            <li class="list-group-item">
                <span>{{ genre.name }}</span>
                <input type="button" class="d-inline btn btn-danger" v-if="isEdit"
                       role="button" @click="deleteGenreFromBook" value="X"/>
            </li>
        </ul>
    </div>
</template>

<script>

    export default {
        props: ['genres'],
        data() {
            return {
                isEdit: false
            }
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