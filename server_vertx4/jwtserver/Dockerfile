FROM openjdk:17.0.2-jdk
WORKDIR /app
ADD target/jwtserver-1.0.0-SNAPSHOT-fat.jar /app/app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
