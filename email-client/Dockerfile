FROM eclipse-temurin:17-alpine-3.23
WORKDIR /myapp
COPY ./ ./
RUN mkdir target/classes
RUN javac EmailClient.java Application.java -d target/classes
RUN jar cfm app.jar resources/manifest.txt target/classes/* resources/attachments/*
CMD ["java", "-jar", "app.jar"]