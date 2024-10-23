# Software Engineering Project

## Introduction

This project involves the development of an application that simulates the board game "Maestri del Rinascimento." 

It utilizes the Java Model-View-Controller (MVC) pattern and adheres to Object-Oriented Programming principles.

Additionally, a Client-Server architecture is implemented using sockets, enabling efficient communication between components.

User interaction is facilitated through both a Command Line Interface (CLI) and a Graphical User Interface (GUI), providing flexibility and enhancing the overall user experience.

## Features

- CLI
- GUI
- Socket
- Multiple games: Server can handle multiple games at the same time

## Usage

#### SERVER CREATION

| JAR COMMAND | DESCRIPTION 
| ------- | --- 
| java -jar MasterOfRenaissance.jar  --server / -s | create server on default port 12460
| java -jar MasterOfRenaissance.jar  --server / -s  --port / -p  xyzw | create server on port xyzw


#### CLIENT CREATION WITH CLI

| JAR COMMAND | DESCRIPTION
| ------- | --- 
| java -jar shade/AM22.jar  --cli / -c | launch CLI without any connection info
| java -jar shade/AM22.jar  --cli / -c  --hostname / -h  "hostname"  --port / -p  xyzw | launch CLI on server "hostname" on port xyzw


#### CLIENT CREATION WITH GUI

| JAR COMMAND       | DESCRIPTION   
| ------- | --- 
| java -jar shade/AM22.jar  --gui / -g | launch GUI without any connection info
| java -jar shade/AM22.jar  --gui / -g  --hostname / -h  "hostname"  --port / -p  xyzw | launch GUI on server "hostname" on port xyzw

#### CLIENT CREATION WITH CHEATS

When using this command the created game allows user to use cheats.

To use cheat user has to type "-cheat" on CLI or click on "CHEAT" button on GUI.

Activating cheats will substitute the two possesed Leader cards with two WhiteMarbleConversion cards already active.

| JAR COMMAND | INTERFACE
| ------- | ---
| java -jar shade/AM22.jar  --devcli / -dc | CLI
| java -jar shade/AM22.jar  --devcli / -dc  --hostname / -h  "hostname"  --port / -p  xyzw | |
| java -jar shade/AM22.jar  --devgui / -dg | GUI
| java -jar shade/AM22.jar  --devgui / -dg  --hostname / -h  "hostname"  --port / -p  xyzw | |

## Authors

[Yongzhou Chen](https://github.com/chenyongzhouking)

[Daniele Cicala](https://github.com/99-Daniele)

[Daniele Civati](https://github.com/Civati)
