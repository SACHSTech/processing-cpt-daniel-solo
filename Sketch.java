import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

class Hole {
  // Attributes
  float fltX;
  float fltY;

  // Constructor
  public Hole(float fltX, float fltY) {
     this.fltX = fltX;
     this.fltY = fltY;
  }
}

class Rabbit {
  // Attributes
  static int intDead = 2; 
  static PImage[] imgRabbit = new PImage[2];
  Hole hole;
  int intState;
  int intSpawnCount;
  int intDeadCount;
  
  // Constructor
  public Rabbit(Hole hole, int intFrameCount) {
     this.hole = hole;
     this.intState = 0;
     this.intSpawnCount = intFrameCount;
  }

  public float getX() {
    return hole.fltX + 15;
  }

  public float getY() {
    return hole.fltY - 15;
  }  
  
  public void killed(int intFrameCount) {
    intState = intDead;
    intDeadCount = intFrameCount;    
  }

  public boolean isAlive() {
    return intState < intDead;
  }

  public void move() {
    if (isAlive() == true){
      intState++;
      if (intState == 2){
        Sketch.intLives--;
      }
    }
  }
}

public class Sketch extends PApplet {

  /**
   * Set the size of the call
   * 
   * @param strFileName The name of stritesheet image 
   * @param intColumn   The number of columns that spritesheet can be broken into
   * @param intRow      The number of rows that spritesheet can be broken into
   * @param intX        The column number
   * @param intY        The row number
   * @return            The desired image from the spritesheet 
   * 
   */
  PImage loadImage(String strFileName, int intColumn, int intRow, int intX, int intY){
    PImage imgSpriteSheet = loadImage(strFileName);
    int intFrameWidth = imgSpriteSheet.width / intColumn;
    int intFrameHeight = imgSpriteSheet.height / intRow;
    PImage imgFrame = imgSpriteSheet.get(intX * intFrameWidth, intY * intFrameHeight, intFrameWidth, intFrameHeight);
    return imgFrame;
  }
	
  ArrayList<PImage> imgMoveRabbit = new ArrayList<>();

  float[] fltRabbitY = new float[3];
  float[] fltRabbitX = new float[3];

  Rabbit rabbitPop;

  // Declare image variables
  PImage imgGrass;
  PImage imgHoles;
  PImage imgScope;
  PImage imgGunShot;
  PImage imgRabbitShadow;
  PImage imgHeart;


  // Hole spawning variables
  Hole[] holes = new Hole[10];

  // Variables for loading screen and to only draw once
  boolean isFinish2 = false;
  boolean isRabbitsRunning = false;
  boolean isPaused = false;

  // Bunny animation variables
  PImage imgMovingBunnies;
  int intBunnyFrames = 4;
  int intBunnyFrameWidth;
  int intBunnyFrameHeight;

  PImage imgJumpBunnies;
  int intJumpFrames = 2;
  int intJumpFramesWidth;
  int intJumpFramesHeight;

  // Counter variables
  boolean[] isAlive = new boolean[3]; 
  int intScore = 0;
  int intHighScore = 0;
  int intLiveRabbits = 0;
  static int intLives = 5;

