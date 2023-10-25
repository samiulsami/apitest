FROM maven:3.9.5-eclipse-temurin-21-alpine
COPY . ./apps
COPY ./pom.xml ./apps
COPY ./books.json ./apps
COPY ./META-INF ./apps
WORKDIR ./apps

RUN mvn install
#RUN mvn package

CMD ["java", "-jar", "target/apitest-1.0-SNAPSHOT-jar-with-dependencies.jar"]
