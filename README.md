basis-java-api
==============

Library to download your raw Basis B1 device data from the mybasis website. 
You can learn more about Basis at [http://www.mybasis.com/](http://www.mybasis.com/)


## Instructions 

You can compile the source, or if you want to quickly download your data: 

1. Download [https://github.com/hof/basis-java-api/releases/download/v0.2/basis-download-0.2-jar-with-dependencies.jar] (https://github.com/hof/basis-java-api/releases/download/v0.2/basis-download-0.2-jar-with-dependencies.jar) 


2. Open a command prompt, go to the directory where you downloaded the .jar file and: 

`java -jar basis-download-0.2-jar-with-dependencies.jar <username> <password> YYYY-MM-DD`

Username and password are your mybasis.com credentials. 

YYYY-MM-DD is the year, month, day of that day you want to download. 

After the program completes, your data will be in the following files in the current directory: 

*me.json* -> your profile data

*basis-data-YYYY-MM-DD.json* -> your raw data for the given day

*activities-YYYY-MM-DD.json* -> your activities for the given day

#### Example output 

```

Basis Data Download 0.2
Copyright (C) 2014 Erik van het Hof (hof@hofcom.nl @erikvanhethof)

Logging in: 302 Found access_token --> 15112ba78ca14ea0a9740820a066042c
Profile: 200 OK Basis-ID: 510ffb2091d73079xxxxxxxx
Data: 200 OK
Activities: 200 OK

```