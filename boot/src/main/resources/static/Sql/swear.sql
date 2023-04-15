create table if not exists swear
(
    swear_id     bigint           not null
        primary key,
    user_id      bigint           null comment '用户ID',
    swear_time   datetime         null comment '发誓时间',
    swear_status char default '0' null comment '完成状况，0未完成，1已经完成',
    constraint swear_swear_id_uindex
        unique (swear_id)
)
    comment '发誓表';

