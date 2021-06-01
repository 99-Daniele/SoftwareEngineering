SERVER

| JAR COMMAND | DESCRIPTION 
| ------- | --- 
| java -jar shade/AM22.jar --server/-s | create server on default port 12460
| java -jar shade/AM22.jar --server/-s --port/-p xyzw | create server on port xyzw


CLI

| JAR COMMAND | DESCRIPTION
| ------- | --- 
| java -jar shade/AM22.jar --cli/-c | launch CLI without connection info
| java -jar shade/AM22.jar --cli/-c --hostname/-h  "hostname" --port/-p xyzw | launch CLI on server "hostname" on port xyzw


GUI

| JAR COMMAND | DESCRIPTION
| ------- | --- 
| java -jar shade/AM22.jar --gui/-g | launch GUI without connection info
| java -jar shade/AM22.jar --gui/-g --hostname/-h  "hostname" --port/-p xyzw | launch GUI on server "hostname" on port xyzw

CLI COMMAND HELPER

| CMD       | DESCRIPTION     
| :------------- | :----------: 
| turn / -tu | ask Server if it's your turn   
| numplayer / num / -np 1/2/3/4   | send to Server the number of player of the game |
| chose leader / leader / -cl 49/50..../63/64 49/50....../63/64 | chose first two leader cards before start game|
| chose resource / chose r / resource / -cr coin(co)/shield(sh)/stone(st)/servant(se)| chose first resource before start game
| see playerboard / see board / see p / -sp 1/2/3/4| print the board of player 1/2/3/4
| see my playerboard / see my p / -smp| print your playerboard
| see warehouse / see wh / -swh 1/2/3/4| print the warehouse of player 1/2/3/4
| see my warehouse / see my wh / -smw| print your warehouse
| see strongbox / see sb / -ssb 1/2/3/4| print the strongbox of player 1/2/3/4
| see my strongbox / see my sb / -sms| print your strongbox
| see cards / see c / -sc 1/2/3/4| print the development cards of player 1/2/3/4
| see my cards / see my c / -smc| print your development cards
| see leader / see l / -sl 1/2/3/4| print the leader cards of player 1/2/3/4
| see my leader / see my l / -sml| print your leader cards
| see market / see m / -sm| print the market
| see faithtrack / see f / -sf| print the faithTrack
| see decks / see d  / -sd| print current buyable development cards
| buy card /buy / -bc 1/2.../47/48 warehouse(w)/strongbox(s)| buy card 1/2.../47/48 taking resources from warehouse/strongbox
| chose slot / chose s / slot / -cs 1/2/3| chose in which slot insert the buying card
| take marble / market / -tm row(r)/column(c) 1/2/3/4| take row/column 1/2/3/4 from market
| usemarble / marble / -um red(r)/white(w)/purple(p)/yellow(y)/grey(g)/blue(b)| use one of the chosen marbles
| whiteconversion / white / -wc 1/2| chose which active WhiteConversionCard use for white marble
| switch / -sw 1/2/3/4/5 1/2/3/4/5| switch depot 1/2/3/4/5 with depot 1/2/3/4/5
| power card / power c / -cp 1/2/3 warehouse(w)/strongbox(s)| activate the power of slot 1/2/3 taking resource from warehouse/strongbox
| power basic / power b / -bp coin(co)/shield(sh)/stone(st)/servant(se) coin(co)/shield(sh)/stone(st)/servant(se)  coin(co)/shield(sh)/stone(st)/servant(se) warehouse(w)/strongbox(s)| activate basic power  deleting first two resource and gaining third resource
| power leader / power l / -lp 1/2 coin(co)/shield(sh)/stone(st)/servant(se) warehouse(w)/strongbox(s)| activate the power of AdditionalProductionPower card taking resource from warehouse/strongbox and gaining 1 resource
| power end / -ep| inform Server that player has ended its production
| leader active / leader a / -la 1/2| activate leader card 1/2
| leader discard / leader d / -ld| discard leader card 1/2
| end turn / -et| end your turn
| help / -h| print help text
