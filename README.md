REST-based microservice utilizing JDK 8, Spring boot, Docker, Maven, Mockito/JUnit.



** Spring-boot REST service which provides following User related functionalities
 1. Sign-up a new user
 2. Sign-in and get a JWT token
 3. Get details of a user with given UUID.
 4. Search user by a given email address

 Embedded In-memory H2 database used
 

** Testing
 
3 ways to run and test the application viz.

   1) Running 'LaunchApplication' class as a Spring boot configuration with profile set as 'non-prod' (for reference, 'LaunchApplication.png' image has been added) and then test using Postman scripts (scripts have been uploaded to repo)
   
   2) Running locally using maven and test by running Postman scripts

               Build : mvn clean install -Dspring.profiles.active=non-prod
               Run locally: mvn spring-boot:run -Drun.profiles=non-prod

   3) Create a docker image of the application and test by running Postman scripts (scripts have been added)

               Build : mvn clean install -Dspring.profiles.active=non-prod
               Build Docker image (tag will be 'latest' by default): docker build -t users .
               Run Docker image in non-prod or local system (sample): docker run -e "SPRING_PROFILES_ACTIVE=non-prod" -p 8080:8080 -d users:latest


** Miscellaneous information

Run Docker image in PROD (sample):             
    
                  *docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 444:8443 -p 80:8080 -d users:latest*

Push Docker image (sample):

                  *docker login (put in your credentials)*
                  *docker push users*

Pull Docker image (sample):

                  *docker pull users*
