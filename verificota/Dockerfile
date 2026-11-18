FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./pom.xml
COPY consultita/pom.xml ./consultita/pom.xml
COPY verificota/pom.xml ./verificota/pom.xml
COPY verificota/src ./verificota/src

RUN mvn -B -ntp -pl verificota -am clean package -DskipTests
RUN cp verificota/target/*.jar /app.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app.jar ./app.jar

USER 10001:10001
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]