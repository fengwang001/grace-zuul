FROM maven:3.5.4-alpine
ADD . /app
WORKDIR /app/
RUN mvn clean package
EXPOSE 8771
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar target/qiyi-zuul-server-0.0.1-SNAPSHOT.jar