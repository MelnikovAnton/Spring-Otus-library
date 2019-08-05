<template>
    <tr>
        <td>{{ author.id }}</td>
        <td>
            <input class="form-control" name="name" type="text" v-model="author.name"/>
        </td>
        <td>
            <a class="d-inline btn btn-primary" @click="saveAuthor">Save</a>
        </td>
        <td>
            <a class="d-inline btn btn-danger"
               role="button" @click="deleteAuthor">delete</a>
        </td>
    </tr>
</template>

<script>

    import {mapActions} from 'vuex'

    export default {
        props: ['author'],
        methods: {
            ...mapActions(['removeAuthorAction', 'updateAuthorAction']),
            deleteAuthor: function () {
                this.removeAuthorAction(this.author)
            },
            saveAuthor: function () {
                this.updateAuthorAction(this.author)
            }
        }
    }
</script>

<style scoped>

</style>