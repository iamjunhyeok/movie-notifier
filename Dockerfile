FROM openjdk:17-alpine

COPY build/libs/MovieNotifier-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT java -jar -Djasypt.encryptor.password=${JASYPT_PASSWORD} app.jar
