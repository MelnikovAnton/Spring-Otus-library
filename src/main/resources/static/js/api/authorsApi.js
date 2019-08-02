import Vue from 'vue'

const authors = Vue.resource('/authorApi{/id}')

export default {
    add: author => books.save({}, author),
    update: author => books.update({id: author.id}, author),
    remove: author => books.remove({author}),
    get: author => books.get({author})
}