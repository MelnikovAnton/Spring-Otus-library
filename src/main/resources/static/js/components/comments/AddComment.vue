<template>
    <div class="container">
        <h4>Add comment</h4>
        <div class="row justify-content-center">
            <div class="col">
                <textarea class="form-control" v-bind:key="comment.id" v-model="comment.comment" name="comment" rows="3"></textarea>
                <button class="form-control btn btn-primary" @click="addComment">Save</button>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: ['comments','book'],
        data() {
            return {
                isEdit: false,
                comment: {}
            }
        },
        methods: {
            addComment() {
                this.comment.book=this.book
                this.$resource('/commentApi/').save(this.comment).then(result => {
                    if (result.ok) {
                        this.comments.push(result.data)
                        this.comment = {}
                    }
                })
            }
        }
    }
</script>