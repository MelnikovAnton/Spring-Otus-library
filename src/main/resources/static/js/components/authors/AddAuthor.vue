<template>
    <div class="row" style="margin-left: 0px">
        <select class="d-inline" v-model="auth">
            <option v-for="aauthor in aauthors" :value="aauthor">{{ aauthor.name }}</option>
        </select>
        <a class="d-inline btn btn-primary" @click="addAuthor" role="button">Add</a>
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