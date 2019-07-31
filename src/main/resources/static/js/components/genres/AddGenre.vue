<template>
    <div class="row" style="margin-left: 0px">
        <select class="d-inline" v-model="genre">
            <option v-for="agenre in agenres" :value="agenre" v-if="isContains(agenre)">{{ agenre.name }}</option>
        </select>
        <button class="d-inline btn btn-primary" @click="addGenre" role="button">Add</button>
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
        methods: {
            addGenre() {
                this.genres.push(this.genre)
            },
            isContains(agenre) {
                var agen = JSON.parse(JSON.stringify(agenre))
                var flag = true

                this.genres.forEach(v => {
                    var gen = JSON.parse(JSON.stringify(v))
                    if (gen.id === agen.id) flag = false
                })
                return flag
            }
        },
        created() {
            this.$resource('/genreApi/').get().then(result =>
                result.json().then(data =>
                    data.forEach(gen =>
                        this.agenres.push(gen)
                    )
                )
            )
        }
    }
</script>