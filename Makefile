# javac *.java
# jar cfe MarsBase.jar MarsBase *.class

JCFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JCFLAGS) $*.java

CLASSES = *.java

default: clean classes jar

classes: $(CLASSES:.java=.class)

jar: $(classes)
	jar cfe Stocks.jar StocksDriver *.class

clean:
		$(RM) *.class
		$(RM) *.jar