# Software-Engineering-Project-LTAG-
Software Engineering project for Jim Strother

----------------------------------------------------------------
HOW-TO-RUN /////////////////////////////////////////////////////
----------------------------------------------------------------

Requirements :
Make sure to have postgresql installed, and have a database set up with a table named players.
The players table should have 4 columns, 1st being id, then codename, hardwareID and team.

All you must do is download our files, make sure you have java installed, then, in your user terminal, navigate to the programs directory, then run the following code.

Use cd ~/ExampleDirectory with "ExampleDirectory" being the area you downloaded it to.

For example, cd ~/Downloads/Software-Engineering-Project-LTAG-

**********************************
INSTALL DEPENDENCIES **IMPORTANT**
**********************************
Make sure you have java installed.

Make sure to have python3 installed. 
Make sure you have updated postgresql.
Make sure you have psycoph2 installed.

To do the above steps, run these in your linux terminal

sudo apt update
sudo apt upgrade -y
sudo apt install python3

Make sure python runs by running the following in your terminal

python3 --version

Next, we will update postgreSQL and install dependencies for the python traffic generator.

Run the following in your linux terminal

sudo apt-get update
sudo apt-get install libpq-dev
pip install psycopg2


Then, 

TO RUN:
*****************
java -jar app.jar
*****************
in terminal

----------------------------------------------------------------
Team Members ///////////////////////////////////////////////////
----------------------------------------------------------------
kaipokrandt : Kai Pokrandt /
codingdodia : Dodia Dhairya /
dylanf9     : Dylan Flanagan /
SherazM1    : Sheraz Mukhtar /
ejmoneyyy1  : EJ Alobuia /

-----------------------------------------------------------------
UPDATES /////////////////////////////////////////////////////////
-----------------------------------------------------------------
Added main.java, base UDP client-server architecture     - 1/28/2025 /
Added SplashScreen.java and EntryScreen.java             - 1/30/2025 /
Implemented more detailed UDP client-server interaction  - 2/4/2025  /
Implemented Database and finished GUI for Entry Screen   - 2/13/2025 /
Implemented basic UDP functionality                      - 2/13/2025 /
Implemented UDP functionality                            - 2/15/2025 /
Implemented update IP functionality and button           - 2/15/2025 /
Implemented Start Game button with UDP code sending      - 2/15/2025 /
Implemented countdown for Start Game                     - 2/15/2025 /

-----------------------------------------------------------------
#TO-DO //////////////////////////////////////////////////////////
-----------------------------------------------------------------

BASH SCRIPT or EXE file for ease of use / DONE /
Link EntryScreen.java to Database.java / DONE /
Store players from EntryScreen to SQLDatabase / DONE /
Create UDP sockets and transmit equipment codes / DONE /
Add option to select different UDP Network / DONE /



