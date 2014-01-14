basis-java-api
==============

Library to download your raw Basis B1 device data from the mybasis website. 
You can learn more about Basis at [http://www.mybasis.com/](http://www.mybasis.com/)


## Instructions 

You can compile the source, or if you want to quickly download your data: 

1. Download [https://github.com/hof/basis-java-api/releases/download/v0.1/basis-download-0.1-jar-with-dependencies.jar] (https://github.com/hof/basis-java-api/releases/download/v0.1/basis-download-0.1-jar-with-dependencies.jar) 


2. Open a command prompt, go to the directory where you downloaded the .jar file and: 

`java -jar basis-download-0.1-jar-with-dependencies.jar <username> <password> YYYY-MM-DD <start_offset> <end_offset>`


Username and password are your mybasis.com credentials. 

YYYY-MM-DD is the year, month, day of that day you want to download. 

start_offset and end_offset should be 0. 

After the program completes, your data will be in the following files in the current directory: 

*me.json* -> your profile data

*basis-data-YYYY-MM-DD.json* -> your raw data for the given day

*activities-YYYY-MM-DD.json* -> your activities for the given day

