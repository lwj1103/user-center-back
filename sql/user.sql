-- auto-generated definition
create table user
(
    id          bigint auto_increment comment '用户id'
        primary key,
    username    varchar(256)                       not null comment '用户账号',
    password    varchar(256)                       not null comment '用户密码',
    nick_name   varchar(256)                       null comment '用户昵称',
    avatar_url  varchar(1024)                      null comment '用户头像',
    gender      tinyint                            null comment '性别',
    phone       varchar(128)                       null comment '电话',
    email       varchar(512)                       null comment '邮箱',
    user_states int      default 0                 null comment '状态 0-正常',
    create_time datetime default CURRENT_TIMESTAMP null comment '注册时间',
    update_time datetime default CURRENT_TIMESTAMP null comment '修改时间',
    deleted     tinyint  default 0                 null comment '逻辑删除，1删除',
    version     int      default 1                 null comment '版本号，实现乐观锁',
    user_row    int      default 0                 null comment '用户角色 0 普通成员'
);

