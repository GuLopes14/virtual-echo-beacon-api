#!/bin/bash

# Cores para saída no terminal
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Iniciando instalação do Docker na VM...${NC}"

# Atualizar os pacotes
echo -e "${YELLOW}Atualizando pacotes...${NC}"
sudo apt-get update
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao atualizar pacotes. Verifique sua conexão com a internet.${NC}"
    exit 1
fi

# Instalar pacotes necessários
echo -e "${YELLOW}Instalando pacotes necessários...${NC}"
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao instalar pacotes necessários.${NC}"
    exit 1
fi

# Adicionar chave GPG oficial do Docker
echo -e "${YELLOW}Adicionando chave GPG do Docker...${NC}"
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao adicionar chave GPG do Docker.${NC}"
    exit 1
fi

# Adicionar repositório do Docker
echo -e "${YELLOW}Adicionando repositório do Docker...${NC}"
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao adicionar repositório do Docker.${NC}"
    exit 1
fi

# Atualizar novamente com o novo repositório
echo -e "${YELLOW}Atualizando lista de pacotes...${NC}"
sudo apt-get update
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao atualizar lista de pacotes.${NC}"
    exit 1
fi

# Instalar Docker
echo -e "${YELLOW}Instalando Docker...${NC}"
sudo apt-get install -y docker-ce docker-ce-cli containerd.io
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao instalar Docker.${NC}"
    exit 1
fi

# Adicionar usuário ao grupo docker
echo -e "${YELLOW}Adicionando usuário ao grupo docker...${NC}"
sudo usermod -aG docker $USER
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao adicionar usuário ao grupo docker.${NC}"
    exit 1
fi

# Iniciar e habilitar o serviço Docker
echo -e "${YELLOW}Configurando Docker para iniciar com o sistema...${NC}"
sudo systemctl enable docker
sudo systemctl start docker
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao configurar Docker para iniciar com o sistema.${NC}"
    exit 1
fi

# Verificar instalação
echo -e "${YELLOW}Verificando instalação do Docker...${NC}"
docker --version
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao verificar versão do Docker. A instalação pode ter falhado.${NC}"
    exit 1
fi

echo -e "${GREEN}=============================================${NC}"
echo -e "${GREEN}Docker instalado com sucesso!${NC}"
echo -e "${GREEN}=============================================${NC}"