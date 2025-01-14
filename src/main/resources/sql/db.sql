create table javatemplate.admin_user
(
    id          bigint                               not null
        primary key,
    username    varchar(50)                          not null,
    password    varchar(255)                         not null,
    create_time datetime   default CURRENT_TIMESTAMP not null,
    update_time datetime   default CURRENT_TIMESTAMP not null on
        update CURRENT_TIMESTAMP,
    deleted     tinyint(1) default 0                 not null
);

create table javatemplate.user
(
    id                 bigint                                 not null
        primary key,
    nickname           varchar(255) default '飞跃用户'        not null,
    avatar             varchar(255) default 'default.png'     not null,
    open_id            varchar(50)                            not null,
    phone              varchar(20)  default ''                not null,
    email              varchar(255) default ''                not null,
    remark             varchar(60)  default ''                not null comment '备注',
    personal_statement varchar(255) default ''                not null comment '个人简介',
    gender             tinyint      default 0                 not null comment '默认0=无,1=男,2=女',
    name               varchar(50)  default ''                not null comment '真实姓名',
    blocked            tinyint(1)   default false             not null comment '是否被禁用',
    last_login_time    datetime     default CURRENT_TIMESTAMP not null,
    create_time        datetime     default CURRENT_TIMESTAMP not null,
    update_time        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    deleted            tinyint(1)   default 0                 not null,
    constraint open_id
        unique (open_id)
);

INSERT INTO `user` (id, open_id, phone, email, personal_statement)
VALUES (1, 'test', '13522227777', 'test@test.email', 'i am test');