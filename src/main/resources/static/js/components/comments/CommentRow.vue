<template>
    <li class="list-group-item d-flex justify-content-between">
        <span>{{ comment.comment }}</span>
        <span class="badge">
                   <input type="button" class="d-inline btn btn-danger"
                          role="button" @click="deleteComment" value="delete"/>
                </span>
    </li>
</template>

<script>

    export default {
        components: {
        },
        props: ['comment', 'comments'],
        methods: {
            deleteComment: function () {
                this.$resource('/commentApi{/id}').remove({id: this.comment.id}).then(result => {
                    if (result.ok) {
                        this.comments.splice(this.comments.indexOf(this.comment), 1)
                    }
                })
            }
        }
    }
</script>