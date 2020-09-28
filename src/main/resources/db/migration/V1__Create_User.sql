create table user
(
    id                 bigint primary key auto_increment,
    username           varchar(200),
    encrypted_password varchar(200),
    avatar             varchar(250),
    created_at         datetime,
    updated_at         datetime
)