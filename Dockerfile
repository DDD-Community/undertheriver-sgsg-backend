FROM adoptopenjdk:11-jre
WORKDIR /home/ubuntu
CMD ["mkdir", "-p", "server/build/libs"]
COPY ./build/libs/sgsg-0.0.1-SNAPSHOT.jar ./server/build/libs/sgsg-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "-Dspring.profiles.active=develop", "./server/build/libs/sgsg-0.0.1-SNAPSHOT.jar"]