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
        <comment-list :book="bookItem" v-if="isEdit"></comment-list>

    </div>


</template>

<script>

    import {mapActions, mapMutations, mapState} from 'vuex'
    import CommentList from "components/comments/CommentList.vue";
    import AuthorList from "components/authors/AuthorList.vue";
    import GenreList from "components/genres/GenreList.vue";


    export default {
        components: {AuthorList, CommentList, GenreList},
        // props: ['book','books'],
        methods: {
            ...mapActions(['removeBookAction', 'updateBookAction', 'getBookItem', 'getItemCommentsAction', 'addBookAction']),
            ...mapMutations(['getBookItemMutation']),
            deleteBook() {
                this.removeBookAction(this.book)
            },
            save() {
                if (this.isEdit) this.updateBookAction(this.bookItem)
                if (this.isAdd) {
                    this.addBookAction(this.bookItem)
                    console.log(this.bookItem.id)
                }
            },
            doEdit() {
                var bookId = this.$attrs.id;
                this.bookId = bookId
                this.getBookItem(bookId)
                this.getItemCommentsAction(bookId)
            }
        },
        data() {
            return {
                isEdit: false,
                isAdd: false
            }
        },
        watch: {
            bookItem() {
                if (this.bookItem.id) this.$router.push({name: 'edit', params: {id: this.bookItem.id}})
            },
            '$route'(to, from) {
                this.isEdit = this.$route.name === 'edit'
                this.isAdd = this.$route.name === 'addBook'
                if (to.name === 'edit') this.doEdit()
            }
        },
        computed: mapState(['bookItem']),
        created() {
            this.isEdit = this.$route.name === 'edit'
            this.isAdd = this.$route.name === 'addBook'

            console.log(this.isAdd)
            console.log(this.isEdit)
            if (this.isAdd) {
                var bookItem = {title: '', authors: [], genres: [], content: ''}
                this.getBookItemMutation(bookItem)
            }
            if (this.isEdit) {
                this.doEdit()
            }
        },
        beforeRouteUpdate(to, from, next) {
            console.log(to)
            console.log(from)
            next()
        }

    }

</script>

<style>

</style>