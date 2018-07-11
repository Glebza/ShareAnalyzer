CREATE TABLE public.shares_history
(
    record_id integer NOT NULL DEFAULT nextval('shares_history_recordid_seq'::regclass),
    shares_sec_id character varying COLLATE pg_catalog."default",
    legal_close_price character varying COLLATE pg_catalog."default",
    trade_date date,
    board_id character varying COLLATE pg_catalog."default",
    CONSTRAINT shares_history_pkey PRIMARY KEY (record_id),
    CONSTRAINT shares_history_shares_sec_id_fk FOREIGN KEY (shares_sec_id)
        REFERENCES public.shares (sec_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);