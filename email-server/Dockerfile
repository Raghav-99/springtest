FROM maven:3.9.16-eclipse-temurin-17-alpine as mvn-build
WORKDIR /myapp
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY ./ ./
RUN mvn -DskipTests clean package

FROM eclipse-temurin:17-alpine-3.23
COPY --from=mvn-build /myapp/target/*.jar app.jar
CMD ["java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "app.jar"]