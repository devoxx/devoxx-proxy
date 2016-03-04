FROM java:8
VOLUME /tmp
ADD devoxx-proxy-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dyoutube.apiKey=AIzaSyAsW6-dsm6fcNtPqx_etSbArMAFK-IEOaE","-jar","/app.jar"]