FROM openjdk:11-jre-slim-buster
MAINTAINER sravana.pullivendula@gmail.com
COPY target/assignment-0.0.1-SNAPSHOT.jar payconiqApp.jar
ENTRYPOINT ["java","-jar","/payconiqApp.jar"]