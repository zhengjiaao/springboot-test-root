-- liquibase formatted sql

-- 格式 changeset author:id attribute1:value1 attribute2:value2 [...]

-- changeset liquibase:1
CREATE TABLE IF NOT EXISTS v_person (
  id bigint NOT NULL AUTO_INCREMENT,
  firstname varchar(16) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- changeset liquibase:2
ALTER TABLE v_person ADD lastname varchar(16);

-- changeset liquibase:3 dbms:oracle,mysql
ALTER TABLE v_person ADD name varchar(16);
