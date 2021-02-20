create table admin
(
    id          int          not null
        primary key,
    username    varchar(50)  null,
    password    varchar(255) null,
    icon        varchar(255) null,
    email       varchar(255) null,
    nick_name   varchar(255) null,
    create_time date         null,
    login_time  date         null,
    status      int          null
);

create table changelog
(
    build_number int           not null
        primary key,
    version      varchar(50)   null,
    description  varchar(2000) null
);

create table changelog_v2
(
    id           int auto_increment
        primary key,
    build_number int           not null,
    version      varchar(50)   null,
    description  varchar(2000) null,
    download_url varchar(200)  null
);

create table course
(
    id             int auto_increment
        primary key,
    student_number varchar(50) not null,
    semester       varchar(50) not null,
    course_id      varchar(50) null,
    course_name    varchar(50) null,
    classroom      varchar(50) null,
    raw_week       varchar(50) null,
    course_row     int         null,
    row_span       int         null,
    course_column  int         null,
    teacher        varchar(50) null,
    default_color  varchar(50) null,
    custom_color   int         null,
    top            int         null
);

create table device
(
    android_id      varchar(50) not null
        primary key,
    brand           varchar(50) null,
    device          varchar(50) null,
    android_version varchar(50) null,
    model           varchar(50) null,
    product         varchar(50) null,
    platform        varchar(50) null
);

create table fstar_admin
(
    id       int auto_increment
        primary key,
    username varchar(50)  not null,
    password varchar(500) not null,
    roles    varchar(200) not null
);

create table fstar_user
(
    id              int auto_increment
        primary key,
    app_version     varchar(20) null,
    build_number    int         null,
    android_id      varchar(50) null,
    android_version varchar(20) null,
    brand           varchar(50) null,
    device          varchar(50) null,
    model           varchar(50) null,
    product         varchar(50) null,
    platform        varchar(20) not null
);

create table message
(
    id                       int auto_increment
        primary key,
    content                  varchar(2000) not null,
    publish_time             datetime      null,
    max_visible_build_number int           not null,
    min_visible_build_number int           not null
);

create table parse_config
(
    id           int auto_increment
        primary key,
    school_name  varchar(50)   not null,
    school_url   varchar(200)  null,
    user         varchar(50)   null,
    author       varchar(50)   not null,
    pre_url      varchar(200)  null,
    code_url     varchar(200)  not null,
    publish_time datetime      null,
    remark       varchar(1000) null,
    download     int default 0 not null
);

create table score
(
    id                        varchar(100) not null
        primary key,
    student_number            varchar(50)  null,
    no                        varchar(50)  null,
    semester                  varchar(50)  null,
    score_no                  varchar(50)  null,
    name                      varchar(50)  null,
    score                     varchar(50)  null,
    credit                    varchar(50)  null,
    `period` varchar (50) null,
    evaluation_mode           varchar(50)  null,
    course_property           varchar(50)  null,
    course_nature             varchar(50)  null,
    alternative_course_number varchar(50)  null,
    alternative_course_name   varchar(50)  null,
    score_flag                varchar(50)  null
);

create table score_v2
(
    id                        int auto_increment
        primary key,
    student_number            varchar(50) null,
    no                        varchar(50) null,
    semester                  varchar(50) null,
    score_no                  varchar(50) null,
    name                      varchar(50) null,
    score                     varchar(50) null,
    credit                    varchar(50) null,
    `period` varchar (50) null,
    evaluation_mode           varchar(50) null,
    course_property           varchar(50) null,
    course_nature             varchar(50) null,
    alternative_course_number varchar(50) null,
    alternative_course_name   varchar(50) null,
    score_flag                varchar(50) null
);

