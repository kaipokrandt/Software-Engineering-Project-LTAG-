set -u -e
javac -cp .:../lib/postgresql-42.7.5.jar *.java

cd ..
python3 run_me.py

#java -cp .:../lib/postgresql-42.7.5.jar main
rm *.class