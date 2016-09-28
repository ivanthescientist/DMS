# DMS
Distributed in-Memory Storage with priority-based data convergence. 

###Running locally in cluster mode:

To launch locally, each server instance will need to be supplied with a configuration file 
describing all nodes in cluster, refer to sample_cluster_config.json for examples. 

Additionally to config file, due to the fact that by default server uses default Spring Boot port for data server (8080), 
an alternate server.port setting should be supplied in VM params to override the default one. 

######Example Usage (launching 3 different interconnected instances):
`java -Dserver.port=8001 -jar dms-1.0-SNAPSHOT.jar /opt/configs/instance1_config.json`

`java -Dserver.port=8002 -jar dms-1.0-SNAPSHOT.jar /opt/configs/instance2_config.json`

`java -Dserver.port=8003 -jar dms-1.0-SNAPSHOT.jar /opt/configs/instance3_config.json`

Data itself can be accessed through simplistic REST-like API: 

`GET {hostname:port}/value/{key}` - to get value by key

`PUT {hostname:port}/value/{key}` - to set value by key (request body should contain value)
