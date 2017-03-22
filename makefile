BIN = bin
CLASSPATH = src
JFLAGS = -g -d $(BIN)
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) -classpath $(CLASSPATH) $(JFLAGS) $*.java

SOURCE_FILES = \
GossipP2PServer.java

build: $(SOURCE_FILES:.java=.class)

clean:
	$(RM) $(BIN)/*.class