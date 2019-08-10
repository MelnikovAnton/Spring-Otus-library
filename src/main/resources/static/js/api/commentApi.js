import Vue from 'vue'

const comments = Vue.resource('/comments{/id}')

export default {
    add: comment => comments.save({}, comment),
    update: comment => comments.update({id: comment.id}, comment),
    remove: id => comments.remove({id}),
    get: id => comments.get({id})
}