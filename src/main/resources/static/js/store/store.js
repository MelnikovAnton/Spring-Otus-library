import Vue from 'vue';
import Vuex from 'vuex';
import booksApi from 'api/books'
import authorsApi from "api/authorsApi"
import genresApi from "api/genresApi"
import commentApi from "api/commentApi"
import router from "router/router";

Vue.use(Vuex);


const store = new Vuex.Store({
    state: {
        books: [],
        bookItem: {},
        comments: [],
        aauthors: [],
        agenres: [],
        userAuth: {}
    },
    getters: {
        books: state => state.books,
        authors: state => state.bookItem.author
    },
    mutations: {
        setBookItemMutation(state, book) {
            state.bookItem = book;
        },
        setBooks(state, books) {
            state.books = books
        },
        addBookMutation(state, book) {
            state.books = [
                ...state.books,
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
        deleteAuthorMutation(state, author) {
            const deletionIndex = state.aauthors.findIndex(item => item.id === author.id)
            if (deletionIndex > -1) {
                state.aauthors = [
                    ...state.aauthors.slice(0, deletionIndex),
                    ...state.aauthors.slice(deletionIndex + 1)
                ]
            }
        },
        updateAuthorsMutation(state, author) {
            const updateIndex = state.aauthors.findIndex(item => item.id === author.id)
            state.aauthors = [
                ...state.aauthors.slice(0, updateIndex),
                author,
                ...state.aauthors.slice(updateIndex + 1)
            ]
        },
        addAuthorMutation(state, author) {
            state.aauthors = [
                ...state.aauthors,
                author
            ]
        },
        deleteGenreMutation(state, genre) {
            const deletionIndex = state.agenres.findIndex(item => item.id === genre.id)

            if (deletionIndex > -1) {
                state.agenres = [
                    ...state.agenres.slice(0, deletionIndex),
                    ...state.agenres.slice(deletionIndex + 1)
                ]
            }
        },
        updateGenresMutation(state, genre) {
            const updateIndex = state.agenres.findIndex(item => item.id === genre.id)
            state.agenres = [
                ...state.agenres.slice(0, updateIndex),
                genre,
                ...state.agenres.slice(updateIndex + 1)
            ]
        },
        addGenreMutation(state, genre) {
            state.agenres = [
                ...state.agenres,
                genre
            ]
        }
    },
    actions: {
        async getAllBookAction({commit}) {
            try {
                const result = await booksApi.get()
                const data = await result.json()
                commit('setBooks', result.data)
            } catch (e) {
                router.push('/login')
            }
        },
        async addBookAction({commit, state}, book) {
            try {
                const result = await booksApi.add(book)
                const data = await result.json()
                const index = state.books.findIndex(item => item.id === data.id)

                if (index > -1) {
                    commit('updateBookMutation', data)
                } else {
                    commit('addBookMutation', data)
                    commit('setBookItemMutation', data)
                }
            } catch (e) {
                router.push('/login')
            }
        },
        async updateBookAction({commit}, book) {
            try {
                const result = await booksApi.update(book)
                const data = await result.json()
                commit('updateBookMutation', data)
            } catch (e) {
                router.push('/login')
            }
        },
        async removeBookAction({commit}, book) {
            try {
                const result = await booksApi.remove(book.id)
                if (result.ok) {
                    commit('deleteBookMutation', book)
                }
            } catch (e) {
                router.push('/login')
            }
        },
        async getBookItem({commit}, bookId) {
            try {
                const result = await booksApi.get(bookId)
                const data = await result.json()
                console.log(data)
                commit('setBookItemMutation', data)
            } catch (e) {
                router.push('/login')
            }
        },
        async getAllAuthorsAction({commit}) {
            try {
                const result = await authorsApi.get()
                const data = await result.json()
                commit('addAllAuthorsMutation', data)
            } catch (e) {
                router.push('/login')
            }
        },
        async getAllGenresAction({commit}) {
            try {
                const result = await genresApi.get()
                const data = await result.json()
                commit('addAllGenresMutation', data)
            } catch (e) {
                router.push('/login')
            }
        },
        async getItemCommentsAction({commit}, bookid) {
            try {
                const result = await commentApi.get(bookid)
                const data = await result.json()
                commit('addCommentsMutation', data)
            } catch (e) {
                router.push('/login')
            }
        },
        async removeCommentAction({commit}, comment) {
            try {
                const result = await commentApi.remove(comment.id)
                if (result.ok) {
                    commit('deleteCommentMutation', comment)
                }
            } catch (e) {
                router.push('/login')
            }
        },
        async addCommentAction({commit}, comment) {
            try {
                const result = await commentApi.add(comment)
                if (result.ok) {
                    const data = await result.json()
                    commit('addCommentMutation', data)
                }
            } catch (e) {
                router.push('/login')
            }
        },
        async removeAuthorAction({commit}, author) {
            try {
                const result = await authorsApi.remove(author.id)
                if (result.ok) {
                    commit('deleteAuthorMutation', author)
                }
            } catch (e) {
                router.push('/login')
            }
        },
        async updateAuthorAction({commit}, author) {
            try {
                const result = await authorsApi.update(author)
                const data = await result.json()
                commit('updateAuthorsMutation', data)
            } catch (e) {
                router.push('/login')
            }
        },
        async addAuthorAction({commit}, author) {
            try {
                const result = await authorsApi.add(author)
                if (result.ok) {
                    const data = await result.json()
                    commit('addAuthorMutation', data)
                }
            } catch (e) {
                router.push('/login')
            }
        },

        async removeGenreAction({commit}, genre) {
            try {
                const result = await genresApi.remove(genre.id)
                if (result.ok) {
                    commit('deleteGenreMutation', genre)
                }
            } catch (e) {
                router.push('/login')
            }
        },
        async updateGenreAction({commit}, genre) {
            try {
                const result = await genresApi.update(genre)
                const data = await result.json()
                commit('updateGenresMutation', data)
            } catch (e) {
                router.push('/login')
            }
        },
        async addGenreAction({commit}, genre) {
            try {
                const result = await genresApi.add(genre)
                if (result.ok) {
                    const data = await result.json()
                    commit('addGenreMutation', data)
                }
            } catch (e) {
                router.push('/login')
            }
        },
        async clearBookItem({commit}) {
            commit('setBookItemMutation', {authors: [], genres: []})
        }
    }
})

export default store