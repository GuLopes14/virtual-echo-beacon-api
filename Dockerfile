# Base image
FROM eclipse-temurin:21

# Mapeia a porta 8080 do container para a porta 8080 do hospedeiro
EXPOSE 8080

# Copie o arquivo jar gerado na pasta target para a raiz do contêiner
COPY target/ride-echo-beacon-api-0.0.1-SNAPSHOT-native.jar /app/app.jar

# Define o comando para executar a aplicação dentro do container
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
