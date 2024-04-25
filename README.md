# SimplePacman
"Program of a strategy game on an MxN grid where the character, guided by the user, must visit all squares. Maintain energy by eating food while avoiding ghosts."

At the start:
- Load Game      (L/l) : Loads new game from files(JSON)
- Quit           (Q/q) : Quit game
When we load a game we are asked to give the next move of the player. After theghosts move we continue with the player moves until we press "Z" or "z" to pause the game and show the menu.
The main menu of the program is provided below
- Load Game      (L/l) : Loads new game from files(JSON)
- Debug Dijkstra (D/d) : Toggle button for printing Dijkstra board(contains distances of all positions
- Show History   (H/h) : Shows history of player and ghosts moves
- Jump to move   (J/j) : Jumps back to move based on (H/h) and continues game from there
- Continue game  (C/c) : Continue game if possible
- Quit           (Q/q) : Quit game
The menu is printed if 
