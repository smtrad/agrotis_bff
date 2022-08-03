# Agrotis BFF
Demonstração Spring/Java baseado nos desafio técnico da Agrotis.

## Instruções de desenvolvimento
Vide AgrotisTesteBackEnd.pdf  
[Protótipo](https://www.figma.com/proto/PXi5PcZWks8Z7veqA8WU30/Agrotis-Teste-(Front-end)?node-id=1%3A760&starting-point-node-id=1%3A760) 

## Collections
Disponíveis do arquivo 'Agrotis_bff.postman_collection.json'  
Obs.: Cadastre pelo menos uma propriedade e um laboratório para inserir clientes.

## Padrões de Implementação
 - SOLID / Clean Code
 - RestFUL API with HETEOAS. Obs.: O uso de HETEOS pode ser desabilitado via Applications.yaml (performance).
 - Foram utilizadas abstrações, disponíveis no pacote "core" para padronizinação e reutilização de código, baseado em Java Reflection.
 - Resources, services e repositories desacoplados via IoC (Inversão de controle) e Programatic Injection.
 - Versionamento de api auto gerenciada, baseado na qualificação dos componente de serviços.
 - Foi utilizado o conceito de visão (Jackson View) para evitar a utilização de DTOs na construção de CRUDs.   
 - API documentation via Swagger (Personalização via Applications.yaml).
 
 ## Notas
 Um [erro](https://stackoverflow.com/questions/61656985/jackson-module-not-registered-after-update-to-spring-boot-2) no rgistro do modulo de serialização 'jackson-datatype-hibernate5' na versão do SPRING/JACSON utilizada, impediu que as entidades de relacionamento JPA fossem utilizadas com LAZY LOAD.

