# playlists

Commands for non-prod environment

Build : *clean install -Dspring.profiles.active=non-prod*

Run locally: *mvn spring-boot:run -Drun.profiles=non-prod*

Build Docker image: *docker build -t dockyardaws.cloud.capitalone.com/small-business-direct-lending/httpd:migration .*

Run Docker image in QA or local system: *docker run -e "SPRING_PROFILES_ACTIVE=qa" -p 444:8443 -d dockyardaws.cloud.capitalone.com/small-business-direct-lending/httpd:migration*

Run Docker image in PROD: *docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 444:8443 -d dockyardaws.cloud.capitalone.com/small-business-direct-lending/httpd:migration*

Execute curl command to initiate data transfer process (Sample command):
*curl -ik -X POST 'https://localhost:444/orchestrator/migrate?pathToFile=export_copy.dsv' -H 'authorization: Bearer eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwicGNrIjoxLCJhbGciOiJkaXIiLCJ0diI6Miwia2lkIjoiYTdxIn0..auwh0B07sxZPy5b8RatARQ.caHXKW56egjurulfvhuBqRUuU3D34xL1Iol07-75SCC15ZUEshOqtEp8wYYJf2y4xMHHi0zJcziXz2Ia7nb2CM9p63p1Pfx-cTjeQUwU6bkhkvOq2jqMf3fMB65DVaEtYOwQp1HHMZG76indCnW2KQoswVL5VbW3HysYkXws17iTLMyCM_RAl__JaatBjTTs-8eoaBiRz0xjGQhwy7M3hy1GWqVsDZ6nD7Zb8RNiNpLZXpJNyZZj9TrBbTvw43ry.JQAp-QtFhi57vlGlCPfoCw'*

Push Docker image:
*docker push dockyardaws.cloud.capitalone.com/small-business-direct-lending/httpd:migration*

Pull Docker image:
# playlistDOs
*docker pull dockyardaws.cloud.capitalone.com/small-business-direct-lending/httpd:migration*
