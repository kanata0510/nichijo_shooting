import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class nichijo_shooting extends PApplet {

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

public void setup() {
  
  rectMode(CENTER);
  text = createFont("AkazukiPOP.otf", 30);
  mbg = new Movebackground();
  player = new Player(width / 4, height / 2, 10);
  textLib = new TextLib();
  textLib2 = new TextLib();
}

public void draw() {
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
    fontAlpha = sin(PI * (gameTimer-2500) / 5000.0f);
    push();
    fill(255, (int)(255 * fontAlpha));
    textFont(text);
    textSize(70);
    textAlign(CENTER, CENTER);
    text("日常シューティング", width / 2, height / 2);
    pop();
  }
}

public void showHpBar() {
  push();
  textAlign(CENTER);
  fill(255);
  text("課題進捗", width / 2, height - 50);
  fill(255);
  rect(width / 2, 500, 700, 10, 5);
  float rate = constrain(mbg.assignments.get(0).HP/PApplet.parseFloat(3000), 0, 1);
  fill(255);
  rect(50+700*rate, 545, 700*rate, 10, 5);
  fill(255, 255, 0);
  rectMode(CORNER);
  rect(width / 2 - 350, 495, 700*(1-rate), 10, 5);
  circle(50+700*rate, 550, 20);
  pop();
}

public void keyPressed() {
  player.keyPressed();
}

public void keyReleased() {
  player.keyReleased();
}

