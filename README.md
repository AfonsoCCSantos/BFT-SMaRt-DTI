# Deteção e Tolerância a Intrusões - Projeto 1 - Grupo 11
#### Afonso Santos - FC56368
#### Raquel Domingos - FC56378
#### Miguel Faísco - FC56954

## Quick start
### Compilar
Primeiramente, deve-se compilar o programa e para tal deve-se usar o script `gradlew`. Na pasta base do projeto deve então ser executado o seguinte comando:
Linux: `./gradlew installDist`
Windows: `gradlew installDist`

### Correr
Para correr as réplicas devem-se executar os seguintes comandos na pasta build/install/library.

Linux:
`./smartrun.sh intol.bftmap.DTIServer 0`
`./smartrun.sh intol.bftmap.DTIServer 1`
`./smartrun.sh intol.bftmap.DTIServer 2`
`./smartrun.sh intol.bftmap.DTIServer 3`
Windows:
`smartrun intol.bftmap.DTIServer 0`
`smartrun intol.bftmap.DTIServer 1`
`smartrun intol.bftmap.DTIServer 2`
`smartrun intol.bftmap.DTIServer 3`

Antes de inicializar o cliente deve-se esperar que os servidores estejam prontos a receber operações (Aparecerá uma mensagem na consola de cada servidor dizendo "Ready to process operations")

Para inicializar um cliente deve-se executar o seguinte comando (na mesma pasta onde foram executados os comandos para executar os servidores, ie, build/install/library).
Linux:
`./smartrun.sh intol.bftmap.DTIClient clientId`
Windows:
`smartrun intol.bftmap.DTIClient clientId`
onde clientId corresponderá ao id único pretendido para o cliente (por norma, um número superior a 0)

## Notas
- Após inicializar o cliente, o mesmo verá uma série de operações que pode efetuar. As operações são todas as definidas no enunciado do projeto. 
- Para realizar uma operação o cliente deve executar o comando correspondente (Por exemplo, MINT para criar uma nova coin) e inserir os dados à medida que são pedidos pela aplicação.
- Tal como indicado no enunciado, apenas um cliente inicializado com id 4 pode executar a operação MINT.

## Limitações
Foram implementadas todas as operações pedidas no enunciado. O grupo não detetou nenhuma limitação quanto à sua utilização.

