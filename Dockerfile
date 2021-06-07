FROM adoptopenjdk:11
WORKDIR /home/ubuntu
RUN mkdir -p server/undertheriver-sgsg-backend/build/libs
COPY ./build/libs/sgsg-0.0.1-SNAPSHOT.jar ./server/undertheriver-sgsg-backend/build/libs/sgsg-0.0.1-SNAPSHOT.jar
CMD ["nohup","java", "-jar", "-Dspring.profiles.active=develop", "./server/build/libs/sgsg-0.0.1-SNAPSHOT.jar", ">>/home/ubuntu/deploy.log", "2>&1", "&"]