package line;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class WordCounter {

    HashMap<String, Integer> wordMap;
    HashMap<String, Integer> personMap;
    File file;
    String read;
    FileReader fileReader;
    BufferedReader br;
    Tokenizer tokenizer;

    public WordCounter(String filePath) throws FileNotFoundException, IOException {
        wordMap = new HashMap<String, Integer>();
        personMap = new HashMap<String, Integer>();
        this.file = new File(filePath);
        fileReader = new FileReader(file);
        br = new BufferedReader(fileReader);
        //形態素解析結果を保存するためのトークン
        tokenizer = Tokenizer.builder().build();
        //一行ずつ解析を行う
        while (true) {
            read = br.readLine();
            if (read == null) {
                break;
            }
            //入力されたテキストファイルをタブ区切りに読んでいく
            String[] splitWords = read.split("	");
            //もじ３つ　（時間　Tab 名前 Tab 発言　だった場合のみ処理する　Lineの規格がそんな感じだから
            if (splitWords.length >= 3) {
                //--------------処理したいカウントを呼びだそう------------------
                wordcount(splitWords);
                personCount(splitWords);
                //---------------------------------------------------------
            }
        }
    }

    
    /*---------------------------------------------
                    実際にカウントを行っているメソッド達
    ---------------------------------------------*/
    public final LinkedHashMap<String, Integer> wordcount(String[] result1) {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        //ユーザーの発言部分のみを取得する
        String counttext = "";
        for (int i = 0; i < result1.length; i++) {
            if (i >= 2) {
                counttext += result1[i];
            }
        }
        
        // この1行で形態素解析できる
        List<Token> tokens = tokenizer.tokenize(counttext);

        // 単語ごとにカウント開始
        for (Token token : tokens) {
            //ここで名詞とか副詞とかを書くとフィルタリング出来る
            if (token.getPartOfSpeech().contains("名詞")) {
                String word = token.getSurfaceForm();
                if (personMap.get(word) == null) {
                    if (wordMap.get(word) == null) {
                        wordMap.put(word, 1);
                    } else {
                        wordMap.put(word, wordMap.get(word) + 1);
                    }
                }
            }
        }
        return result;
    }
    
    //人別発言数のカウント
    public final HashMap<String, Integer> personCount(String[] result1) {
        if (personMap.get(result1[1]) == null) {
            personMap.put(result1[1], 1);
        } else {
            personMap.put(result1[1], personMap.get(result1[1]) + 1);
        }
        return personMap;
    }
    
    /*---------------------------------------------
                    取得系メソッド
    ---------------------------------------------*/
    //会話の単語帳を取得する
    public HashMap<String, Integer> getWordMap() {
        return sort(wordMap);
    }

    //ユーザーの発言回数を取得する
    public HashMap<String, Integer> getPersonMap() {
        return sort(personMap);
    }
    
    /*---------------------------------------------
                    閲覧系メソッド
    ---------------------------------------------*/    
    public void wordMapShow(){
        show(wordMap);

    }

    public void personMapShow(){
        show(personMap);
    }
    public void show(HashMap<String,Integer> map){
        map = sort(map);
        for(String key : map.keySet()){
            System.out.print(key+"  :  ");
            System.out.println(map.get(key));
        }
    }

    /*---------------------------------------------
                    その他内部的な奴
    ---------------------------------------------*/
    private HashMap<String, Integer> sort(HashMap<String, Integer> sortMap) {
        //ちょっと頭の良くないソート
        List<Map.Entry> entries = new ArrayList<Map.Entry>(sortMap.entrySet());
        Collections.sort(entries, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Map.Entry e1 = (Map.Entry) o1;
                Map.Entry e2 = (Map.Entry) o2;
                return ((Integer) e1.getValue()).compareTo((Integer) e2.getValue());
            }
        });

        //並び替えたやつを再度HashMapに変換する
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry entry : entries) {
            String key = entry.getKey().toString();
            int val = Integer.parseInt(entry.getValue().toString());
            result.put(key, val);
        }
        return result;
    }
}
