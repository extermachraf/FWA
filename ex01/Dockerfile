FROM maven:3.9.6-eclipse-temurin-22 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:22-jdk
RUN apt-get update && apt-get install -y wget
RUN wget https://dlcdn.apache.org/tomcat/tomcat-11/v11.0.6/bin/apache-tomcat-11.0.6.tar.gz
RUN tar xzf apache-tomcat-11.0.6.tar.gz && mv apache-tomcat-11.0.6 /usr/local/tomcat

WORKDIR /usr/local/tomcat
COPY --from=builder /app/target/servlet-app.war webapps/cinema.war
COPY src/main/resources/application.properties conf/application.properties

EXPOSE 8080
CMD ["bin/catalina.sh", "run"]