# lineAnalyzer
ラインのグループチャット履歴を解析するJavaプログラムです

#できること
1. 誰が何回発言したかのカウント
2. 出現した単語のカウント

#分析できること
いっぱい発言してる人と，どんな話題が多いのかについて分析可能

#使い方
kuromojiをDLして使いましょう

#Java内での使い方
//準備

String filePath = "ファイルパスを書く";

WordCounter count = new WordCounter(filePath);

//取得系

count.getPersonMap();//HashMap型で人別発話数取得

count.getWordMap();//HashMap型で出現単語数取得

//表示系

count.wordMapShow(); //出現単語数表示

count.personMapShow(); //人別発話数表示
