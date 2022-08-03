# Agrotis BFF
Demonstração Spring/Java baseado nos desafio técnico da Agrotis.

## Instruções de desenvolvimento
Vide AgrotisTesteBackEnd.pdf  
[Protótipo](https://www.figma.com/proto/PXi5PcZWks8Z7veqA8WU30/Agrotis-Teste-(Front-end)?node-id=1%3A760&starting-point-node-id=1%3A760) 

## Collections
Disponíveis do arquivo 'Agrotis_bff.postman_collection.json'  
Obs.: Cadastre pelo menos uma propriedade e um laboratório para inserir clientes.

## Como testar e revisar
 1 Chamar endpoint laboratorio/insert ou  laboratorio/find (se já houver cadastro) para setar a variável {{idLaboratorio}};
 2 Chamar endpoint propriedade/insert ou  propriedade/find (se já houver cadastro) para setar a variável {{idPropriedade}};
 3 Chamar endpoint cliente/salvar para cadastrar;
 4 Chamar endpoint cliente/find para setar a variável {{idPropriedade}};
 5 Chamar endpoint cliente/salvar para alterar os dados de cadastro;
 Obs.:  
 - endpoints find e insert sempre setam variáveis de ambiente dos respectivos modelos (Laboratório, Propriedade e Cliente), para uso nos endpints: get, update, remove.
 - O principal endpoint do desafio é o "cliente/salvar", implementado no resource ClienteResource.java.

## Padrões de Implementação
 - SOLID / Clean Code
 - RestFUL API with HETEOAS. Obs.: O uso de HETEOS pode ser desabilitado via Applications.yaml (performance).
 - Foram utilizadas abstrações, disponíveis no pacote "core" para padronizinação e reutilização de código.
 - Resources, services e repositories desacoplados via IoC (Inversão de controle) e Programatic Injection.
 - Versionamento de api auto gerenciada, baseado na qualificação dos componente de serviços.
 - Foi utilizado o conceito de visão (Jackson View) para evitar a utilização de DTOs na construção de CRUDs.   
 - API documentation via Swagger (Personalização via Applications.yaml).
 
 ## Notas
 - Devido ao prazo do desafio elaborei os testes unitários apenas do fluxo principal da aplicação. 
 - Um [erro](https://stackoverflow.com/questions/61656985/jackson-module-not-registered-after-update-to-spring-boot-2) no rgistro do modulo de serialização 'jackson-datatype-hibernate5' na versão do SPRING/JACSON utilizada, impediu que as entidades de relacionamento JPA fossem utilizadas com LAZY LOAD.