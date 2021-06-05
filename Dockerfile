FROM adoptopenjdk:11
WORKDIR /home/ubuntu
RUN mkdir server
COPY . ./server
CMD ["nohup","java", "-jar", "-Dspring.profiles.active=develop", "./server/build/libs/sgsg-0.0.1-SNAPSHOT.jar", ">>/home/ubuntu/deploy.log", "2>&1", "&"]