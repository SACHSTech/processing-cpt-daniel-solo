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
  static PImage[] imgRabbit = new PImage[2];
  Hole hole;
  int intState;  
  
  // Constructor
  public Rabbit(Hole hole) {
     this.hole = hole;
     this.intState = 0;
  }
  
  public void move() {
    intState++;
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


  // Hole spawning variables
  Hole[] holes = new Hole[10];

  // Variables for loading screen and to only draw once
  boolean isFinish = false;
  boolean isFinish2 = false;
  boolean isFinish3 = false;

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
  int[][] intMap;
  int intBlockWidth;
  int intBlockHeight;
  boolean[] isAlive = new boolean[3];
  int intScore = 0;
  int intLiveRabbits = 0;

  // Blocks are seperated into 0, 1, or 2, to tell what is on that block
  int intEmpty = 0; 
  int intRabbit = 1; 
  int intHole = 2; 

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

    imgMovingBunnies = loadImage("bunny-hop-spritesheet.png");
    intBunnyFrameWidth = imgMovingBunnies.width / 4;
    intBunnyFrameHeight = imgMovingBunnies.height / 4;

    imgJumpBunnies = loadImage("bunny-hop-spritesheet.png");
    intJumpFramesWidth = imgJumpBunnies.width / 9;
    intJumpFramesHeight = imgJumpBunnies.height / 6;
    
    intBlockWidth = Math.max(intBunnyFrameWidth, imgHoles.width);
    intBlockHeight = Math.max(intBunnyFrameHeight, imgHoles.height);
    intMap = new int[600/intBlockWidth][600/intBlockHeight];
    randomHoles();
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    background(imgGrass);
	  loadingScreen();
    rabbitMove();
    rabbitJump();
    showScore();
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
      while(i < holes.length){
        holes[i] = new Hole((float)(Math.random() * (600 - imgHoles.width)), (float)(Math.random() * (600 - imgHoles.height)));
        int intBlockX = (int) holes[i].fltX/intBlockWidth;
        int intBlockY = (int) holes[i].fltY/intBlockHeight;
        if(intMap[intBlockX][intBlockY]==intEmpty){
          intMap[intBlockX][intBlockY]=intHole;
          i++;
        }
      }
  }

  public void drawHoles() {
    for (Hole hole : holes) {
      image(imgHoles, hole.fltX, hole.fltY);
    }
  }

  public void mouseMoved() {
    if (isFinish2 == true) {
      image(imgScope, mouseX - 30, mouseY - 30);
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
      
      if (mousePressed){
        if (mouseX > 250 && mouseX < 350){
          if (mouseY > 350 && mouseY < 450){
            isFinish2 = true;
          }
        }
      }
    }
  }
  
  public void rabbitJump() {
    if (isFinish2 == true){
      drawHoles();
      if (rabbitPop == null){
        int intHole = (int)(Math.random() * 10);
        rabbitPop = new Rabbit(holes[intHole]);
      }
      if (rabbitPop.intState==2){
        rabbitPop = null;
      }else {
        image(Rabbit.imgRabbit[rabbitPop.intState], rabbitPop.hole.fltX - 20, rabbitPop.hole.fltY - 60);
      }
      if (frameCount%60==0){        
        rabbitPop.move();
      }
    }
  }

  public void rabbitMove() {
    if (isFinish3 ==  false){
      clearRabbits();
      int a = 0;
      while(a < fltRabbitY.length) {
        fltRabbitY[a] = (float)(Math.random() * (600 - intBunnyFrameHeight));
        fltRabbitX[a] = 0;
        int intBlockX = (int) fltRabbitX[a]/intBlockWidth;
        int intBlockY = (int) fltRabbitY[a]/intBlockHeight;
        if(intMap[intBlockX][intBlockY]==intEmpty){
          intMap[intBlockX][intBlockY]=intRabbit;
          isAlive[a] = true;
          intLiveRabbits++;
          a++;
        }
      } 
      isFinish3 = true;
    }
    if (isFinish2 == true){
      for (int i = 0; i < fltRabbitY.length; i++){ 
        if (isAlive[i]){
          if (mousePressed && isRabbitHit(mouseX, mouseY, i)){
            isAlive[i] = false;
            intScore++;
            intLiveRabbits--;
          }
          if (fltRabbitX[i] >= 600){
            isFinish3 = false;
            intLiveRabbits--;
          }
          image(imgMoveRabbit[(frameCount / 5) % intBunnyFrames], fltRabbitX[i], fltRabbitY[i]);
          fltRabbitX[i]++;
        }
      }
      if (intLiveRabbits <= 0){
        isFinish3 = false;
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

  public void showScore() {
    fill(255);
    textSize(15);
    text("Score: " + intScore, 525, 50);
  }

  public void clearRabbits() {
    for (int i = 0; i < intMap[0].length; i++){
      if (intMap[0][i] == intRabbit){
        intMap[0][i] = intEmpty;
      }
    }
  }



}
