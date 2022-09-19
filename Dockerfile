FROM gradle:7.5.1-jdk11 AS builder

ADD ./ /build

WORKDIR /build

RUN gradle bootJar

FROM amazoncorretto:11-alpine

COPY --from=builder /build/build/libs/*.jar /bin/imagesearch/ImageSearch.jar

EXPOSE 80

VOLUME /data

ENV DATASOURCE=jdbc:sqlite:/data/imagetag.db

WORKDIR /data

RUN ls /bin/imagesearch -la

CMD ["java","-server" ,"-jar","/bin/imagesearch/ImageSearch.jar","--spring.config.location=classpath:/application.properties,file:/data/application.properties"]
