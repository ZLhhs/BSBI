import java.io.*;
import java.util.*;

public class BSBIndexing {
    //using BSBI algorithm to create the index for search engine;
	int PRIME = 45199; // this is a prime.and used for hash function by %;
	static int DictNum = 0; // the number in the dictionary(hash table) from 0 to DictNum-1
	HashTableNode [] HashTable = new HashTableNode [PRIME]; // the hash table,at first all is false
	int MAXSIZE = 5000;//1000000; // the size of <wordID-docID>
	wordIDdocID [] wordIDdocIDArrayList = new wordIDdocID[MAXSIZE];
	int wordIDdocIDListCount = 0;
	int tempIndexCount = 0;
	static String tempIndexStorAddress = "c:/tempIndex/";
	static String finalIndexStorAddress ="c:/finalIndex/";
	int MAXTEMPINDEXNUMBER = 10000;
	String [] tempIndexList = new String [MAXTEMPINDEXNUMBER];
	
	
	
	public int word2IDHash (String str) {
		// from string to int use hash table
		// this function will create the HashTable
		//System.out.println("in hash:"+str);
		int res = 1;
		for (int i = 0; i<str.length(); i ++) {
			res = ( ( (int)str.charAt(i)%PRIME )* res) % PRIME;
			// System.out.println(res);
		}
		if (HashTable[res] == null) { // the first time 
			HashTable[res] = new HashTableNode(); // must new a HashTableNode first
			HashTable[res].p = new HashTableListNode ();
			HashTable[res].p.next = null;
			HashTable[res].p.s = str;
			HashTable[res].p.id = DictNum; // record the ID
			HashTable[res].last = HashTable[res].p;
			DictNum ++;
			return DictNum-1; // return the count for this string
		}
		else { // already in the HashTable but not quite sure
			int ID = exist(res,str);
			if ( ID == -1 ) { // ID == -1 means the world isn't existence
				HashTableListNode q = new HashTableListNode();
				q.next = null;
				q.s = str;
				q.id = DictNum;
				HashTable[res].last.next = q;
				HashTable[res].last = q;
				DictNum++;
				return DictNum-1;
			}
			else { // the word is already existence
				return ID;
			}
		}
	}
	
	public int exist (int address, String str) {
		HashTableListNode p = HashTable[address].p;
		while (p != null) {
			if ( p.s.equals(str) ) {
				return p.id;
			}
			p = p.next;
		}
		return -1;
	}
	
	public void printWordIDdocIDList () {
		for (int i = 0 ; i<wordIDdocIDListCount; i++) {
			System.out.println(wordIDdocIDArrayList[i].wordID+" "+wordIDdocIDArrayList[i].docID);
		}
	}
	
	public void createWordIDdocIDArrayList (String str, int docID) {  // important
		int wordID = word2IDHash (str);
		//System.out.println(str+" "+wordID);
		wordIDdocIDArrayList[wordIDdocIDListCount] = new wordIDdocID();
		wordIDdocIDArrayList[wordIDdocIDListCount].wordID = wordID;
		wordIDdocIDArrayList[wordIDdocIDListCount].docID = docID;
		wordIDdocIDListCount ++ ;
		//printWordIDdocIDList();
		
	}
	
	public tempIndexHeadNode createTempIndexInMemory(wordIDdocID [] wordIDdocIDArrayList, int wordIDdocIDListCount) {
		if (wordIDdocIDListCount <= 0) return null;
		tempIndexHeadNode head = new tempIndexHeadNode();
		tempIndexHeadNode nowHeadNode = head;
		tempIndexListNode nowListNode = new tempIndexListNode();
		head.p = nowListNode;
		head.below = null;
		head.wordID = wordIDdocIDArrayList[0].wordID;
		head.docFrequent = 1;
		head.wordAllFrequent = 1;
		nowListNode.next = null;
		nowListNode.wordDocFrequent = 1;
		nowListNode.docID = wordIDdocIDArrayList[0].docID;
		//      all the up code is for initial.
		for (int i = 1 ; i<wordIDdocIDListCount; i++) {
			if (wordIDdocIDArrayList[i].wordID != wordIDdocIDArrayList[i-1].wordID) {
				// we need create a new headNode
				nowHeadNode.below = new tempIndexHeadNode();
				nowHeadNode = nowHeadNode.below;
				nowHeadNode.below = null;
				nowHeadNode.docFrequent = 1;
				nowHeadNode.wordAllFrequent = 1;
				nowHeadNode.wordID = wordIDdocIDArrayList[i].wordID;
				nowHeadNode.p = new tempIndexListNode();
				nowListNode = nowHeadNode.p;
				nowListNode.next = null;
				nowListNode.docID = wordIDdocIDArrayList[i].docID;
				nowListNode.wordDocFrequent = 1;
			}
			else if (wordIDdocIDArrayList[i].docID != wordIDdocIDArrayList[i-1].docID) {
				// we need create a new listNode
				nowListNode.next = new tempIndexListNode();
				nowListNode = nowListNode.next;
				nowListNode.docID = wordIDdocIDArrayList[i].docID;
				nowListNode.next = null;
				nowListNode.wordDocFrequent = 1;
				nowHeadNode.docFrequent ++;
				nowHeadNode.wordAllFrequent ++;
			}
			else {
				// don't need create new node ,just update nowHeadNode and nowListNode
				nowListNode.wordDocFrequent ++;
				nowHeadNode.wordAllFrequent ++;
				
			}
		} // end for
		return head;
	}
	
