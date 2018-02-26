JC = javac
JFLAGS = -g
CLASSPATH = /usr/share/java/gtk-4.1.jar
SRCDIR = ./UI

default: login.class

login.class: $(SRCDIR)/login.java
	$(JC) $(JFLAGS) -cp $(CLASSPATH) $(SRCDIR)/login.java

clean:
	$(RM) $(SRCDIR)/*.class ./*~ $(SRCDIR)/*~
