# Etapa de Construção
FROM maven:3.8.4-openjdk-17-slim AS build

EXPOSE 8080

COPY pom.xml /build/
WORKDIR /build
RUN mvn dependency:go-offline

COPY ./src /build/src
RUN mvn package

# Etapa de Execução
FROM openjdk:11-slim
WORKDIR /app

COPY --from=build /build/target/*.jar /app/app.jar
COPY --from=build /build/src /app/src

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

ENTRYPOINT ["java", "-jar", "app.jar"]