create table if not exists admin
(
    admin_id        bigint           not null comment '管理员id'
        primary key,
    account         varchar(100)     not null comment '登录账户',
    password        varchar(100)     not null comment '密码',
    tel             varchar(20)      null comment '电话',
    remark          varchar(500)     null comment '描述',
    role            char default '1' null comment '区分管理员和超级管理员，0是超级管理员，1是普通管理员',
    add_create_time datetime         null comment '添加时间',
    keep_name       varchar(200)     null comment '持有人',
    salt            varchar(255)     null comment '盐',
    user_status     char default '0' null comment '用户状态',
    lock_time       datetime         null comment '锁定或解锁时间',
    constraint admin_account_uindex
        unique (account),
    constraint admin_adminId_uindex
        unique (admin_id)
)
    comment '管理员表';

