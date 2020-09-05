# TestCodeGenerator
A simple web-based tool to create Test Codes. These codes are generated into a csv and downloaded to your PC

https://password-test-code-generator.herokuapp.com

# Architecture
Backend Designed with a reactive library (Vert.x) and Java 11
Frontend designed using HTML5, Materialize and Vanilla JavaScript

# How to run

## Using Docker

* `docker pull mcadecio/testcodegen`
* `docker run --name testcodegen -d -p 80:80 mcadecio/testcodegen`
* www.localhost:80

## Using a Jar

* `mvn clean package`
* `java -jar target/testcodegen-1.0-SNAPSHOT.jar`
* www.localhost:80


### In case you are wondering what it looks like
https://github.com/mcadecio/TestCodeGenerator/blob/master/example/frontend_pic.PNG

### Example Output
https://github.com/mcadecio/TestCodeGenerator/blob/master/example/example_output.csv
