# Base image
FROM eclipse-temurin:21

# Mapeia a porta 8080 do container para a porta 8080 do hospedeiro
EXPOSE 8080

# Copie o código da aplicação para o container
COPY . /app

# Define o comando para executar a aplicação dentro do container
ENTRYPOINT ["java", "-jar", "/app/ride-echo-beacon-api-0.0.1-SNAPSHOT-native.jar"]
