<template>
    <div class="container-fluid">
        <form id="edit-form">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Title</th>
                    <th scope="col">Authors</th>
                    <th scope="col">Genres</th>
                    <th scope="col">content</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">ID</th>
                    <td>
                        <input class="form-control" name="title" type="text" value="title1"/>
                    </td>
                    <td>
                        <div class="form-group">
                            <div class="row" style="margin-left: 0px">
                                <select class="col form-control" multiple id="authorSelect">
                                    <option>Author1</option>
                                </select>
                            </div>
                            <div class="row" style="margin-left: 0px">

                                <select class="d-inline" id="addAuthorSelect">
                                    <option>Author1</option>
                                </select>
                                <a class="d-inline btn btn-primary" onclick="addAuthor()" role="button">Add</a>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="form-group">
                            <div class="row" style="margin-left: 0px">
                                <select class="col form-control" multiple id="genreSelect">
                                    <option>Genre1
                                    </option>
                                </select>
                            </div>
                            <div class="row" style="margin-left: 0px">

                                <select class="d-inline" id="addGenreSelect">
                                    <option>Genre</option>
                                </select>
                                <a class="d-inline btn btn-primary" onclick="addGenre()" role="button">Add</a>


                            </div>
                        </div>
                    </td>
                    <td>
                        <input name="contentPath" type="text" value="ContentPath1"/>
                    </td>
                    <td>
                        <a href="@{'/edit/'+ ${book.id}}">cancel</a>
                        <button class="btn btn-primary" type="submit">Save</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>

    </div>
</template>

<script>

</script>

<style>

</style>