	public void printTempIndex (tempIndexHeadNode head) {
		if (head == null) return;
	    tempIndexHeadNode p = head;
	    while (p != null) {
	    	System.out.println("-----wordID:"+p.wordID+" docFreq:"+p.docFrequent+" wordAllFreq:"+p.wordAllFrequent+"-----");
	    	tempIndexListNode q = p.p;
	    	while (q != null) {
	    		System.out.println("docID:"+q.docID+" wordDocFreq:"+q.wordDocFrequent);
	    		q = q.next;
	    	}
	    	p = p.below;
	    }
	}
	
	public void indexing () throws Exception{  // the most important function
		//while (analyzer.hasNext()) {
		//	String str = analyzer.next();
		//createWordIDdocIDArrayList(str, docID);
		
	    quickSort(wordIDdocIDArrayList, 0, wordIDdocIDListCount-1);
	    //printWordIDdocIDList();
	    tempIndexHeadNode head = createTempIndexInMemory(wordIDdocIDArrayList, wordIDdocIDListCount);
        //printTempIndex(head);    
	    tempIndexList[tempIndexCount] = tempIndexStor( head, tempIndexStorAddress);
	    wordIDdocIDListCount = 0; // equal to free the arrayList
	    tempIndexCount ++;
	    head = null; // free the memory used by tempIndex
	    System.gc(); // free the memory tempIndex used
	
	}
	
	public String tempIndexStor (tempIndexHeadNode head, String tempIndexStorAddress) throws Exception{
		if (head == null) return null;
		File fileOutput = new File(tempIndexStorAddress+tempIndexCount+".txt");
		PrintWriter output = new PrintWriter(fileOutput);
		
	    tempIndexHeadNode p = head;
	    while (p != null) {
	    	//System.out.println("-----wordID:"+p.wordID+" docFreq:"+p.docFrequent+" wordAllFreq:"+p.wordAllFrequent+"-----");
	    	output.println(p.wordID+" "+p.docFrequent+" "+p.wordAllFrequent);
	    	tempIndexListNode q = p.p;
	    	while (q != null) {
	    		//System.out.println("docID:"+q.docID+" wordDocFreq:"+q.wordDocFrequent);
	    		output.println(q.docID+" "+q.wordDocFrequent);
	    		q = q.next;
	    	}
	    	p = p.below;
	    }
		output.close();
		return tempIndexCount+".txt";
	}
	
	public void quickSort(wordIDdocID [] wordIDdocIDList, int from, int to) {
		if (from >= to) return;
		int start = from,end = to; // count the from and to;
		wordIDdocID temp = new wordIDdocID();
		temp.wordID = wordIDdocIDList[from].wordID;
		temp.docID = wordIDdocIDList[from].docID;
		while (from < to) { // < or <= ?
			while (from<to && bigger(wordIDdocIDList[to], temp)) to--; // >=
			wordIDdocIDList[from] = wordIDdocIDList[to];
			while (from<to && smaller(wordIDdocIDList[from], temp)) from++;  // <=
			wordIDdocIDList[to] = wordIDdocIDList[from];
		}
		wordIDdocIDList[from] = temp;
		quickSort(wordIDdocIDList, start, from-1);
		quickSort(wordIDdocIDList, from+1, end);
	}
	
	public boolean bigger (wordIDdocID x, wordIDdocID y) { // >=  not small
		if (x.wordID > y.wordID)
			return true;
		if (x.wordID == y.wordID && x.docID>=y.docID)
			return true;
		return false;
	}
	
