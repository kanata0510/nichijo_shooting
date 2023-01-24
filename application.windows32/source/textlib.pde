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
  color c=color(255);

  TextLib() {
  }

  void setColor(color _color) {
    this.c = _color;
  }

  //mode = 0のときdurationの時間内でテキストを表示する
  //mode = 1のとき1文字あたりintervalの時間でテキストを表示する
  void setText(String _text, float _x, float _y, float _time, int _mode) {
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

  void setVisible(boolean _visible) {
    this.visible = _visible;
  }

  void draw() {
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
