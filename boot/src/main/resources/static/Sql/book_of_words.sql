create table if not exists book_of_words
(
    word_id          bigint auto_increment
        primary key,
    book_id          bigint       null,
    word             varchar(50)  null,
    meaning          varchar(200) null comment '词性：词意；词性：词意',
    ukphone          varchar(100) null comment '音标',
    word_insert_time datetime     null,
    constraint book_of_words_word_id_uindex
        unique (word_id)
);

