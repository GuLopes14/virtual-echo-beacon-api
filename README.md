CRIAR MAQUINA VIRTUAL
cd ./scripts/

chmod +x create-vm.sh

./create-vm.sh

INSTALAR O DOCKER

ssh azureuser@4.201.120.25

chmod +x install-docker.sh


mvn clean install

cp target/ride-echo-beacon-api-0.0.1-SNAPSHOT-native.jar .

docker login

docker build -t ride-echo-api . 

docker run -p 8080:8080 ride-echo-api