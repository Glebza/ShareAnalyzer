CREATE TABLE public.shares
(
    isin character varying(150) COLLATE pg_catalog."default",
    sec_type character varying(150) COLLATE pg_catalog."default",
    short_name character varying(150) COLLATE pg_catalog."default" NOT NULL,
    sec_id character varying(150) COLLATE pg_catalog."default" NOT NULL,
    lat_name character varying(40) COLLATE pg_catalog."default",
    status character varying COLLATE pg_catalog."default",
    settle_date date,
    board_name character varying(100) COLLATE pg_catalog."default",
    board_id character varying(15) COLLATE pg_catalog."default",
    CONSTRAINT "SHARES_pkey" PRIMARY KEY (sec_id)
);