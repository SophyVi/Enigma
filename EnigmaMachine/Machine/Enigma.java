package Machine;

import java.util.ArrayList;
import Helpers.HelpFunctions;

public class Enigma {
	private Roter rightRoter;
	private Roter leftRoter;
	private Roter midRoter;
	private Plugboard plugBoard;
	private Reflector reflector = new Reflector();
	private int currentLetter;
	private ArrayList<Integer> encodedString = new ArrayList<Integer>();
	
	public Enigma(int[] roterNumber, int[] settings, int[] offset,int[]boardPairs,int[] stringToEncode) {
		leftRoter = new Roter(roterNumber[0],settings[0],offset[0]);
		midRoter = new Roter(roterNumber[1],settings[1],offset[1]);
		rightRoter = new Roter(roterNumber[2],settings[2],offset[2]);
		if(boardPairs != null)
			plugBoard = new Plugboard(boardPairs);
		else
			plugBoard = null;
		for (int i = 0; i < stringToEncode.length; i++) {
			currentLetter = stringToEncode[i];
			if(currentLetter != -3)
				encode();
			encodedString.add(currentLetter);
		}
		String output = HelpFunctions.stringToChar(encodedString);
		System.out.println("Encoded string: " + output);
	}
	
	private void encode() {		
		usePlugboard();
		rotateRoters();
		int forOrRev = 0;
		currentLetter = rightRoter.encode(currentLetter,forOrRev);
		currentLetter = midRoter.encode(currentLetter,forOrRev);
		currentLetter = leftRoter.encode(currentLetter,forOrRev);
		currentLetter = reflector.encodeLetter(currentLetter);
		forOrRev = 1;
		currentLetter = leftRoter.encode(currentLetter,forOrRev);
		currentLetter = midRoter.encode(currentLetter,forOrRev);
		currentLetter = rightRoter.encode(currentLetter,forOrRev);
		usePlugboard();
		
	}
	
	private void usePlugboard() {
		if(plugBoard != null) {
			int temp = plugBoard.usePlugboard(currentLetter);
			if(temp != -1)
				currentLetter = temp;
		}
	}
	
	private void rotateRoters() {
		boolean flag;
		flag = rightRoter.rotate();
		if (flag==true || midRoter.getNotch() == midRoter.getOffset())
			flag = midRoter.rotate();
		if (flag == true|| leftRoter.getNotch() == midRoter.getOffset())
			leftRoter.rotate();
	}
}
