import psycopg2
import socket
import random
import time
import os
import subprocess
import sys

# Launch Java application (main class)
def start_java_game():
    try:
        print("Launching Java game...")
        classpath_separator = ':'
        jdbc_jar = './lib/postgresql-42.7.5.jar'
        java_classpath = './src' + classpath_separator + jdbc_jar

        subprocess.Popen(['java', '-cp', java_classpath, 'main'])
        # Wait a bit to let the GUI and server spin up
        time.sleep(5)
    except Exception as e:
        print(f"Failed to launch Java game: {e}")

# Database connection function
def get_player_ids(team):
    try:
        conn = psycopg2.connect(
            dbname="photon",
            user="student",
            password="student",
            host="localhost",
            port="5432"
        )
        cursor = conn.cursor()
        cursor.execute("SELECT id FROM players WHERE team = %s AND isPlaying = %s;", (team,True,))
        player_ids = cursor.fetchall()
        conn.close()
        return [str(player[0]) for player in player_ids]
    except Exception as e:
        print(f"Error fetching player IDs: {e}")
        return []

# Define UDP settings:
bufferSize = 1024

# Java UDP server address: (host, port) 7500 is where Java listens
serverAddressPort = ("127.0.0.1", 7500)
# Python client receiver will bind to a different port: 7501
clientAddressPort = ("127.0.0.1", 7501)

# Create UDP socket(s) for Python:
# This socket will be used to receive messages; we bind it to port 7501.
UDPServerSocketReceive = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
UDPServerSocketReceive.bind(clientAddressPort)
print(f"Python receiving socket bound to port {clientAddressPort[1]}.")

# This socket is used for transmitting messages. We do not bind it explicitly.
UDPClientSocketTransmit = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Step 1: Launch Java game
start_java_game()

# Step 2: Wait for game start signal ("202") from Java
print("Waiting for game start signal (202)...")
received_data = ''
while received_data != '202':
    received_packet, addr = UDPServerSocketReceive.recvfrom(bufferSize)
    received_data = received_packet.decode('utf-8')
    print(f"Received from Java: {received_data}")
print("Game started!")

# Step 3: Fetch updated players from DB
red_players = get_player_ids('Red')
green_players = get_player_ids('Green')
print(f"Red Team: {red_players}")
print(f"Green Team: {green_players}")

# Step 4: Start game simulation loop
counter = 0
base_hits = {}  # Dictionary to track players who hit the base

while True:
    red_player = random.choice(red_players)
    green_player = random.choice(green_players)

    if random.randint(1, 2) == 1:
        message = f"{red_player}:{green_player}"
    else:
        message = f"{green_player}:{red_player}"

    if counter == 10:
        hitter = random.choice(green_players)
        message = f"{hitter}:43"  # Simulate a Red base hit event (Green scores)
        
    if counter == 20:
        hitter = random.choice(red_players)
        message = f"{hitter}:53"  # Simulate a Green base hit event (Red scores)
        

    print(f"Transmitting to game: {message}")
    # Send the message to the Java UDP server (which listens on port 7500)
    UDPClientSocketTransmit.sendto(message.encode(), serverAddressPort)

    # Wait for and print the response from Java via our bound socket (port 7501)
    try:
        UDPServerSocketReceive.settimeout(5)  # 5-second timeout
        response_packet, addr = UDPServerSocketReceive.recvfrom(bufferSize)
        response = response_packet.decode('utf-8')
        print(f"Received from Java: {response}\n")
    except socket.timeout:
        #print("No response received within timeout.\n")
        response = ""

    if response == "221":
        print("Game Ended!")
        break

    counter += 1
    time.sleep(random.randint(1, 3))

UDPServerSocketReceive.close()
UDPClientSocketTransmit.close()
print("Simulation complete.")