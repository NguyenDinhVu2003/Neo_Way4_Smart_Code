FROM openjdk:17
EXPOSE 8080
ADD target/Smart_code_CICD.jar Smart_code_CICD.jar
ENTRYPOINT ["java","-jar","Smart_code_CICD.jar"]