abstract class GameObject {
  PVector position;
  PVector startPosition;
  float speed;
  GameObject(float x, float y, float speed) {
    startPosition = position = new PVector(x, y);
    this.speed = speed;
  }

  abstract void draw();
}

class FlowObject extends GameObject {
  int startTimer;
  boolean isFirst = true;

  FlowObject(float x, float y, float speed) {
    super(x, y, speed);
  }

  void draw() {
    if (isFirst) {
      isFirst = false;
      startTimer = millis();
    }
    position = new PVector(
      startPosition.x + (startTimer - millis()) * speed, 
      position.y
      );
  }
}

class BackgroundDot extends FlowObject {
  final int DOT_SIZE = 10;
  FlowObject Object;

  BackgroundDot(float x, float y, float speed) {
    super(x, y, speed);
  }

  void draw() {
    super.draw();
    circle(position.x, position.y, DOT_SIZE);
  }
}
