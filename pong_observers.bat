call pong_defs.bat
echo Run the distributed version: Networking to localhost
start java server/Server
echo When server finishes setup
pause
start java  client/Client MC 1
start java  client/Client MC 1
start java  client/Client MC 2
start java  client/Client MC 2
