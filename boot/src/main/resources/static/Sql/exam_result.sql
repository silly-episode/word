create table if not exists exam_result
(
    result_id bigint   not null
        primary key,
    grade     int      not null comment '取三次考试的平均分数',
    exam_time datetime null comment '考试结果时间',
    plan_id   bigint   null comment '考试计划id',
    user_id   bigint   null comment '用户id',
    constraint exam_result_result_id_uindex
        unique (result_id)
);

