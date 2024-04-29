FROM openjdk:17-jdk-alpine
COPY target/*.jar *.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "*.jar"]
