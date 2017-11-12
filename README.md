 Primary tools/frameworks used: 
 
                JDK 8, Spring boot, Docker, Maven, Mockito/JUnit, Gson
 
 
 4 ways to run and test the application viz.

   1) Running locally using maven and test by running the 'PlaylistResourceTests' integration test class.
              
              
               Build : mvn clean install -Dspring.profiles.active=non-prod
              
               Run locally: mvn spring-boot:run -Drun.profiles=non-prod

   2) Running 'LaunchApplication' class as a Spring boot configuration with profile set as 'non-prod' (for reference, 'LaunchApplication.png' image has been uploaded to repo)
   
   3) Running locally using maven and test by running Postman scripts (scripts have been uploaded to repo)
   
   
               Build : mvn clean install -Dspring.profiles.active=non-prod
              
               Run locally: mvn spring-boot:run -Drun.profiles=non-prod


   4) Create a docker image of the application and test by running Postman scripts (scripts have been uploaded to git repo)
   
   
               Build : mvn clean install -Dspring.profiles.active=non-prod
               
               Build Docker image (tag will be 'latest' by default): docker build -t playlists .
               
               Run Docker image in non-prod or local system (sample): docker run -e "SPRING_PROFILES_ACTIVE=non-prod" -p 8080:8080 -d playlists:latest
      



Miscellaneous information

Run Docker image in PROD (sample):             
    
                  *docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 444:8443 -p 80:8080 -d playlists:latest*

Push Docker image (sample):

                  *docker login (put in your credentials)*
                  *docker push playlists*

Pull Docker image (sample):

                  *docker pull playlists*

