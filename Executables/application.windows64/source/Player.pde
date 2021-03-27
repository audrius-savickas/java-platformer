public class Player {
  public boolean canDoubleJump = false;
  public boolean hasPotion = false;
  private boolean onEdgeLeft = false, onEdgeRight = false;
  private int edgeX, direction = 0, animationCounter = 0;
  private int delay = 0;

  void initPlayer() {
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

  void drawPlayer() {
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
      fill(#56FF15, 150);
      rect(1500, 395, 80, 80);
      image(mapO.tiles[36], 1510, 400);
    }
  }
}
