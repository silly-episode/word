create table if not exists community
(
    community_id    bigint       not null,
    version         varchar(50)  not null comment '版本信息',
    wish            varchar(100) null comment '愿景',
    create_time     datetime     null comment '创建时间',
    member_num      int          null comment '会员人数',
    top_online_num  int          null comment '最高在线人数',
    article_num     int          null comment '文章数',
    module_num      int          null comment '单词模块数',
    git_hub_address varchar(200) null comment 'gitHub地址',
    tel             varchar(50)  null comment '联系电话',
    constraint community_communityId_uindex
        unique (community_id)
)
    comment '社区运行状况（redis固化）';

