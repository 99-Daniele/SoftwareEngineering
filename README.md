# Prova Finale di Ingegneria del Software - AA 2020-2021
![alt text](https://raw.githubusercontent.com/chenyongzhouking/ingswAM2021-Civati-Cicala-Chen/master/src/main/resources/photos/logo.png?token=APOZOGKTI6S6W7Q6FE7I6ILA3X5S6)

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
## Esecuzione 
####SERVER

| JAR COMMAND | DESCRIPTION 
| ------- | --- 
| java -jar shade/AM22.jar  --server / -s | create server on default port 12460
| java -jar shade/AM22.jar  --server / -s  --port / -p  xyzw | create server on port xyzw


####CLI

| JAR COMMAND | DESCRIPTION
| ------- | --- 
| java -jar shade/AM22.jar  --cli / -c | launch CLI without connection info
| java -jar shade/AM22.jar  --cli / -c  --hostname / -h  "hostname"  --port / -p  xyzw | launch CLI on server "hostname" on port xyzw


####GUI

| JAR COMMAND       | DESCRIPTION   
| ------- | --- 
| java -jar shade/AM22.jar  --gui / -g | launch GUI without connection info
| java -jar shade/AM22.jar  --gui / -g  --hostname / -h  "hostname"  --port / -p  xyzw | launch GUI on server "hostname" on port xyzw

#####PARAMETRI
Permette al giocatore di sostituire le due carte Leader in possesso con due carte WhiteMarbleLeader già attive. 
Questi comandi sono da aggiungersi a quelli utilizzati per avviare GUI o CLI.

| JAR COMMAND | INTERFACE
| ------- | ---
| -dc / --devcli | CLI
| -dg / --devgui | GUI
