import Vue from 'vue'

const genres = Vue.resource('/genreApi{/id}')

export default {
    add: genre => genres.save({}, genre),
    update: genre => genres.update({id: genre.id}, genre),
    remove: genre => genres.remove({genre}),
    get: genre => genres.get({genre})
}