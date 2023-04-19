# 27.03 - 31.03

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
-Show description of next minigame
-Show title of minigame that is played next
-Add page to scan QR Code
-Generate avatar when nickname is entered
## Guojun Wu



























## Naseem Hassan
- Host can now assign players to each of the teams in the main Lobby [#6](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/6):
  - Added a Drag and Drop system to drag players who have joined into one of the two teams
  - When dropped inside a team container the color of the player container changes to the teams color
  - Edited colors in theme.scss to be more distinct
- Added a qr code with the invite code of the lobby in it using the external api [#7](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/7):
  - A qr code containing the lobby invite code is now visible in the main Lobby
  - The qr code was generated with the following external api: https://goqr.me/de/api/