import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
	
	// Declare global variables
  PImage imgGrass;
  PImage imgHoles;
  PImage imgScope;
  PImage imgGunShot;
  float[] fltHoleX = new float[10];
  float[] fltHoleY = new float[10];
  boolean isFinish = false;
  boolean isFinish2 = false;

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(600, 600);
  }

  /** 
   * Called once at the beginning of execution.  Add initial set up
   * values here i.e background, stroke, fill etc.
   */
  public void setup() {
    imgGrass = loadImage("istockphoto-951924976-170667a.jpg");
    imgGrass.resize(width, height);
    background(imgGrass);
    imgHoles = loadImage("soil_PNG43.png");
    imgHoles.resize(width / 7, height / 7);
    imgScope = loadImage("Scope_PNG.png");
    imgScope.resize(width / 8, height / 8);
    imgGunShot = loadImage("GunShot.png"); 
    imgGunShot.resize(width / 8, height / 8);
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
	  loadingScreen();
    randomHoles();
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
      for (int i = 0; i < fltHoleX.length; i++){
        fltHoleX[i] = (float)(Math.random() * 540);
        fltHoleY[i] = (float)(Math.random() * 540);
        
        image(imgHoles, fltHoleX[i], fltHoleY[i]);
        isFinish = true;
        // Check if any of the holes are overlapping
      }
    }
  }

  public void mouseMoved() {
    if (isFinish2 == true){
      background(imgGrass);
      for (int a = 0; a < 10; a++){
        image(imgHoles, fltHoleX[a], fltHoleY[a]);
      }
      image(imgScope, mouseX - 30, mouseY - 30);
    }
  }

  public void mousePressed() {
    if (isFinish2 == true){
      image(imgGunShot, mouseX - 30, mouseY - 30);
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

}
// There is only one rabbit that appears from set holes, and the user has lives
// If the user misses the rabbit three times, they lose
// The rabbit popping speed becomes faster as time moves on