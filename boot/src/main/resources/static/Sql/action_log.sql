create table if not exists action_log
(
    action_id   bigint       not null
        primary key,
    action_time datetime     null comment '操作时间',
    admin_id    bigint       null comment '持有人ID',
    keep_name   varchar(100) null comment '持有人姓名',
    role        char         null comment '角色',
    action_kind varchar(50)  null comment '操作类型',
    remark      varchar(200) null comment '结果描述',
    constraint table_name_action_id_uindex
        unique (action_id)
);

