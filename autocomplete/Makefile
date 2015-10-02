# Using a Makefile just for simplicity.

JFLAGS = -g
JC = javac
CLASSES = \
	AutoCompleteMain.java \
	AutoComplete.java

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

default: program.jar

program.jar: $(CLASSES:.java=.class)
	jar cfe program.jar AutoCompleteMain $(CLASSES:.java=.class)

clean:
	$(RM) *.class *.jar