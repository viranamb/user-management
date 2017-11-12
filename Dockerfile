FROM dockyardaws.cloud.capitalone.com/digital_product_engineering/java8
MAINTAINER Vishwa <nambiarvishwajeet@gmail.com>

ADD */playlist-0.0.1-SNAPSHOT.jar app.jar

ADD sample.json sample.json

RUN sh -c 'touch /app.jar'

# expose standard http/https ports
EXPOSE 8080 8443

# expose jmx ports for monitoring
EXPOSE 11400 11401 11402

# Run the script that puts the new relic files in place and then calls your entrypoint above
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]
