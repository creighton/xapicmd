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
