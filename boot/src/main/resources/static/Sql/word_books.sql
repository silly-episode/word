create table if not exists word_books
(
    book_id          bigint        not null
        primary key,
    book_name        varchar(20)   not null,
    user_id          bigint        null,
    book_create_time datetime      null,
    book_update_time datetime      null,
    word_count       int default 0 null,
    constraint wordBooks_book_id_uindex
        unique (book_id)
);

