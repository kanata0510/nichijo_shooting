class Temptation extends FlowObject {
  int size;
  final int ATTRACT_RADIUS = 300;
  final float GAIN = 0.01;
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

  void attract() {
    position.y -= (position.y - player.position.y)*GAIN;
  }
  
  void dissociate() {
    position.y += (position.y - player.position.y)*GAIN;
  }

  void draw() {
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

  void play() {
    textLib2.setVisible(false);
    textLib2.setText(hitText.get(label), width / 2, height - 100, 0.1, 1);
    isActive = false;
    player.isActive = false;
    startTimer = millis();
    decidedSeconds = playSecondsMin + random(playSecondsMax - playSecondsMin);
    isPlaying = true;
  }

  boolean checkHit() {
    return dist(position.x, position.y, player.position.x, player.position.y) <= size / 2;
  }
}
