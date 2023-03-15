FROM openjdk:19
EXPOSE 8080
ADD target/docker-sping-boot.jar docker-sping-boot.jar
ENTRYPOINT ["java", "-jar", "/docker-sping-boot.jar"]