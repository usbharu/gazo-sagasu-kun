CREATE TABLE IF NOT EXISTS image(
                                    id      INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name    TEXT        NOT NULL COLLATE NOCASE,
                                    path    TEXT UNIQUE NOT NULL COLLATE NOCASE,
                                    groupId INTEGER     NOT NULL,
                                    FOREIGN KEY (groupId) REFERENCES groupId(id)
);
CREATE TABLE IF NOT EXISTS tag
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ,
    name TEXT UNIQUE NOT NULL COLLATE NOCASE
);
CREATE TABLE IF NOT EXISTS image_tag
(
    image_id INTEGER NOT NULL,
    tag_id   INTEGER NOT NULL,
    UNIQUE (image_id, tag_id),
    FOREIGN KEY (image_id) REFERENCES image(id),
    FOREIGN KEY (tag_id) REFERENCES tag(id)
);
CREATE TABLE IF NOT EXISTS groupId
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE ,
    name TEXT NOT NULL UNIQUE COLLATE NOCASE
);
CREATE TABLE IF NOT EXISTS image_hash
(
    image_id INTEGER PRIMARY KEY NOT NULL UNIQUE ,
    hash BLOB NOT NULL ,
    FOREIGN KEY (image_id) REFERENCES image(id)
)
