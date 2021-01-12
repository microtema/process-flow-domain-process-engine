FROM java:8

COPY ./target/*.jar /app.jar

USER 1001

EXPOSE 8080

ENV SERVER_PORT=8080

ENTRYPOINT ["java","-jar","app.jar"]
