class Assignment extends GameObject{
  HashMap<String, Integer> HPList = new HashMap<String, Integer>() {
    {
      put("情報表現基礎", 0);
      put("解析学", 1);
      put("線形代数学", 2);
      put("情報数学", 3);
    }
  };
  int HP = 3000;
  final int TEXT_ADJUST = 1;
  final int HEIGHT_ADJUST = 10;
  float w;
  float h;
  String label;
  int textSize;
  float xlimit;
  float dx = 0.1;
  float dy = 0.1;
  int timer;
  boolean isFirst = true;
  boolean isActive = true;
  
  Assignment(float x, float y, float speed, float w, float h, String label, int textSize, float xlimit, int HP) {
    super(x, y, speed);
    this.w = w;
    this.h = h;
    this.label = label;
    this.textSize = textSize;
    this.xlimit = xlimit;
    this.HP = HP;
  }
  
  void draw() {
    if (!isActive){
      return;
    }
    if (isFirst) {
      isFirst = false;
      timer = millis();
    }
    if (HP < 0){
      textLib2.setVisible(false);
      textLib2.setText(label+"の課題が終わった！", width / 2, height - 100, 0.1, 1);
      isActive = false;
      return;
    }
    if (position.x < xlimit || width < position.x){
      position.x = constrain(position.x, xlimit, width);
      dx = -dx;
      position.x += dx;
    }
    if (position.y < 0 || height < position.y){
      position.y = constrain(position.y, 0, height);
      dy = -dy;
      position.y += dy;
    }
    position = new PVector(
      position.x + (timer - millis()) * dx, 
      position.y + (timer - millis()) * dy
      );
    timer = millis();
    for (Bullet bullet : player.bullets){
      if (bullet.isActive && checkHit(bullet.position)){
        score += 100;
        HP--;
        bullet.isActive = false;
        hitEffect();
      }
    }
    push();
    fill(255);
    rect(position.x, position.y, w, h);
    fill(0);
    textFont(text);
    textSize(textSize);
    text(label, position.x, position.y + HEIGHT_ADJUST, textWidth(label.toCharArray()[0]) + TEXT_ADJUST, h);
    pop();
  }
  
  boolean checkHit(PVector bulletPosition){
    return (position.x - w / 2 < bulletPosition.x && bulletPosition.x < position.x + w / 2) && 
    (position.y - h / 2 < bulletPosition.y && bulletPosition.y < position.y + h / 2);
  }
  
  void hitEffect(){
    //println("hit");
  }
}
