<template>
    <div>
        <ul class="list-group" v-for="genre in genres" :genre="genre" v-bind:key="genre.id">
            <li class="list-group-item">{{ genre.name }}</li>
        </ul>
    </div>
</template>

<script>

    export default {
        props: ['genres']
    }

</script>

<style>

</style>