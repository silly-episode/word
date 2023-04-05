create table if not exists login_log
(
    log_id      bigint       not null
        primary key,
    login_time  datetime     null,
    account     varchar(100) null comment '登录账户',
    ip          varchar(30)  null,
    user_id     bigint       not null comment '用户id',
    result      char         null comment '0成功，1用户不存在，2验证码过期或不存在，3验证码错误，4密码错误，5账户已锁定',
    nick_name   varchar(100) null comment '用户名',
    tel         varchar(20)  null comment '电话',
    log_remark  varchar(300) null comment '结果的描述',
    user_status char         null comment '用户状态',
    login_type  varchar(20)  null comment '登录方式：账号登录，短信登录',
    constraint loginLog_logId_uindex
        unique (log_id)
);

