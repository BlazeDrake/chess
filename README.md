# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

## Server diagram
UML for server diagram:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIcDcuj3ZfF5vD6L9tQAr5pcw63O+nxPF+SwfgC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm65TTtASAAF76jSoqjLMEHvCaRLhhaHLphYqA0DASGHjAPIAGYQKG9Fup2OHlGwqipCgto0bakAwCUSDcZYjrOgS+EsuUnrekGAZBiGrrSpGTHRjAsbBtoSmJsmMElmhPLZrmmCASCcElFcr4jqMC6Tn006zs246tn0BzFk5hTZD2MD9oOvRuSBHn+dWU5Br587xW2ZicKu3h+IEXgoOge4Hr4zDHukmSYGFF5FNQ17SAAorutX1LVzQtA+qhPt0PmNugf5so5rldXOUEOVZIKXvBiGFb6qFTWAGEYthcq4SpAlqTA5JgCZ2l1t1aB0UygkGeUADiFIwJRhi8bAABksnAMgWiZDA04wJAc4wPxB36c54LGTpZk4RqqmkkYKDcJkW0mbpwMRoUloyGDFKGC9b09YDSaFP1BX2EVdkIHmo1CVeaY9EFxOdhVYB9gOMBDkuGWeFlG6Qrau7QjAx2jqyJWnuV57MM517HY1LX2KOnVJbtvWWUCJb9INzb-CNstjVVTryux0Kc6MqjbTOu2LfBQOrSDG1bQre2fWasPsidZ0XTAV0wLdVD3Ugj3I0Gr27R9enmhjau-VDANLcbX2mxS2soGzsSwsH8j7dbjF28wyCxDAEDcTAIA+FAkKkGLOtWwxAfCRzo4x1g6My6m5SV1Hqh4wTKtE9VJNTIXajjJU-SdwAktI3cAIy9gAzAALE8J6ZAaFYTF8OgIKADaz6BfRfJ3AByo7z3sMCNGTbcU-z1NRT0Hdc93FS96OA-D2Pk9TNP5Hufc69PIvy+rx589PFvO-rz3gfdKK5GbrkCNgXO2BuDwF1E9KOKRSpnhyALcaLlKi1AaKLcWwRJZziHP-UYh9jg12BANPBitqx9EIW-Nsjk2Rlw0pkKOsI4BwJQFHeaWFTDoxWuHd060KTmwoZbP2Nt4anWYA7J2Ls3Ye2el7VGSRi6HR+hreOwBzLLRkDDdS7CWEaOmDQxOJc4ZMTYV6J6wAYBoBQMkdao4eGhwDljCxepOFqHsvQ0uxMrgX1GHfcoI8J4wGIR2AOlNT5Dn8SgQJMBgnj1CSAzK4CAiWDBkhexAApCAPJy6jECJ-EADY+YoIYb4jBlI7wtE7hLHa+DejQOAOkqAcAIBISgLMfu0gwmqyxvLERH5pjPCXi0tpHShnUNvoPUoCToItzUQhGAAArXJaAWGrJ5B4tEC0nFGzwibARZstIWxMYdMxKdzpvgKTIu6D09QKL9N7d6KjvpoKDv9eQWiw7WzJJHUccdPnACMdMs5+kLkczOn6QwndvbnWwB7UwYjW7qyWVHeMzi+G-PhR7Fh6LtBgv9hCoy0KbF2JgE0lpAByVkDsZKkpANASE4BXpemGHs1FGMsY5K2aOJuytUworlr04+KDT6016PTUBa5soBC8M0rsXpYDAGwNAwg8REiIN5pTcpbdygVDqg1JqLVjDS0xqNK48zBWLPUtwPAsJDaop+eGW1SqHWvKJbbBG4NkbKhgMypYrI8mmq0Vyi1iq8D8u8YLEmIqIknwijTOmySgA

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
