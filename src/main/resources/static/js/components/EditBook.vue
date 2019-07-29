<template>
    <div class="container-fluid">
        <div class="container-fluid">
            <form id="edit-form">
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
                        <th scope="row">{{ book.id }}</th>
                        <td>
                            <input class="form-control" name="title" type="text" v-model="book.title"/>
                        </td>
                        <td>
                            <div class="form-group">
                                <div class="row" style="margin-left: 0px">
                                    <select class="col form-control" multiple id="authorSelect">
                                        <option> Author1</option>
                                    </select>
                                </div>
                                <div class="row" style="margin-left: 0px">

                                    <select class="d-inline" id="addAuthorSelect">
                                        <option>Author1</option>
                                    </select>
                                    <!--                                    <a class="d-inline btn btn-primary" onclick="addAuthor()" role="button">Add</a>-->
                                </div>
                            </div>
                        </td>
                        <td>
                            <div class="form-group">
                                <div class="row" style="margin-left: 0px">
                                    <select class="col form-control" multiple id="genreSelect">
                                        <option>Genre1</option>
                                    </select>
                                </div>
                                <div class="row" style="margin-left: 0px">

                                    <select class="d-inline" id="addGenreSelect">
                                        <option>Genre</option>
                                    </select>
                                    <!--                                    <a class="d-inline btn btn-primary" onclick="addGenre()" role="button">Add</a>-->


                                </div>
                            </div>
                        </td>
                        <td>
                            <input name="contentPath" type="text" v-model="book.contentPath"/>
                        </td>
                        <td>
                            <router-link :to="{name: 'home'}"
                                         class="d-inline btn btn-primary" role="button">cancel</router-link>
                                <button class="btn btn-primary" @click="save">Save</button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>

        </div>

    </div>
</template>

<script>


    export default {
        components: {},
        // props: ['book','books'],
        methods: {
            deleteBook: function () {
                this.$resource('/bookApi{/id}').remove({id: this.book.id}).then(result => {
                    if (result.ok) {
                        this.books.splice(this.books.indexOf(this.book), 1)
                    }
                })
            },
            save() {
                this.$resource('/bookApi{/id}').update({id: this.book.id},this.book).then(result => {
                    console.log(result.json())
                })
            }
        },
        data() {
            return {
                book: {}
            }
        },
        created() {
            var book1 = {};
            this.$resource('/bookApi/' + this.$attrs.id).get()
                .then(result => result.json())
                .then(b => this.book = b)
        }
    }

</script>

<style>

</style>