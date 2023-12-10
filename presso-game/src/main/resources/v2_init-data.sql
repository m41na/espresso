insert into tbl_questions (content, posted_by)
values ('What is the capital of Wisconsin', 'tester') on conflict (content) do nothing;
insert into tbl_questions (content, posted_by)
values ('What is the capital of Illinois', 'tester') on conflict (content) do nothing;
insert into tbl_questions (content, posted_by)
values ('What is the capital of California', 'tester') on conflict (content) do nothing;

with q1 as (select id from tbl_questions where content = 'What is the capital of Wisconsin')
insert
into tbl_choices (choice, correct_choice, question_fk)
values ('Milwaukee', false, (select id from q1)),
       ('Appleton', false, (select id from q1)),
       ('Madison', true, (select id from q1)),
       ('Steven''s Point', false, (select id from q1)) on conflict (choice, question_fk) do nothing;

insert into tbl_clues (clue, choice_fk)
values ('Not close to lake Michigan', (select id from tbl_choices where choice = 'Milwaukee')),
       ('Not close to lake Winnebago', (select id from tbl_choices where choice = 'Appleton')),
       ('Does not have two syllables', (select id from tbl_choices where choice = 'Steven''s Point')),
       ('Wedged between two lakes',
        (select id from tbl_choices where choice = 'Madison')) on conflict (clue, choice_fk) do nothing;

with q2 as (select id from tbl_questions where content = 'What is the capital of Illinois')
insert
into tbl_choices (choice, correct_choice, question_fk)
values ('Chicago', false, (select id from q2)),
       ('Peoria', false, (select id from q2)),
       ('Rock Island', false, (select id from q2)),
       ('Springfield', true, (select id from q2)) on conflict (choice, question_fk) do nothing;

insert into tbl_clues (clue, choice_fk)
values ('Not the largest of them all', (select id from tbl_choices where choice = 'Chicago')),
       ('Does not have two syllables', (select id from tbl_choices where choice = 'Rock Island')),
       ('Not on Hwy 74', (select id from tbl_choices where choice = 'Peoria')),
       ('Rhymes with fields of corn',
        (select id from tbl_choices where choice = 'Springfield')) on conflict (clue, choice_fk) do nothing;

with q3 as (select id from tbl_questions where content = 'What is the capital of California')
insert
into tbl_choices (choice, correct_choice, question_fk)
values ('Los Angeles', false, (select id from q3)),
       ('Sacramento', true, (select id from q3)),
       ('San Fransisco', false, (select id from q3)),
       ('San Diego', false, (select id from q3)) on conflict (choice, question_fk) do nothing;

insert into tbl_clues (clue, choice_fk)
values ('Angles do not live there', (select id from tbl_choices where choice = 'Los Angeles')),
       ('Not close to the mexican border', (select id from tbl_choices where choice = 'San Diego')),
       ('Not the most expensive city to live in', (select id from tbl_choices where choice = 'San Fransisco')),
       ('Does not have two syllables',
        (select id from tbl_choices where choice = 'Sacramento')) on conflict (clue, choice_fk) do nothing;

select *
from tbl_choices;