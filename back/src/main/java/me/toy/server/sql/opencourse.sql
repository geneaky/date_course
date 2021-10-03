-- create database opencourse;

use
opencourse;

create table `user`
(
    `user_id`     int unsigned not null auto_increment,
    `email`       varchar(255) not null unique,
    `password`    varchar(255) not null,
    `name`        varchar(255) not null,
    `imageUrl`    varchar(255) null,
    `provider`    varchar(255) null,
    `provider_id` varchar(255) null,
    `created`     datetime     not null,
    `updated`     datetime null,
    primary key (`user_id`)
)ENGINE=InnoDB;

create table `follow`
(
    `follow_id`   int unsigned not null auto_increment,
    `followee_id` int not null,
    `user_id`     int unsigned not null,
    primary key (`follow_id`),
    foreign key (`user_id`) references `user` (`user_id`)
)ENGINE=InnoDB;

alter table `follow`
    add unique key (`followee_id`,`user_id`);

create table `course`
(
    `course_id` int unsigned not null auto_increment,
    `title`     varchar(255) not null,
    `created`   datetime     not null,
    `updated`   datetime null,
    primary key (`course_id`)
)ENGINE=InnoDB;

create table `comment`
(
    `comment_id` int unsigned not null auto_increment,
    `content`    text     not null,
    `created`    datetime not null,
    `updated`    datetime null,
    `user_id`    int unsigned not null,
    `course_id`  int unsigned not null,
    primary key (`comment_id`),
    foreign key (`user_id`) references `user` (`user_id`),
    foreign key (`course_id`) references `course` (`course_id`)
)ENGINE=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

create table `user_course_like`
(
    `user_course_like_id` int unsigned not null auto_increment,
    `user_id`             int unsigned not null,
    `course_id`           int unsigned not null,
    primary key (`user_course_like_id`),
    foreign key (`user_id`) references `user` (`user_id`),
    foreign key (`course_id`) references `course` (`course_id`)
)ENGINE=InnoDB;

alter table `user_course_like`
    add unique key (`user_id`,`course_id`);

create table `user_course_save`
(
    `user_course_save_id` int unsigned not null auto_increment,
    `user_id`             int unsigned not null,
    `course_id`           int unsigned not null,
    primary key (`user_course_save_id`),
    foreign key (`user_id`) references `user` (`user_id`),
    foreign key (`course_id`) references `course` (`course_id`)
)ENGINE=InnoDB;

alter table `user_course_save`
    add unique key (`user_id`,`course_id`);

create table `location`
(
    `location_id` int unsigned not null auto_increment,
    `name`        varchar(255) not null,
    `text`        text null,
    `photo_url`   varchar(255) null,
    `posx`        float        not null,
    `posy`        float        not null,
    `created`     datetime     not null,
    `updated`     datetime null,
    `course_id`   int unsigned not null,
    primary key (`location_id`),
    foreign key (`course_id`) references `course` (`course_id`)
)ENGINE=InnoDB CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

create table `tag`
(
    `tag_id` int unsigned not null auto_increment,
    `name`   varchar(255) not null,
    primary key (`tag_id`)
)ENGINE=InnoDB;

create table `location_tag`
(
    `location_tag_id` int unsigned not null auto_increment,
    `location_id`     int unsigned not null,
    `tag_id`          int unsigned not null,
    primary key (`location_tag_id`),
    foreign key (`location_id`) references `location` (`location_id`),
    foreign key (`tag_id`) references `tag` (`tag_id`)
)ENGINE=InnoDB;