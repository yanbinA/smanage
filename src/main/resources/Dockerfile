FROM openjdk:11

LABEL com.example.version="1.0.0" com.example.release="smanage"

WORKDIR /home/smanage/

VOLUME ["/home/smanage/logs","/home/smanage/image"]

EXPOSE 8080

ADD manage-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT java -jar -Dspring.profiles.active=prd app.jar
