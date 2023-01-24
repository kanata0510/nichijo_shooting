class Bullet extends GameObject{
  final int BULLET_SIZE = 20;
  boolean isActive = true;
  PVector vec;
  Bullet(float x, float y, float speed) {
    super(x, y, speed);
    vec = new PVector(speed,0).rotate(random(1)*(HALF_PI)-PI/4.0);
  }

  void draw() {
    if (!isActive){
      return;
    }
    push();
    fill(255, 255, 0);
    position = position.add(vec);
    circle(position.x, position.y, BULLET_SIZE);
    pop();
  }
}
