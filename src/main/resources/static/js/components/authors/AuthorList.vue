<template>
    <div>
        <ul class="list-group" v-for="author in authors" :author="author" v-bind:key="author.id">
            <li class="list-group-item d-flex">
                <span class="mr-auto">{{ author.name }}</span>
                <div class=" btn-sm btn-danger ml-auto" v-if="isEdit"
                     @click="deleteAuthorFromBook">delete
                </div>
            </li>
        </ul>
        <add-author v-if="isEdit" :authors="authors"></add-author>
    </div>
</template>

<script>
    import AddAuthor from 'components/authors/AddAuthor.vue'

    export default {
        props: ['authors'],
        components: {
            AddAuthor
        },
        data() {
            return {
                isEdit: false
            }
        },
        methods: {
            deleteAuthorFromBook: function () {
                this.authors.splice(this.authors.indexOf(this.author), 1)
            }
        },
        created() {
            this.isEdit = this.$route.name === 'edit'
        }
    }

</script>

<style>

</style>