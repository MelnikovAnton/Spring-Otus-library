<template>
    <div class="container-fluid">
        <div class="container-fluid">
            <div id="edit-form">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Title</th>
                        <th scope="col">Authors</th>
                        <th scope="col">Genres</th>
                        <th scope="col">content</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row">{{ bookItem.id }}</th>
                        <td>
                            <input class="form-control" name="title" type="text" v-model="bookItem.title"/>
                        </td>
                        <td>
                            <div class="form-group">
                                <author-list :authors="bookItem.authors"/>
                            </div>
                        </td>
                        <td>
                            <div class="form-group">
                                <genre-list :genres="bookItem.genres"/>
                            </div>
                        </td>
                        <td>
                            <input name="contentPath" type="text" v-model="bookItem.contentPath"/>
                        </td>
                        <td>
                            <router-link :to="{name: 'home', props: {}}" replace
                                         class="d-inline btn btn-primary" role="button">cancel
                            </router-link>
                            <button class="btn btn-primary" @click="save">Save</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

        </div>
        <comment-list :book="bookItem"  v-if="isEdit"></comment-list>

    </div>


</template>

<script>

    import {mapActions, mapState} from 'vuex'
    import CommentList from "components/comments/CommentList.vue";
    import AuthorList from "components/authors/AuthorList.vue";
    import GenreList from "components/genres/GenreList.vue";


    export default {
        components: {AuthorList, CommentList, GenreList},
        // props: ['book','books'],
        methods: {
            ...mapActions(['removeBookAction', 'updateBookAction', 'getBookItem', 'getItemCommentsAction']),
            deleteBook() {
                this.removeBookAction(this.book)
            },
            save() {
                this.updateBookAction(this.bookItem)
            },
            addGenre() {
                console.log(genre)
            }
        },
        data() {
            return {
                isEdit: false,
                isAdd: false
            }
        },
        computed: mapState(['bookItem']),
        created() {
            this.isEdit = this.$route.name === 'edit'
            this.isAdd = this.$route.name === 'addBook'

            var bookId = this.$attrs.id;
            this.bookId=bookId
            this.getBookItem(bookId)

            this.getItemCommentsAction(bookId)
        }

    }

</script>

<style>

</style>