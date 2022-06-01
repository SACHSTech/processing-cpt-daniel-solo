import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
	
  // Declare image variables
  PImage imgGrass;
  PImage imgHoles;
  PImage imgScope;
  PImage imgGunShot;
  PImage imgMovingBunnies;
  PImage[] imgBunnyFrames;
  PImage imgBunnies;

  // Hole and rabbit spawning variables
  float[] fltHoleX = new float[10];
  float[] fltHoleY = new float[10];
  float[] fltRabbitY = new float[3];
  float[] fltRabbitX = new float[3];

  // Variables for loading screen and to only draw once
  boolean isFinish = false;
  boolean isFinish2 = false;
  boolean isFinish3 = false;

  // Bunny animation variables
  int intBunnyFrames = 4;
  int intBunnyFrameWidth;

  // Variables for map
  int[][] intMap;
  int intBlockWidth;
  int intBlockHeight;
  boolean[] isAlive = new boolean[3];
  int intScore = 0;
  int intLiveRabbits = 0;

  // Blocks are seperated into 0, 1, or 2, to tell what is on that block
  static int intEmpty = 0; 
  static int intRabbit = 1; 
  static int intHole = 2; 

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
    imgBunnies = imgMovingBunnies.get(0, imgMovingBunnies.height/4,  intBunnyFrameWidth * intBunnyFrames, imgMovingBunnies.height/4);
    intBlockWidth = Math.max(intBunnyFrameWidth, imgHoles.width);
    intBlockHeight = Math.max(imgBunnies.height, imgHoles.height);
    intMap = new int[600/intBlockWidth][600/intBlockHeight];
    // Load each frame image onto an index of imgBunnyFrames[]
    imgBunnyFrames = new PImage[intBunnyFrames];
    for (int intFrames = 0; intFrames < imgBunnyFrames.length; intFrames++){
      imgBunnyFrames[intFrames] = imgBunnies.get(intBunnyFrameWidth * intFrames, 0, intBunnyFrameWidth, imgBunnies.height);
    }
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
    background(imgGrass);
	  loadingScreen();
    rabbitMove();
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
    if (isFinish == false){
      int i = 0;
      while(i < fltHoleX.length){
        fltHoleX[i] = (float)(Math.random() * (600 - imgHoles.width));
        fltHoleY[i] = (float)(Math.random() * (600 - imgHoles.height));
        int intBlockX = (int) fltHoleX[i]/intBlockWidth;
        int intBlockY = (int) fltHoleY[i]/intBlockHeight;
        if(intMap[intBlockX][intBlockY]==intEmpty){
          intMap[intBlockX][intBlockY]=intHole;
          i++;
        }
      }
      isFinish = true;
    }
    for (int a = 0; a < fltHoleX.length; a++){
      image(imgHoles, fltHoleX[a], fltHoleY[a]);
    }
  }

  public void mouseMoved() {
    if (isFinish2 == true){
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

  public void rabbitMove() {
    if (isFinish3 ==  false){
      clearRabbits();
      int a = 0;
      while(a < fltRabbitY.length) {
        fltRabbitY[a] = (float)(Math.random() * (600 - imgBunnies.height));
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
      randomHoles();
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
          image(imgBunnyFrames[(frameCount / 5) % intBunnyFrames], fltRabbitX[i], fltRabbitY[i]);
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
      if (fltY >= fltRabbitY[intNum] && fltY <= fltRabbitY[intNum] + imgBunnies.height){
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
