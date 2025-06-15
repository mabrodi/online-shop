FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/online-shop.jar online-shop.jar

EXPOSE 7080
CMD ["java", "-Djava.io.tmpdir=/tmp", "-jar", "online-shop.jar"]