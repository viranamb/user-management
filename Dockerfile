FROM openjdk:8-jre-alpine
MAINTAINER Vishwa <nambiarvishwajeet@gmail.com>

ADD */playlist-0.0.1-SNAPSHOT.jar app.jar

ADD content_preroll.json content_preroll.json

RUN sh -c 'touch /app.jar'

# expose standard http/https ports
EXPOSE 8080

# expose jmx ports for monitoring
EXPOSE 11400 11401 11402

# Run the script that puts the new relic files in place and then calls your entrypoint above
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]
