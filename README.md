TechIT is a work order system.

It incorporates following use-cases:
1) User reports an issue(raise a ticket) to a particular Unit in the organization. 
2) Supervisor of a unit assigns that ticket to Technician(s) of same unit.
3) Technician(s) work on that ticket to resolve and updates the status as and when required.
4) User, Technician, Supervisor, Admin all can track end to end flow of the ticket based on their roles.

Tech-Spec:

Implementation (Java-Spring-Hibernate)
1) Designed models from scratch. and providing their DAO interfaces and JPA implementations.
2) Tested DAO methods using TestNG.
3) Developed Rest endpoints using Java-Spring with JWT Token Authentication.
4) Used Hibernate as ORM which interacted with MySQL database.
5) Unit test of each Rest endpoints using Spring MockMvc.
6) Built using Maven and deployed on Tomcat Apache.
7) Used GitHub for version control.
