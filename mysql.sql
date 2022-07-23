use blog;

drop table if exists `user`;
create table `user`
(
    `id`          int primary key     not null auto_increment,
    `username`    varchar(128) unique not null comment '用户名',
    `password`    varchar(128) comment '用户密码',
    `email`       varchar(128) unique comment '邮箱',
    `avatar_url`  varchar(128) comment '头像地址',
    `type`        tinyint(1)          not null comment '用户类型，0-普通用户，1-管理员',
    `deleted`     tinyint(1)          not null default 0 comment '1-已删除，0-未删除',
    `create_time` datetime            not null default current_timestamp comment '创建时间',
    `update_time` datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    key `index_username` (`username`(24)),
    key `index_email` (`email`(24))
) engine = InnoDB
  auto_increment = 10001
  default charset = utf8mb4
  collate = utf8mb4_bin;

drop table if exists `category`;
create table `category`
(
    `id`          int primary key    not null auto_increment,
    `name`        varchar(32) unique not null comment '分类名称',
    `deleted`     tinyint(1)         not null default 0 not null comment '1-已删除，0-未删除',
    `create_time` datetime           not null default current_timestamp comment '创建时间',
    `update_time` datetime           not null default current_timestamp on update current_timestamp comment '更新时间'
) engine = InnoDB
  auto_increment = 1
  default charset = utf8mb4
  collate = utf8mb4_bin;

drop table if exists `article`;
create table `article`
(
    `id`          int primary key not null auto_increment,
    `user_id`     int             not null comment '作者id',
    `category_id` int             not null comment '文章分类id',
    `title`       varchar(128)    not null comment '文章标题',
    `content`     longtext        not null comment '文章内容',
    `deleted`     tinyint(1)      not null default 0 comment '1-已删除，0-未删除',
    `create_time` datetime        not null default current_timestamp comment '创建时间',
    `update_time` datetime        not null default current_timestamp on update current_timestamp comment '更新时间',
    constraint `a_user_id` foreign key (`user_id`) references user (`id`),
    constraint `a_category_id` foreign key (`category_id`) references category (`id`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8mb4
  collate = utf8mb4_bin;

drop table if exists `tag`;
create table `tag`
(
    `id`          int primary key    not null auto_increment,
    `name`        varchar(32) unique not null comment '标签名称',
    `deleted`     tinyint(1)         not null default 0 comment '1-已删除，0-未删除',
    `create_time` datetime           not null default current_timestamp comment '创建时间',
    `update_time` datetime           not null default current_timestamp on update current_timestamp comment '更新时间'
) engine = InnoDB
  auto_increment = 1
  default charset = utf8mb4
  collate = utf8mb4_bin;

drop table if exists `article_tag`;
create table `article_tag`
(
    `article_id` int not null comment '文章id',
    `tag_id`     int not null comment '标签id',
    constraint `at_article_id` foreign key (`article_id`) references article (`id`),
    constraint `at_tag_id` foreign key (`tag_id`) references tag (`id`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8mb4
  collate = utf8mb4_bin;

drop table if exists `talk`;
create table `talk`
(
    `id`          int primary key not null auto_increment,
    `user_id`     int             not null comment '用户id',
    `content`     text            not null comment '说说内容',
    `device`      varchar(32) comment '设备信息',
    `deleted`     tinyint(1)      not null default 0 not null comment '1-已删除，0-未删除',
    `create_time` datetime        not null default current_timestamp comment '创建时间',
    `update_time` datetime        not null default current_timestamp on update current_timestamp comment '更新时间',
    constraint `t_user_id` foreign key (`user_id`) references user (`id`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8mb4
  collate = utf8mb4_bin;

drop table if exists `chat_record`;
create table `chat_record`
(
    `id`          int primary key not null auto_increment,
    `user_id`     int                      default null comment '用户id',
    `username`    varchar(128)             default null comment '用户名',
    `avatar_url`  varchar(128)             default null comment '用户头像',
    `content`     varchar(1024)   not null comment '消息内容',
    `deleted`     tinyint(1)      not null default 0 not null comment '1-已删除，0-未删除',
    `create_time` datetime        not null default current_timestamp comment '创建时间',
    `update_time` datetime        not null default current_timestamp on update current_timestamp comment '更新时间'
) engine = InnoDB
  auto_increment = 1
  default charset = utf8mb4
  collate = utf8mb4_bin;