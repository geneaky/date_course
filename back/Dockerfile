FROM openjdk:8-jdk-alpine
WORKDIR '/back'
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
CMD ["java","-jar","/app.jar"]