Name = Main

JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Backtracking.java \
        EventCase.java \
        Case.java \
		SKeyCodeEvent.java \
		SudokuLogique.java \
		CSudoku.java \
    	Main.java 

classes: $(CLASSES:.java=.class)

default: classes

clean:
	$(RM) *.class

run : 
	java $(Name)

.PHONY : but clean