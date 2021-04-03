FROM openjdk:11-jre-slim
COPY ./build/libs/*.jar kkoribyeol.jar
ENTRYPOINT ["java", "-Xmx200m", "-jar", "-Duser.timezone=Asia/Seoul", "/kkoribyeol.jar"]