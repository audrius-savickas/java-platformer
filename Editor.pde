public class Editor {
  private int whichLevel = 1;
  private int i_start = 0, j_start = 0;
  private int editorWidth = 32;
  private int arrowWidth = 62;
  private int arrowX = 480, arrowY = 670;
  private int tilesetX = 1050, tilesetY = 600;
  private float tilesetW = 448*1.2, tilesetH = 224*1.2;
  private float tileW = 32*1.2;
  private int xMap = 0, yMap = 0;
  private int wMap = editorWidth * 32, hMap = 640;
  private int tile = 0;
  private int mapTile = 0;
  private boolean tileSelected = false;
  
  private float relX, relY;
  
  void drawEditor() {
    background(#C9E7ED);  
    
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
          map[int(relY * mapX + relX + j_start)] = tile;
        }
        else if (mouseButton == RIGHT) {
          map[int(relY * mapX + relX + j_start)] = 0;
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
  
  void drawEditorMap(int map [], int resize, int i_start, int j_start) {
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
  
  void drawTileset() {
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
  
  int tilePos(float x, float y, int windowWidth, float tileW, float startX, float startY) {
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
    return int(tileY * windowWidth + tileX);
  }
  
  void drawText() {
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
    fill(#F70505);
    text("PLAY", 1330, 560);
    fill(0);
    textSize(30);
    text("Press left and right arrows to scroll map.", arrowX + arrowWidth-10, arrowY + 125);
  }
}

void mousePressed() {
  isClicked = true;
}

void mouseReleased() {
  isClicked = false;
}
