#!/bin/bash

# Cores para saída no terminal
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Iniciando provisionamento da infraestrutura na Azure...${NC}"

# Configurações da VM
RESOURCE_GROUP="MottuChallenge"
LOCATION="brazilsouth"
VM_NAME="mottu-app-vm"
VM_SIZE="Standard_B2s"
ADMIN_USERNAME="azureuser"

# Verificar se já está autenticado no Azure
echo -e "${YELLOW}Verificando autenticação na Azure...${NC}"
az account show &> /dev/null

if [ $? -ne 0 ]; then
    echo -e "${YELLOW}Você precisa autenticar na Azure. Executando 'az login'...${NC}"
    az login
else
    echo -e "${GREEN}Já autenticado na Azure.${NC}"
fi

# Criar grupo de recursos
echo -e "${YELLOW}Criando grupo de recursos '$RESOURCE_GROUP'...${NC}"
az group create \
    --name $RESOURCE_GROUP \
    --location $LOCATION

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Grupo de recursos criado com sucesso!${NC}"
else
    echo -e "\033[0;31mErro ao criar grupo de recursos. Saindo.${NC}"
    exit 1
fi

# Criar a VM
echo -e "${YELLOW}Criando máquina virtual '$VM_NAME'...${NC}"
VM_RESULT=$(az vm create \
    --resource-group $RESOURCE_GROUP \
    --name $VM_NAME \
    --image Ubuntu2204 \
    --size $VM_SIZE \
    --admin-username $ADMIN_USERNAME \
    --generate-ssh-keys \
    --public-ip-sku Standard)

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Máquina virtual criada com sucesso!${NC}"
    
    VM_RESULT_FILE="vm_result.json"
    echo $VM_RESULT > $VM_RESULT_FILE

    if command -v jq &> /dev/null; then
        # Se o jq estiver instalado
        PUBLIC_IP=$(jq -r '.publicIpAddress' $VM_RESULT_FILE)
    else
        # Alternativa sem jq, usando grep e cut (mais básico)
        PUBLIC_IP=$(grep -o '"publicIpAddress": "[^"]*' $VM_RESULT_FILE | cut -d'"' -f4)
    fi

    # Verificar se PUBLIC_IP não está vazio
    if [ -z "$PUBLIC_IP" ]; then
        echo -e "\033[0;31mNão foi possível extrair o IP público automaticamente.${NC}"
        echo -e "${YELLOW}Por favor, obtenha o IP público manualmente no portal da Azure e informe abaixo:${NC}"
        read -p "IP Público da VM: " $PUBLIC_IP
    fi

    echo $PUBLIC_IP > vm_ip.txt
    echo -e "${YELLOW}IP da VM salvo em 'vm_ip.txt': $PUBLIC_IP${NC}"
else
    echo -e "\033[0;31mErro ao criar a máquina virtual.${NC}"
    exit 1
fi

# Abrir as portas necessárias
echo -e "${YELLOW}Abrindo portas no grupo de segurança de rede...${NC}"

echo -e "${YELLOW}Abrindo porta HTTP (80)...${NC}"
az network nsg rule create \
    --resource-group $RESOURCE_GROUP \
    --nsg-name "${VM_NAME}NSG" \
    --name allow-http \
    --protocol tcp \
    --priority 1001 \
    --destination-port-range 80 \
    --access allow

echo -e "${YELLOW}Abrindo porta Spring Boot (8080)...${NC}"
az network nsg rule create \
    --resource-group $RESOURCE_GROUP \
    --nsg-name "${VM_NAME}NSG" \
    --name allow-spring \
    --protocol tcp \
    --priority 1003 \
    --destination-port-range 8080 \
    --access allow

echo -e "${GREEN}Portas configuradas com sucesso!${NC}"

# Informações finais
echo -e "${GREEN}=============================================${NC}"
echo -e "${GREEN}Provisionamento concluído com sucesso!${NC}"
echo -e "${GREEN}Grupo de Recursos: $RESOURCE_GROUP${NC}"
echo -e "${GREEN}Nome da VM: $VM_NAME${NC}" 
echo -e "${GREEN}Usuário: $ADMIN_USERNAME${NC}"
echo -e "${GREEN}IP Público: $PUBLIC_IP${NC}"
echo -e "${GREEN}=============================================${NC}"