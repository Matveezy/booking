FROM openjdk:17
ADD target/booking.jar booking.jar
ENTRYPOINT ["java", "-jar","booking.jar"]
EXPOSE 8080