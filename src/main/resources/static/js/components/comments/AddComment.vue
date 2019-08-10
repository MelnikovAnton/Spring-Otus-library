<template>
    <div class="container">
        <h4>{{ $t('action.addComment')}}</h4>
        <div class="row justify-content-center">
            <div class="col">
                <textarea class="form-control" v-bind:key="comment.id" v-model="comment.comment" name="comment" rows="3"></textarea>
                <button class="form-control btn btn-primary" @click="addComment">{{ $t('action.save')}}</button>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapActions,mapState} from 'vuex'

    export default {
        data() {
            return {
                isEdit: false,
                comment: {}
            }
        },
        computed: mapState(['bookItem']),
        methods: {
            ...mapActions(['addCommentAction']),
            addComment() {
                var book = JSON.parse(JSON.stringify(this.bookItem))
                this.comment.book = book
                this.addCommentAction(this.comment)
                this.comment = {}
            }
        }
    }
</script>