FROM openjdk:8-jdk-alpine
WORKDIR '/back'
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
CMD ["java","-jar","/app.jar"]