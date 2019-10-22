create table tb_file (
    uuid varchar(40),
    size bigint,
    type varchar(20),
    name varchar(100),
    create_time TIMESTAMP,
    save_path varchar(100),
    primary key(uuid)
);