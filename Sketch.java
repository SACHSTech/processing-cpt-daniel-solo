import processing.core.PApplet;
import processing.core.PImage;

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
  int intDeadCount;
  
  // Constructor
  public Rabbit(Hole hole) {
     this.hole = hole;
     this.intState = 0;
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

  PImage loadImage(String strFileName, int intColumn, int intRow, int intX, int intY){
    PImage imgSpriteSheet = loadImage(strFileName);
    int intFrameWidth = imgSpriteSheet.width / intColumn;
    int intFrameHeight = imgSpriteSheet.height / intRow;
    PImage imgFrame = imgSpriteSheet.get(intX * intFrameWidth, intY * intFrameHeight, intFrameWidth, intFrameHeight);
    return imgFrame;
  }
	
  PImage[] imgMoveRabbit = new PImage[4];


  float[] fltRabbitY = new float[3];
  float[] fltRabbitX = new float[3];

  Rabbit rabbitPop;

  // Declare image variables
  PImage imgGrass;
  PImage imgHoles;
  PImage imgScope;
  PImage imgGunShot;
  PImage imgRabbitShadow;


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

  // Variables for map
  boolean[] isAlive = new boolean[3]; 
  int intScore = 0;
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
    imgMoveRabbit[0] = loadImage("bunny-hop-spritesheet.png", 4, 4, 0, 1);
    imgMoveRabbit[1] = loadImage("bunny-hop-spritesheet.png", 4, 4, 1, 1);
    imgMoveRabbit[2] = loadImage("bunny-hop-spritesheet.png", 4, 4, 2, 1);
    imgMoveRabbit[3] = loadImage("bunny-hop-spritesheet.png", 4, 4, 3, 1);

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

    imgMovingBunnies = loadImage("bunny-hop-spritesheet.png");
    intBunnyFrameWidth = imgMovingBunnies.width / 4;
    intBunnyFrameHeight = imgMovingBunnies.height / 4;

    imgJumpBunnies = loadImage("bunny-hop-spritesheet.png");
    intJumpFramesWidth = imgJumpBunnies.width / 9 + 5;
    intJumpFramesHeight = imgJumpBunnies.height / 6 + 20;    
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
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

  public boolean isRabbitOverlap(int i) {
    for (int j = 0; i > j; j++){
        if (fltRabbitY[i] >= fltRabbitY[j] && fltRabbitY[i] <= fltRabbitY[j] + intBunnyFrameHeight){
          return true;
        }
        if (fltRabbitY[j] >= fltRabbitY[i] && fltRabbitY[j] <= fltRabbitY[i] + intBunnyFrameHeight){
          return true;
        }
      }
    return false;
  }

  public void drawHoles() {
    for (Hole hole : holes) {
      image(imgHoles, hole.fltX, hole.fltY);
    }
  }

  public void mouseMoved() {
    if (isFinish2 == true && isPaused == false) {
      image(imgScope, mouseX - 30, mouseY - 30);
    }
  }

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
  
  public void rabbitJump() {
    if (isFinish2 == true && isPaused == false){
      drawHoles();
      if (rabbitPop == null || (!rabbitPop.isAlive() &&  frameCount > rabbitPop.intDeadCount + 60)){
        int intHole = (int)(Math.random() * 10);
        rabbitPop = new Rabbit(holes[intHole]);
      }
      if (rabbitPop.isAlive()){
        image(Rabbit.imgRabbit[rabbitPop.intState], rabbitPop.hole.fltX - 20, rabbitPop.hole.fltY - 60);
      }
      if (frameCount % 60 == 0){        
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
        }  
      } 
    }
  }

  public void rabbitMove() {
    if (isRabbitsRunning ==  false){
      int a = 0;
      while (a < fltRabbitY.length) {
        fltRabbitY[a] = (float)(Math.random() * (600 - intBunnyFrameHeight));
          if (!isRabbitOverlap(a)){
            fltRabbitX[a] = 0;
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
            intLiveRabbits--;
          }
          if (fltRabbitX[i] >= 600){
            isRabbitsRunning = false;
            intLiveRabbits--;
            intLives--;
          }
          image(imgMoveRabbit[(frameCount / 5) % intBunnyFrames], fltRabbitX[i], fltRabbitY[i]);
          fltRabbitX[i]++;
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

  public boolean isRabbitHit(float fltX, float fltY, int intNum) {
    if (fltX >= fltRabbitX[intNum] && fltX <= fltRabbitX[intNum] + intBunnyFrameWidth){
      if (fltY >= fltRabbitY[intNum] && fltY <= fltRabbitY[intNum] + intBunnyFrameHeight){
        return true;
      }
    }
    return false;
  }

  public boolean isRabbitHit2(float fltX2, float fltY2) {
    rect( rabbitPop.getX(), rabbitPop.getY(), intJumpFramesWidth, intJumpFramesHeight);
    if (fltX2 >= rabbitPop.getX()  && fltX2 <= rabbitPop.getX() + intJumpFramesWidth){
      if (fltY2 >= rabbitPop.getY() && fltY2 <= rabbitPop.getY() + intJumpFramesHeight){
        return true;
      }
    }
    return false;
  }


  public void showScore() {
    fill(255);
    textSize(15);
    text("Score: " + intScore, 525, 50);
  }

  public void clearRabbits() {
    isPaused = false;
    isFinish2 = false;
    rabbitPop = null;
    intLiveRabbits = 0;
    isRabbitsRunning = false;
  }

  public void drawLives() {
    int intAddX = 0;
    if (intLives <= 0){
      clearRabbits();
      intScore = 0;
    }
    if (isFinish2 == true && isPaused == false){
      for (int i = 0; i < intLives; i++){
          rect(intAddX, 0, 30, 30);
          intAddX = intAddX + 35;
      }  
    } 
  }



}
