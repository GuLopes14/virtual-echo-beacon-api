
# Sistema EchoBeacon 🚨

## Integrantes 👥

- Gustavo Lopes Santos da Silva - RM: 556859  
- Renato de Freitas David Campiteli - RM: 555627  
- Gabriel Santos Jablonski - RM: 555452  

## Visão Geral

O projeto em desenvolvimento para a empresa **Mottu** tem como objetivo implementar uma solução tecnológica para **melhorar a organização e a localização das motos** no pátio da empresa, facilitando a **gestão e identificação** de cada veículo de forma mais eficiente. A solução é composta pelos seguintes componentes:

1. **EchoBeacon**: Pequenas placas eletrônicas instaladas em cada moto, com **sistema de som (alarme)** e **LED** para identificação rápida 🔊💡  
2. **Sistema de Cadastro**: Desenvolvido em **Java** e **NextJS**, registra as informações das motos no banco de dados 🧾  
3. **Aplicativo Móvel**: Conectado ao banco de dados, permite aos colaboradores consultar os detalhes das motos e **ativar o alarme e LED** para localização 📱  

## Objetivo 🎯

Resolver o problema de identificar rapidamente as motos no pátio, otimizando a gestão e melhorando a eficiência da empresa **Mottu**.

## Configuração ⚙️

### Pré-requisitos
- **Conta na Azure**: Para criar e gerenciar a máquina virtual.
- **Docker Instalado**: No servidor remoto (VM criada na Azure).
- **Java Development Kit (JDK)**: Para compilar o projeto Java.
- **Maven**: Para o gerenciamento de dependências do projeto Java.

### Passos

1. **Baixe a imagem no seu Docker Desktop**
    docker build -t seuUsuarioDockerHub/ride-echo-api .  

2. **Acesse a sua conta da azure**
    ```bash
    az login
    ```
3. **Acesse o diretório de scripts**
    ```bash
    cd ./scripts/
    ```

4. **Crie uma VM na Azure**
    ```bash
    bash create-vm.sh
    ```
    - **Nota:** O script `create-vm.sh` irá criar uma VM na Azure e registrar o IP público em um arquivo chamado `vm_ip.txt`.

5. **Execute o arquivo de instalação do docker**
    ```bash
    bash install_docker.sh
    ```

6. **Baixe o Docker na VM**
    ```bash
    bash install_docker.sh
    scp install_docker_remote.sh azureuser@<IpFornecido>:~/
    ssh azureuser@<IpFornecido> 'bash install_docker_remote.sh'
    ```
     - **Nota:** Substitua `<IpFornecido>` pelo IP público fornecido no terminal, caso estiver com dificuldades de achar, procure no arquivo `vm_ip.txt`.

7. **Faça login na VM**
    ```bash
    ssh azureuser@<IpFornecido>
    ```

8. **Faça login no docker**
    ```bash
    docker login
    ```

9. **Resgate a imagem criada anteriormente**
    ```bash
    docker pull seuUsuarioDockerHub/ride-echo-api
    ```

10. **Execute a imagem do Docker na VM da Azure**
    ```bash
    docker run -p 8080:8080 -d gustal14/ride-echo-api
    ```

## Uso 🚀

- **Acesso à aplicação**: Após o container estar em execução, acesse a API pelo postman/ insomnia, etc.
- **URL**: `http://<ipFornecidoDaVM:8080/`

# 📘 Exemplos de Requisições para a API 

## 🔹 Criar um EchoBeacon (POST)
- POST ipFornecidoDaVM:8080/echo-beacons
- Content-Type: application/json
```http
{
  "numeroIdentificacao": 4,
  "status": "DESATIVADO",
  "versaoFirmware": "v1.0.0",
  "statusConexao": "CONECTADO",
  "dataRegistro": "2025-04-19"
}
```

## 🔹 Atualizar um EchoBeacon (PUT)
- PUT ipFornecidoDaVM:8080/echo-beacons/4
- Content-Type: application/json
```http
{
  "numeroIdentificacao": 4,
  "status": "ATIVO",
  "versaoFirmware": "v1.0.0",
  "statusConexao": "CONECTADO",
  "dataRegistro": "2025-04-19"
}
```

## 🔹 Criar uma Moto (POST)
- POST ipFornecidoDaVM:8080/motos
- Content-Type: application/json
```http
{
  "placa": "XYZ5678",
  "chassi": "1HGCM82633A654321",
  "modelo": "MOTTU_SPORT",
  "problema": "Problema no motor",
  "echoBeacon": {
    "id": 4
  },
  "dataRegistro": "2025-05-10"
}
```

## 🔹 Atualizar uma Moto (PUT)

- PUT ipFornecidoDaVM:8080/motos/4
- Content-Type: application/json
```http
{
  "placa": "XYZ5678",
  "chassi": "1HGCM82633A654321",
  "modelo": "MOTTU_SPORT",
  "problema": "Problema no motor",
  "echoBeacon": {
    "id": 4
  },
  "dataRegistro": "2025-05-10"
}
```

## ❌ Deletar uma Moto (DELETE)
```
ipFornecidoDaVM:8080/motos/4
```

## ❌ Deletar um EchoBeacon (DELETE)
```
ipFornecidoDaVM:8080/echo-beacons/4
```