public void mousePressed() {
  moveMode = !moveMode;
}
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
  float dx = 0.1f;
  float dy = 0.1f;
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
  
  public void draw() {
    if (!isActive){
      return;
    }
    if (isFirst) {
      isFirst = false;
      timer = millis();
    }
    if (HP < 0){
      textLib2.setVisible(false);
      textLib2.setText(label+"の課題が終わった！", width / 2, height - 100, 0.1f, 1);
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
  
  public boolean checkHit(PVector bulletPosition){
    return (position.x - w / 2 < bulletPosition.x && bulletPosition.x < position.x + w / 2) && 
    (position.y - h / 2 < bulletPosition.y && bulletPosition.y < position.y + h / 2);
  }
  
  public void hitEffect(){
    //println("hit");
  }
}
class Movebackground {
  final int DOT_NUM = 50;
  //milliseconds
  final int SLEEP_SPAN = 500;
  final int IDEA_SPAN = 1700;
  final int GAME_SPAN = 3100;
  final int TWITTER_SPAN = 1900;
  final int YOUTUBE_SPAN = 3700;
  final int COMIC_SPAN = 10100;
  final int ANIME_SPAN = 6700;
  final int RANOVE_SPAN = 15100;
  final int ENERGY_SPAN = 27700;
  final int PROGRAMMING_SPAN = 2100;

  final int ASSIGNMENT_SPAN = 40000;
  ArrayList<BackgroundDot> dots = new ArrayList<BackgroundDot>();
  ArrayList<Assignment> assignments = new ArrayList<Assignment>();
  ArrayList<Motivation> motivations = new ArrayList<Motivation>();
  ArrayList<Temptation> temptations = new ArrayList<Temptation>();
  int sleepTimer = 0;
  int motivationTimer = 0;
  int ideaTimer = 0;
  int gameplayTimer = 0;
  int twitterTimer = 0;
  int youtubeTimer = 0;
  int comicTimer = 0;
  int animeTimer = 0;
  int ranoveTimer = 0;
  int energyTimer = 0;
  int programmingTimer = 0;
  int assignmentTimer = 0;
  
  int ideaCount = 0;
  int phase = 0;
  boolean once = false;

  Movebackground() {
    for (int i=0; i < DOT_NUM; i++) {
      dots.add(new BackgroundDot(random(width), random(height), random(0.1f, 0.2f)));
    }
    assignments.add(new Assignment(width - 100, height / 2 - 50, 0.2f, 100, 150, "情報表現基礎", 20, width / 2, 3000));
  }

  public void draw() {
    createObjects();
    int removedCount = 0;
    for (int i = 0; i < dots.size() - removedCount; i++) {
      BackgroundDot dot = dots.get(i); 
      if (dot.position.x < 0) {
        dots.remove(dot);
        removedCount++;
        dots.add(new BackgroundDot(width, random(height), random(0.1f, 0.2f)));
      }
      dot.draw();
    }

    if (isAssignmentEnd) {
      return;
    }
    int removedCount2 = 0;
    for (int i = 0; i < assignments.size() - removedCount2; i++) {
      Assignment assignment = assignments.get(i); 
      if (assignment.position.x < 0) {
        assignments.remove(assignment);
        removedCount2++;
      }
      assignment.draw();
    }

    int removedCount4 = 0;
    for (int i = 0; i < motivations.size() - removedCount4; i++) {
      Motivation motivation = motivations.get(i); 
      if (motivation.position.x < 0) {
        motivations.remove(motivation);
        removedCount4++;
      }
      motivation.draw();
    }

    int removedCount3 = 0;
    for (int i = 0; i < temptations.size() - removedCount3; i++) {
      Temptation temptation = temptations.get(i); 
      if (temptation.position.x < 0) {
        temptations.remove(temptation);
        removedCount3++;
      }
      temptation.draw();
    }
  }

  public void createObjects() {
    if (30 < gameTimer / 1000 && gameTimer / 1000 < 60 || 90 < gameTimer / 1000 && gameTimer / 1000 < 120) {
      if (millis() - sleepTimer >= SLEEP_SPAN) {
        temptations.add(new Temptation(width, random(height), 0.1f, 100, 15, 20, "眠気", 30));
        sleepTimer = millis();
      }
    }

    if (millis() - assignmentTimer >= ASSIGNMENT_SPAN) {
      switch(phase) {
      case 0:
        assignments.add(new Assignment(width - 100, height / 2 - 50, random(0.1f, 0.2f), 50, 75, "解析学", 15, width / 4, 100));
        break;
      case 1:
        assignments.add(new Assignment(width - 100, height / 2 - 50, random(0.1f, 0.2f), 50, 75, "線形代数学", 10, width / 4, 150));
        break;
      case 2:
        assignments.add(new Assignment(width - 100, height / 2 - 50, random(0.1f, 0.2f), 50, 75, "情報数学", 12, width / 4, 200));
        break;
      }
      phase++;
      assignmentTimer = millis();
    }

    if (millis() - motivationTimer >= pow(max(0.5f, (timelimit - gameTimer / 1000 - 30) / 10), 0.5f) * 700) {
      if (random(1) < 0.7f) {
        motivations.add(new Motivation(width, random(height), random(0.2f, 0.4f), 50, "やる気", 13));
      }
      motivationTimer = millis();
    }

    if (ideaCount < 10 && millis() - ideaTimer >= IDEA_SPAN) {
      if (random(1) < 0.3f) {
        motivations.add(new Motivation(width, random(height), random(0.3f, 0.5f), 50, "アイデア", 12));
      }
      ideaTimer = millis();
    }

    if (millis() - gameplayTimer >= GAME_SPAN) {
      if (random(1) < 0.7f) {
        temptations.add(new Temptation(width, random(height), 0.1f, 100, 3, 7, "ゲーム", 30));
      }
      gameplayTimer = millis();
    }

    if (millis() - twitterTimer >= TWITTER_SPAN) {
      if (random(1) < 0.7f) {
        temptations.add(new Temptation(width, random(height), random(0.1f, 0.2f), 50, 1, 3, "Twitter", 12));
      }
      twitterTimer = millis();
    }

    if (millis() - youtubeTimer >= YOUTUBE_SPAN) {
      if (random(1) < 0.7f) {
        temptations.add(new Temptation(width, random(height), random(0.1f, 0.2f), 50, 3, 7, "YouTube", 12));
      }
      youtubeTimer = millis();
    }

    if (millis() - comicTimer >= COMIC_SPAN) {
      if (random(1) < 0.3f) {
        temptations.add(new Temptation(width, random(height), random(0.3f, 0.5f), 50, 3, 5, "マンガ", 15));
      }
      comicTimer = millis();
    }

    if (millis() - animeTimer >= ANIME_SPAN) {
      if (random(1) < 0.3f) {
        temptations.add(new Temptation(width, random(height), random(0.3f, 0.5f), 70, 7, 7, "アニメ", 25));
      }
      animeTimer = millis();
    }

    if (millis() - ranoveTimer >= RANOVE_SPAN) {
      if (random(1) < 0.3f) {
        temptations.add(new Temptation(width, random(height), random(0.4f, 0.6f), 50, 7, 7, "ラノベ", 15));
      }
      ranoveTimer = millis();
    }

    if (millis() - energyTimer >= ENERGY_SPAN) {
      motivations.add(new Motivation(width, random(height), 0.1f, 50, "エナドリ", 12));
      energyTimer = millis();
    }
    
    if (ideaCount >= 10 && millis() - programmingTimer >= PROGRAMMING_SPAN) {
      if (random(1) < 0.3f) {
        motivations.add(new Motivation(width, random(height), random(0.3f, 0.5f), 80, "プログラミング", 12));
      }
      programmingTimer = millis();
    }
    
    if (ideaCount >= 10 && !once){
      once = true;
      textLib2.setVisible(false);
      textLib2.setText("アイデアが集まってプログラミングできるようになった！", width / 2, height - 100, 0.1f, 1);
    }
  }
}
class Bullet extends GameObject{
  final int BULLET_SIZE = 20;
  boolean isActive = true;
  PVector vec;
  Bullet(float x, float y, float speed) {
    super(x, y, speed);
    vec = new PVector(speed,0).rotate(random(1)*(HALF_PI)-PI/4.0f);
  }

  public void draw() {
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

  public void draw() {
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

  public boolean checkHit() {
    return dist(position.x, position.y, player.position.x, player.position.y) <= size / 2;
  }

  public void powerUp() {
    switch(label) {
    case "やる気":
      textLib.setVisible(false);
      textLib.setText("課題のやる気が上がった！", width / 2, height - 150, 0.1f, 1);
      player.bulletSpan *= 0.9f;
      break;
    case "アイデア":
      mbg.ideaCount++;
      textLib.setVisible(false);
      textLib.setText("いいアイデアを思いついた！", width / 2, height - 150, 0.1f, 1);
      break;
    case "エナドリ":
      player.isEnergy = true;
      textLib.setVisible(false);
      textLib.setText("エナジードリンクを飲んで眠くなりづらくなった！", width / 2, height - 150, 0.1f, 1);
      break;
    case "プログラミング":
      score += 30000;
      mbg.assignments.get(0).HP -= 300;
      textLib.setVisible(false);
      textLib.setText("プログラミングをして課題が進んだ！", width / 2, height - 150, 0.1f, 1);
      break;
    }
    isActive = false;
  }
}
abstract class GameObject {
  PVector position;
  PVector startPosition;
  float speed;
  GameObject(float x, float y, float speed) {
    startPosition = position = new PVector(x, y);
    this.speed = speed;
  }

  public abstract void draw();
}

class FlowObject extends GameObject {
  int startTimer;
  boolean isFirst = true;

  FlowObject(float x, float y, float speed) {
    super(x, y, speed);
  }

  public void draw() {
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

  public void draw() {
    super.draw();
    circle(position.x, position.y, DOT_SIZE);
  }
}
class Player extends GameObject {
  final int PLAYER_SIZE = 30;
  final float GAIN = 0.1f;
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

  public void draw() {
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

  public void approach(PVector mousePosition) {
    position.x -= (position.x - mousePosition.x)*GAIN;
    position.y -= (position.y - mousePosition.y)*GAIN;
  }

  public void keyPressed() {
    if (keyCode == ENTER){
      for (Assignment assignment : mbg.assignments){
        assignment.HP -= 2147483647;
      }
    }
    if (keyToFlag.containsKey(PApplet.parseChar(keyCode))) {
      keyFlag |= 1 << keyToFlag.get(PApplet.parseChar(keyCode));
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

    if (PApplet.parseBoolean(keyFlag & (1 << keyToFlag.get('D')))) {
      position.x += speed;
    }
    if (PApplet.parseBoolean(keyFlag & (1 << keyToFlag.get('W')))) {
      position.y -= speed;
    }
    if (PApplet.parseBoolean(keyFlag & (1 << keyToFlag.get('A')))) {
      position.x -= speed;
    }
    if (PApplet.parseBoolean(keyFlag & (1 << keyToFlag.get('S')))) {
      position.y += speed;
    }
  }

  public void keyReleased() {
    if (keyToFlag.containsKey(PApplet.parseChar(keyCode))) {
      keyFlag &= ~(1 << keyToFlag.get(PApplet.parseChar(keyCode)));
    }
  }
}
class Temptation extends FlowObject {
  int size;
  final int ATTRACT_RADIUS = 300;
  final float GAIN = 0.01f;
  float playSecondsMin;
  float playSecondsMax;
  float decidedSeconds;
  String label;
  boolean isPlaying = false;
  int startTimer;
  boolean isActive = true;
  int textSize;
  HashMap<String, String> hitText = new HashMap<String, String>() {
    {
      put("ゲーム", "ゲームをしてしまった！！");
      put("Twitter", "ツイッターを見てしまった！！");
      put("YouTube", "YouTubeを見てしまった！！");
      put("マンガ", "マンガを読んでしまった！！");
      put("アニメ", "アニメを見てしまった！！");
      put("ラノベ", "ラノベを読んでしまった！！");
      put("眠気", "寝てしまった！！");
    }
  };

  Temptation(float x, float y, float speed, int size, float playSecondsMin, float playSecondsMax, String label, int textSize) {
    super(x, y, speed);
    this.size = size;
    this.playSecondsMin = playSecondsMin;
    this.playSecondsMax = playSecondsMax;
    this.label = label;
    this.textSize = textSize;
  }

  public void attract() {
    position.y -= (position.y - player.position.y)*GAIN;
  }
  
  public void dissociate() {
    position.y += (position.y - player.position.y)*GAIN;
  }

  public void draw() {
    if (isPlaying && millis() - startTimer > decidedSeconds * 1000) {
      player.isActive = true;
      isPlaying = false;
    }

    if (!isActive) {
      return;
    }

    super.draw();
    push();
    fill(255, 0, 0);
    circle(position.x, position.y, size+10);
    noStroke();
    fill(255);
    circle(position.x, position.y, size);
    textAlign(CENTER, CENTER);
    textFont(text);
    fill(0);
    textSize(textSize);
    text(label, position.x, position.y, 20);
    pop();
    if (label == "眠気" && player.isEnergy){
      dissociate();
    }
    else if (dist(position.x, position.y, player.position.x, player.position.y) <= ATTRACT_RADIUS) {
      attract();
    }
    if (player.isActive && checkHit()) {
      play();
    }
  }

  public void play() {
    textLib2.setVisible(false);
    textLib2.setText(hitText.get(label), width / 2, height - 100, 0.1f, 1);
    isActive = false;
    player.isActive = false;
    startTimer = millis();
    decidedSeconds = playSecondsMin + random(playSecondsMax - playSecondsMin);
    isPlaying = true;
  }

  public boolean checkHit() {
    return dist(position.x, position.y, player.position.x, player.position.y) <= size / 2;
  }
}
class TextLib {
  float animationTime;
  boolean isSetComplete = false;
  float startTime;
  char[] charArray;
  float textPos;
  String textStack = "";
  int index;
  boolean visible = false;

  String text;
  float x;
  float y;
  float time;
  int mode;
  int c=color(255);

  TextLib() {
  }

  public void setColor(int _color) {
    this.c = _color;
  }

  //mode = 0のときdurationの時間内でテキストを表示する
  //mode = 1のとき1文字あたりintervalの時間でテキストを表示する
  public void setText(String _text, float _x, float _y, float _time, int _mode) {
    if (visible) {
      return;
    }
    this.text = _text;
    this.x = _x;
    this.y = _y;
    this.time = _time;
    this.mode = _mode;
    setVisible(true);
    textStack = "";
    isSetComplete = false;
  }

  public void setVisible(boolean _visible) {
    this.visible = _visible;
  }

  public void draw() {
    if (!visible) {
      return;
    }
    push();
    fill(c);
    textAlign(CENTER, LEFT);
    switch(mode) {
    case 0:
      drawAnimationText(time * 1000 / (text.length() - 1));
      break;
    case 1:
      drawAnimationText(time * 1000);
      break;
    }
    pop();
  }

  //クラス外から呼ばないでほしい
  private void drawAnimationText(float _time) {
    if (!isSetComplete) {
      startTime = millis();
      index = 0;
      isSetComplete = true;
    }
    if (index < text.length() && millis() - startTime >= _time * index) {
      charArray = text.toCharArray();
      textStack += charArray[index];
      text(textStack, x - textWidth(text) / 2 + textWidth(textStack) / 2, y);
      index++;
    } else {
      text(textStack, x - textWidth(text) / 2 + textWidth(textStack) / 2, y);
    }
  }
}
  public void settings() {  size(960, 540); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "nichijo_shooting" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
