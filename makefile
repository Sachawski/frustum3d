build: 
	javac src/*/*.java -d classes

build-frustum:
	javac -d classes src/personal/frustum/*.java src/personal/frustum/*/*.java
