<template>
    <tr>
        <td>{{ author.id }}</td>
        <td>
            <input class="form-control" name="name" type="text" v-model="author.name"/>
        </td>
        <td>
            <a class="d-inline btn btn-primary" @click="saveAuthor">{{ $t('action.add')}}</a>
        </td>
    </tr>
</template>

<script>

    import {mapActions} from 'vuex'

    export default {
        data() {
            return {
                author: {}
            }
        },
        methods: {
            ...mapActions(['addAuthorAction']),
            saveAuthor: function () {
                this.addAuthorAction(this.author)
                this.author = {}

            }
        }
    }
</script>

<style scoped>

</style>