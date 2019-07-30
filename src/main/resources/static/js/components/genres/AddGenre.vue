<template>
    <div class="row" style="margin-left: 0px">
        <select class="d-inline" v-model="genre">
            <option v-for="agenre in agenres" :value="agenre">{{ agenre.name }}</option>
        </select>
        <a class="d-inline btn btn-primary" @click="addGenre" role="button">Add</a>
    </div>
</template>

<script>

    export default {
        props: ['genres'],
        data() {
            return {
                isEdit: false,
                agenres: [],
                genre: {}
            }
        },
        watch: {
            //TODO Удалять из общего списка тех что уже есть в книге
          genres: function (val) {
             console.log(val)
         //     val.then(g => console.log(g))
          }
        },
        methods: {
            addGenre() {
                this.genres.push(this.genre)
            }
        },
        created() {
            this.$resource('/genreApi/').get().then(result =>
                result.json().then(data =>
                    data.forEach(gen => this.agenres.push(gen))
                )
            )
        }
    }
</script>