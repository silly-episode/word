create table if not exists role
(
    role_id   bigint       not null comment '角色id'
        primary key,
    role_name varchar(100) not null comment '角色名称',
    role_code varchar(50)  null comment '权限编码',
    constraint role_roleId_uindex
        unique (role_id)
)
    comment '角色表';

