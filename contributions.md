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

## Naseem Hassan
- Added the join lobby page in frontend according to mockups [#3](https://github.com/sopra-fs23-group-18/pocket-party-web/issues/3):
  - Added Lobby.js (main lobby page)
  - Added Player.js (to display individual players)
  - Edited theme.scss to match colors with mockups
- 
## Sven Ringger

## Guojun Wu
