# gazo-sagasu-kun
画像を検索できるアプリ。

JPG形式のタグ、PixivBathDownloaderのMetafileに対応しています。

## 使い方

`java -jar ImageSearch-*.*.*.jar`

## Docker

```docker-compose
version: "3"
services:
  imagesearch:
    image: usbharu/imagesearch
    ports:
      - "8080:80"
    volumes:
      - path/to/images/folder:/data
```

## LICENSE

APACHE LICENSE, VERSION 2.0

## Library

- [Spring Framework](https://spring.io/)
- [Apache Commons Imaging](https://commons.apache.org/proper/commons-imaging/)
- [SQLite JDBC](https://github.com/xerial/sqlite-jdbc)
- [JImageHash](https://github.com/KilianB/JImageHash)
