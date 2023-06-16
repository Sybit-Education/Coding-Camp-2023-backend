INSERT INTO "user" (id, username, password)
VALUES ('bmLmro3mde', 'admin', '$2a$10$q08EqdPRMBGZkfHzAgTH4eGB2ZWZPcNe8Q9m8tGnH/WxYQl8b1R4C'),
       ('1amLmdnrod', 'sybit', '$2a$10$q08EqdPRMBGZkfHzAgTH4eGB2ZWZPcNe8Q9m8tGnH/WxYQl8b1R4C');

INSERT INTO tamagotchi (id, name, mood, owner_id, hunger, thirst, bored, tired, is_dead, is_sleeping, started_sleeping,
                        dirty, score, color, height, width, eyes, shape)
VALUES ('1', 'Tammy', 'HUNGRY', '1amLmdnrod', 100.0, 35.0, 80.0, 100.0, false, FALSE, NULL, 80.0, 10, '#000000', 100, 100,
        'SMALL', 'TRIANGLE'),
       ('2', 'Timo', 'TIRED', 'bmLmro3mde', 10.0, 15.0, 0.0, 80.0, false, FALSE, NULL, 20.0, 30, '#FF2D00', 150, 150,
        'LARGE', 'CIRCLE'),
       ('3', 'Tina', 'SAD', '1amLmdnrod', 100.0, 100.0, 100.0, 100.0, true, FALSE, NULL, 100.0, 60, '#000000', 100, 100,
        'SMALL', 'TRIANGLE'),
       ('4', 'Tom', 'SAD', '1amLmdnrod', 100.0, 100.0, 100.0, 100.0, true, FALSE, NULL, 100.0, 20, '#000000', 100, 100,
        'SMALL', 'TRIANGLE'),
       ('5', 'Tessa', 'SAD', 'bmLmro3mde', 100.0, 100.0, 100.0, 100.0, true, FALSE, NULL, 100.0, 0, '#000000', 100, 100,
        'SMALL', 'TRIANGLE');
