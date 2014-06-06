# Command Line Java Client using Java xAPI Library

## Installing / Set up
This project uses Maven for building and configuration. It is dependent on  
- google gson - can install from maven central
- asg cliche - included in lib
- adl jxapi - included in lib

You will need to install cliche and jxapi to your local maven repository. To do this  
__cliche__
```
> mvn install:install-file -Dfile=lib/asg.cliche-110413.jar -DgroupId=asg.cliche -DartifactId=cliche -Dversion=11.04.13 -Dpackaging=jar
```  
__jxapi__
```
> mvn install:install-file -Dfile=lib/xapi-1.0-SNAPSHOT.jar -DgroupId=gov.adlnet.xapi -DartifactId=xapi -Dversion=1.0 -Dpackaging=jar
```  
Then build the jar with dependencies:  
```
> mvn clean compile assembly:single
```  
Then run the jar
```
> java -jar target/xapicmd-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```  

### Importing project in Eclipse  
- clone project `git clone <url to your fork>`
- File > Import > Maven > Maven Projects
- choose root directory of project
- click finish

### Running the project from the executable jar
The xapicmd jar at the root of the project is executable and contains all of the dependencies. You can run the app by simply  
- open a command promt
- navigate to the root project folder
- run `> java -jar xapicmd-0.0.1-SNAPSHOT-jar-with-dependencies.jar`

## Use
This is a simple application shell allowing you to interact with the jxapi library. Use `?list` to show a current list of commands. Each xAPI endpoint is broken into its own sub shell.
```
> java -jar xapicmd-0.0.1-SNAPSHOT-jar-with-dependencies.jar 
Welcome to the XAPICmd tool. Type '?list' for a list of commands.
xAPI Commands
xapi> ?list
abbrev	name	params
s	     statement	()
c	     config	(endpoint, username, password)
xapi> s
Statements
xapi/statements> ?list
abbrev	name	params
	      get	()
xapi/statements> get
4ccfa41f-6c4c-43e3-bb76-9d1146294449: Tyler Mulligan experienced How to Make French Toast Chapter: glossary, page: glossary
0f22ac5e-2b98-4f2c-913e-ea62fa992cac: Tyler Mulligan experienced How to Make French Toast Chapter: glossary, page: butter
6235e2bb-60c3-47b3-abea-1df90a5b989f: Tyler Mulligan experienced How to Make French Toast Chapter: glossary, page: glossary
41ac4d11-bb08-46ac-ac49-dae25cb02faa: Tyler Mulligan experienced How to Make French Toast Chapter: glossary, page: bread
2bd32329-cb16-4785-89c3-54794e23f518: Tyler Mulligan experienced How to Make French Toast Chapter: glossary, page: glossary
...
```
### Statement Sub Shell
The statement sub command shell allows interaction with the Statement endpoint.  
Type s at the xapi shell prompt to enter the statement sub shell:  
```
xapi> s
xapi/statements> 
```   

#### Statement Get
From the statement sub shell you can issue GET requests to the LRS to retrieve statements:  
```
xapi/statements> get
d6a21c06-ecd8-11e3-b464-005056a25e99: mailto:tom@example.com http://adlnet.gov/expapi/verbs/asked q://do/you/like/green-eggs-n-ham
46f189cd-5371-407d-acd6-7848a6fcd814: Jason Haag experienced How to Make French Toast Chapter: toc, page: toc
a73a5728-85ed-40a7-9752-45bba1a3cf84: Jason Haag experienced How to Make French Toast Chapter: toc, page: toc
0bf118bb-3acf-4619-a509-5ddd541c44c0: Jason Haag experienced How to Make French Toast Chapter: toc, page: toc
9cda8160-801e-4a19-9f6f-aa91eafc9ab9: Jason Haag experienced How to Make French Toast Chapter: toc, page: toc
...
```  
It also supports some of the filter parameters [agent, verb, activity, related_agents, related_activities]:  
```
xapi/statements> get activity=act:adlnet.gov/JsTetris_XAPI related_activites=true
aff4b15a-a2f6-4153-9e0c-8f1280ae6213: Andron Radu finished Js Tetris - xAPI Prototype
02098b2c-d649-4c9b-ba70-c1dec8921bc1: Andron Radu started Js Tetris - xAPI Prototype
732d9bd5-1822-4c7f-9028-017c654897ef: tom creighton started Js Tetris - xAPI Prototype
...
```  

##### Get Single Statement by GUID  
You can get a single statement by guid:
```
Welcome to the XAPICmd tool. Type '?list' for a list of commands.
xAPI Commands
xapi> s
Statements
xapi/statements> getstmt 22cdb531-2cae-4cb0-9214-d8c7dc5fc860
22cdb531-2cae-4cb0-9214-d8c7dc5fc860: Rob rezzed Trapdoor2
xapi/statements> exit
xapi> exit
Exiting XAPICmd tool.
```

#### Statement Send
From the statement sub shell you can issue POST requests to send a statement to the LRS:
```
xapi/statements> send '{"actor":{"mbox":"mailto:tom@example.com", "name":"tom"}, "verb":{"id":"http://tom.example.com/xapi/verbs/loves", "display":{"en-US":"loves"}}, "object":{"id":"http://i.luv.it/green-eggs-n-ham"}}'
64913788-ecdc-11e3-b464-005056a25e99
```
