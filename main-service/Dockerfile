FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar main-service.jar
ENTRYPOINT ["java","-Dspring.profiles.active=dev","-jar","/main-service.jar"]
