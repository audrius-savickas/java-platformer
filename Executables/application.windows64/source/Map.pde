public class Map {
  public PImage[] tiles = new PImage[150];
  
  //Methods
  void getTiles() {
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
  
  void drawMap(int map []) {
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
  void initObstacles() {
    for (int i = 0; i < 99; i++) {
      obstacles[i] = 0;  
    }
    obstacles[1] = 1; obstacles[2] = 1; obstacles[3] = 1;
    obstacles[34] = 1;
    obstacles[48] = 1; obstacles[49] = 1; obstacles[50] = 1;
    obstacles[32] = 1; obstacles[33] = 1;
  }
}
