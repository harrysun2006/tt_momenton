### Task Requirement
Build an organisation chart from input data

### Setup
- [Install JDK 12](https://www.oracle.com/java/technologies/javase/jdk12-archive-downloads.html)
- [Install Gradle](https://gradle.org/install/)
- [Install Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- Clone this repo `git clone `

### Build
```
cd tt_momenton
# org_chart.jar will be created in current folder
./gradlew build
```

### Run
```
# run from gradle
./gradlew run --args='./test.txt'
# run from java
java -jar org_chart.jar ./test.txt
```