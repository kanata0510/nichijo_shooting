class Player extends GameObject {
  final int PLAYER_SIZE = 30;
  final float GAIN = 0.1;
  int keyFlag = 0;
  float startSpeed;
  boolean isActive = true;
  boolean isEnergy = false;
  HashMap<Character, Integer> keyToFlag = new HashMap<Character, Integer>() {
    {
      put('W', 0); 
      put('S', 1); 
      put('A', 2); 
      put('D', 3);
    }
  };
  ArrayList<Bullet> bullets = new ArrayList<Bullet>();
  //milliseconds
  int bulletSpan = 500;
  int timer = millis();

  Player(float x, float y, float speed) {
    super(x, y, speed);
    startSpeed = speed;
  }

  void draw() {
    if (isActive && millis() - timer >= bulletSpan) {
      bullets.add(new Bullet(player.position.x, player.position.y, 10));
      timer = millis();
    }
    PVector mousePosition = new PVector(mouseX, mouseY);
    if (isActive && moveMode == false) {
      //position = mousePosition;
      approach(mousePosition);
    }
    int removedCount = 0;
    for (int i = 0; i < bullets.size() - removedCount; i++) {
      Bullet obj = bullets.get(i); 
      if (obj.position.x > width) {
        bullets.remove(obj);
        removedCount++;
      }
      obj.draw();
    }
    circle(position.x, position.y, PLAYER_SIZE);
  }

  void approach(PVector mousePosition) {
    position.x -= (position.x - mousePosition.x)*GAIN;
    position.y -= (position.y - mousePosition.y)*GAIN;
  }

  void keyPressed() {
    if (keyCode == ENTER){
      for (Assignment assignment : mbg.assignments){
        assignment.HP -= 2147483647;
      }
    }
    if (keyToFlag.containsKey(char(keyCode))) {
      keyFlag |= 1 << keyToFlag.get(char(keyCode));
    }
    int count = 0;
    for (int i = 0; i < 4; i++) {
      count += keyFlag & 1 << i;
    }
    speed = startSpeed;
    if (count >= 2) {
      speed = startSpeed / sqrt(2);
    }

    if (!isActive || moveMode == false) {
      return;
    }

    if (boolean(keyFlag & (1 << keyToFlag.get('D')))) {
      position.x += speed;
    }
    if (boolean(keyFlag & (1 << keyToFlag.get('W')))) {
      position.y -= speed;
    }
    if (boolean(keyFlag & (1 << keyToFlag.get('A')))) {
      position.x -= speed;
    }
    if (boolean(keyFlag & (1 << keyToFlag.get('S')))) {
      position.y += speed;
    }
  }

  void keyReleased() {
    if (keyToFlag.containsKey(char(keyCode))) {
      keyFlag &= ~(1 << keyToFlag.get(char(keyCode)));
    }
  }
}
