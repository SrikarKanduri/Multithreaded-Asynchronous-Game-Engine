# Multithreaded-Asynchronous-Game-Engine
A multithreaded asynchronous Game Engine, implemented in Processing, that handles multiple asynchronous client requests.

# Working:
The Client class starts off two other threads, one for handling inputs and the other for outputs. The writing thread and the main (Processing) thread are synchronized on a lock, so that output is written to the server only on user input. This will make the communication perfectly asynchronous without any overheads.

Each Client sends in their Rectangle object (which is an awt that matches the properties of the Processing’s rectangle) to the server. Server’s main Processing thread displays these objects and responds to all the clients with all the client objects. So, each client will in turn display all the objects and at the end, all the client views and server view display the same thing, which is the core server requirement of a multi-player game. 

This design when extended will support multi-player game modes but may have bottlenecks if too many objects are present.
