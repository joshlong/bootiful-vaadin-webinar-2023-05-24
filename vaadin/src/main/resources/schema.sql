create table if not exists post
(
    post_id serial primary key,
    title   text,
    body    text
);