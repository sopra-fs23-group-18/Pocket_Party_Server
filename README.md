# Pocket Party

## Introduction

In our game Pocket Party, a host can easily create a lobby via the Web App. The players will then join the lobby by either enter the pin or scan the QR code. Players are then divided into two teams. In each lobby, there will be a competition between the teams. In this competition, we will have a series of minigames (i.e., the games that are short and simple) sequentially for the teams to compete with each other. Moreover the minigames will usually require the player to make some movement with their mobile phone. After each minigame, each team will get a predefined score based on the rule. The winner of the whole competition will be decided by whether a team has reached the winning score or not.

## Technologies used

-   Sever: Spring Boot, STOMP, JPA, H2
-   Client: React, STOMP, Matter.js
-   Mobile: React Native, STOMP

## High-level components

-   Server: The server will handle the communication between the client and the mobile. It will also store the data of the game.
-   Client: The client is responsible for the user interface. It will display the game by communicating with the server.
-   Mobile: The mobile is responsible for detecting the action of the players for each game with the sensors or screen. It will send the data to the server.

## Launch & Deployment

### Setup this Template with your IDE of choice

Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

### IntelliJ

1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code

The following extensions can help you get started more easily:

-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs23` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle

You can use the local Gradle Wrapper to build the application.

-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode

You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## Roadmap (Top features for new developers to contribute)

-   Add more minigames
-   Make the UI more attractive

## Authors

-   **Stefan Schuler** -- Sopra Group 18
-   **Naseem Hassan** -- Sopra Group 18
-   **Sven Ringger** -- Sopra Group 18
-   **Guojun Wu** -- Sopra Group 18
-   **Nils Grob** -- Sopra Group 18

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
