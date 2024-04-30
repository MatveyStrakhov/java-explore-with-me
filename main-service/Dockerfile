FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar main-server.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/main-server.jar"]