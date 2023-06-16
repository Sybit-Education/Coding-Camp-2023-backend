CREATE TABLE tamagotchi
(
    id               VARCHAR(255)            NOT NULL,
    name             VARCHAR(255)            NOT NULL,
    rank             BIGINT    DEFAULT 0     NOT NULL,
    created_at       TIMESTAMP DEFAULT NOW() NOT NULL,
    score            BIGINT    DEFAULT 0,
    mood             VARCHAR(255)            NOT NULL,
    color            VARCHAR(255)            NOT NULL,
    eyes             VARCHAR(255)            NOT NULL,
    shape            VARCHAR(255)            NOT NULL,
    height           INT                     NOT NULL,
    width            INT                     NOT NULL,
    owner_id         VARCHAR(10)             NOT NULL,
    hunger           DOUBLE PRECISION        NOT NULL,
    thirst           DOUBLE PRECISION        NOT NULL,
    bored            DOUBLE PRECISION        NOT NULL,
    tired            DOUBLE PRECISION        NOT NULL,
    dirty            DOUBLE PRECISION        NOT NULL,
    feed_cooldown    INT       DEFAULT -1,
    drink_cooldown   INT       DEFAULT -1,
    play_cooldown    INT       DEFAULT -1,
    hunger_death_in  INT       DEFAULT -1,
    thirst_death_in  INT       DEFAULT -1,
    bored_death_in   INT       DEFAULT -1,
    is_dead          BOOLEAN   DEFAULT FALSE NOT NULL,
    is_sleeping      BOOLEAN   DEFAULT FALSE NOT NULL,
    started_sleeping TIMESTAMP,
    died_at          TIMESTAMP,
    CONSTRAINT pk_tamagotchi PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id       VARCHAR(10)  NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_username UNIQUE (username);

ALTER TABLE tamagotchi
    ADD CONSTRAINT FK_TAMAGOTCHI_ON_OWNER FOREIGN KEY (owner_id) REFERENCES "user" (id);
