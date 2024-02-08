drop table IF EXISTS history;


create TABLE IF NOT EXISTS history (

id BIGSERIAL PRIMARY KEY,
 house_id bigint NOT NULL,
 person_id bigint NOT NULL,
 create_date timestamp(6) without time zone NOT NULL,
 type VARCHAR (20));

