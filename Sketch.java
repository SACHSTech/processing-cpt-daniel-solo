import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
	
	// Declare global variables
  PImage imgGrass;
  PImage imgHoles;
  float[] fltHoleX = new float[10];
  float[] fltHoleY = new float[10];
  boolean isFinish = false;

  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	// put your size call here
    size(1000, 600);
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
    imgHoles.resize(width / 10, height / 10);
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
	  randomHoles();
  }
  
  // define other methods down here.
  /**
   * Randomly spawns holes across the map at the start of each game where the rabbits can appear from
   * 
   * @param nothing
   * @return nothing
   * 
   */
  public void randomHoles(){
    if (isFinish == false){
      for (int i = 0; i < 10; i++){
        fltHoleX[i] = (float)(Math.random() * 900);
        fltHoleY[i] = (float)(Math.random() * 540);
        image(imgHoles, fltHoleX[i], fltHoleY[i]);
        isFinish = true;
        // Check if any of the holes are overlapping
      }
    }
  }

}