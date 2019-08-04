import Vue from 'vue';
import Vuex from 'vuex';
import booksApi from 'api/books'
import authorsApi from "api/authorsApi"
import genresApi from "api/genresApi"
import commentApi from "../api/commentApi";

Vue.use(Vuex);


export default new Vuex.Store({
    state: {
        books: [],
        bookItem: {},
        comments: [],
        aauthors: [],
        agenres: []
    },
    getters: {
        books: state => state.books,
        // bookItem: state => state.bookItem,
        // bookId: (state,getters) => getters.bookItem
    },
    mutations: {
        getBookItemMutation(state, book) {
            state.bookItem = book;
        },
        setBooks(state, books) {
            state.books = books
        },
        addBookMutation(state, book) {
            state.messages = [
                ...state.messages,
                book
            ]
        },

        updateBookMutation(state, book) {
            const updateIndex = state.books.findIndex(item => item.id === book.id)
            state.books = [
                ...state.books.slice(0, updateIndex),
                book,
                ...state.books.slice(updateIndex + 1)
            ]
        },
        deleteBookMutation(state, book) {
            const deletionIndex = state.books.findIndex(item => item.id === book.id)

            if (deletionIndex > -1) {
                state.books = [
                    ...state.books.slice(0, deletionIndex),
                    ...state.books.slice(deletionIndex + 1)
                ]
            }
        },
        addAllAuthorsMutation(state, authors) {
            state.aauthors = authors
        },
        addAllGenresMutation(state, genres) {
            state.agenres = genres
        },
        addCommentsMutation(state, comments) {
            state.comments = comments
        },
        deleteCommentMutation(state, comment) {
            const deletionIndex = state.comments.findIndex(item => item.id === comment.id)

            if (deletionIndex > -1) {
                state.comments = [
                    ...state.comments.slice(0, deletionIndex),
                    ...state.comments.slice(deletionIndex + 1)
                ]
            }
        },
        addCommentMutation(state, comment) {
            state.comments = [
                ...state.comments,
                comment
            ]
        },
    },
    actions: {
        async getAllBookAction({commit}) {
            const result = await booksApi.get()
            const data = await result.json()
            commit('setBooks', data)
        },
        async addBookAction({commit, state}, book) {
            const result = await booksApi.add(book)
            const data = await result.json()
            const index = state.books.findIndex(item => item.id === data.id)

            if (index > -1) {
                commit('updateBookMutation', data)
            } else {
                commit('addBookMutation', data)
            }
        },
        async updateBookAction({commit}, book) {
            const result = await booksApi.update(book)
            const data = await result.json()
            commit('updateBookMutation', data)
        },
        async removeBookAction({commit}, book) {
            const result = await booksApi.remove(book.id)
            if (result.ok) {
                commit('deleteBookMutation', book)
            }
        },
        async getBookItem({commit}, bookId) {
            const result = await booksApi.get(bookId)
            const data = await result.json()
            commit('getBookItemMutation', data)
        },
        async getAllAuthorsAction({commit}) {
            const result = await authorsApi.get()
            const data = await result.json()
            commit('addAllAuthorsMutation', data)
        },
        async getAllGenresAction({commit}) {
            const result = await genresApi.get()
            const data = await result.json()
            commit('addAllGenresMutation', data)
        },
        async getItemCommentsAction({commit},bookid) {

            console.log(bookid)
            const result = await commentApi.get(bookid)
            const data = await result.json()
            commit('addCommentsMutation', data)
        },
        async removeCommentAction({commit}, comment) {
            const result = await commentApi.remove(comment.id)
            if (result.ok) {
                commit('deleteCommentMutation', comment)
            }
        },
        async addCommentAction({commit}, comment) {
            const result = await commentApi.add(comment)
            if (result.ok) {
                commit('addCommentMutation', comment)
            }
        }
    }
})