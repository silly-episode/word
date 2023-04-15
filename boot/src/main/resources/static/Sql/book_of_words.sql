create table if not exists book_of_words
(
    word_id          bigint auto_increment
        primary key,
    book_id          bigint       null,
    word             varchar(50)  null comment '单词',
    trans            varchar(200) null comment '解释',
    pos              varchar(100) null comment '词性',
    word_insert_time datetime     null comment '单词插入时间',
    sentence_en      varchar(200) null comment '例句',
    sentence_zh      varchar(200) null comment '例句翻译',
    constraint book_of_words_word_id_uindex
        unique (word_id)
);

