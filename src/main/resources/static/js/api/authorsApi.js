import Vue from 'vue'

const authors = Vue.resource('/authorApi{/id}')

export default {
    add: author => authors.save({}, author),
    update: author => authors.update({id: author.id}, author),
    remove: author => authors.remove({author}),
    get: author => authors.get({author})
}