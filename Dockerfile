FROM maven:3.9.5-eclipse-temurin-21-alpine
COPY . ./apps
WORKDIR ./apps

RUN mvn install
#RUN mvn package

EXPOSE 8000

CMD ["java", "-jar", "target/apitest-1.0-SNAPSHOT-jar-with-dependencies.jar"]
