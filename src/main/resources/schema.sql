CREATE TABLE IF NOT EXISTS image(
                                    id      INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name    TEXT        NOT NULL,
                                    path    TEXT UNIQUE NOT NULL,
                                    groupId INTEGER     NOT NULL
);
CREATE TABLE IF NOT EXISTS tag
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ,
    name TEXT UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS image_tag
(
    image_id INTEGER NOT NULL,
    tag_id   INTEGER NOT NULL,
    UNIQUE (image_id, tag_id)
);
CREATE TABLE IF NOT EXISTS groupId
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);
