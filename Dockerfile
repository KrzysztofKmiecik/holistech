FROM openjdk:11-jdk-oraclelinux8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} holistech.jar
EXPOSE 8090
RUN echo "Europe/Warsaw" > /etc/timezone

ENTRYPOINT ["java","-jar","/holistech.jar"]