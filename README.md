# playlists

Commands for non-prod environment

Build : *clean install -Dspring.profiles.active=non-prod*

Run locally: *mvn spring-boot:run -Drun.profiles=non-prod*

Build Docker image: *docker build -t dockyardaws.cloud.capitalone.com/coding-exercise:latest .*

Run Docker image in QA or local system: *docker run -e "SPRING_PROFILES_ACTIVE=non-prod" -p 444:8443 -d dockyardaws.cloud.capitalone.com/coding-exercise:latest*

Run Docker image in PROD: *docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 444:8443 -d dockyardaws.cloud.capitalone.com/coding-exercise:latest*

Pull Docker image:
# playlistDOs
*docker pull dockyardaws.cloud.capitalone.com/coding-exercise:latest*
