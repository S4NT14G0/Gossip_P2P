JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	GossipP2PServer.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	$(RM) gossipp2p/messages/*.class
	$(RM) gossipp2p/serverthreads/*.class