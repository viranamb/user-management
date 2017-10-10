# data-migration

Commands for non-prod environment

Build : *mvn clean install -Dspring.profiles.active=qa*

Run locally: *mvn spring-boot:run -Drun.profiles=qa*

Build Docker image: *docker build -t dockyardaws.cloud.capitalone.com/small-business-direct-lending/httpd:migration .*

Run Docker image locally: *docker run -e "SPRING_PROFILES_ACTIVE=qa" -p 443:8443 -d dockyardaws.cloud.capitalone.com/small-business-direct-lending/httpd:migration*

