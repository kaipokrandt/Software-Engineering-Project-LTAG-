set -u -e
javac -cp .:../lib/postgresql-42.7.5.jar *.java

java -cp .:../lib/postgresql-42.7.5.jar main
rm *.class