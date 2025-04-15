import psycopg2
import socket
import random
import time

# Database connection function
def get_player_ids(team):
    try:
        # Connect to the database
        conn = psycopg2.connect(
            dbname="photon", 
            user="student", 
            password="student", 
            host="localhost", 
            port="5432"
        )
        cursor = conn.cursor()
        
        # Query to fetch equipment IDs for a specific team
        cursor.execute(f"SELECT id FROM players WHERE team = %s;", (team,))
        player_ids = cursor.fetchall()

        # Close the database connection
        conn.close()

        # Return a list of player IDs
        return [str(player[0]) for player in player_ids]
    except Exception as e:
        print(f"Error fetching player IDs: {e}")
        return []

# Define the server and client address
bufferSize  = 1024
serverAddressPort = ("127.0.0.1", 7500)
clientAddressPort = ("127.0.0.1", 7500)

# Fetch player IDs from the database
red_players = get_player_ids('Red')
green_players = get_player_ids('Green')

print(f"Red Team: {red_players}")
print(f"Green Team: {green_players}")

# Create UDP sockets
UDPServerSocketReceive = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)
UDPClientSocketTransmit = socket.socket(family=socket.AF_INET, type=socket.SOCK_DGRAM)

# Bind server socket
UDPServerSocketReceive.bind(serverAddressPort)

# Wait for game start (202 signal)
print("Waiting for game start signal...")
received_data = ''
while received_data != '202':
    received_data, address = UDPServerSocketReceive.recvfrom(bufferSize)
    received_data = received_data.decode('utf-8')
    print(f"Received from game software: {received_data}")
print('Game started.')

# Start generating traffic
counter = 0

while True:
    # Randomly select players from each team
    red_player = random.choice(red_players)
    green_player = random.choice(green_players)

    # Simulate hit or base event
    if random.randint(1, 2) == 1:
        message = f"{red_player}:{green_player}"
    else:
        message = f"{green_player}:{red_player}"

    # After 10 iterations, simulate base hit for red team
    if counter == 10:
        message = f"{red_player}:43"
    # After 20 iterations, simulate base hit for green team
    if counter == 20:
        message = f"{green_player}:53"

    # Send message to the game server
    print(f"Transmitting to game: {message}")
    UDPClientSocketTransmit.sendto(str.encode(message), clientAddressPort)

    # Receive response from game software
    received_data, address = UDPServerSocketReceive.recvfrom(bufferSize)
    received_data = received_data.decode('utf-8')
    print(f"Received from game software: {received_data}")
    print('')
    
    counter += 1

    # Exit condition if game stops (221 signal)
    if received_data == '221':
        break

    time.sleep(random.randint(1, 3))

print("Simulation complete.")