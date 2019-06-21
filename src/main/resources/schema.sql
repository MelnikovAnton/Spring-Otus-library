DROP TABLE IF EXISTS genre;
CREATE TABLE genre
(
    ID   INT PRIMARY KEY,
    NAME VARCHAR(255)
);

DROP TABLE IF EXISTS author;
CREATE TABLE author
(
    ID   INT PRIMARY KEY,
    NAME VARCHAR(255)
);

DROP TABLE IF EXISTS book;
CREATE TABLE book
(
    ID           INT PRIMARY KEY,
    TITLE        VARCHAR(255),
    CONTENT_PATH varchar(255)
);

DROP TABLE IF EXISTS author_book;
CREATE TABLE author_book
(
    BOOK_ID   INT NOT NULL,
    AUTHOR_ID INT NOT NULL,
    foreign key (BOOK_ID) references book (ID),
    foreign key (AUTHOR_ID) references author (ID)
);

DROP TABLE IF EXISTS genre_book;
CREATE TABLE genre_book
(
    BOOK_ID   INT NOT NULL,
    GENRE_ID INT NOT NULL,
    foreign key (BOOK_ID) references book (ID),
    foreign key (GENRE_ID) references genre (ID)
);
