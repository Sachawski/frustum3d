build: 
	javac src/*/*.java -d classes

build-frustum:
	javac -d classes src/graphicx/*.java src/graphicx/*/*.java
