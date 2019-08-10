<template>
    <li class="list-group-item d-flex justify-content-between">
        <span>{{ comment.comment }}</span>
        <span class="badge">
                   <div  class="d-inline btn btn-danger"
                        role="button" @click="deleteComment">{{ $t('action.delete')}}</div>
                </span>
    </li>
</template>

<script>
    import {mapActions} from 'vuex'

    export default {
        components: {
        },
        props: ['comment'],
        methods: {
            ...mapActions(['removeCommentAction']),
            deleteComment: function () {
                this.removeCommentAction(this.comment)
            }
        }
    }
</script>