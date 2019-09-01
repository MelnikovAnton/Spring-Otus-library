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
                            <router-link :to="{name: 'home', props: {}}">{{ $t('action.cancel') }}
                            </router-link>
                            <button class="btn btn-primary" @click="save">{{ $t('action.save') }}</button>
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
        beforeRouteLeave(to, from, next) {
            if (from.name === 'edit' && to.name === 'addBook') this.clearBookItem().then(() => next())
            else next()
        },
        methods: {
            ...mapActions(['removeBookAction', 'updateBookAction', 'getBookItem', 'getItemCommentsAction', 'addBookAction','clearBookItem']),
            ...mapMutations(['setBookItemMutation']),
            deleteBook() {
                this.removeBookAction(this.book)
            },
            save() {
                if (this.isEdit) {
                    this.updateBookAction(this.bookItem).then(() => this.$router.push({name: 'home'}))
                }
                if (this.isAdd) {
                    this.addBookAction(this.bookItem)
                }
            },
            doEdit() {
                const bookId = this.$attrs.id;
                console.log(bookId)
                this.bookId = bookId
                this.getBookItem(bookId).then(book=>console.log(book))
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

            if (this.isAdd) {
                var bookItem = {title: '', authors: [], genres: [], content: ''}
                this.setBookItemMutation(bookItem)
            }
            if (this.isEdit) {
                this.doEdit()
            }
        }

    }

</script>

<style>

</style>