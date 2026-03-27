build: 
	javac src/*/*.java -d classes

build-frustum:
	javac -d classes src/frustum3d/*.java src/frustum3d/*/*.java
