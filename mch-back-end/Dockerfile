FROM eclipse-temurin:17

LABEL maintainer="nguyenducanh2105@gmail.com"

WORKDIR /app

COPY target/mch-back-end-0.0.1-SNAPSHOT.jar /app/mch-back-end.jar

ENTRYPOINT ["java", "-jar", "mch-back-end.jar"]