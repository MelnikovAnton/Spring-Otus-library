<template>

    <div class="container-fluid">
        <h4>Comments</h4>
        <add-comment :comments="comments" :book="book"></add-comment>
        <ul class="list-group">
            <comment-row v-for="comment in comments" :comment="comment" :comments="comments"
                         v-bind:key="comment.id"></comment-row>
        </ul>

    </div>
</template>

<script>
    import CommentRow from 'components/comments/CommentRow.vue'
    import AddComment from 'components/comments/AddComment.vue'


    export default {
        components: {
            AddComment,
            CommentRow
        },
        props: ['comments','book'],
        data() {
            return {
                comment: null
            }
        }
    }
</script>