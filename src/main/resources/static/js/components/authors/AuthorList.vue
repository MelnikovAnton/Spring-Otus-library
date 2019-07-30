<template>
    <div>
        <ul class="list-group" v-for="author in authors" :author="author" v-bind:key="author.id">
            <li class="list-group-item">
                <span>{{ author.name }}</span>
                <input type="button" class="d-inline btn btn-danger" v-if="isEdit"
                       role="button" @click="deleteAuthorFromBook" value="X"/>
            </li>
        </ul>
    </div>
</template>

<script>

    export default {
        props: ['authors'],
        data() {
            return {
                isEdit: false
            }
        },
        methods: {
            deleteAuthorFromBook: function () {
                this.authors.splice(this.authors.indexOf(this.author), 1)
            }
        },
        created() {
            this.isEdit = this.$route.name === 'edit'
            }
    }

</script>

<style>

</style>