# JavaSockets
# Program objective: simple distance measuring between node objects in a graph
# Using TCP sockets, the Nodes are Serialized so they can be sent over a graph
# Results are collected in a method titled "searchBenchmark" that creates of a Hash Map: 
#   Map<Node, Map< Node, Integer>> searchBenchmark(int num, Node[] nodes) {}
#
# ClientServer does the following:
#     1. Creates Graph
#     2. Connects to server using TCP 
#     3. Ships array of nodes to MyServer
#     4. Recieves answer from MyServer and prints out the result
#
# MyServer does the following:
#     1. Waits for connection from ClientServer
#     2. Runs searchBenchmark method on graph recieved from ClientServer
#     3. Sends result back to MyClient
#
#
#
# TO RUN THE PROGRAM: 
# 1. Open up 2 terminal windows with directory set to folder containing the files
# 2. Start up MyServer with the following command: $ java MyServer
#    
# 3. "Server up and ready to go..." will be printed on the MyServer terminal window
# 4. On the 2nd terminal window, start up ClientServer with the following command:
#      $ java ClientServer
