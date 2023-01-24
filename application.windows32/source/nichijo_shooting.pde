PFont text;
Movebackground mbg;
Player player;

//moveMode:trueのときキー操作
//moveMode:falseのときマウス操作
boolean moveMode = false;
boolean isAssignmentEnd = false;

final int timelimit = 185;
int gameTimer = 0;
int score = 0;
int start = millis();
TextLib textLib;
TextLib textLib2;
float fontAlpha = 0;

void setup() {
  size(960, 540);
  rectMode(CENTER);
  text = createFont("AkazukiPOP.otf", 30);
  mbg = new Movebackground();
  player = new Player(width / 4, height / 2, 10);
  textLib = new TextLib();
  textLib2 = new TextLib();
}

void draw() {
  int remainTime = timelimit - gameTimer / 1000;
  background(0);
  
  mbg.draw();
  text("スコア："+score, 300, 50);
  showHpBar();
  text("残り時間"+(remainTime/60)+"分"+(remainTime-remainTime/60*60)+"秒", 50, 50);
  if (isAssignmentEnd) {
    push();
    textAlign(CENTER, CENTER);
    textSize(230);
    text("提出完了", width / 2, height / 2);
    pop();
    return;
  }
  gameTimer = millis() - start;
  textLib.draw();
  textLib2.draw();
  player.draw();
  if (remainTime <= 0 || mbg.assignments.get(0).HP < 0) {
    isAssignmentEnd = true;
  }
  if (gameTimer-2500 < 5000){
    fontAlpha = sin(PI * (gameTimer-2500) / 5000.0);
    push();
    fill(255, (int)(255 * fontAlpha));
    textFont(text);
    textSize(70);
    textAlign(CENTER, CENTER);
    text("日常シューティング", width / 2, height / 2);
    pop();
  }
}

void showHpBar() {
  push();
  textAlign(CENTER);
  fill(255);
  text("課題進捗", width / 2, height - 50);
  fill(255);
  rect(width / 2, 500, 700, 10, 5);
  float rate = constrain(mbg.assignments.get(0).HP/float(3000), 0, 1);
  fill(255);
  rect(50+700*rate, 545, 700*rate, 10, 5);
  fill(255, 255, 0);
  rectMode(CORNER);
  rect(width / 2 - 350, 495, 700*(1-rate), 10, 5);
  circle(50+700*rate, 550, 20);
  pop();
}

void keyPressed() {
  player.keyPressed();
}

void keyReleased() {
  player.keyReleased();
}

void mousePressed() {
  moveMode = !moveMode;
}
