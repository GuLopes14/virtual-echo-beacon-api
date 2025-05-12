#!/bin/bash

# Cores para saída no terminal
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Script de instalação do Docker na VM Azure...${NC}"

# Obter IP da VM
if [ -f "vm_ip.txt" ]; then
    VM_IP=$(cat vm_ip.txt)
    echo -e "${GREEN}IP da VM: $VM_IP${NC}"
else
    echo -e "${YELLOW}Arquivo vm_ip.txt não encontrado. Por favor, informe o IP da VM:${NC}"
    read -p "IP da VM: " VM_IP
fi

VM_USER="azureuser"

# Exibir instrução de conexão
echo -e "\n${BLUE}O script vai gerar o comando de instalação do Docker.${NC}"
echo -e "${BLUE}Será necessário conectar-se à VM e executar o script gerado.${NC}\n"

# Criar o arquivo de script de instalação do Docker
cat > install_docker_remote.sh << 'EOF'
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
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
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

# Criar diretório para persistência dos dados
echo -e "${YELLOW}Criando diretório para persistência de dados...${NC}"
mkdir -p ~/app-data
if [ $? -ne 0 ]; then
    echo -e "${RED}Erro ao criar diretório para persistência.${NC}"
    exit 1
fi

echo -e "${GREEN}=============================================${NC}"
echo -e "${GREEN}Docker instalado com sucesso!${NC}"
echo -e "${GREEN}Diretório para persistência criado: ~/app-data${NC}"
echo -e "${GREEN}=============================================${NC}"
echo -e "${YELLOW}Observação: Você precisará abrir uma nova sessão SSH ou executar 'newgrp docker' para usar o Docker sem sudo${NC}"
EOF

# Tornar o script executável
chmod +x install_docker_remote.sh

echo -e "${GREEN}Script de instalação do Docker criado: install_docker_remote.sh${NC}"
echo -e "${GREEN}=============================================${NC}"
echo -e "${YELLOW}Para instalar o Docker na VM, você tem duas opções:${NC}"
echo -e "\n${BLUE}Opção 1: Copiar o script para a VM e executá-lo:${NC}"
echo -e "scp install_docker_remote.sh $VM_USER@$VM_IP:~/"
echo -e "ssh $VM_USER@$VM_IP 'bash install_docker_remote.sh'"

echo -e "\n${BLUE}Opção 2: Conectar-se à VM e executar os comandos manualmente:${NC}"
echo -e "ssh $VM_USER@$VM_IP"
echo -e "# Após conectar, copie e cole o conteúdo do arquivo install_docker_remote.sh"

echo -e "\n${GREEN}=============================================${NC}"
echo -e "${YELLOW}Após a instalação do Docker, você poderá implantar sua aplicação Spring Boot com:${NC}"
echo -e "docker run -d --name mottu-spring-app -p 8080:8080 -v ~/app-data:/data --restart unless-stopped seu-dockerhub/mottu-app:latest"
echo -e "${GREEN}=============================================${NC}"