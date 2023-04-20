# 27.03 - 05.04

## Nils Grob

- Implemented the ability to communicate via WebRTC:
  - Added message handeling to the server for signaling. ([#41](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/41), [#42](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/42), [#43](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/43), [#44](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/44))
  - Added PeerConnection class on web app ([#31](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/31), [#34](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/34)) & mobile ([#8](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/8), [#9](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/9))
  - Added WebSocketConnection class which handles also automatic reconnections.
- Implemented shake detection on mobile [#7](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/7)
  
  - Added an listenToShake method which recognizes shakes by reading the smartphone accelerometer.
  - Added the view which is displayed when the mobile app is in shake detection mode.

## Stefan Schuler
- Added the MinigameType enum and later also added the MinigameDescription class that just provides an enumMap that maps enums to a description to display the correct data when creating an instance of Minigame in the backend. [#40](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/40)

- implemented the POST endpoint for the creation of a lobby & added the service class for the lobby and updated methods to include uniqueness of inviteCode and the minigamesChoice list. [#21](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/21)
- implemented the GET endpoint for getting the current Minigame instance & updated getMinigame call to have the correct get call for the minigame and to update the minigamesPlayed list when getting new Minigame. [#32](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/32)
- added POST-, GET-Lobby & GET-Minigame to the Mapper class and added their DTO's.

- implemented the basic layout of the Minigame class and refactored how it & it's information in the lobby works several times. Updated the lobby class with the neccessary information (minigamesPlayed & minigamesChoice lists + upcomingMinigame) [#34](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/34)
- also moved out a lot of methods from the lobby class into the corresponding service classes.

Additionally:
- left some parts in the code as comments that could probably be deleted but wasn't sure yet.
- Also added some more methods in the service classes for the creation of minigames and to add them to the right lists which is part of later tasks mainly accessed by [#82](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/82) & [#83](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/83)


## Naseem Hassan
- Added the join lobby page in frontend according to mockups [#3](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/3):
  - Added Lobby.js (main lobby page)
  - Added Player.js (to display individual players)
  - Edited theme.scss to match colors with mockups
- Added create lobby page according to mock-ups with create button [#4](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/4):
  - Added CreateLobby.js
  - Added the create Lobby button
  - Added routing so clicking the create button redirects to the lobby
## Sven Ringger
- Show description of next minigame (web) #19
- Show title of minigame that is played next (web) #18
- Create components for features above (web) #19, #18 & #21
- Show screen with players competing in the next minigame (web) #21
## Guojun Wu
- create lobby, team and player class and their methods(server) [#20](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/20)
- create lobby repository and database(server) [#81](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/81)

# 06.04 - 19.04

## Nils Grob
- Started implementing the timing game [#25](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/25)
- Made joining a lobby possible from the mobile phone (implememted the full stack) [#26](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/26)

## Stefan Schuler
- updated the websocket endpoint to update the lobby with the new players when they join [#86](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/86)
    - added a check for players joining a lobby to see if the nickname is allowed and if it is full already

- added assign & unassign endpoints to call when a player gets moved around in the client while assigning players to teams [#66](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/66)
	- methods in teamservice and lobbymanagement to update the lobby/team accordingly (unassignedPlayers list and teams) (add and remove methods for team & unassignedPlayers)
	- get methods to get teams & players from repo

- added startGame endpoint (Lobby PUT) that when all players are assigned and you press start it will create a new Minigame Instance and adds that to the lobby [#76](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/76)

- updated the create lobby endpoint and some service methods to initialize the teams and unassigned players list when creating the lobby [#21](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/21) 

- some minor not notable changes like changing the input from lobbyId to lobby directly or a second getLobby method
- added checks for methods if input is null and each change to entities is saved
- minor changes to DTO and mapper (also send unassignedPlayers list)

- updated Player (& DTO, mapper) to include the avatar as a string [#26](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/26)

- started implementing the random Player picker, but not finished yet [#80](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/80)

- additional fixes and changes for the creation of the minigame [#82](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/82)

- some rearrangements because of errors in the code

Upcoming:
- finish the random player picker
- methods and endpoints for updating the score after each minigame, including creating and getting a new instance &
 also to get the total score and a final score if the winning score was reached [#70](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/70) [#71](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/71) [#83](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/83)
- also add Tests for the tasks that are otherwise finished

## Naseem Hassan
- Host can now assign players to each of the teams in the main Lobby [#6](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/6):
  - Added a Drag and Drop system to drag players who have joined into one of the two teams
  - When dropped inside a team container the color of the player container changes to the teams color
  - Edited colors in theme.scss to be more distinct
- Added a qr code with the invite code of the lobby in it using the external api [#7](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/7):
  - A qr code containing the lobby invite code is now visible in the main Lobby
  - The qr code was generated with the following external api: https://goqr.me/de/api/
## Sven Ringger
- Add page to scan QR Code (mobile) #3
- Add field to manually input Lobby code (mobile) #3
- Generate avatar when nickname is entered (mobile) #39
- Add WaitingScreen for display until lobby is filled (mobile) #15
- Create GameHeader component and add it to all existing components (web) => improve #19, #18 & #21 and others from last week
- Create GameWon screen (web) #13
- Add confetti effect to GameWon screen (web) #13

## Guojun Wu
- Detect tap on smartphone(mobile) [#6](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/6)
- Design an in game screen (mobile) [#5](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/5)

