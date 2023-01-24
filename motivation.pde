class Motivation extends FlowObject {
  int size;
  String label;
  boolean isActive = true;
  int textSize;
  Motivation(float x, float y, float speed, int size, String label, int textSize) {
    super(x, y, speed);
    this.size = size;
    this.label = label;
    this.textSize = textSize;
  }

  void draw() {
    if (!isActive) {
      return;
    }
    super.draw();
    push();
    if (label == "プログラミング") {
      fill(0, 255, 0);
    } else {
      fill(0, 0, 255);
    }
    circle(position.x, position.y, size+10);
    noStroke();
    fill(255);
    circle(position.x, position.y, size);
    fill(0);
    textAlign(CENTER, CENTER);
    textFont(text);
    textSize(textSize);
    text(label, position.x, position.y, 20);
    pop();
    if (checkHit()) {
      powerUp();
    }
  }

  boolean checkHit() {
    return dist(position.x, position.y, player.position.x, player.position.y) <= size / 2;
  }

  void powerUp() {
    switch(label) {
    case "やる気":
      textLib.setVisible(false);
      textLib.setText("課題のやる気が上がった！", width / 2, height - 150, 0.1, 1);
      player.bulletSpan *= 0.9;
      break;
    case "アイデア":
      mbg.ideaCount++;
      textLib.setVisible(false);
      textLib.setText("いいアイデアを思いついた！", width / 2, height - 150, 0.1, 1);
      break;
    case "エナドリ":
      player.isEnergy = true;
      textLib.setVisible(false);
      textLib.setText("エナジードリンクを飲んで眠くなりづらくなった！", width / 2, height - 150, 0.1, 1);
      break;
    case "プログラミング":
      score += 30000;
      mbg.assignments.get(0).HP -= 300;
      textLib.setVisible(false);
      textLib.setText("プログラミングをして課題が進んだ！", width / 2, height - 150, 0.1, 1);
      break;
    }
    isActive = false;
  }
}
