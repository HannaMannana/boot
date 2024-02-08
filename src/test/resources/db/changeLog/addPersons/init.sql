drop table IF EXISTS person;

 CREATE TABLE IF NOT EXISTS person
(
    id bigint NOT NULL DEFAULT nextval('person_id_seq'::regclass),
    name character varying(255)  NOT NULL,
    passport_number character varying(255) NOT NULL,
    passport_series character varying(255) NOT NULL,
    sex_type character varying(255),
    surname character varying(255) NOT NULL,
    uuid uuid NOT NULL,
    create_date timestamp(6) without time zone NOT NULL,
    update_date timestamp(6) without time zone NOT NULL,
    house_id bigint NOT NULL,

    CONSTRAINT person_pkey PRIMARY KEY (id),
    CONSTRAINT uk_2q4w4iw55dwwmpybiwugqrh6l UNIQUE (passport_series),
    CONSTRAINT uk_9tt3moyr2yhcaddgdp1was853 UNIQUE (passport_number),
    CONSTRAINT uk_g9lr9owdvq776erw8111fr4l6 UNIQUE (uuid),
    CONSTRAINT fk6fe2v09hiovxvythi1tpg0igb FOREIGN KEY (house_id)
        REFERENCES public.house (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT person_sex_type_check CHECK (sex_type::text = ANY (ARRAY['FEMALE'::character varying, 'MALE'::character varying]::text[]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.person
    OWNER to postgres;


    CREATE TABLE IF NOT EXISTS house
(
    id bigint NOT NULL DEFAULT nextval('house_id_seq'::regclass),
    area real NOT NULL,
    city character varying(255) NOT NULL,
    country character varying(255) NOT NULL,
    create_date timestamp(6) without time zone NOT NULL,
    "number" bigint NOT NULL,
    street character varying(255) NOT NULL,
    uuid uuid NOT NULL,
    CONSTRAINT house_pkey PRIMARY KEY (id),
    CONSTRAINT uk_kv6qqcj5f61xixkte8c23l1hl UNIQUE (uuid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.house
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS person_house
(
    person_id bigint NOT NULL,
    house_id bigint NOT NULL,
    CONSTRAINT fkecgumnwuhik45r7h72r1ix9gt FOREIGN KEY (house_id)
        REFERENCES public.house (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkmjwuvu5kppqdsk2idydrsdpa7 FOREIGN KEY (person_id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.person_house
    OWNER to postgres;