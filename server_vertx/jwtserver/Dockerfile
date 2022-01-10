FROM adoptopenjdk/openjdk11
RUN mkdir /app
WORKDIR /app
ADD target/jwtserver-fat.jar /app
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "jwtserver-fat.jar"]
