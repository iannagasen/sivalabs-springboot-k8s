repository.findAll()
  - returning the entities directly to the consumer as a response because any change
    in database schema will affect the response
  - it is better to load data into DTO (since we are only reading) than loading an Entity
    - Used DTO Projection using Flyway Migration

H2 Link: http://localhost:8080/h2-console/ 
  jdbc-url: [Check from logs]
  username: sa
  password: [blank]

SpringBoot supports 2 Proper Database Migration Tool
  1. Flyway
  2. Liquibase

Flyway migration follow a naming pattern:
  Ex: V1__create_bookmarks_table.sql

After creating the migration script, check in H2 flyway_schema_history

you can query: select * from "flyway_schema_history";

installed_rank  	version  	description  	                              type    script  	                        checksum  	  installed_by    installed_on                execution_time  success  
-1	              null	    << Flyway Schema History table created >>	  TABLE	                                    null	        SA	            2023-04-03 22:52:58.904804	0	              TRUE
1	                1	        create bookmarks table	                    SQL	    V1__create_bookmarks_table.sql	  509433377	    SA	            2023-04-03 22:52:58.924826	7	              TRUE


NOTE: if you change the V1__create_bookmarks_table.sql, it will produce a different checksum, and the compilation fails.
Checksums validate that the script is the same.
RULE: Dont touch the script once it is executed.

Check FlywayAutoConfiguration class
  from org.springframework.boot.autoconfigure.flyway
  then
Check DatabaseDriver class
  from org.springframework.boot.jdbc.DatabaseDriver;

{vendor} -> productName.toLowerCase();
H2 -> h2

-----------
5. Using Spring Data Jpa DTO Projections

in the List View, we dont need to expose all the properties and columns from database.
  - We should load only what we needed

SEE docs for sprind data jpa projections
2 Ways to create Projections:
  1. Class-based projection using DTO
  2. Interface-based

