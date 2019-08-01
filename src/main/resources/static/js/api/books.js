import Vue from 'vue'

const books = Vue.resource('/bookApi{/id}')

export default {
    add: book => books.save({}, book),
    update: book => books.update({id: book.id}, book),
    remove: id => books.remove({id}),
    get: id => books.get({id})
}