	public boolean smaller (wordIDdocID x,wordIDdocID y) { // <= not big
		if (x.wordID < y.wordID)
			return true;
		if (x.wordID == y.wordID && x.docID<=y.docID)
			return true;
		return false;
	}
	
	public boolean isFull () {
		if (wordIDdocIDListCount == MAXSIZE) 
			return true;
		return false;
	}
	
	public static void deleteOldTempIndex () {
		File [] oldFile = new File(tempIndexStorAddress).listFiles();
		for (int i = 0 ; i<oldFile.length; i++) {
			oldFile[i].delete();
		}   // delete the old tempIndex.
	}
	
	public void printTempIndexName() {
		System.out.println("This is the temp index name:");
		for (int i = 0 ; i<tempIndexCount; i++) {
			System.out.println(tempIndexList[i]);
		}
	}
	
	public boolean notEnoughMemory(String [] keyWordList) {
		if (wordIDdocIDListCount + keyWordList.length > MAXSIZE)
			return true;
		return false;
	}
	
	public void printInputBuffer(finalIndexHeadNode [][] inputBuffer, int [] indexMergeNowPoint) {
		for (int i = 0 ; i<tempIndexCount; i++) {
			System.out.print( inputBuffer[indexMergeNowPoint[i]][i].wordID+" ");
		}
	}
	
	public void createFinalIndex () throws Exception{
	    // merge all temp index
		int [] indexMergeNowPoint = new int [tempIndexCount];
		int [] indexMergeNowSize = new int [tempIndexCount];
		int inputBufferMaxSize = 100;
		boolean [] fileEmpty = new boolean [tempIndexCount];
		Scanner [] inputList = new Scanner [tempIndexCount];
		finalIndexHeadNode [][] inputBuffer = new finalIndexHeadNode [inputBufferMaxSize][tempIndexCount];
	    finalIndexHeadNode [] finalIndex = new finalIndexHeadNode [DictNum];
	    
		//File outputFile = new File(finalIndexStorAddress);
		//PrintWriter output = new PrintWriter(outputFile);
		
		File tempIndexFileFolder = new File(tempIndexStorAddress);
		String [] tempIndexFileList = tempIndexFileFolder.list();
		for (int i = 0 ; i<tempIndexCount; i++) {
			//System.out.println(tempIndexStorAddress + tempIndexFileList[i]);
			File inputFile = new File(tempIndexStorAddress + tempIndexFileList[i]);
			inputList[i] = new Scanner (inputFile);
		}
		for (int i = 0 ; i<tempIndexCount; i++) {
			//System.out.println(fileEmpty[i]);
			bufferRead(i, inputBuffer, inputList, fileEmpty, inputBufferMaxSize, indexMergeNowPoint, indexMergeNowSize); 
			// full of the buffer or the file is empty
		}
		
		
		
		while (!sizeAllZero(indexMergeNowSize)) { // still have something
			printInputBuffer(inputBuffer, indexMergeNowPoint);
			int wordID = DictNum+1; int docID = 2147483640; int count = -1;
			for (int i = 0 ; i<tempIndexCount; i++) { // find min
				if (indexMergeNowSize[i] == 0) // buffer is empty and file is empty
					continue;
				//else if (indexMergeNowPoint == indexMergeNowSize) // buffer is empty but file not empty
				if (inputBuffer[indexMergeNowPoint[i]][i].wordID<wordID
						|| (inputBuffer[indexMergeNowPoint[i]][i].wordID==wordID && inputBuffer[indexMergeNowPoint[i]][i].p.docID<docID)) {
					count = i;
					wordID = inputBuffer[indexMergeNowPoint[i]][i].wordID;
					docID = inputBuffer[indexMergeNowPoint[i]][i].p.docID;
				}
			}
			System.out.println("we have find a min ,it is "+count+" wordID:"+wordID+" docID:"+docID);
			
			if (finalIndex[wordID] == null) { // a new one
				if (wordID != 0) {
				    indexStor(finalIndex[wordID-1]); // count-1 is over,stor it to file;
				    finalIndex[wordID-1] = null; // free the memory
				}
			    finalIndex[wordID] = new finalIndexHeadNode();
				finalIndex[wordID] = inputBuffer[indexMergeNowPoint[count]][count];
				/*
				finalIndex[wordID].p = new finalIndexListNode();
				finalIndex[wordID].wordID = wordID;
				finalIndex[wordID].docFrequent = inputBuffer[indexMergeNowPoint[count]][count].docFrequent;
				finalIndex[wordID].last = finalIndex[wordID].p;
				finalIndex[wordID].wordAllFrequent = inputBuffer[indexMergeNowPoint[count]][count].wordAllFrequent;
				finalIndex[wordID].p.next = null;
				finalIndex[wordID].p.wordDocFrequent = inputBuffer[indexMergeNowPoint[count]][count].p.wordDocFrequent;
				finalIndex[wordID].p.docID = inputBuffer[indexMergeNowPoint[count]][count].p.docID;
				*/
			}
			else { // wordID already existence
				finalIndex[wordID].docFrequent += inputBuffer[indexMergeNowPoint[count]][count].docFrequent;
				finalIndex[wordID].wordAllFrequent += inputBuffer[indexMergeNowPoint[count]][count].wordAllFrequent;
				finalIndex[wordID].last.next = inputBuffer[indexMergeNowPoint[count]][count].p;
				finalIndex[wordID].last = inputBuffer[indexMergeNowPoint[count]][count].last;
				/*
				finalIndex[wordID].last.next = new finalIndexListNode();
				finalIndex[wordID].last = finalIndex[wordID].last.next;
				finalIndex[wordID].last.next = null;
				finalIndex[wordID].last.docID = inputBuffer[indexMergeNowPoint[count]][count].p.docID;
				finalIndex[wordID].last.wordDocFrequent = inputBuffer[indexMergeNowPoint[count]][count].p.wordDocFrequent;
				*/
			}
			indexMergeNowPoint[count]++;
			
			if (indexMergeNowPoint[count] == indexMergeNowSize[count]) { // buffer is empty
				System.out.println("try read");
				if (inputBufferMaxSize == indexMergeNowSize[count]) { // file not empty
					System.out.println("buffer read!");
					indexMergeNowPoint[count] = 0;
					indexMergeNowSize[count] = 0;
					bufferRead(count, inputBuffer,inputList, fileEmpty, inputBufferMaxSize, indexMergeNowPoint, indexMergeNowSize);
				}
				else { // file is empty too
					indexMergeNowPoint[count] = 0;
					indexMergeNowSize[count] = 0;
					fileEmpty[count] = true;
				}
			}
			// then delete min and be careful add new one
			
		} // end for while()
		indexStor(finalIndex[DictNum-1]); // save the last list;
		finalIndex[DictNum-1] = null; // free the memory
		for (int i = 0 ; i<inputList.length; i++) {
			inputList[i].close(); // close all~
		}
	}
	
