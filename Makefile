JC = javac
JFLAGS = -g
CLASSPATH = /usr/share/java/gtk-4.1.jar:.
SRCDIR = ./UI

default: login.class controller.class

login.class: $(SRCDIR)/login.java
	$(JC) $(JFLAGS) -cp $(CLASSPATH) -d . $(SRCDIR)/login.java

controller.class: $(SRCDIR)/controller.java
	$(JC) $(JFLAGS) -cp $(CLASSPATH) -d . $(SRCDIR)/controller.java

clean:
	$(RM) $(SRCDIR)/*.class ./*~ $(SRCDIR)/*~
