# For Unix and cygwin environment
DEPRECATED=-Xlint:deprecation 
DEPRECATED=
FLAGS=
FLAGS= -source 1.7 -Xlint:unchecked
FLAGS= -source 1.7
FLAGS= -source 1.8

compile:	
	javac $(DEPRECATED) $(FLAGS)       \
                            client/*.java  \
                            server/*.java  \
                            common/*.java     

clean:
	rm -f */*.class */*.bak *.bak *.tgz
	rm -f -r html *.jar *.bluej
	rm -f 00text 00text.rtf 000text 000text.rtf
	rm -f -r doc/*.* doc/*
	rm -f Clients/__SHEL*
	rm -f _list.sh

documentation:
	echo "Making documentation"
	javadoc -sourcepath $(WIN_SDK1)\\src.zip         \
	-group Pong Pong                                 \
	-header "<FONT color="teal">Pong</FONT>"         \
	-author -windowtitle "Pong netwoked" \
	-use -splitindex -d html \
	-package client/*.java server/*.java common/*.java

tgz:
	tar cvfz pong.tgz client/*.java server/*.java common/*.java Makefile

jar:
	jar cfe Server.jar server.Server server/*.class common/*.class
	jar cfe Client.jar client.Client client/*.class common/*.class

runMC:
	java server/Server MC &
	sleep 1
	java client/Client MC 1&
	sleep 1
	java client/Client MC 1&
	sleep 2
	java client/Client MC 2&
	sleep 1
	java client/Client MC 2&

runTCP:
	java server/Server TCP &
	sleep 1
	java client/Client TCP 1&
	sleep 1
	java client/Client TCP 1&
	sleep 2
	java client/Client TCP 2&
	sleep 1
	java client/Client TCP 2&

runTCPm:
	java client/Client TCP 1&
	sleep 1
	java client/Client TCP 1&
	sleep 2
	java client/Client TCP 2&
	sleep 1
	java client/Client TCP 2&


observers:
	java client/Client Observer 1&
	sleep 1
	java client/Client Observer 2&
	sleep 1
	java client/Client Observer 2&

help:
	echo "runMC     - Run in multicast mode"
	echo "runTCP    - Run in TCP mode"
	echo "runTCPm   - More TCP games"
	echo "observers - Run observers of the game"
