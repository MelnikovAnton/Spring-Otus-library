import Vue from 'vue'

const authors = Vue.resource('/authorApi{/id}')

export default {
    add: author => authors.save({}, author),
    update: author => authors.update({id: author.id}, author),
    remove: id => authors.remove({id}),
    get: id => authors.get({id})
}