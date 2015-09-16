// import HMM.java
// import Stemmer.java


public class Analyzer {
    // this class is analysis the token from text and the input from user
    // if the string is Chinese,we do ChineseSegmentation
	// else if the string is English,we do EnglishStemmer.
	
	public String filter (String str) {
		// remove the character from string if it not a CN or EN.
		// we use whitespace instead of the char
		char [] charList = str.toCharArray();
		for (int i = 0 ; i<charList.length; i++) {
			if ( !isCNorEN(charList[i]) ) {
				charList[i] = ' ';
			}
		}
		return String.valueOf(charList);
	}
	
	public boolean isCNorEN(char c) {
		// whether a character is a CN or EN
		if (c<='z' && c>='a')
			return true;
		else if (c<='Z' && c>='A')
			return true;
		else if ((int)c<=40869 && (int)c>=19968)  
			//Chinese in unicode is from 19968 to 40869
	        return true;
		else return false;
	}
	
	public void print(String str) {
		// just a short print
		System.out.println(str);
	}
	
	public String splitAndDivide(String str) {
		// split the str and divide is to CNStr and ENStr.
		// first,use whitespace to split the input str and get a String list.
		// then,for every String in the list,divide it to CNstr and ENstr.
		// for ENstr we use Stemmer algorithm and for CNstr we use chineseSegmentation.
		// (HMM, CRF, dictionary and so on...)
		// in the end, we use a string named keyword to store the answer,then return it.
		String [] strList = str.trim().split(" ");
		String keyWord="";
		
		for (int i =0 ; i<strList.length; i++) {
			if (strList[i].length() == 0 || strList[i].equals(" "))
				continue; // remove the empty string
			//System.out.println(strList[i]);
			keyWord += (divideToCNEN(strList[i])+" "); // we use whitespace to distinguish
			//System.out.println("in splitAndDivide,keyWord="+keyWord);
		}
		return keyWord;		
	}
	
	public String divideToCNEN(String str) {
		// divide a String to CNString and ENString
		// CNString only contains Chinese and 
		// ENString only contains English
		char [] strCharList = str.toCharArray();
		char [] strCNList = new char[str.length()];
		char [] strENList = new char[str.length()];
		for (int i = 0 ; i<str.length(); i++) {
			if (strCharList[i]<=40869 && strCharList[i]>=19968)  
				//Chinese in unicode is from 19968 to 40869
		        strCNList[i] = strCharList[i];
			else strENList[i] = strCharList[i];
		}
		String CN = String.valueOf(strCNList);
		String EN = String.valueOf(strENList);
		//System.out.println("in divideToCNEN,cn="+CN+" en="+EN);
		return CN+" "+EN;
	}
	
	public String analysis (String keyWord, dictionarySegmentation CNCut, Stemmer myStemmer) {
		// The analysis function, the String "keyword" is contained keywords and distinguish.
		// by whitespace. we split the keyword string and get a string list.
		// for every string in the list, if it is Chinese,we use HMM or RF or dictionary.
		// if it is English, we use stemmer. 
		// Then, we get the answer.(also distinguish by whitespace)
		String [] keyWordList = keyWord.split(" ");
		String answer = "";
		for (int i = 0 ; i<keyWordList.length; i++) {
			String target = keyWordList[i].trim();
			if (target.length() == 0)
				continue; // ignore the empty string
			else if ((int)target.charAt(0)<=40869 && (int)target.charAt(0)>=19968) {
				// CN in unicide is from 19968 to 40869
				String str = CNCut.segmentation(new StringBuffer(target));
				answer += (str+" "); // use whitespace to distinguish
			}
			else { //must be EN
				char [] charList = target.toLowerCase().toCharArray();
			    for (int j = 0 ; j<charList.length; j++)
			    	myStemmer.add(charList[j]);
			    myStemmer.stem();
			    answer += (myStemmer.toString()+" "); // use whitespace to distinguish
			}
			//System.out.println("in analysisFunction:"+answer);
		}
		return answer.trim();
	}
	
	
	public static void main(String[] args) throws Exception{
		// Just test the Analyzer, to see whether it can work 
        Analyzer myAnalyzer = new Analyzer();
        //HMM myHMM = new HMM();
        //myHMM.loadData();  // we use dictionary instead of HMM
        dictionarySegmentation CNCut = new dictionarySegmentation();
        CNCut.loadDict();
        Stemmer myStemmer = new Stemmer();
        String str1 = "大一统回答作为一名谷粉 看完发布会 我有点想哭。Apple 再也不需要在任何一个行业和其他厂商单纯比拼硬件了。Apple将在相当长的一段时间内，用自家的逆天的技术吊打所有竞争对手。做电视的、做盒子的、做Pad的、做电脑的、还有做手机的做手表的，以及语音识别。这些技术有：Force Touch iSight摄像头逆天调教安全性流畅性无人能敌的Touch ID极度流畅稳定省电又保持更新的iOS 、tvOS、OS X生态逆天的App Store Siri世界级的设计且每年均稳定好评以及 中区杀手锏：Apple全部服务 中国完美支持我是谷粉，你可以想象，我说这些的时候，是含着泪的。这些还不够？让我们看看苹果的实力还强大到了何处：首先我来说说发布会上介绍的一个新计划，Apple Upgrade Program 虽然是美国专项，但我们来看一下：每个月32刀 你可以拥有：1.每年一部新iPhone，不限运营商不锁运营商。光这一点就体现出实力了。能让用户接受这个方案，前提就是用户对iPhone的连续100%满意以至于确定追随Apple一辈子（逃试问其他厂商 你们有连续3代产品获得用户如此的好评嘛？2.Apple Care+支持再看看之间我提到的iPhone无可比拟的那些优势，你觉得32刀真的贵吗？再来看看iSight摄像头吧苹果都1200万像素了 这下子 Windows Phone，安卓手机，安卓平板（Surface？哈哈哈快算了吧）们 绝逼不能在像素上再1300万了 否则绝逼被吊打 于是只好刷像素或者搞什么双摄像头之类的噱头。。。我们有理由相信 这又是一颗惊艳的摄像头。想想使用的几个场景：日常拍摄照片*快速连拍*慢动作视频*标星号的 均是逆天的体验。==============即将继续更新：Force TouchApple WatchiPhone 6StvOS以及自己对Apple的未来的展望。尽情期待==============如果您赞同本回答，我将十分感谢，并会根据赞同数加快更新速度，毕竟学生党，感谢您的支持。另外弱弱的求一下粉。。。请关注我，我将尽力提供最优质的回答。==============本回答来自 tnl.space站长Tony1ee。本站诚邀科技爱好者共同维护 在这里发出你的声音！详情私信。";
        // this is test String 
        System.out.println("待分词字符串："+str1);
        String str2 = myAnalyzer.filter(str1);
        String  keyWord ;
        keyWord = myAnalyzer.splitAndDivide(str2);
        String answer = myAnalyzer.analysis(keyWord, CNCut, myStemmer);
        System.out.println(answer);
        String [] keyWordList = answer.trim().split(" ");
        for (int i = 0 ; i<keyWordList.length; i++) {
        	System.out.println("keyWord "+i+": "+keyWordList[i]);
        }   // print out the keyWord 
        
	}

}
