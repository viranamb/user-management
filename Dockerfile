FROM dockyardaws.cloud.capitalone.com/digital_product_engineering/java8
MAINTAINER Cy <cy.neita@capitalone.com>

ADD */demo-0.0.1-SNAPSHOT.jar app.jar
# ADD cacerts /etc/ssl/certs/java/cacerts
#ADD newrelic newrelic
ADD rtmqa-clientid.jks rtmqa-clientid.jks
ADD rdt-nsb-turing.jks rdt-nsb-turing.jks

#COPY docstart.sh /docstart.sh
#RUN chmod 544 /docstart.sh

# set timezone to eastern timezone
# RUN ln -snf /usr/share/zoneinfo/EST5EDT /etc/localtime
#ENV TZ=America/New_York
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN sh -c 'touch /app.jar'
#ENV JAVA_OPTS="-Xms1024m -Xmx1024m -Xmn384m -javaagent:/newrelic/newrelic-agent-3.27.0.jar"

# expose standard http/https ports
EXPOSE 8080 8443

# expose jmx ports for monitoring
EXPOSE 11400 11401 11402

# ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]

# Run the script that puts the new relic files in place and then calls your entrypoint above
#CMD ["/docstart.sh"]
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]
