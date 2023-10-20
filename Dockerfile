FROM maven:3.9.5
COPY . ./apps
WORKDIR /apps

RUN mvn install
#RUN mvn package

CMD ["java", "-jar", "target/apitest-1.0-SNAPSHOT-jar-with-dependencies.jar"]
