FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY consultita/pom.xml ./consultita/pom.xml
COPY verificota/pom.xml ./verificota/pom.xml
COPY consultita/src ./consultita/src

RUN mvn -B -ntp -pl consultita -am clean package -DskipTests
RUN cp consultita/target/*.jar /app.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app.jar ./app.jar

USER 10001:10001
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]