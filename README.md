# Software-Engineering-Project-LTAG-
Software Engineering project for Jim Strother

----------------------------------------------------------------
HOW-TO-RUN /////////////////////////////////////////////////////
----------------------------------------------------------------

**********************************
INSTALL DEPENDENCIES **IMPORTANT**
**********************************
Make sure you have java installed.
Make sure to have python3 installed. 
Make sure you have updated postgresql.
Make sure you have psycoph2 installed.

To do the above steps, run these in your linux terminal and enter Y when prompted (you might need to enter your password, for us, it is student)

sudo apt update

sudo apt upgrade 

sudo apt install default-jdk 

sudo apt install python3

verify python runs by running the following in your terminal

python3 --version

Next, we will update postgreSQL and install dependencies for the python traffic generator.

Run the following in your linux terminal

sudo apt-get update

sudo apt-get install libpq-dev

pip install psycopg2


Then, 

TO RUN:
**************************************************************************************************
1) Open Terminal
2) **Navigate to the src project directory (similar to /home/student/Documents/././src/)**
3) Enter "javac *.java" to create class files for all java files.
4) **Now, navigate to the parent directory of /src/ (this contains run_me.py and the readme.txt)**
5) Run run_me.py by entering "python3 run_me.py"
6) Done!
**************************************************************************************************


----------------------------------------------------------------
Team Members ///////////////////////////////////////////////////
----------------------------------------------------------------
kaipokrandt : Kai Pokrandt /
codingdodia : Dodia Dhairya /
dylanf9     : Dylan Flanagan /
SherazM1    : Sheraz Mukhtar /
ejmoneyyy1  : EJ Alobuia /


-----------------------------------------------------------------
#TO-DO //////////////////////////////////////////////////////////
-----------------------------------------------------------------

Implement stylized B next to player username after they hit the base

Implement username population upon duplicate ID immediately when entering ID (currently updates when submitting)




