[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-c66648af7eb3fe8bc4f294546bfd86ef473780cde1dea487d3c4ff354943c9ae.svg)](https://classroom.github.com/online_ide?assignment_repo_id=7919692&assignment_repo_type=AssignmentRepo)
# Rabbit Shooter Game (ICS3U1B CPT)

# Learning Objectives

Design a program that will effectively demonstrate my knowledge of:

Variables and expressions, User input and interactivity, Conditional logic and boolean expressions,  Repetition, Methods, Arrays, and Programming style best practices.



# Objective of the game (what's the point of playing)
The objective of the game is to shoot all the rabbits on the screen without missing any. If the user misses 5 rabbits, the game ends and they are sent back to the home screen. As the user shoots more rabbits, there will be an increasing score displayed on the top right of the screen. The final objective is for the user to get the highest score possible. When the user reaches a certain score, the spawn rate of rabbits will increase until the user eventually loses. 



# Gameplay mechanics and/or user interaction 
The user can interact with the game using a gun scope, which follows their mouse coordinates. When the user presses their mouse, a bullet hole will appear where their mouse is. If this bullet hole hits a rabbit, the rabbit will disappear and the user gains a point. Rabbits will move across the screen in a straight horizontal line at random y-coordinates. Rabbits will also appear randomly from the rabbit holes on the map. A gameplay mechanic is the loading screen which is found at the start of each game or once the user dies. The pausing screen can also be activated by pressing the TAB key while the game is ongoing.



# Scoring 
The scoring system of this game follows the idea that every rabbit shot is considered one point. The number of points that a user has is displayed on the top right and will change every time a rabbit is hit. When the user fails to hit 5 rabbits, the game will go back to the loading screen and the user's top score will be displayed. If the user beats their top score, the top score will be updated. 



# Limitations (any important things your program doesn't do that is worth noting)
It is important to note that once the program has ended, the user's top score will no longer be recorded. Therefore when the user reruns the program, their top score won't be there anymore. Additionally, there is an issue with the gunshot effect where the user can hold their mouse and the gunshot will not dissapear. This is due to the mousePressed variable working as long as the user presses their mouse.

