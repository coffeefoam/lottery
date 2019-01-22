FROM java:8
EXPOSE 8081

VOLUME /tmp
ADD lottery-api.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