	public void indexStor(finalIndexHeadNode head) throws Exception{
		File outputFile = new File(finalIndexStorAddress + head.wordID+".txt");
		PrintWriter output = new PrintWriter(outputFile);
		finalIndexListNode p = head.p;
		while (p != null) {
			output.println(p.docID+" "+p.wordDocFrequent);
			p=p.next;
		}
		output.close();
	}
	public boolean sizeAllZero (int [] indexMergeNowSize) {
		for (int i = 0 ; i<indexMergeNowSize.length; i++) {
			if (indexMergeNowSize[i] != 0 )
				return false;
		}
		return true;
	}
	
	public void bufferRead (int i, finalIndexHeadNode [][] inputBuffer,Scanner [] inputList, boolean [] fileEmpty, int inputBufferMaxSize, int [] indexMergeNowPoint, int [] indexMergeNowSize) {
		if (fileEmpty[i] ) {System.out.println("~!@#$^&~!@#%^&~!@#$%^~@#%^~!@#%^");return;}
		while (inputList[i].hasNext() && indexMergeNowSize[i]<inputBufferMaxSize) {
			inputBuffer[indexMergeNowSize[i]][i] = new finalIndexHeadNode();
			//System.out.println(inputList[i].nextLine());
			//System.out.println(inputList[i].nextLine());
			//System.out.println(inputList[i].nextLine());
			inputBuffer[indexMergeNowSize[i]][i].wordID = inputList[i].nextInt();
			int docFrequent = inputList[i].nextInt();
			inputBuffer[indexMergeNowSize[i]][i].docFrequent = docFrequent;
			inputBuffer[indexMergeNowSize[i]][i].wordAllFrequent = inputList[i].nextInt();
			inputBuffer[indexMergeNowSize[i]][i].p = new finalIndexListNode();
			inputBuffer[indexMergeNowSize[i]][i].p.next = null;
			inputBuffer[indexMergeNowSize[i]][i].p.docID = inputList[i].nextInt();
			inputBuffer[indexMergeNowSize[i]][i].p.wordDocFrequent = inputList[i].nextInt();
			inputBuffer[indexMergeNowSize[i]][i].last = inputBuffer[indexMergeNowSize[i]][i].p;
			for (int j = 1 ; j<docFrequent; j++) {
				inputBuffer[indexMergeNowSize[i]][i].last.next = new finalIndexListNode();
				inputBuffer[indexMergeNowSize[i]][i].last = inputBuffer[indexMergeNowSize[i]][i].last.next;
				inputBuffer[indexMergeNowSize[i]][i].last.next = null;
				inputBuffer[indexMergeNowSize[i]][i].last.docID = inputList[i].nextInt();
				inputBuffer[indexMergeNowSize[i]][i].last.wordDocFrequent = inputList[i].nextInt();
			}
			
			//inputBuffer[indexMergeNowSize[i]][i].last = null; // no use the last at here
			///////////////////////////
			indexMergeNowSize[i]++;
		}
		if(indexMergeNowSize[i] != inputBufferMaxSize) {
			fileEmpty[i] = true;  // the file is empty now;
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		long time1 = System.currentTimeMillis();
		
		BSBIndexing index = new BSBIndexing();
		dictionarySegmentation CNCut = new dictionarySegmentation ();
		Stemmer ENCut = new Stemmer ();
		Analyzer myAnalyzer = new Analyzer();
		CNCut.loadDict();
		String targetFileAddress = "C:/webData/";
		File targetFileFolder = new File(targetFileAddress);
		String [] targetFileList = targetFileFolder.list();
		File nowTargetFile;
		Scanner input;
		//FileUtils.cleanDirectory(targetFileAddress);
		deleteOldTempIndex();
		for (int i = 0 ; i<targetFileList.length; i++) {
			System.out.println(targetFileList[i]);
			
			nowTargetFile = new File(targetFileAddress + targetFileList[i]);
			input = new Scanner(nowTargetFile);
			String content = input.nextLine();
			String noUse = input.nextLine();noUse = input.nextLine();
			noUse = input.nextLine();noUse = input.nextLine();noUse = input.nextLine();
			while (input.hasNext()) 
				content += (" " + input.nextLine());
			System.out.println(content);
			
			String keyWord = myAnalyzer.analysis(myAnalyzer.splitAndDivide(myAnalyzer.filter(content)), CNCut, ENCut);
		    String [] keyWordList = keyWord.split(" ");
		    
		    if (keyWordList.length > index.MAXSIZE)
		    	System.out.println("-----Doc is too bigger! This Doc will be ignore!-----\n\n");
		    else {
		    	if (index.notEnoughMemory(keyWordList)) 	    
		    	    index.indexing();
		        for (int j = 0 ; j<keyWordList.length; j++) 
		    	    index.createWordIDdocIDArrayList(keyWordList[j], Integer.parseInt(new StringBuffer(targetFileList[i]).substring(0, 8).toString()));;
		        System.out.println("-----Doc has been indexing-----\n\n");
		    }
		}
		if (index.wordIDdocIDListCount != 0)
		    index.indexing();  // create index for the least id list
		index.printTempIndexName ();
		System.out.println("temp indexing successful~");
		
		////////////indexing successful//////////////
		index.createFinalIndex();
		System.out.println("final indexing successful~");
		System.out.println(System.currentTimeMillis() - time1);
	}
}


class HashTableNode {
	HashTableListNode p = null;
	HashTableListNode last = null; //pointer for last node
}
class HashTableListNode {
	String s = "";
	int id = -1;
	HashTableListNode next = null;
}
class wordIDdocID {
	int wordID = -1;
	int docID = -1;
}
class tempIndexHeadNode {
	int wordID = -1;
	int docFrequent = -1;
	int wordAllFrequent = -1;
	tempIndexListNode p = null;
	tempIndexHeadNode below = null;
}
class tempIndexListNode {
	int docID = -1;
	int wordDocFrequent = -1;
	tempIndexListNode next = null;
}
class finalIndexHeadNode {
	int wordID = -1;
	int docFrequent = -1;
	int wordAllFrequent = -1;
	finalIndexListNode p = null;
	finalIndexListNode last = null;
}
class finalIndexListNode {
	int docID = -1;
	int wordDocFrequent = -1;
	finalIndexListNode next = null;
}