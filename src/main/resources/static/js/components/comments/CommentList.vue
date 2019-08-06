<template>

    <div class="container-fluid">
        <h4>{{ $t('comments')}}</h4>
        <add-comment :comments="comments"></add-comment>
        <ul class="list-group">
            <comment-row v-for="comment in comments" :comment="comment" :comments="comments"
                         v-bind:key="comment.id"></comment-row>
        </ul>

    </div>
</template>

<script>
    import {mapActions, mapState} from 'vuex'
    import CommentRow from 'components/comments/CommentRow.vue'
    import AddComment from 'components/comments/AddComment.vue'


    export default {
        components: {
            AddComment,
            CommentRow
        },
        methods: {
            ...mapActions(['getItemCommentsAction']),
        },
        computed: {
            ...mapState(['comments']),
        }
    }
</script>

<style>

</style>