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

sudo apt-get install python3-pip

verify python runs by running the following in your terminal

python3 --version

Next, we will update postgreSQL and install dependencies for the python traffic generator.

Run the following in your linux terminal

sudo apt-get update

**REQUIRED**
sudo apt-get install libpq-dev

pip install psycopg2
**REQUIRED**

Then, 

TO RUN:

***PRIMARY METHOD***
**************************************************************************************************
1) Open Terminal
2) Navigate to the project directory
3) Run the run.bash file (command for linux: "./run.bash")
**************************************************************************************************

***ALTERNATE METHOD 1***
**************************************************************************************************
1) Open Terminal
2) Navigate to the src project directory (similar to /home/student/Documents/././src/)
3) Run the run.bash file (command for linux: "./run.bash")
**************************************************************************************************

**HOW TO USE**
1) Enter USERID (ex. 1)
2) press ENTER to find username
3) if no username associated with ID, asks you to enter new username
4) enter HARDWAREID based on HARDWAREID entered into python generator
5) SUBMIT button to add players 1 by 1 down array to SQL database
6) STARTGAME button to begin the game

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




