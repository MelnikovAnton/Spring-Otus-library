import Vue from 'vue';
import Vuex from 'vuex';
import booksApi from 'api/books'

Vue.use(Vuex);


export default new Vuex.Store({
    state: {
        books: [],
        bookItem: {}
    },
    getters: {
        books: state => state.books
    },
    mutations: {
        getBookItemMutation(state, book) {
            state.bookItem = book;
        },
        addBooksMutation(state, books) {
            books.forEach(b => state.books.push(b))
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
                message,
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
            commit('addBooksMutation', data)
            return data
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