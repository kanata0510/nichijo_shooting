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
      dots.add(new BackgroundDot(random(width), random(height), random(0.1, 0.2)));
    }
    assignments.add(new Assignment(width - 100, height / 2 - 50, 0.2, 100, 150, "情報表現基礎", 20, width / 2, 3000));
  }

  void draw() {
    createObjects();
    int removedCount = 0;
    for (int i = 0; i < dots.size() - removedCount; i++) {
      BackgroundDot dot = dots.get(i); 
      if (dot.position.x < 0) {
        dots.remove(dot);
        removedCount++;
        dots.add(new BackgroundDot(width, random(height), random(0.1, 0.2)));
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

  void createObjects() {
    if (30 < gameTimer / 1000 && gameTimer / 1000 < 60 || 90 < gameTimer / 1000 && gameTimer / 1000 < 120) {
      if (millis() - sleepTimer >= SLEEP_SPAN) {
        temptations.add(new Temptation(width, random(height), 0.1, 100, 15, 20, "眠気", 30));
        sleepTimer = millis();
      }
    }

    if (millis() - assignmentTimer >= ASSIGNMENT_SPAN) {
      switch(phase) {
      case 0:
        assignments.add(new Assignment(width - 100, height / 2 - 50, random(0.1, 0.2), 50, 75, "解析学", 15, width / 4, 100));
        break;
      case 1:
        assignments.add(new Assignment(width - 100, height / 2 - 50, random(0.1, 0.2), 50, 75, "線形代数学", 10, width / 4, 150));
        break;
      case 2:
        assignments.add(new Assignment(width - 100, height / 2 - 50, random(0.1, 0.2), 50, 75, "情報数学", 12, width / 4, 200));
        break;
      }
      phase++;
      assignmentTimer = millis();
    }

    if (millis() - motivationTimer >= pow(max(0.5, (timelimit - gameTimer / 1000 - 30) / 10), 0.5) * 700) {
      if (random(1) < 0.7) {
        motivations.add(new Motivation(width, random(height), random(0.2, 0.4), 50, "やる気", 13));
      }
      motivationTimer = millis();
    }

    if (ideaCount < 10 && millis() - ideaTimer >= IDEA_SPAN) {
      if (random(1) < 0.3) {
        motivations.add(new Motivation(width, random(height), random(0.3, 0.5), 50, "アイデア", 12));
      }
      ideaTimer = millis();
    }

    if (millis() - gameplayTimer >= GAME_SPAN) {
      if (random(1) < 0.7) {
        temptations.add(new Temptation(width, random(height), 0.1, 100, 3, 7, "ゲーム", 30));
      }
      gameplayTimer = millis();
    }

    if (millis() - twitterTimer >= TWITTER_SPAN) {
      if (random(1) < 0.7) {
        temptations.add(new Temptation(width, random(height), random(0.1, 0.2), 50, 1, 3, "Twitter", 12));
      }
      twitterTimer = millis();
    }

    if (millis() - youtubeTimer >= YOUTUBE_SPAN) {
      if (random(1) < 0.7) {
        temptations.add(new Temptation(width, random(height), random(0.1, 0.2), 50, 3, 7, "YouTube", 12));
      }
      youtubeTimer = millis();
    }

    if (millis() - comicTimer >= COMIC_SPAN) {
      if (random(1) < 0.3) {
        temptations.add(new Temptation(width, random(height), random(0.3, 0.5), 50, 3, 5, "マンガ", 15));
      }
      comicTimer = millis();
    }

    if (millis() - animeTimer >= ANIME_SPAN) {
      if (random(1) < 0.3) {
        temptations.add(new Temptation(width, random(height), random(0.3, 0.5), 70, 7, 7, "アニメ", 25));
      }
      animeTimer = millis();
    }

    if (millis() - ranoveTimer >= RANOVE_SPAN) {
      if (random(1) < 0.3) {
        temptations.add(new Temptation(width, random(height), random(0.4, 0.6), 50, 7, 7, "ラノベ", 15));
      }
      ranoveTimer = millis();
    }

    if (millis() - energyTimer >= ENERGY_SPAN) {
      motivations.add(new Motivation(width, random(height), 0.1, 50, "エナドリ", 12));
      energyTimer = millis();
    }
    
    if (ideaCount >= 10 && millis() - programmingTimer >= PROGRAMMING_SPAN) {
      if (random(1) < 0.3) {
        motivations.add(new Motivation(width, random(height), random(0.3, 0.5), 80, "プログラミング", 12));
      }
      programmingTimer = millis();
    }
    
    if (ideaCount >= 10 && !once){
      once = true;
      textLib2.setVisible(false);
      textLib2.setText("アイデアが集まってプログラミングできるようになった！", width / 2, height - 100, 0.1, 1);
    }
  }
}
