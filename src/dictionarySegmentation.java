import java.io.*;
import java.util.*;



public class dictionarySegmentation {
    // This class do Chinese Segmentation use dictionary
	
	int LengthOfDict = 204440;  // the dict have 204440 words
	String [] dict = new String [LengthOfDict]; // dictionary is a String List
	String fileInputAddress = "c:/dictNew.txt"; // the address for our dictionary
	
	
	public void loadDict () throws Exception {
		// read the dictionary from our dictiionary address
		// and store as a string list
		File fileRead = new File(fileInputAddress);
		Scanner input = new Scanner (fileRead);
		for (int i = 0 ; input.hasNext(); i++ ) {
			dict[i] = input.nextLine();
			//System.out.println(i);
		}
		input.close();
	}
	
	public String segmentation(StringBuffer str) {
		// try to segmentation a string,we try 4,3,2 and find the substring
		// in the dictionary .if there is,then cut.else try another.and if 
		// all of the 4,3,2 are not in the dictionary,we just cut 1 step. 
		int count =0, start = 0, i ;
		int [] index = new int [str.length()];
		while (start < str.length()) {
			for ( i = 4 ; i >= 2; i-- ) { // try 4,3,2 steps
				if ( exist(str, start, i) ) { // be careful, may be index out of bound
					index[count++] = (start+i);
					start += i;
					break;
				}
			}
			if (i == 1) { // the word is single
				index[count++] = (start+1);
				start += 1;
			}
		}
		for ( i = count-1 ; i>=0 ; i-- ) { // from back to front
			str.insert(index[i], ' '); // use whitespace to distinguish
		}
		//System.out.println(str);
		return str.toString();
	}
	
	public boolean exist(StringBuffer str, int start, int L) {
		// whether a string(from start to start+L in str) is in our dictionary
		// ps : StringBuffer is more useful than String
		if (start + L > str.length())
			return false; // 
		String s = str.substring(start, start+L);
		for (int i =0 ; i < LengthOfDict ; i++ ) {
			if (dict[i].equals(s))
				return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception {
		// Test for dictionarySegmentation
		dictionarySegmentation CNCut = new dictionarySegmentation();
		CNCut.loadDict();
		StringBuffer s = new StringBuffer ("中国共产党领导的中国人民解放军是抗日战争的中坚力量是民族解放运动的中流砥柱");
		CNCut.segmentation(s);

	}

}
