Maven commands

Build : *mvn clean install -Dspring.profiles.active=non-prod*

Run locally: *mvn spring-boot:run -Drun.profiles=non-prod*



Docker commands

Build Docker image (sample): *docker build -t playlists .*

Run Docker image in non-prod or local system (sample): *docker run -e "SPRING_PROFILES_ACTIVE=non-prod" -p 8080:8080 -d playlists:latest*

Run Docker image in PROD (sample): *docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 444:8443 -p 80:8080 -d playlists:latest*

Push Docker image (sample):
*docker login (put in your credentials)*
*docker push playlists*

Pull Docker image (sample):
*docker pull playlists*
