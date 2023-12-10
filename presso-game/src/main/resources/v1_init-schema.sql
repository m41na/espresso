CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

drop table if exists tbl_clues;
drop table if exists tbl_choices;
drop table if exists tbl_questions;

create table if not exists tbl_questions
(
    id        uuid DEFAULT uuid_generate_v4() primary key not null,
    content   varchar(64) unique                          not null,
    posted_by varchar(32)                                 not null
);

create table if not exists tbl_choices
(
    id             uuid DEFAULT uuid_generate_v4() primary key not null,
    choice         varchar(64)                                 not null,
    correct_choice bool default false,
    question_fk    uuid                                        not null references tbl_questions (id) on delete cascade,
    constraint uniq_choice unique (question_fk, choice)
);

create table if not exists tbl_clues
(
    id        uuid DEFAULT uuid_generate_v4() primary key not null,
    clue      varchar(128)                                not null,
    choice_fk uuid                                        not null references tbl_choices (id) on delete cascade,
    constraint uniq_clue unique (choice_fk, clue)
);

