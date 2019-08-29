import Vue from 'vue'
import axios from 'axios';

const books = Vue.resource('/books{/id}')

export default {
    add: book => books.save({}, book),
    update: book => books.update({id: book.id}, book),
   remove: id => books.remove({id}),
   get: id => books.get({id})

}