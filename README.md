mvn clean install

cp target/ride-echo-beacon-api-0.0.1-SNAPSHOT-native.jar .

docker login

docker build -t ride-echo-api . 

docker run -p 8080:8080 ride-echo-api