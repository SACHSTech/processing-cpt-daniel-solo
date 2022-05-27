[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=7919692&assignment_repo_type=AssignmentRepo)
# Rabbit Shooter Game (ICS3U1B CPT)

# Learning Objectives

Design a program that will effectively demonstrate knowledge of:

Variables and expressions, User input and interactivity, Conditional logic and boolean expressions,  Repetition, Methods, Arrays, Programming style best practices



# Objective of the game (what's the point of playing)
The objective of the game is to shoot all the rabbits on the screen and avoid having the screen overflooded by rabbits. If there are 25 or more rabbits on the screen, the user loses and is sent back to the homescreen. As the user shoots more rabbits, there will be an increasing score displayed on the top left of the screen. The ultimate objective is for the user to get the highest score possible to beat their own record or others' records. When the user reaches a certain point score, the spawn rate of rabbits will increase until the user eventually loses. 



# Gameplay mechanics and/or user interaction 
The user can interact with the game using their gun scope avatar. The gun scope follows wherever the user's mouse is located. When the user presses their mouse, the gun scope will fire a bullet the size of the scope and will kill any rabbits it touches. When the user presses the 'B' key, the area around the gun scope will explode, causing any rabbits to die. Unlike the mouse click-to-fire, the 'B' key cannot be spammed and can only be used once every 5 seconds. A gameplay mechanic is the loading screen which is found at the start of each game once the user dies. The pausing screen can also be activated by clicking a certain button while the game is ongoing.



# Scoring 
The scoring system of this game follows the idea that every rabbit shot is considered one point. The number of points that a user has is displayed on the top right and will change every time a rabbit is hit. When the number of rabbits on the screen at once reaches 25, the game will go back to the loading screen and the user's top score will be displayed. If the user beats their top score, the top score will be updated. 



# Limitations (any important things your program doesn't do that is worth noting)
It is important to note that once the program has ended, the user's top score will no longer be recorded. Therefore when the user reruns the program, their top score won't be there anymore. 

