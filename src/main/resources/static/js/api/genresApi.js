import Vue from 'vue'

const genres = Vue.resource('/genres{/id}')

export default {
    add: genre => genres.save({}, genre),
    update: genre => genres.update({id: genre.id}, genre),
    remove: id => genres.remove({id}),
    get: id => genres.get({id})
}