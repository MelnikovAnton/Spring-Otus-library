import Vue from 'vue';
import Vuex from 'vuex';
import booksApi from 'api/books'
import authorsApi from 'api/authorsApi'

Vue.use(Vuex);


export default new Vuex.Store({
    state: {
        books: [],
        bookItem: {},
        authors: [],
        authorsItem: []
    },
    getters: {
        books: state => state.books
    },
    mutations: {
        getBookItemMutation(state, book) {
            state.bookItem = book;
        },
        setBooks(state, books) {
            state.books=books
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
        }
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
            console.log(data)

            commit('getBookItemMutation', data)

        }

    }


})