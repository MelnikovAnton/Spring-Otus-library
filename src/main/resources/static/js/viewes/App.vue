<template>
    <div>
        <top v-if="this.$route.name !== 'loginForm'"></top>
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