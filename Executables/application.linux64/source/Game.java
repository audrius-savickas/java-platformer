import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Game extends PApplet {

JSONObject json;
PImage tileset, background1, background2, background3, arrow_left, arrow_right;
PImage player;
PImage [] players = new PImage [12];

int [] obstacles = new int [99];
int mapX = 58; int mapY = 19;
int [] map = new int[mapX * mapY]; //58 x 19 tiles 
int playerX = 770, playerY = 410, playerW = 60, playerH = 80;
int offsetX = 0, offsetY = 0;
boolean left = false, right = false, up = false, down = false, boost = false, jump = false, e = false, isClicked = false, isFinished = false;
int offset = 0;
int cameraX = 0, cameraY = 0;
int level = 0;
boolean isDead = false, isJump = true;
int jumpStrength = 0, weight = 1;
boolean inBlock = false;
Player playerO = new Player();
Map mapO = new Map();
Editor editor = new Editor();

PFont myFont;
int startTime, endTime;

public void setup() {
  background(0xffC9E7ED);
  tileset = loadImage("../data/tileset2.png"); //14 x 7 tiles
  background1 = loadImage("../data/background1.png");
  background2 = loadImage("../data/background2.png");
  background3 = loadImage("../data/background3.png");
  arrow_left = loadImage("../data/arrow_left.png");
  arrow_right = loadImage("../data/arrow_right.png");

  mapO.initObstacles();
  mapO.getTiles();
  loadPlayerImages();
  playerO.initPlayer();
  
  
  
  myFont = createFont("FFFFORWA.TTF", 32);
  textFont(myFont);
  loadMapFile();
}

public void draw() {
  if (level == 0) {
    textSize(130);
    textAlign(CENTER);
    fill(0xffE80E12);
    text("PLATFORMER", width/2, height/2-150);
    fill(0xff717171);
    textSize(19);
    text("generic", width/2-630, height/2-150);
    textSize(25);
    fill(0xff717171);
    text("Jump away!", width/2, height/2-100);
    textSize(55);
    fill(0);
    text("Press ENTER to start game", width/2, height/2+20);
    text("Press E to edit level", width/2, height/2 + 120);
    textSize(32);
    fill(0xff717171);
    text("Made by Audrius Savickas©, Vilnius University 2021", width/2, height/2+300);
    if (key == '\n') {
      level = 1;
      loadMapFile();
    }
    else if (key == 'e') {
      level = -1;
    }
  }
  else if (level == 1){
    if (isDead) {
      restartGame();
    }
    else {
      startTime = millis();
      mapO.drawMap(map);
      playerO.drawPlayer();
    }
  }
  else if (level == 2){
    if (isDead) {
      restartGame();
    }
    else {
      mapO.drawMap(map);
      playerO.drawPlayer();
    }
  }
  else if (level == 3) {
    if (isDead) {
      restartGame();  
    }
    else {
      mapO.drawMap(map);
      playerO.drawPlayer();
    }
  }
  else if (level == 4) {
    if (!isFinished) {
      endTime = millis();
      isFinished = true;
    }
    gameFinished();
  }
  else if (level == -1) {
    editor.drawEditor();  
  }
}

public void loadPlayerImages() {
  PImage picture = loadImage("../data/players.png");
  int x = 0;
  for (int i = 0; i < 12; i++) {
    players[i] = picture.get(x, 0, 60, 80);  
    x += 60;
  }
}

public void loadMapFile() {
  switch (level) {
    case 1:
      json = loadJSONObject("../data/map1.json");
      break;
    case 2:
      json = loadJSONObject("../data/map2.json");
      break;
    case 3:
      json = loadJSONObject("../data/map3.json");
      break;
    default:
      json = loadJSONObject("../data/map1.json");
      break;
  }
  
  JSONArray layers = json.getJSONArray("layers");
  JSONObject layer = layers.getJSONObject(0);
  JSONArray array = layer.getJSONArray("data");
  for (int i = 0; i < array.size(); i++) {
    map[i] = array.getInt(i);
  }
}



public void keyPressed() {
  //w - 87, s - 83, a - 65, d - 68, shift - 16, space - 32, e - 69
  switch(keyCode) {
    case 87:
      up = true;
      break;
    case 83:
      down = true;
      break;
    case 65:
      left = true;
      break;
    case 68:
      right = true;
      break;
    case 16:
      boost = true;
      break;
    case 32:
      jump = true;
      break;
    case 82:
      //isDead = false;
      break;
    case 69:
      e = true;
      break;
  }  
}

public void keyReleased() {
  //w - 87, s - 83, a - 65, d - 68
  switch(keyCode) {
    case 87:
      up = false;
      break;
    case 83:
      down = false;
      break;
    case 65:
      left = false;
      break;
    case 68:
      right = false;
      break;
    case 16:
      boost = false;
      break;
    case 32:
      jump = false;
      break;
    case 69:
      e = false;
      break;
  }  
}



public int tilePos(float x, float y, int mapX) {
  int tileX, tileY;
  int temp = 0;
  while (temp < x) {
    temp = temp + 32 - 1;  
  }
  tileX = temp / 64;
  temp = 0;
  while (temp < y) {
    temp += 64;  
  }
  tileY = temp/64 - 1;
  return tileY * mapX + tileX;
}

public void restartGame() {
  if (key != 'r') {
    textSize(80);
    textAlign(CENTER);
    text("PRESS R TO RESTART", width/2, height/2+100);
  }
  else {
    isDead = false;
    playerO.initPlayer();
  }
}

public void gameFinished() {
  background(0xffC9E7ED);
  textAlign(CENTER);
  textSize(90);
  fill(0xff717171);
  text("CONGRATULATIONS!", 800, 400);
  textSize(50);
  fill(70, 207, 255);
  text("You have finished the game.", 800, 550);
  text("Your time:", 800, 630);
  fill(0xff717171);
  text( ( ( (endTime - startTime) / 1000) + "s"), 800, 730);
}
public class Editor {
  private int whichLevel = 1;
  private int i_start = 0, j_start = 0;
  private int editorWidth = 32;
  private int arrowWidth = 62;
  private int arrowX = 480, arrowY = 670;
  private int tilesetX = 1050, tilesetY = 600;
  private float tilesetW = 448*1.2f, tilesetH = 224*1.2f;
  private float tileW = 32*1.2f;
  private int xMap = 0, yMap = 0;
  private int wMap = editorWidth * 32, hMap = 640;
  private int tile = 0;
  private int mapTile = 0;
  private boolean tileSelected = false;
  
  private float relX, relY;
  
  public void drawEditor() {
    background(0xffC9E7ED);  
    
    drawText();
    
    image(arrow_left, arrowX, arrowY); //450 700
    image(arrow_right, arrowX + arrowWidth-1, arrowY);
    
    //start button
    if (mouseX >= 1177 && mouseX < 1475 && mouseY >= 470 && mouseY < 571) {
        if (isClicked) level = 1;
        else {
          fill(0, 50);
          rect(1177, 470, 298, 101);
        }
    }
    
    if (isClicked){
      //arrow left
      if (mouseX >= arrowX && mouseX < arrowX + arrowWidth && mouseY >= arrowY && mouseY < arrowY + arrowWidth) {
          if (j_start > 0) j_start -= 1; 
      }
      //arrow right
      else if (mouseX >= arrowX + arrowWidth && mouseX < arrowX + 2*arrowWidth && mouseY >= arrowY && mouseY < arrowY + arrowWidth) {
        if (j_start < mapX - editorWidth) j_start += 1;  
      }
      //tile selection
      else if (mouseX >= tilesetX && mouseX < tilesetX + tilesetW && mouseY >= tilesetY && mouseY < tilesetY + tilesetH) {
        tile = (tilePos(mouseX, mouseY, 14, tileW, tilesetX, tilesetY) - 245);
        tileSelected = true;
      }
      //tile placement
      else if (mouseX >= xMap && mouseX < xMap + wMap && mouseY >= yMap && mouseY < yMap + hMap) {
        mapTile = tilePos(mouseX, mouseY, editorWidth, 32, xMap, yMap);
        if (mouseButton == LEFT) {
          map[PApplet.parseInt(relY * mapX + relX + j_start)] = tile;
        }
        else if (mouseButton == RIGHT) {
          map[PApplet.parseInt(relY * mapX + relX + j_start)] = 0;
        }
      }
    }
    
    //draw map
    if (whichLevel == 1) {
      drawEditorMap(map, 32, i_start, j_start); 
    }
    
    drawTileset();
    
    //draw tile on mouse cursor
    if (tileSelected) {
      image(mapO.tiles[tile], mouseX, mouseY, 32, 32);   
    }
    
    strokeWeight(8);
    line(1035, 0, 1035, height);
    strokeWeight(6);
    rectMode(CENTER);
    noFill();
    rect(1325, 520, 300, 100);
    rectMode(CORNER);
  }
  
  public void drawEditorMap(int map [], int resize, int i_start, int j_start) {
    int x = 0, y = 0;
    for (int i = i_start; i < mapY; i++) {
      for (int j = j_start; j < j_start + editorWidth; j++) {
        image(mapO.tiles[map[i * mapX + j]], x, y, resize, resize); 
        x += resize;
      }
      y += resize;
      x = 0;
    }
  }  
  
  public void drawTileset() {
    rectMode(CORNER);
    noFill();
    strokeWeight(2);
    image(tileset, tilesetX, tilesetY, tilesetW, tilesetH); //tileset originally is 896 x 448 or 14 x 7
    for (int i = 0; i < 7; i++) {
      for (int j = 0; j < 14; j++) {
        rect(tilesetX + j * tileW, tilesetY + i * tileW, tileW, tileW);
      }
    }
  }
  
  public int tilePos(float x, float y, int windowWidth, float tileW, float startX, float startY) {
    float tileX, tileY;
    float temp = startX;
    while (temp < x) {
      temp = temp + 2;  
    }
    tileX = temp / tileW;
    relX = tileX;
    temp = startY;
    while (temp < y) {
      temp += tileW;  
    }
    tileY = temp / tileW - 1;
    relY = tileY;
    return PApplet.parseInt(tileY * windowWidth + tileX);
  }
  
  public void drawText() {
    textSize(50);
    fill(0);
    text("MAP EDITOR", 1330, 100);
    textSize(30);
    text("Click on tile to choose.", 1330, 200);
    textSize(24);
    text("Left click on map to place tile.", 1330, 350);
    text("Right click on map to remove tile.", 1330, 410);
    textSize(64);
    text("PLAY", 1330, 560);
    textSize(60);
    fill(0xffF70505);
    text("PLAY", 1330, 560);
    fill(0);
    textSize(30);
    text("Press left and right arrows to scroll map.", arrowX + arrowWidth-10, arrowY + 125);
  }
}

public void mousePressed() {
  isClicked = true;
}

public void mouseReleased() {
  isClicked = false;
}
public class Map {
  public PImage[] tiles = new PImage[150];
  
  //Methods
  public void getTiles() {
    int x = 0, y = 0;
    int resize = 64;
    mapO.tiles[0] = tileset.get(448, 64, 64, 64);
    for (int i = 0; i < 7; i++) {
      for (int j = 1; j < 15; j++) {
        tiles[i * 14 + j] = tileset.get(x, y, 64, 64);
        x += resize;
      }
      y += resize;
      x = 0;
    }
  }
  
  public void drawMap(int map []) {
    if(level == 1) {
      background(background1);
    }
    else if (level == 2) {
      background(background2);
    }
    else if (level == 3) {
      background(background3);
    }
    int x = cameraX, y = cameraY;
    int counter = 0;
    for (int i = 0; i < mapY; i++) {
      for (int j = 0; j < mapX; j++) {
        image(tiles[map[counter++]], x, y); 
        x += 64;
      }
      y += 64;
      x = cameraX;
    }
  }  
  public void initObstacles() {
    for (int i = 0; i < 99; i++) {
      obstacles[i] = 0;  
    }
    obstacles[1] = 1; obstacles[2] = 1; obstacles[3] = 1;
    obstacles[34] = 1;
    obstacles[48] = 1; obstacles[49] = 1; obstacles[50] = 1;
    obstacles[32] = 1; obstacles[33] = 1;
  }
}
public class Player {
  public boolean canDoubleJump = false;
  public boolean hasPotion = false;
  private boolean onEdgeLeft = false, onEdgeRight = false;
  private int edgeX, direction = 0, animationCounter = 0;
  private int delay = 0;

  public void initPlayer() {
    player = players[0];
    cameraX = 0; 
    cameraY = 0; 
    playerX = 770; 
    playerY = 410; 
    offsetX = 0;
    canDoubleJump = false;
    hasPotion = false;
    onEdgeLeft = false; 
    onEdgeRight = false;
  }

  public void drawPlayer() {
    int speed = 10;
    int speedX = 0;
    int speedY = 0;

    //Horizontal speed calculation
    if (left) {
      direction = 1;
      speedX = -speed;
    }
    if (right) {
      direction = 2;
      speedX = speed;
    }
    if ((!left && !right) || (left && right)) {
      speedX = 0;
    }

    //Animations
    if (direction == 1) {
      delay++;
      if (isJump) {
        player = players[11];
      } else if (left && delay == 2) {
        player = players[animationCounter % 5 + 5];  
        animationCounter++;
      } else {
        player = players[9];
      }
    } else if (direction == 2) {
      delay++;
      if (isJump) {
        player = players[10];
      } else if (right && delay == 2) {
        player = players[animationCounter % 5];  
        animationCounter++;
      } else {
        player = players[0];
      }
    }
    if (delay == 2) delay = 0;

    //Dead screen
    if ((playerY + speedY) > 1120) {
      rectMode(CENTER);
      fill(0, 150);
      strokeWeight(8);
      stroke(999, 0, 0);
      rect(800, 410, 1200, 400);
      textSize(100); 
      fill(999, 0, 0); 
      textAlign(CENTER, CENTER);
      text("DEAD", 800, 350); 
      rectMode(CORNER);
      fill(999, 0, 0);
      isDead = true;
      return;
    }


    //check which tile is next to player
    int right = tilePos(playerX+playerW/2+speedX+5, playerY+playerH/2+speedY, mapX);
    int left = tilePos(playerX+speedX-5, playerY+playerH/2+speedY, mapX);
    int bot = tilePos(playerX+playerW/2-15, playerY+playerH+1, mapX);
    int top = tilePos(playerX+playerW/2, playerY-1, mapX);

    //check if player is on potion, if yes, display text and check if e is pressed
    if (!hasPotion) {
      if (map[tilePos(playerX+playerW/2, playerY+playerH/2, mapX)] == 36) {
        rectMode(CENTER);
        fill(0, 150);
        strokeWeight(8);
        stroke(70, 207, 255);
        rect(800, 260, 1300, 150);
        fill(70, 207, 255);
        textAlign(CENTER);
        textSize(60);
        text("PRESS E TO GAIN HIGH JUMP", 800, 300);  
        if (e) {
          hasPotion = true;  
          canDoubleJump = true;
        }
      }
    }

    //check if on door
    if (map[tilePos(playerX+playerW/2, playerY+playerH/2, mapX)] == 37) {
      rectMode(CENTER);
      fill(0, 150);
      strokeWeight(8);
      stroke(70, 207, 255);
      rect(800, 260, 1300, 150);
      fill(70, 207, 255);
      fill(70, 207, 255);
      textAlign(CENTER);
      textSize(60);
      text("PRESS E TO ENTER NEXT LEVEL", 800, 300);
      if (e) {
        level++;
        loadMapFile();
        initPlayer();
      }
    }

    //Jump calculation
    if (obstacles[map[bot]] == 1 && up) {
      if (hasPotion) jumpStrength = -27;
      else jumpStrength = -20;
      isJump = true;
      canDoubleJump = true;
    } else if (obstacles[map[bot]] != 1) {
      isJump = true;
    } else if (obstacles[map[bot]] == 1 && !up) {
      isJump = false;
    } else if (obstacles[map[bot]] == 1 && down) {
      jumpStrength = 12;
      inBlock = true;
    }
    if (obstacles[map[bot]] != 1 && inBlock) {
      inBlock = false;
    }

    speedY = jumpStrength;
    if (jumpStrength <= 13) 
      jumpStrength = jumpStrength + 1;

    //Actual speed change
    if (speedX < 0 && obstacles[map[left]] == 1) speedX = 0;
    if (speedX > 0 && obstacles[map[right]] == 1) speedX = 0;
    if (speedY > 0 && obstacles[map[bot]] == 1 && inBlock == false) speedY = 0;
    if ((speedY < 0 && obstacles[map[top]] == 1 && inBlock == false) || (speedY < 0 && playerY < 50)) {
      speedY = 0;
      jumpStrength = 0;
    }

    playerX += speedX;
    cameraX -= speedX;
    playerY += speedY;
    cameraY -= speedY;

    //Edge camera detection and modification
    if (cameraX >= 0 && !onEdgeLeft) {
      onEdgeLeft = true;
      edgeX = playerX;
    }
    if (cameraX <= -2050 && !onEdgeRight) {
      onEdgeRight = true;
      edgeX = playerX;
    }
    if (onEdgeLeft) {
      offsetX += speedX;
      cameraX = 0;
      if (playerX + offsetX > edgeX) onEdgeLeft = false;
    } else if (onEdgeRight) {
      offsetX += speedX;
      cameraX = -2050;
      if (playerX + offsetX < edgeX) onEdgeRight = false;
    } else {
      edgeX = -1000;
    }

    //Player drawing
    image(player, width/2-playerW/2+offsetX, height/2-playerH/2);

    //Potion drawing
    if (hasPotion) {
      rectMode(CORNER);
      strokeWeight(5);
      stroke(0);
      fill(0xff56FF15, 150);
      rect(1500, 395, 80, 80);
      image(mapO.tiles[36], 1510, 400);
    }
  }
}
  public void settings() {  size(1600, 900); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Game" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
