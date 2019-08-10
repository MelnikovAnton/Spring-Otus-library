<template>
    <div>

        <ul class="list-group" v-for="author in authors" :author="author" v-bind:key="author.id">
            <li class="list-group-item d-flex">

                <span class="mr-auto">{{ author.name }}</span>
                <button role="button" class=" btn-sm btn-danger ml-auto" v-if="isEdit || isAdd"
                        @click="deleteAuthorFromBook(author)">{{ $t('action.delete')}}
                </button>

            </li>
        </ul>

        <add-author v-if="isEdit || isAdd" :authors="authors"></add-author>
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
                isEdit: false,
                isAdd: false
            }
        },
        methods: {
            deleteAuthorFromBook: function (author) {
                const deletionIndex = this.authors.findIndex(item => item.id === author.id)
                this.authors.splice(deletionIndex, 1)
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