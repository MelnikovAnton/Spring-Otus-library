<template>
    <div>
        <top></top>
        <router-view></router-view>
    </div>
</template>

<script>
    import Top from 'components/top.vue'

    export default {
        components: {
            Top
        }
    }
</script>

<style>

</style>