  /**
   * Set the size of the call
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void settings() {
    size(600, 600);
  }

  /**
   * Load images and resize the images
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void setup() {
    Rabbit.imgRabbit[0] = loadImage("912139339.png", 9, 6, 3, 2);
    Rabbit.imgRabbit[0].resize(width / 6, height / 6);
    Rabbit.imgRabbit[1] = loadImage("912139339.png", 9, 6, 3, 1);
    Rabbit.imgRabbit[1].resize(width / 6, height / 6);
    imgMoveRabbit.add(loadImage("bunny-hop-spritesheet.png", 4, 4, 0, 1));
    imgMoveRabbit.add(loadImage("bunny-hop-spritesheet.png", 4, 4, 1, 1));
    imgMoveRabbit.add(loadImage("bunny-hop-spritesheet.png", 4, 4, 2, 1));
    imgMoveRabbit.add(loadImage("bunny-hop-spritesheet.png", 4, 4, 3, 1));

    imgGrass = loadImage("istockphoto-951924976-170667a.jpg");
    imgGrass.resize(width, height);

    imgHoles = loadImage("soil_PNG43.png");
    imgHoles.resize(width / 9, height / 9);

    imgScope = loadImage("Scope_PNG.png");
    imgScope.resize(width / 8, height / 8);

    imgGunShot = loadImage("GunShot.png"); 
    imgGunShot.resize(width / 8, height / 8);

    imgRabbitShadow = loadImage("rabbitshadow1.png");
    imgRabbitShadow.resize(width / 8, height / 8);

    imgHeart = loadImage("heart-png-15.png");
    imgHeart.resize(width / 36, height / 36);

    imgMovingBunnies = loadImage("bunny-hop-spritesheet.png");
    intBunnyFrameWidth = imgMovingBunnies.width / 4;
    intBunnyFrameHeight = imgMovingBunnies.height / 4;

    imgJumpBunnies = loadImage("bunny-hop-spritesheet.png");
    intJumpFramesWidth = imgJumpBunnies.width / 9 + 5;
    intJumpFramesHeight = imgJumpBunnies.height / 6 + 20;    
  }

  /**
   * Runs everything inside 60 times a second
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void draw() {
    background(imgGrass);
    pauseScreen();
	  loadingScreen();
    rabbitMove();
    rabbitJump();
    showScore();
    drawLives();
  }
  
  /**
   * Randomly spawns holes across the map at the start of each game where the rabbits can appear from
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void randomHoles() {
      int i = 0;
      while (i < holes.length){
        holes[i] = new Hole((float)(Math.random() * (600 - imgHoles.width)), (float)(Math.random() * (600 - imgHoles.height)));
        for (int j = 0; j < i; j++){
           if (isHoleOverlap(holes[j], holes[i].fltX, holes[i].fltY)){
              i--;
              break;
           }
        }
        i++;
      }
  }
  
  /**
   * Checks if the holes are overlapping
   * 
   * @param hole  The first hole that has already been drawn
   * @param x     The x coordinate of second hole
   * @param y     The y coordinate of second hole
   * @return      true or false (If overlap)
   * 
   */
  public boolean isHoleOverlap(Hole hole, float x, float y) {
    if (x >= hole.fltX && x <= hole.fltX + imgHoles.width){
      if (y >= hole.fltY && y <= hole.fltY + imgHoles.height){
        return true;
      }
    }
    if (x + imgHoles.width >= hole.fltX && x + imgHoles.width <= hole.fltX + imgHoles.width){
      if (y >= hole.fltY && y <= hole.fltY + imgHoles.height){
        return true;
      }
    }
    if (x >= hole.fltX && x <= hole.fltX + imgHoles.width){
      if (y + imgHoles.height >= hole.fltY && y + imgHoles.height <= hole.fltY + imgHoles.height){
        return true;
      }
    }
    if (x + imgHoles.width >= hole.fltX && x + imgHoles.width <= hole.fltX + imgHoles.width){
      if (y  + imgHoles.height >= hole.fltY && y + imgHoles.height <= hole.fltY + imgHoles.height){
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the holes are overlapping
   * 
   * @param i     Index value of the second rabbit 
   * @return      true or false (If overlap)
   * 
   */
  public boolean isRabbitOverlap(int i) {
    for (int j = 0; j < i; j++){
        if (fltRabbitY[i] >= fltRabbitY[j] && fltRabbitY[i] <= fltRabbitY[j] + intBunnyFrameHeight){
          return true;
        }
        if (fltRabbitY[j] >= fltRabbitY[i] && fltRabbitY[j] <= fltRabbitY[i] + intBunnyFrameHeight){
          return true;
        }
      }
    return false;
  }

  /**
   * Draws the holes on their randomized locations
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void drawHoles() {
    for (Hole hole : holes) {
      image(imgHoles, hole.fltX, hole.fltY);
    }
  }

  /**
   * Draws a scope if the mouse is moved
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void mouseMoved() {
    if (isFinish2 == true && isPaused == false) {
      image(imgScope, mouseX - 30, mouseY - 30);
    }
  }

  /**
   * Pauses the screen if user presses TAB
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void pauseScreen() { 
    if (keyPressed && isFinish2 == true){
      if (key == TAB){
        isPaused = true;
        background(0);
        fill(134);
        rect(200, 350, 200, 100);
        fill(255);
        textSize(20);
        text("Click to resume", 220, 410);
        textSize(40);
        text("Pause Screen", 175, 200);
      }
    }
    keyReleased();
    
    if (mousePressed){
      if (mouseX > 200 && mouseX < 400){
        if (mouseY > 350 && mouseY < 450){
          isPaused = false;
        }
      }
    }
  }

  /**
   * Draws the pause screen even when TAB is released
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void keyReleased() {
    if (key == TAB && isFinish2 == true && isPaused == true){
      background(0);
      fill(134);
      rect(200, 350, 200, 100);
      fill(255);
      textSize(20);
      text("Click to resume", 220, 410);
      textSize(40);
      text("Pause Screen", 175, 200);
    }
  }

  /**
   * Draws the initial loading screen
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void loadingScreen() {
    if (isFinish2 == false){
      background(0);
      fill(255);
      rect(250, 350, 100, 100);
      textSize(30);
      fill(0, 408, 612);
      text("Start", 265, 410);
      text("RABBIT SHOOTER GAME", 140, 200);
      intLives = 5;
      fill(255);
      textSize(20);
      text("High score: " + intHighScore, 30, 40);
      
      if (mousePressed){
        if (mouseX > 250 && mouseX < 350){
          if (mouseY > 350 && mouseY < 450){
            randomHoles();
            isFinish2 = true;
          }
        }
      }
    }
  }
  
  /**
   * Draws the rabbits that pop out of the holes
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void rabbitJump() {
    if (isFinish2 == true && isPaused == false){
      drawHoles();
      if (rabbitPop == null || (!rabbitPop.isAlive() &&  frameCount > rabbitPop.intDeadCount + 60)){
        int intHole = (int)(Math.random() * 10);
        rabbitPop = new Rabbit(holes[intHole], frameCount);
      }
      if (rabbitPop.isAlive()){
        image(Rabbit.imgRabbit[rabbitPop.intState], rabbitPop.hole.fltX - 20, rabbitPop.hole.fltY - 60);
      }
      if ((frameCount - rabbitPop.intSpawnCount + 1) % popSpeed() == 0){        
        rabbitPop.move();
      }
      image(imgScope, mouseX - 30, mouseY - 30);
      if (mousePressed == true){
        image(imgGunShot, mouseX - 30, mouseY - 30);
      }
      if (rabbitPop.isAlive()){
        if (mousePressed && isRabbitHit2(mouseX, mouseY)){
          rabbitPop.killed(frameCount);
          intScore++;
          intHighScore = Math.max(intHighScore, intScore);
        }  
      } 
    }
  }

  /**
   * Draws the horizontally moving rabbits
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void rabbitMove() {
    if (isRabbitsRunning ==  false){
      int a = 0;
      while (a < fltRabbitY.length) {
        fltRabbitY[a] = (float)(Math.random() * (600 - intBunnyFrameHeight));
          if (!isRabbitOverlap(a)){
            fltRabbitX[a] = (a * -200);
            isAlive[a] = true;
            intLiveRabbits++;            
            a++;
          }
      } 
      isRabbitsRunning = true;
    }
    if (isFinish2 == true && isPaused == false){
      for (int i = 0; i < fltRabbitY.length; i++){ 
        if (isAlive[i]){
          if (mousePressed && isRabbitHit(mouseX, mouseY, i)){
            isAlive[i] = false;
            intScore++;
            intHighScore = Math.max(intHighScore, intScore);
            intLiveRabbits--;
          }
          if (isAlive[i] && fltRabbitX[i] >= 600){
            intLiveRabbits--;
            intLives--;
            isAlive[i] = false;
            if(i==2){
              isRabbitsRunning = false;
            }
          }
          image(imgMoveRabbit.get((frameCount / 5) % intBunnyFrames), fltRabbitX[i], fltRabbitY[i]);
          fltRabbitX[i]+=runSpeed();
        }
      }
      if (intLiveRabbits <= 0){
        isRabbitsRunning = false;
      }
      image(imgScope, mouseX - 30, mouseY - 30);
      if (mousePressed == true){
        image(imgGunShot, mouseX - 30, mouseY - 30);
      }
    }
  }

  /**
   * Checks to see if horizontally moving rabbits are hit
   * 
   * @param fltX    The X location of the user's mouse
   * @param fltY    The Y location of the user's mouse
   * @param intNum  The index number of the rabbit
   * @return        true or false (Hit or not)
   * 
   */
  public boolean isRabbitHit(float fltX, float fltY, int intNum) {
    if (fltX >= fltRabbitX[intNum] && fltX <= fltRabbitX[intNum] + intBunnyFrameWidth){
      if (fltY >= fltRabbitY[intNum] && fltY <= fltRabbitY[intNum] + intBunnyFrameHeight){
        return true;
      }
    }
    return false;
  }

  /**
   * Checks to see if jumping rabbits are hit
   * 
   * @param fltX2    The X location of the user's mouse
   * @param fltY2    The Y location of the user's mouse
   * @return         true or false (Hit or not)
   * 
   */
  public boolean isRabbitHit2(float fltX2, float fltY2) {
    rect( rabbitPop.getX() - 5, rabbitPop.getY() - 5 , intJumpFramesWidth + 10, intJumpFramesHeight + 10);
    if (fltX2 >= rabbitPop.getX() - 5  && fltX2 <= rabbitPop.getX() + intJumpFramesWidth + 5){
      if (fltY2 >= rabbitPop.getY() - 5 && fltY2 <= rabbitPop.getY() + intJumpFramesHeight + 5){
        return true;
      }
    }
    return false;
  }

  /**
   * Displays the user's score on the screen
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void showScore() {
    if (isFinish2 == true){
      fill(255,255,0);
      textSize(15);
      text("High Score: " + intHighScore, 485, 20);
      text("Score: " + intScore, 525, 40);
    }
  }

  /**
   * Resets values once lives reaches 0
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void clearRabbits() {
    isPaused = false;
    isFinish2 = false;
    rabbitPop = null;
    intLiveRabbits = 0;
    isRabbitsRunning = false;
  }

  /**
   * Draws the number of lives the user has
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void drawLives() {
    int intAddX = 0;
    if (intLives <= 0){
      clearRabbits();
      intScore = 0;
    }
    if (isFinish2 == true && isPaused == false){
      for (int i = 0; i < intLives; i++){
          image(imgHeart, intAddX, 10);
          intAddX = intAddX + 17;
      }  
    } 
  }

  public int popSpeed() {
    return Math.max(30, 120 - 30*(intScore/10)); 
  }

  public float runSpeed() {
    return Math.min((float)2.5, 1 + (float)(0.5*(intScore/10))); 
  }

}
