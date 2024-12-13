CREATE user cs209a WITH PASSWORD 'cs209a';
ALTER USER cs209a WITH SUPERUSER;

DROP TABLE IF EXISTS Users, Questions, Answers, Comments, Tags, Topics, Post_Topics, Bugs, Post_Bugs;

CREATE TABLE Users
(
    user_id         BIGINT PRIMARY KEY,
    display_name    TEXT      NOT NULL,
    creation_date   TIMESTAMP NOT NULL,

    reputation      INT       NOT NULL,
    accept_rate     INT,
    up_vote_count   INT       NOT NULL,
    down_vote_count INT       NOT NULL
);

CREATE TABLE Questions
(
    question_id        BIGINT PRIMARY KEY,
    owner_id           BIGINT,

    title              TEXT      NOT NULL,
    body               TEXT      NOT NULL,

    creation_date      TIMESTAMP NOT NULL,
    last_activity_date TIMESTAMP NOT NULL,
    last_edit_date     TIMESTAMP,

    accepted_answer_id BIGINT,

    score              INT       NOT NULL,
    view_count         INT       NOT NULL,
    up_vote_count      INT       NOT NULL,
    down_vote_count    INT       NOT NULL,
    favorite_count     INT       NOT NULL
);

CREATE TABLE Answers
(
    answer_id          BIGINT PRIMARY KEY,
    question_id        BIGINT REFERENCES Questions (question_id),
    owner_id           BIGINT,

    body               TEXT      NOT NULL,

    creation_date      TIMESTAMP NOT NULL,
    last_activity_date TIMESTAMP NOT NULL,
    last_edit_date     TIMESTAMP,

    is_accepted        BOOLEAN   NOT NULL,
    score              INT       NOT NULL,
    up_vote_count      INT       NOT NULL,
    down_vote_count    INT       NOT NULL
);

CREATE TABLE Comments
(
    comment_id    BIGINT PRIMARY KEY,
    post_id       BIGINT    NOT NULL,
    owner_id      BIGINT,

    body          TEXT,

    creation_date TIMESTAMP NOT NULL,
    score         INT       NOT NULL
);

CREATE TABLE Topics
(
    topic_id   SERIAL PRIMARY KEY,
    topic_name TEXT NOT NULL,
    frequency  BIGINT DEFAULT 0
);

CREATE TABLE Post_Topics
(
    post_topic_id BIGSERIAL PRIMARY KEY,
    post_id       BIGINT NOT NULL,
    post_type     INT    NOT NULL, -- 'Question' or 'Answer'
    topic_id      BIGINT REFERENCES Topics (topic_id)
);

CREATE TABLE Bugs
(
    bug_id        SERIAL PRIMARY KEY,
    bug_name      TEXT NOT NULL,
    bug_type      INT  NOT NULL, -- 'FatalError' or 'Exception'
    bug_frequency INT DEFAULT 0,

    bug_desc      TEXT
);

CREATE TABLE Post_Bugs
(
    post_bug_id BIGSERIAL PRIMARY KEY,
    post_id     BIGINT NOT NULL,
    post_type   INT    NOT NULL, -- 'Question' or 'Answer'
    bug_id      BIGINT NOT NULL REFERENCES Bugs (bug_id),
    frequency   BIGINT DEFAULT 0
);
