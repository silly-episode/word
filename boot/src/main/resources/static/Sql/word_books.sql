create table if not exists word_books
(
    book_id          bigint        not null
        primary key,
    book_name        varchar(20)   not null comment '单词本名称',
    user_id          bigint        null comment '用户Id',
    book_create_time datetime      null comment '单词本创建时间',
    book_update_time datetime      null comment '单词本更新时间',
    word_count       int default 0 null comment '单词本单词数量',
    constraint wordBooks_book_id_uindex
        unique (book_id)
);

