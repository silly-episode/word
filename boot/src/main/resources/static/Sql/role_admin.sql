create table if not exists role_admin
(
    role_id  bigint not null,
    admin_id bigint not null,
    primary key (admin_id, role_id),
    constraint RoleAdmin_admin_adminId_fk
        foreign key (admin_id) references admin (admin_id),
    constraint RoleAdmin_role_roleId_fk
        foreign key (role_id) references role (role_id)
)
    comment '角色管理员表';

