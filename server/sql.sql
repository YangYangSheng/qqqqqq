create
database if not exists ikun;
drop table if exists tb_user;
create table if not exists tb_user
(
    id
    int
    primary
    key
    auto_increment
    comment
    '用户的id',
    use_name
    varchar
(
    10
) unique comment '账号',
    password varchar
(
    10
) comment '密码'
    );