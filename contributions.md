# 27.03 - 05.04

## Nils Grob

-   Implemented the ability to communicate via WebRTC:
    -   Added message handeling to the server for signaling. ([#41](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/41), [#42](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/42), [#43](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/43), [#44](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/44))
    -   Added PeerConnection class on web app ([#31](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/31), [#34](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/34)) & mobile ([#8](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/8), [#9](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/9))
    -   Added WebSocketConnection class which handles also automatic reconnections.
-   Implemented shake detection on mobile [#7](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/7)

    -   Added an listenToShake method which recognizes shakes by reading the smartphone accelerometer.
    -   Added the view which is displayed when the mobile app is in shake detection mode.

## Stefan Schuler

-   Added the MinigameType enum and later also added the MinigameDescription class that just provides an enumMap that maps enums to a description to display the correct data when creating an instance of Minigame in the backend. [#40](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/40)

-   implemented the POST endpoint for the creation of a lobby & added the service class for the lobby and updated methods to include uniqueness of inviteCode and the minigamesChoice list. [#21](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/21)
-   implemented the GET endpoint for getting the current Minigame instance & updated getMinigame call to have the correct get call for the minigame and to update the minigamesPlayed list when getting new Minigame. [#32](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/32)
-   added POST-, GET-Lobby & GET-Minigame to the Mapper class and added their DTO's.

-   implemented the basic layout of the Minigame class and refactored how it & it's information in the lobby works several times. Updated the lobby class with the neccessary information (minigamesPlayed & minigamesChoice lists + upcomingMinigame) [#34](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/34)
-   also moved out a lot of methods from the lobby class into the corresponding service classes.

Additionally:

-   left some parts in the code as comments that could probably be deleted but wasn't sure yet.
-   Also added some more methods in the service classes for the creation of minigames and to add them to the right lists which is part of later tasks mainly accessed by [#82](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/82) & [#83](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/83)

## Naseem Hassan

-   Added the join lobby page in frontend according to mockups [#3](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/3):
    -   Added Lobby.js (main lobby page)
    -   Added Player.js (to display individual players)
    -   Edited theme.scss to match colors with mockups
-   Added create lobby page according to mock-ups with create button [#4](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/4):
    -   Added CreateLobby.js
    -   Added the create Lobby button
    -   Added routing so clicking the create button redirects to the lobby

## Sven Ringger

-   Show description of next minigame (web) #19
-   Show title of minigame that is played next (web) #18
-   Create components for features above (web) #19, #18 & #21
-   Show screen with players competing in the next minigame (web) #21

## Guojun Wu

-   create lobby, team and player class and their methods(server) [#20](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/20)
-   create lobby repository and database(server) [#81](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/81)

# 06.04 - 19.04

## Nils Grob

-   Started implementing the timing game [#25](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/25)
-   Made joining a lobby possible from the mobile phone (implememted the full stack) [#26](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/26)

## Stefan Schuler

-   updated the websocket endpoint to update the lobby with the new players when they join [#86](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/86)

    -   added a check for players joining a lobby to see if the nickname is allowed and if it is full already

-   added assign & unassign endpoints to call when a player gets moved around in the client while assigning players to teams [#66](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/66)

    -   methods in teamservice and lobbymanagement to update the lobby/team accordingly (unassignedPlayers list and teams) (add and remove methods for team & unassignedPlayers)
    -   get methods to get teams & players from repo

-   added startGame endpoint (Lobby PUT) that when all players are assigned and you press start it will create a new Minigame Instance and adds that to the lobby [#76](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/76)

-   updated the create lobby endpoint and some service methods to initialize the teams and unassigned players list when creating the lobby [#21](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/21)

-   some minor not notable changes like changing the input from lobbyId to lobby directly or a second getLobby method
-   added checks for methods if input is null and each change to entities is saved
-   minor changes to DTO and mapper (also send unassignedPlayers list)

-   updated Player (& DTO, mapper) to include the avatar as a string [#26](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/26)

-   started implementing the random Player picker, but not finished yet [#80](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/80)

-   additional fixes and changes for the creation of the minigame [#82](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/82)

-   some rearrangements because of errors in the code

Upcoming:

-   finish the random player picker
-   methods and endpoints for updating the score after each minigame, including creating and getting a new instance &
    also to get the total score and a final score if the winning score was reached [#70](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/70) [#71](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/71) [#83](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/83)
-   also add Tests for the tasks that are otherwise finished

## Naseem Hassan

-   Host can now assign players to each of the teams in the main Lobby [#6](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/6):
    -   Added a Drag and Drop system to drag players who have joined into one of the two teams
    -   When dropped inside a team container the color of the player container changes to the teams color
    -   Edited colors in theme.scss to be more distinct
-   Added a qr code with the invite code of the lobby in it using the external api [#7](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/7):
    -   A qr code containing the lobby invite code is now visible in the main Lobby
    -   The qr code was generated with the following external api: https://goqr.me/de/api/

## Sven Ringger

-   Add page to scan QR Code (mobile) #3
-   Add field to manually input Lobby code (mobile) #3
-   Generate avatar when nickname is entered (mobile) #39
-   Add WaitingScreen for display until lobby is filled (mobile) #15
-   Create GameHeader component and add it to all existing components (web) => improve #19, #18 & #21 and others from last week
-   Create GameWon screen (web) #13
-   Add confetti effect to GameWon screen (web) #13

## Guojun Wu

-   Detect tap on smartphone(mobile) [#6](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/6)
-   Design an in game screen (mobile) [#5](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/5)

# 20.04 - 26.04

## Nils Grob

-   Refactored WebRTC stuff (currently not working as intended)
-   Changed the input reading from using WebRTC to STOMP websockets.
-   Finished Timing Game
-   Made Github Action to build an android apk on release
-   Handled mobile app navigation [#16](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/16)

## Stefan Schuler

-   added tests for LobbyController (committed in corresponding issue ([#21](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/21)) or in new issue [#90](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/90))
    -   also smaller changes in older issues, like changing how the Player is stored in the Minigame instance (now Player) [#32](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/32), [#76](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/76)
-   minor change to random player picker (finished now) [#80](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/80)
-   added PUT endpoint + DTO for updating the lobby [#71](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/71)
-   added GET endpoint + DTO for overall scores of teams [#70](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/70)
-   added service methods for updating scores, updating minigame, updating player, update isFinished [#83](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/83)
-   added 2 GET request for determining if a winner exists and getting that winner [#69](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/69)
-   added methods to check if a team has won [#79](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/79)
-   some minor things that have no issue attached like adding error handling, changing some details in dto's and descriptions in controller

## Naseem Hassan

-   add a picture of the minigame which is played next [#16](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/16):
    -   Depending on the next Minigame a coresponding preview picture is shown in the GamePreview
    -   The pictures are all pong at this point (because we don't have the others yet)
    -   Right now pictures are only shown when running the application local. When deployed the pictures did not show up (still need to fix that).
-   Implement score overview screen for overall score [#14](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/14):
    -   After the winner for a minigame is announced the TeamScoreOverview page is shown
    -   Added a simple animation (bars growing depending on the number of points won)
-   Also did some general cleaning up and styling in the whole frontend

## Sven Ringger

-   Implement HotPotato Game #40
-   Add Routing (loop so that game runs indefinitely until WinningScore is reached)
-   Game logic API calls
-   A lot of fixes not related to any issues
-   Implement redirect to WinnerScreen #17
-   Complete show winner on host device #13
-   Implement show Gamewinning screen #9
-   Implement TeamScoreOverview screen for current game #13

## Guojun Wu

-   Send each tap to the web app(mobile) [#4](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/4)
-   Create tap counter on web app & update on received tap(web) [#27](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/27)
-   Show 20second countdown after which the game ends(web) [#30](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/30)
-   Test for service, reposiroty, and rest mapper

# 27.04 - 04.05

## Nils Grob

-   Started implementing the Vibration Game
    -   Made three vibration patterns and play them on mobile [#22](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/22)
    -   Commanded the replay of vibration from the web app through the stomp connection [#59](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/59)
    -   Made an visual representation for each vibration pattern and animated them according to the pattern [#58](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/58)

## Stefan Schuler

-   added the basic useState and empty layout of the settings page to write down what it needs to show. [#53](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/53)

-   added enums for the settings, for the gameMode (Single, team) & for the decision how many players a minigame needs [#125](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/125) [#124](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/124)
-   updated service methods & restored all tests to include the change that a minigame can have more than 1 player and tried to implement all players option for potato game [#142](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/142)
-   started with splitting lobby into lobby & game which is also directly linked to refactoring the backend & methods [#103](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/103)
-   while doing this also started to set up some DTO's for the creation of the game & to include settings [#122](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/122)

## Naseem Hassan

-   Redirect web app to initial game settings [#10](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/10):
    -   Added a Button in WinnerScreen which onClick redirects to /createLobby
    -   OnClick should also delete the lobby (not implemented yet)
-   Display winning score [#8](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/8):
    -   Added the Winning score in WinnerScreen
-   Changed the TeamScoreOverview to Update Scores and redirect to WinnerScreen.
    -   MinigameWon now just displays the winner of the Minigame and then redirects to TeamScoreOverview. Now the score update is also shown if a team has reached the winningScore before redirecting to the WinnerScreen.
-   TODO: WinnerScreen styling

## Guojun Wu

-   Add all the component for pong game [#63](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/63)
-   Make all the components move in the correct mechanism for Pong game [#65](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/65)

## Sven Ringger

-   Implement game logic for hot potato in web, still needs backend for me to continue [#62](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/62)
-   Unify web for better responsiveness [#64](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/64)

# 05.05 - 11.05

## Nils Grob
- Continued with the vibration game:
    - Made the vibration selection screen on mobile [#23](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/23)
- Adjusted frontend api calls to the backend changes which occured on the splitting branch  [#69](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/69)

## Stefan Schuler
- added some buttons and the create Game call in the settings page [#53](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/53)
- fixed the list of players not working properly (database primary key error) [#142](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/142)
- moved winningScore into game and according changes + added forgotten setters & getters [#103](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/103)
- redesigned controller methods, added some endpoints and moved them into controller that fits more [#104](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/104)
- changed a small test to try something [#147](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/147)
- redesigned service methods, added some and moved them into service that fits more + repo calls (not 100% finished yet) [#145](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/145)


## Naseem Hassan

- Added settings layout [#53](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/53):
  - Added styling/layout for Settings page. Now one can easily add more options in the settings page.

- Added settings options for game duration [#70](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/70):
  - Added three options for the duration of game: short, normal and long. When clicking one of the three options the winningScore is automaitcally updated.

## Guojun Wu

- Command of Pong game to mobile from web app [#66](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/66)
- Motion detection for Pong [#25](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/25)
- Scoring for Pong game [#67](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/67)
## Sven Ringger

- Implement ErrorScreen for web [#68](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/68)
- Implement ErrorScreen for mobile [#26](https://github.com/sopra-fs23-group-18/pocket-party-mobile/issues/26)

# 12.05 - 18.05
## Nils Grob

## Stefan Schuler
- added the missing settings option in DTO & game entity [#122](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/122)
- fixed some errors & error handling for methods plus adapted game creation for settings options [#123](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/123)
- add another DTO for overall winnerTeam (with players) [#145](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/145)
- updated put lobby endpoint to include team name changes (+ DTO & service) [#155](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/155)
- exchanged TeamColor functionality with TeamName [#105](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/105)
- 
## Naseem Hassan
- Added input field for the team names [#73](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/73):
  - Teams can now choose their own name in the Lobby screen on the Host device. 

- Added settings options for player choice [#72](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/72):
  - Added two options for how the competing players are chosen before each minigame:
  Random and Voting. (Voting is not implemented yet)

- Started with VotingScreen implementation. (WIP)

## Guojun Wu
## Sven Ringger
- Create Info component
- Add Info component to lobby with corresponding text [#80](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/80)
- Add Info component to CreateLobby with corresponding text [#84](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/84), used to be [#166](https://github.com/sopra-fs23-group-18/pocket-party-server/issues/166) before it was moved.
- Add button in GamePreview instead of timer [#54](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/54)
