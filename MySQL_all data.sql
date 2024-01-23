CREATE DATABASE crawl_data DEFAULT CHARACTER SET utf8;
CREATE USER crawl_user IDENTIFIED BY '1234';
GRANT ALL ON crawl_data.* TO crawl_user;
show databases;
use crawl_data;
show tables;
SHOW COLUMNS FROM metro from crawl_data;

CREATE TABLE metro1 (
id bigint(7) not null auto_increment,
created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
title varchar(767),
content varchar(767),
PRIMARY KEY(id),
unique index (content)
);

SHOW COLUMNS FROM metro FROM crawl_data;
SELECT * FROM metro ORDER BY id DESC;

-- TRUNCATE TABLE metroinfo;
ALTER DATABASE crawl_data CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
ALTER TABLE metro1 AUTO_INCREMENT = 1;
ALTER TABLE metro1 CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE metro1 CHANGE title title VARCHAR(767) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE metro1 CHANGE content content VARCHAR(767) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;