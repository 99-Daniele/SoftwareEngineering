# Prova Finale di Ingegneria del Software - AA 2020-2021
![alt text](src/main/resources/photos/logo.png)

Scopo del progetto è quello di implementare il gioco da tavola "Maestri del Rinascimento" seguendo il pattern architetturale Model View Controller per la realizzazione del modello secondo il paradigma di programmazione orientato agli oggetti. 
La rete è stata gestita con l'utilizzo delle socket.

Interazione e gameplay: linea di comando (CLI) e grafica (GUI).
## Funzionalità

- Regole Complete
- CLI
- GUI
- Socket
- 1 FA (Funzionalità Avanzate):
    - __Partite multiple:__ Realizzare il server in modo che possa gestire più partite contemporaneamente,
                            dopo la procedura di creazione della prima partita, i giocatori che accederanno al server verranno gestiti
                            in una sala d’attesa per creare una seconda partita e così via.
                            
##### SHELL
 
La CLI funziona con Shell Linux e iOS. Per Windows non funziona perché non si vedono i colori.                          
## Esecuzione 
#### SERVER

| JAR COMMAND | DESCRIPTION 
| ------- | --- 
| java -jar shade/AM22.jar  --server / -s | create server on default port 12460
| java -jar shade/AM22.jar  --server / -s  --port / -p  xyzw | create server on port xyzw


#### CLI

| JAR COMMAND | DESCRIPTION
| ------- | --- 
| java -jar shade/AM22.jar  --cli / -c | launch CLI without any connection info
| java -jar shade/AM22.jar  --cli / -c  --hostname / -h  "hostname"  --port / -p  xyzw | launch CLI on server "hostname" on port xyzw


#### GUI

| JAR COMMAND       | DESCRIPTION   
| ------- | --- 
| java -jar shade/AM22.jar  --gui / -g | launch GUI without any connection info
| java -jar shade/AM22.jar  --gui / -g  --hostname / -h  "hostname"  --port / -p  xyzw | launch GUI on server "hostname" on port xyzw

##### CHEAT

Permette al giocatore di sostituire le due carte Leader in possesso con due carte WhiteMarbleConversoinCard già attive,
tramite un comando "-cheat" per la CLI o il bottone "CHEAT" per la GUI
Avviare CLI o GUI con i seguenti comandi

| JAR COMMAND | INTERFACE
| ------- | ---
| java -jar shade/AM22.jar  --devcli / -dc | CLI
| java -jar shade/AM22.jar  --devcli / -dc  --hostname / -h  "hostname"  --port / -p  xyzw | |
| java -jar shade/AM22.jar  --devgui / -dg | GUI
| java -jar shade/AM22.jar  --devgui / -dg  --hostname / -h  "hostname"  --port / -p  xyzw | |

###### AUTORI

[Yongzhou Chen](https://github.com/chenyongzhouking)

[Daniele Cicala](https://github.com/99-Daniele)

[Daniele Civati](https://github.com/Civati)
