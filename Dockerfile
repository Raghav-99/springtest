FROM maven:3.9.16-eclipse-temurin-17-alpine as mvn-build
WORKDIR /myapp
COPY pom.xml ./
RUN mvn dependencies:go-offline
COPY ./ ./
RUN mvn clean install

FROM eclipse-temurin:17-alpine-3.23
COPY --from=mvn-build target/*.jar app.jar
CMD ["java", "-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "app.jar"]