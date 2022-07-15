FROM maven:3.8.4-openjdk-17 AS builder

VOLUME /root/.m2/repository:/root/.m2/repository

COPY pom.xml pom.xml
COPY src/ src/

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17.0.2 as runtime

COPY --from=builder target/blog-0.0.1.jar blog-0.0.1.jar

EXPOSE 9110

ENTRYPOINT ["java", "-jar", "blog-0.0.1.jar"]
