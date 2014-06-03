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
