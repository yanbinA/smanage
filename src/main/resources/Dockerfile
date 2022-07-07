FROM openjdk:11

LABEL com.example.version="1-beta" com.example.release="smanage"

WORKDIR /www/wwwroot/

VOLUME ["/www/wwwroot/logs","/www/wwwroot/image"]

EXPOSE 8080

ADD manage-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT java -jar -Dspring.profiles.active=prd app.jar
