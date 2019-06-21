insert into book (id, title, content_path)
values (1, 'Test1', 'No Content1'),
       (2, 'Test2', 'No Content2'),
       (3, 'Test3', 'No Content3');

insert into genre (id, name)
values (1, 'Ужасы'),
       (2, 'Фантастика'),
       (3, 'Эротика');

insert into Author (id, name)
values (1, 'Author1'),
       (2, 'Author2'),
       (3, 'Author3');

insert into author_book (book_id, author_id)
values (1, 1),
       (2, 2),
       (3, 3),
       (3, 2);

insert into genre_book (book_id, genre_id)
values (1, 1),
       (2, 2),
       (3, 3),
       (3, 2);



