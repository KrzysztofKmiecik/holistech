# Holistech

## Bussines requirements
There is a business need to have systemic possibility to block fixture on production floor.

## Project requirements
- [x] as a user with role TESTER/QUALITY/USER  I would like to log in to be able to block fixture in FIS
- [x] as a user with role TESTER/QUALITY/USER I would like to have recorded fixture status change or name  in history log and email
- [x] as a user with role TESTER/QUALITY/USER  I would like to see history log
- [x] as a user with role TESTER/QUALITY/USER  I would like to see fixture status
- [x] as a user with role TESTER/QUALITY/USER I would like to log in to be able to see status of fixture
- [x] as a user with role TESTER/QUALITY/USER I would like to have possibilities to choose languages Polski|English|Deutsch|Pyсский
- [x] as a user with role TESTER/QUALITY/USER I would like to use API for this
- [x] as a user with role TESTER/QUALITY/USER I would like to have 
- [x] as a user with role TESTER  I would like to login to be able as the only one to create new fixture (name and Type(ICT,SMT_MOL))
- [x] as a user with role QUALITY I would like to login to be able as the only one to unlock fixture in FIS


## Solution

![](src/main/resources/static/photos/Holistech.jpg)
![](src/main/resources/static/photos/screenshots/Holistech_Login.jpg)
![](src/main/resources/static/photos/screenshots/Holistech_TESTER.png)
![](src/main/resources/static/photos/screenshots/Holistech_QUALITY.jpg)
![](src/main/resources/static/photos/screenshots/Holistech_USER.jpg)
![](src/main/resources/static/photos/screenshots/Holistech_historyLog.png)
![](src/main/resources/static/photos/screenshots/Holistech_API.jpg)

### OpenPoints
- [x] FIS
- [x] email
- [x] Locale
- [x] Profiles (dev(H2),prod)
- [x] Inputs validation (frontend/backend)
- [x] Suggestion for inputs (SetOK,SetNOK,FixtureName)
- [x] Patterns for inputs (SetOK,SetNOK,FixtureName) 
- [x] Transfer inputs  to capital letters 
- [ ] Tests -> ongoing
- [X] REST API with Swagger UI
- [x] log history
- [x] healthChecks metrics, prometheus, Grafana
- [x] Docker
- [ ] Holistech email -> canceled due to the IT Security  
- [ ] Holistech API -> waiting for IT feedback

### Used Technologies 
- SpringBoot (Spring Data, Spring Security)
- Thymeleaf with Bootstrap
- Swagger UI

## Info
### new jar creation
- mvn clean package spring-boot::repackage 
- java -jar holistech.jar
### docker
- docker-compose -f "/Volumes/RAM Disk/holistech/docker-compose.yml" up -d
### links
- localhost:8090/fixtures ->app
- localhost:8090/actuator ->metrics
- localhost:9090 -> prometheus
- localhost:3000 -> grafana (login:admin pass:admin) ; url http://prometheus:9090; import jvm Micrometer ID 4701
### metrics
- http_server_requests_seconds_count
- http_server_requests_seconds_sum
- hikaricp_connections_usage_seconds_count
- hikaricp_connections
### obsolete
- old -> docker pull prom/prometheus, docker run -p 9090:9090 prom/prometheus, localhost:9090,
- old -> docker pull grafana/grafana, docker run -p 3000:3000 grafana/grafana  haslo:admin pass:admin, add datasource prometheus,go inside prometheus container,inside contener ifconfig ->eth0 172.17.0.2:9090

