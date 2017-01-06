import java.util.List;

import java.io.BufferedReader;
import java.util.Iterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;




import org.w3c.dom.css.Counter;

public class main {

	public static void main(String[]args) throws Exception {
		//ファイルの位置情報を文字配列に格納する
		String file[]={"E:\\search\\Anthony and Cleopatra.txt",
				   "E:\\search\\Macbeth.txt",
			       "E:\\search\\Coriolanus.txt",
			       "E:\\search\\Othello, the Moor of Venice.txt",
			       "E:\\search\\Hamlet, Prince of Denmark.txt",
			       "E:\\search\\Romeo And Juliet.txt",
			       "E:\\search\\Julius Caesar.txt",
			       "E:\\search\\Timon of Athens.txt",
			       "E:\\search\\King Lear.txt",
			       "E:\\search\\Titus Andronicus.txt",	
	};
		//ファイルごとに上位５位の単語を算出する
		for (int i = 0; i < file.length; i++) {
			System.out.println(file[i]);//ファイル名を出力する
			search(file[i]);//集計結果を出力する
		}
		
	}
	//上位５位の単語を集計するメソッドである。
	public static void search(String filename) throws Exception {
		ArrayList<String> subarray=new ArrayList<String>();//単語を格納するための補助リスト

		HashMap<String, Float> tfidfmap = new HashMap<String, Float>();//各単語とそのTF-IDF値を格納するmap
		loadWord(filename,subarray);//loadWordメソッドを呼び出し、ファイル名を読み込み、ファイルに含まれる単語を配列に格納する
		HashMap<String, Integer> submap = new HashMap<String, Integer>();//TF値だけを格納するための補助map		
		//単語ずつそのTF値をsubmapに格納する
		for (int i = 0; i <subarray.size(); i++) {
			submap.put(subarray.get(i).toString(),countWord(subarray.get(i).toString(),subarray));
		}		
		//単語ずつそのTF-IDF値を計算して、tfidfmapに格納する
        Iterator<String> it = submap.keySet().iterator();
        while (it.hasNext()) {
        	String key = it.next();
        	tfidfmap.put(key, submap.get(key)*countIDF(key));

        }
        //ソートするためのlistである
        List<Map.Entry<String,Float>> entries = 
                new ArrayList<Map.Entry<String,Float>>(tfidfmap.entrySet());
          Collections.sort(entries, new Comparator<Map.Entry<String,Float>>() {
   
              @Override
              public int compare(
                    Entry<String,Float> entry1, Entry<String,Float> entry2) {
                  return (entry2.getValue()).compareTo(entry1.getValue());
              }
          });
          //ソート完了のlistの上位5位を出力する
          for (int i = 0; i < 5; i++) {
        	  System.out.println(entries.get(i).getKey()+":"+entries.get(i).getValue());
		}

	}	
	//ファイルを読み込むメソッドである。
	public static void loadWord(String filename,ArrayList<String> array) throws Exception{

	
		try{
			//ファイルを読み込むである
			  File file = new File(filename);
			  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-16"));
			  //文書を一行ごとに読み込む
			  String line;

			  while((line = br.readLine())!= null ){

				  int index1 = line.indexOf("<");//"<"で始まる行を飛ばす。
				  int length =line.length();
				  if (index1 == -1 && length!=0) {
					  String[] words=line.split("[\\s,.;:!'—?<\\s\\s>()-]+");//一行の文書を単語ごとにに分割する
					  
					  for (int i = 0; i < words.length; i++) {
						  array.add(words[i]);//単語ごとに配列に格納する
						  
					  }
				  }
			  }
			}catch(FileNotFoundException e){
			  System.out.println(e);

			}catch(IOException e){
			  System.out.println(e);
			}

	}
	 // 単語数を数えるメソッドである。
	public static int countWord(String keyword,ArrayList<String> array) throws Exception{		
		int count = 0;//TFの初期値を0として設定する。
		 // 単語数を数える
		for (int i = 0; i < array.size(); i++) {
			  if(array.get(i).equals(keyword)){
				  count++;
			  }				
		}
		return count;
		
	}
	//IDF値を計算するメソッドである
	public static float countIDF(String keyword) throws Exception{
		float N=10;//ファイル数を10として宣言する
		int n=0;//キーワードを含むファイル数の初期値として0を宣言する。
		ArrayList<String> subidfarray=new ArrayList<String>();
		String file[]={"E:\\search\\Anthony and Cleopatra.txt",
				   "E:\\search\\Macbeth.txt",
			       "E:\\search\\Coriolanus.txt",
			       "E:\\search\\Othello, the Moor of Venice.txt",
			       "E:\\search\\Hamlet, Prince of Denmark.txt",
			       "E:\\search\\Romeo And Juliet.txt",
			       "E:\\search\\Julius Caesar.txt",
			       "E:\\search\\Timon of Athens.txt",
			       "E:\\search\\King Lear.txt",
			       "E:\\search\\Titus Andronicus.txt",	
	};//10個のファイルの位置情報を文字列に格納する
		///countTFメソッドを呼び出し、キーワードを含むファイル数を計算する
		for (int i = 0; i < file.length; i++) {
			loadWord(file[i], subidfarray);
			if(countWord(keyword,subidfarray)>0){
				n++;
			}
			subidfarray.clear();
		}
		float countIDF= (float) Math.log(N/n);//idfの値を計算する
		return countIDF;//算出したidfの値を返す
	}
	
}
