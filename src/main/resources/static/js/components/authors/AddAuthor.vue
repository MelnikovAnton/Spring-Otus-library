<template>
    <div class="row" style="margin-left: 0px">
        <select class="d-inline" v-model="auth">
            <option v-for="aauthor in aauthors" :value="aauthor" v-if="isContains(aauthor)">{{ aauthor.name }}</option>
        </select>
        <button class="d-inline btn btn-primary" @click="addAuthor" role="button">Add</button>
    </div>
</template>

<script>

    export default {
        props: ['authors'],
        data() {
            return {
                isEdit: false,
                aauthors: [],
                auth: {}
            }
        },
        methods: {
            addAuthor() {
                this.authors.push(this.auth)
            },
            isContains(author) {
                var aauth = JSON.parse(JSON.stringify(author))
                var flag = true

                this.authors.forEach(v => {
                    var auth = JSON.parse(JSON.stringify(v))
                    if (auth.id === aauth.id) flag = false
                })
                return flag
            }
        },
        created() {
            this.$resource('/authorApi/').get().then(result =>
                result.json().then(data =>
                    data.forEach(auth => this.aauthors.push(auth)
                    )
                )
            )

        }
    }
</script>