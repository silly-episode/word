create table if not exists emotion_words
(
    emo_id          bigint                  not null comment '主键'
        primary key,
    eng_content     varchar(1000)           null comment '英语内容',
    cn_content      varchar(1000)           null comment '中文内容',
    order_id        int comment '排序Id',
    emo_create_time datetime                null comment '录入时间',
    emo_author      varchar(200)            null comment '作者',
    frequency       varchar(20) default '中' null comment '出现频率'
)
    comment '激励语';

create index emotion_words_order_id_index
    on emotion_words (order_id);

alter table emotion_words
    modify order_id int auto_increment comment '排序Id';

