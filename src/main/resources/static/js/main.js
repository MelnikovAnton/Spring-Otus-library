const bookApi = Vue.resource('/bookApi{/id}');

Vue.component('author-list', {
    props: ['authors'],
    template: '<div>' +
        '<ul class="list-group" v-for="author in authors" :author="author" v-bind:key="author.id">\n' +
        '                    <li class="list-group-item">{{ author.name }}</li>\n' +
        '                </ul>' +
        '</div>'
});

Vue.component('genre-list', {
    props: ['genres'],
    template: '<div>' +
        '<ul class="list-group" v-for="genre in genres" :genre="genre" v-bind:key="genre.id">\n' +
        '                    <li class="list-group-item">{{ genre.name }}</li>\n' +
        '                </ul>' +
        '</div>'
});

Vue.component('book-row', {
    props: ['book', 'books'],
    template: '<tr>\n' +
        '            <td>{{ book.title }}</td>\n' +
        '            <td>\n' +
        '                <author-list :authors="book.authors"/>\n' +
        '            </td>\n' +
        '            <td>\n' +
        '                <genre-list :genres="book.genres"/>\n' +
        '            </td>\n' +
        '            <td>\n' +
        '                 <input type="button" class="d-inline btn btn-primary" role="button" @click="editBook(book.id)"th:text="#{action.edit}">Edit</input>\n' +
        '            </td>\n' +
        '            <td>\n' +
        '                <input type="button" class="d-inline btn btn-danger"\n' +
        '                   role="button" @click="deleteBook" th:text="#{action.delete}"/>\n' +
        '            </td>\n' +
        '        </tr>',
    methods: {
        deleteBook: function () {
            bookApi.remove({id: this.book.id}).then(result => {
                if (result.ok) {
                    this.books.splice(this.books.indexOf(this.book), 1)
                }
            })
        },
        editBook: function (id) {
            console.log(id);
            document.location.href = '/edit/'+id
        }
    }
});

Vue.component('books-list', {
    props: ['books'],
    template: '<table class="table table-striped">\n' +
        '        <thead>\n' +
        '        <tr>\n' +
        '            <th scope="col" >Title</th>\n' +
        '            <th scope="col">Authors</th>\n' +
        '            <th scope="col">Genres</th>\n' +
        '        </tr>\n' +
        '        </thead>\n' +
        '        <tbody> ' +
        '<book-row v-for="book in books" :book="book" :books="books" v-bind:key="book.id"></book-row>' +
        ' </tbody>\n' +
        '    </table>',
    created: function () {
        bookApi.get().then(result =>
            result.json().then(data =>
                data.forEach(book => this.books.push(book)
                )
            )
        )
    }
});

new Vue({
    el: '#app',
    data: {
        books: []
    },
    template: '<books-list :books="books"/>'
});