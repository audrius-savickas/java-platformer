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

void setup() {
  background(#C9E7ED);
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
  
  size(1600, 900);
  
  myFont = createFont("FFFFORWA.TTF", 32);
  textFont(myFont);
  loadMapFile();
}

void draw() {
  if (level == 0) {
    textSize(130);
    textAlign(CENTER);
    fill(#E80E12);
    text("PLATFORMER", width/2, height/2-150);
    fill(#717171);
    textSize(19);
    text("generic", width/2-630, height/2-150);
    textSize(25);
    fill(#717171);
    text("Jump away!", width/2, height/2-100);
    textSize(55);
    fill(0);
    text("Press ENTER to start game", width/2, height/2+20);
    text("Press E to edit level", width/2, height/2 + 120);
    textSize(32);
    fill(#717171);
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

void loadPlayerImages() {
  PImage picture = loadImage("../data/players.png");
  int x = 0;
  for (int i = 0; i < 12; i++) {
    players[i] = picture.get(x, 0, 60, 80);  
    x += 60;
  }
}

void loadMapFile() {
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



void keyPressed() {
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

void keyReleased() {
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



int tilePos(float x, float y, int mapX) {
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

void restartGame() {
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

void gameFinished() {
  background(#C9E7ED);
  textAlign(CENTER);
  textSize(90);
  fill(#717171);
  text("CONGRATULATIONS!", 800, 400);
  textSize(50);
  fill(70, 207, 255);
  text("You have finished the game.", 800, 550);
  text("Your time:", 800, 630);
  fill(#717171);
  text( ( ( (endTime - startTime) / 1000) + "s"), 800, 730);
}
