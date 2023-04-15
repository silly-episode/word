create table if not exists plan
(
    plan_id          bigint           not null comment '计划id '
        primary key,
    user_id          bigint           not null comment '用户id',
    plan_create_time datetime         not null comment '创建时间',
    plan_status      char default '0' null comment '状态，0创建的计划,1正在完成的计划，2已经完成的计划',
    all_word         int              null comment '单词总数',
    module_id        bigint           not null comment '模块id',
    finished_word    int  default 0   null comment '已完成单词数量',
    day_word         int  default 1   not null comment '计划每天完成单词数量',
    plan_name        varchar(50)      null comment '计划名称',
    constraint plan_plan_id_uindex
        unique (plan_id)
)
    comment '学习计划';

