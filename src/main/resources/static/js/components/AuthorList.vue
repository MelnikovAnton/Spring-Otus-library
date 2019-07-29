<template>
    <div>
        <ul class="list-group" v-for="author in authors" :author="author" v-bind:key="author.id">
            <li class="list-group-item">{{ author.name }}</li>
        </ul>
    </div>
</template>

<script>

    export default {
        props: ['authors']
    }

</script>

<style>

</style>