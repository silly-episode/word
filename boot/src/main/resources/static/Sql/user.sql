create table if not exists user
(
    user_id       bigint           not null comment '用户id'
        primary key,
    account       varchar(100)     not null comment '登录账号',
    password      varchar(100)     not null comment '登录密码',
    nick_name     varchar(100)     not null comment '用户昵称',
    tel           varchar(20)      null comment '电话',
    email         varchar(100)     null comment '邮箱',
    qq            varchar(50)      null comment 'qq',
    wechat        varchar(50)      null comment '微信',
    register_time datetime         not null comment '注册时间',
    user_status   char default '0' not null comment '用户状态，0正常，1锁定，2删除',
    head_image    varchar(100)     not null comment '头像地址',
    lock_time     datetime         null comment '锁定/解锁时间',
    remark        varchar(300)     null comment '备注',
    signature     varchar(30)      null comment '个性签名',
    salt          varchar(255)     null comment '随机盐',
    integration   int  default 0   null,
    constraint user_account_uindex
        unique (account),
    constraint user_tel_uindex
        unique (tel),
    constraint user_userId_uindex
        unique (user_id)
)
    comment '用户表';

raint user_userId_uindex
        unique (user_id)
)
    comment '用户表';

