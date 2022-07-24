FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Usual:
# FROM openjdk:11-jre-slim
# For Raspberry Pi:
FROM arm32v7/adoptopenjdk:11-jre-hotspot 
COPY --from=build /home/app/target/celeb-mash-1.0-SNAPSHOT.jar /usr/local/lib/celeb-mash-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/celeb-mash-1.0-SNAPSHOT.jar"]
