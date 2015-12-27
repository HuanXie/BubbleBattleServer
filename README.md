# BubbleBattle
In this Project, a new multiplayers Android game is well designed and implemented. Game called BubbleBattle. 
As requirements of Project of course Network Programming with Java, a simple game lobby server using JavaEE 
has to be designed and developed to host game instances, offering the capability to create or join online 
games but also access player statistics such as games won and their current score. And a game clients using 
the Android SDK is also developed with good UI design.


The BubbleBattle application is based on client-server architecture and consists of three main components:
an android client, a game server and a MySQL server. The communication between the client and server is 
based on WebSocket. The reason for choosing WebSocket is mainly due to that it saves a lot of networking 
overhead compared to HTTP. Also WebSocket is very suitable for implementing event-driven gaming application. 
The game server is implemented in Java (servlet) and the persistence technology used is JPA 
(using Eclipse-link as the JPA implementation) which talks to the MySQL database for retrieving/saving 
user information. A Tomcat servlet container is setup to deploy the gaming service.

The first thing client does is of course to establish a WebSocket connection with the game server so 
that the login credential is sent to the server. The server then will validate it against the data saved
in the database. After login, another Android activity will be started so that the user can choose a game
instance to join. There, the client can see the status (ongoing/waiting) of each game instances. 
The client has also the possibility to read other players’ profile and gaming statistics. 
When the game starts, a new Android activity will be entered where the user can hit a button to shoot bubbles. 
Whenever a bubble is shoot, the client send a message about the location of the bubble to the server so that 
the server can forward it to the other player whose client will display an “enemy bubble” on a correct location.
For all bubbles, the client will generate animations to simulate the movements and detect some events during the
animation such as bubble collisions. A player will win the game when its bubble reaches the other side without
colliding with enemy bubble and a message will be sent to the server to record the victory which will be persisted 
in the database. The request/response between the client and server is carried out as WebSocket messages and they 
are implemented in JSON format as it is really convenient to process.
