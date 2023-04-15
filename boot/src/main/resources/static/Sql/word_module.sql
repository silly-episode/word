create table if not exists word_module
(
    module_id               bigint           not null comment '单词模块id'
        primary key,
    module_name             varchar(50)      not null comment '单词模块名称',
    module_image_path       varchar(100)     not null comment '单词头像路径',
    remark                  varchar(200)     null comment '描述',
    word_path               varchar(100)     not null comment '单词文件路径',
    word_module_create_time datetime         not null comment '创建时间',
    word_module_status      char default '0' not null comment '状态',
    study_number            int  default 0   null comment '在线学习人数',
    superior_module         varchar(100)     null,
    word_count              int  default 0   null comment '单词数量',
    lock_time               datetime         null comment '锁定/解锁时间',
    es_index                varchar(200)     null comment 'ES-索引',
    constraint wordModule_moduleId_uindex
        unique (module_id)
);

