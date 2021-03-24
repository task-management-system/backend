--
-- PostgreSQL database dump
--

-- Dumped from database version 13.1 (Debian 13.1-1.pgdg100+1)
-- Dumped by pg_dump version 13.1 (Debian 13.1-1.pgdg100+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: dao_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dao_roles (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    name character varying(100) NOT NULL,
    power bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.dao_roles OWNER TO postgres;

--
-- Name: dao_users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dao_users (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    username character varying(50) NOT NULL,
    name character varying(50),
    is_active boolean DEFAULT false NOT NULL,
    dao_roles_id uuid NOT NULL
);


ALTER TABLE public.dao_users OWNER TO postgres;

--
-- Name: detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.detail (
    id bigint NOT NULL,
    task_id bigint NOT NULL,
    executor_id bigint NOT NULL,
    status_id smallint NOT NULL
);


ALTER TABLE public.detail OWNER TO postgres;

--
-- Name: detail_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.detail_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.detail_id_seq OWNER TO postgres;

--
-- Name: detail_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.detail_id_seq OWNED BY public.detail.id;


--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    power integer DEFAULT 0 NOT NULL,
    text character varying(100) NOT NULL
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.roles_id_seq OWNER TO postgres;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.role.id;


--
-- Name: status; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.status (
    id smallint NOT NULL,
    name character varying(100) NOT NULL
);


ALTER TABLE public.status OWNER TO postgres;

--
-- Name: status_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.status_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.status_id_seq OWNER TO postgres;

--
-- Name: status_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.status_id_seq OWNED BY public.status.id;


--
-- Name: task; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.task (
    id bigint NOT NULL,
    title character varying(200) NOT NULL,
    description text,
    due_date timestamp without time zone NOT NULL,
    creator_id bigint NOT NULL
);


ALTER TABLE public.task OWNER TO postgres;

--
-- Name: task_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.task_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.task_id_seq OWNER TO postgres;

--
-- Name: task_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.task_id_seq OWNED BY public.task.id;


--
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user" (
    id bigint NOT NULL,
    username character varying(50) NOT NULL,
    password text NOT NULL,
    name character varying(50) DEFAULT NULL::character varying,
    email character varying(50) DEFAULT NULL::character varying,
    role_id bigint DEFAULT 2 NOT NULL,
    is_active boolean DEFAULT true NOT NULL
);


ALTER TABLE public."user" OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO postgres;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;


--
-- Name: detail id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detail ALTER COLUMN id SET DEFAULT nextval('public.detail_id_seq'::regclass);


--
-- Name: role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: status id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.status ALTER COLUMN id SET DEFAULT nextval('public.status_id_seq'::regclass);


--
-- Name: task id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task ALTER COLUMN id SET DEFAULT nextval('public.task_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);


--
-- Data for Name: dao_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dao_roles (id, name, power) FROM stdin;
f44b0491-47c1-41df-b41a-c3edd210ee54	Администратор	127
8a8f3163-7eb9-4b5e-880d-f03dfa108f6f	Гость	0
35ba3565-9e65-495c-958d-cacf469782bc	Правитель	63
dec84c26-bc82-4b3d-8499-b7f982634b83	Пупсик	9
\.


--
-- Data for Name: dao_users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dao_users (id, username, name, is_active, dao_roles_id) FROM stdin;
26d3f9a6-1f9a-48b7-a271-788cdbe6df5e	W	\N	t	8a8f3163-7eb9-4b5e-880d-f03dfa108f6f
6dbe1be8-0d5e-495e-8a84-70516273dd59	E	\N	t	dec84c26-bc82-4b3d-8499-b7f982634b83
ea05d373-6edd-4bb6-9b3d-7740aa6146b1	R	\N	f	dec84c26-bc82-4b3d-8499-b7f982634b83
cb374113-0e5a-4fea-bf9b-6f65a27cd8dc	T	\N	t	dec84c26-bc82-4b3d-8499-b7f982634b83
4243f3cd-1228-4fab-a166-b22bceb7e2d0	Y	\N	f	dec84c26-bc82-4b3d-8499-b7f982634b83
12343d38-d2ce-43b0-9197-4c530b11c3e3	U	Uganda	f	f44b0491-47c1-41df-b41a-c3edd210ee54
\.


--
-- Data for Name: detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.detail (id, task_id, executor_id, status_id) FROM stdin;
1	1	2	1
2	1	3	1
13	2	1	1
14	2	1	1
15	2	1	2
16	2	1	2
17	2	1	3
18	2	1	3
19	2	1	4
20	2	1	4
21	20	1	1
22	20	2	1
23	20	3	1
24	20	13	1
25	20	17	1
26	20	27	1
27	21	1	1
28	21	2	1
29	21	3	1
30	21	13	1
31	21	17	1
32	21	27	1
33	22	1	1
34	22	2	1
35	22	3	1
36	22	13	1
37	22	17	1
38	22	27	1
39	23	1	1
40	24	2	1
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, power, text) FROM stdin;
3	9	Классический клерк
1	127	Администратор
2	0	Гость
4	0	Гость2
5	0	Гость3
6	63	Admin ne admin
\.


--
-- Data for Name: status; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.status (id, name) FROM stdin;
1	Новые
2	В работе
3	Отмененные
4	Завершенные
\.


--
-- Data for Name: task; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.task (id, title, description, due_date, creator_id) FROM stdin;
1	Задача 1	Lorem ipsum dolor sit amet, consectetur adipisicing elit. Reiciendis, neque.	2021-02-10 23:20:20	1
2	Задача 2	Ratione magni, hic sequi, aliquid impedit nobis et! Facere, veniam.	2021-02-10 23:20:20	1
3	Задача 3	Dolores voluptatum repudiandae libero doloribus, est, necessitatibus reprehenderit voluptate repellendus.	2021-02-10 23:20:20	1
4	Задача 4	Laudantium commodi voluptatum rem vero deleniti eum quaerat voluptates modi!	2021-02-10 23:20:20	1
5	Задача 5	Hic inventore deserunt sed, velit quod nesciunt aspernatur quos atque.	2021-02-10 23:20:20	1
6	Задача 6	Eveniet deserunt id officia, dolor atque repudiandae magnam quasi, delectus.	2021-02-10 23:20:20	1
7	Задача 7	Suscipit vero similique ad veniam iure beatae nemo, reiciendis et.	2021-02-10 23:20:20	1
8	Задача 8	Eaque eius iure distinctio quam adipisci ad accusamus, quasi odio!	2021-02-10 23:20:20	1
9	Задача 9	Dolorum nam molestias minima natus libero atque laborum dicta. Odio?	2021-02-10 23:20:20	1
10	Задача 10	Sapiente qui libero dolorem ad itaque sint dolorum corrupti hic.	2021-02-10 23:20:20	1
11	Задача 11	Modi labore hic dolorem atque veniam numquam recusandae minus? Eius!	2021-02-10 23:20:20	1
12	Задача 12	Eligendi alias labore odio sed, non, doloribus magni explicabo deleniti.	2021-02-10 23:20:20	1
13	Задача 13	Inventore non, possimus ipsam voluptate, a cumque expedita iure nam.	2021-02-10 23:20:20	1
14	Задача 14	Recusandae earum at veritatis voluptatibus eum quisquam ipsum eius in!	2021-02-10 23:20:20	1
15	Задача 15	Quidem odit ipsam harum aperiam nobis, quasi aliquid, quam facere.	2021-02-10 23:20:20	1
16	Задача 16	Cupiditate voluptatibus dolorum saepe nulla, deserunt, neque quidem ad tempore.	2021-02-10 23:20:20	1
17	Задача 17	Vel excepturi consectetur magnam iure autem distinctio, eveniet rem porro?	2021-02-10 23:20:20	1
18	Задача 18	Minima, totam repellat sit. Impedit odit et unde vero provident!	2021-02-10 23:20:20	1
19	Задача 19	Nostrum error libero adipisci ipsa asperiores quo dolor ipsum! Suscipit.	2021-02-10 23:20:20	1
20	Задача 20	Sed perferendis non voluptatum facere qui. Incidunt at veniam perferendis.	2022-02-28 01:14:56	1
21	Задача 21	Incidunt laudantium vero officia assumenda quas. Est esse, sequi. Nulla?	2022-02-28 01:14:56	1
22	Задача 22	Ea earum labore in itaque nulla tenetur veritatis officia facere!	2022-02-28 01:14:56	1
23	test 1		2021-03-21 20:22:00	1
24	test 2		2021-03-28 20:46:00	1
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."user" (id, username, password, name, email, role_id, is_active) FROM stdin;
46	pppp	password	\N	\N	3	f
48	zzzxcxzczxc	password	\N	\N	3	f
13	qazzaq	password	\N	\N	3	t
37	ffffffffffffffffff	password	\N	\N	3	f
3	bober	password	\N	\N	3	t
47	lllljh	password	\N	\N	3	f
51	test2	password	test	\N	2	t
27	zzzzzzzzz	password	\N	\N	3	f
28	xxxxxxxxx	password	\N	\N	3	f
29	ccccccccc	password	\N	\N	3	f
31	bbb	password	\N	\N	3	f
52	test3	password	\N	\N	1	t
32	fff	password	\N	\N	3	f
17	zzz	password	ЗэЗэЗэ	\N	3	f
43	mmmmmmmm	password	\N	\N	3	f
44	qqqqqq	password	\N	\N	3	f
53	basdasd	password	\N	\N	1	t
50	btest11	password	test1213	\N	3	t
54	Bubel	password	\N	\N	2	t
2	barsuk2	password	\N	\N	6	t
1	admin	password	\N	admin@gmail.com	1	t
55	asd	password	\N	\N	3	t
\.


--
-- Name: detail_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.detail_id_seq', 40, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 6, true);


--
-- Name: status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.status_id_seq', 4, true);


--
-- Name: task_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.task_id_seq', 24, true);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_id_seq', 55, true);


