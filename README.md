# ConwayGameOfLife
This is a Java implementation of Conway's Game of Life using a Swing GUI.

Per [Wikipedia](https://en.wikipedia.org/wiki/Conway's_Game_of_Life):
The universe of the Game of Life is an infinite, two-dimensional orthogonal grid of square cells, each of which is in one of two possible states, alive or dead, (or populated and unpopulated, respectively). Every cell interacts with its eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time, the following transitions occur:

1. Any live cell with fewer than two live neighbors dies, as if by underpopulation.

2. Any live cell with two or three live neighbors lives on to the next generation.

3. Any live cell with more than three live neighbors dies, as if by overpopulation.

4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.


The initial pattern constitutes the seed of the system. The first generation is created by applying the above rules simultaneously to every cell in the seed; births and deaths occur simultaneously, and the discrete moment at which this happens is sometimes called a tick. Each generation is a pure function of the preceding one. The rules continue to be applied repeatedly to create further generations.

# Operating Requirements
This is the raw Java source, no .jar or anything of the like, so simply throw the code into an IDE, compile, and run! This was done to give the testers a way to look directly at the code of judgement.

# How To Play
1. Resize the window if you so choose.
2. The game starts with a randomized grid that is not moving. To get it to play hit Start/Stop
3. If you'd like to clear, hit clear, then Start/Stop twice ~~Java GUI Threading is GREAT.~~
4. If you'd like to draw after clearing, simply click the grids, draw your pattern, then hit Start/Stop and watch the magic happen!
5. To exit, hit the red X or hit the exit button.

# Enjoy!
