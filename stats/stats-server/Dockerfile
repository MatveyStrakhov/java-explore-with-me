FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar stats-server.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/stats-server.jar"]