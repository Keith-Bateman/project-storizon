#!/bin/bash
# Simple script to show how compilation works and test basic UI
make
java -cp .:/usr/share/java/gtk-4.1.jar UI.login
