<template>
    <div class="container-fluid">
        <h4>Comments</h4>
        <ul class="list-group">
            <comment-row v-for="comment in comments" :comment="comment" :comments="comments" v-bind:key="comment.id"></comment-row>
        </ul>

    </div>
</template>

<script>
    import CommentRow from 'components/comments/CommentRow.vue'

    export default {
        components: {
            CommentRow
        },
        props: ['comments'],
        data() {
            return {
                comment: null
            }
        }
    }
</script>