--
-- Name: detail detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detail
    ADD CONSTRAINT detail_pkey PRIMARY KEY (id);


--
-- Name: dao_users key_dao_users_username; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dao_users
    ADD CONSTRAINT key_dao_users_username UNIQUE (username);


--
-- Name: dao_roles pk_dao_roles; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dao_roles
    ADD CONSTRAINT pk_dao_roles PRIMARY KEY (id);


--
-- Name: dao_users pk_dao_users; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dao_users
    ADD CONSTRAINT pk_dao_users PRIMARY KEY (id);


--
-- Name: role roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: status status_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.status
    ADD CONSTRAINT status_pkey PRIMARY KEY (id);


--
-- Name: task task_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT task_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user user_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_username_key UNIQUE (username);


--
-- Name: dao_users fk_dao_users_dao_roles_id_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dao_users
    ADD CONSTRAINT fk_dao_users_dao_roles_id_id FOREIGN KEY (dao_roles_id) REFERENCES public.dao_roles(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: detail fk_detail_executor_id_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detail
    ADD CONSTRAINT fk_detail_executor_id_id FOREIGN KEY (executor_id) REFERENCES public."user"(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: detail fk_detail_status_id_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detail
    ADD CONSTRAINT fk_detail_status_id_id FOREIGN KEY (status_id) REFERENCES public.status(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: detail fk_detail_task_id_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.detail
    ADD CONSTRAINT fk_detail_task_id_id FOREIGN KEY (task_id) REFERENCES public.task(id) ON UPDATE RESTRICT ON DELETE RESTRICT;


--
-- Name: task fk_task_creator_id_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.task
    ADD CONSTRAINT fk_task_creator_id_id FOREIGN KEY (creator_id) REFERENCES public."user"(id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- Name: user fk_user_role_id_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT fk_user_role_id_id FOREIGN KEY (role_id) REFERENCES public.role(id) ON UPDATE RESTRICT ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

