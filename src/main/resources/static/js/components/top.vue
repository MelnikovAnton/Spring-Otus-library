<template>
    <div class="container-fluid">
        <div class="row">
            <h2 class="col">Book List</h2>
            <div class="col_2">
                <div class="container text-center center-block">
                    <a href="@{/(lang=ru_RU)}">RU</a>
                    <a href="@{/(lang=en_US)}">EN</a>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col">
                <router-link :to="{name: 'home'}">To Home</router-link>
            </div>
            <div class="col">
                <router-link :to="{name: 'addBook'}">Add book</router-link>
            </div>
            <div class="col">
                <router-link :to="{name: 'authorList'}">Author List</router-link>
            </div>
            <div class="col">
                <router-link :to="{name: 'genreList'}">Genre List</router-link>
            </div>
        </div>

    </div>
</template>

<script>

</script>