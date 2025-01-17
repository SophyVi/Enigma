package Machine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import Helpers.HelpFunctions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Interface{
	
	public static void main(String[] args) {
		showInterface();
	}
	
	private static void showInterface() {
		int choice = -1;
		System.out.println("Choose type of input. For user input press 1, for loop press 2");
		choice = chooseInteger();
		while(choice != 1 && choice != 2) {
			wrongInput();
			choice = chooseInteger();
		}
		if (choice == 1) {
			showUserInterface();
		}
		if (choice == 2) {
			loop();
		}
	}
	
	private static void EndInterfaceForLoop() {
		boolean flag = true;
		int choice;
		while(flag) {
			flag = false;
			System.out.println("Enter 1 to encode word with different settings");
			System.out.println("Enter 2 to exit");
			choice = chooseInteger();
			if(choice == 1)
				showInterface();
			else if(choice == 2)
				System.exit(0);
			else {
				flag = true;
				wrongInput();
			}
		}
	}
	
	private static void showUserInterface() {
			int[] roters = chooseRoters();			
			int[] settings = chooseSettings();
			int[] offset = chooseOffset();
			int[] plugboard = choosePlugboard();
			int[] toEncode =  chooseStringToEncode();
			//Create enigma machine
			@SuppressWarnings("unused")
			Enigma enigma = new Enigma(roters, settings, offset,plugboard,toEncode);
			EndInterface(roters, settings, offset, plugboard);
			}
	
	private static void EndInterface(int[] roters, int[] settings, int[] offset,int[] plugboard) {
		boolean flag = true;
		while(flag) {
			int choice;
			flag = false;
			System.out.println("Enter 1 to encode word with the same settings");
			System.out.println("press 2 to encode word with different settings");
			System.out.println("Enter 3 to exit");
			choice = chooseInteger();
			if(choice == 1) {
				flag = true;
				newWord(roters, settings, offset,plugboard);
			}
			else if(choice == 2)
				showInterface();
			else if(choice == 3)
				System.exit(0);
			else {
				wrongInput();
				flag = true;
			}
		}
	}
	
	private static int[] chooseRoters() {
		System.out.println("Choose left,middle and right rotors. Enter 3 numbers, each from 1 to 5, with no space between them");
		int intRoters = chooseInteger();
		int flag = 0;
		while(flag == 0) {
			if(intRoters > 999 || intRoters < 100)
				wrongInput();
			else if(!checkRoters(intRoters))
				wrongInput();
			else 
				flag = 1;
			if (flag == 0)
				intRoters = chooseInteger();				
		}
		int[] roters = {(intRoters/10)/10,(intRoters/10)%10,intRoters%10};
		return roters;
	}
	
	private static int[] chooseSettings() {
		System.out.println("Choose settings. Enter 3 letters");
		String setting = chooseString();
		while(setting.equals("@") || setting.length() != 3) {
			wrongInput();
			setting = chooseString();
		}
		return HelpFunctions.stringToInt(setting);
	}
	

	private static int[] chooseOffset() {
		System.out.println("Choose offset. Enter 3 letters");
		String off = chooseString();
		while(off.equals("@") || off.length() != 3) {
			wrongInput();
			off = chooseString();
		}
		return HelpFunctions.stringToInt(off);
	}
	
	private static int[] choosePlugboard() {
		System.out.println("Choose plugboard. Enter each pair with space between them. Each letter can only appears once");
		System.out.println("If you dont want to choose plugboard.Enter 'EMPTY'");
		String pairs = chooseStringWithSpaces();
		ArrayList<Integer> pairsArray = new ArrayList<Integer>();
		boolean flag = false;
		pairs = pairs.replaceAll("\\s","");
		
		while(!flag) {
			if (pairs.toUpperCase().equals("EMPTY"))
				flag = true;
			else if(pairs.length()%2 != 0 || pairs.length()/2>10) {
				wrongInput();
				pairs = chooseStringWithSpaces();
				pairs = pairs.replaceAll("\\s","");
			}
			else if(flag == false) {
				for (int i = 0; i < pairs.length(); i=i+2) {
					String tempPair = "";
					tempPair += pairs.charAt(i);
					tempPair += pairs.charAt(i+1);
					if(tempPair.charAt(0) == tempPair.charAt(1)) {
						wrongInput();
						pairsArray.clear();
						i=-2;
						pairs = chooseStringWithSpaces();
						pairs = pairs.replaceAll("\\s","");
					}	
					int[] pairInt = HelpFunctions.stringToInt(tempPair);
					if(pairsArray.contains(pairInt[0]) || pairsArray.contains(pairInt[1])) {
						wrongInput();
						pairsArray.clear();
						i=-2;
						pairs = chooseStringWithSpaces();
						pairs = pairs.replaceAll("\\s","");
					}
					else {
						pairsArray.add(pairInt[0]);
						pairsArray.add(pairInt[1]);
					}
				}
			}
			if(pairsArray.size() == pairs.length())
				flag = true;
		}
		int[] finalPairs;
		if(!pairsArray.isEmpty())
			finalPairs =  pairsArray.stream().mapToInt(i -> i).toArray();
		else
			finalPairs = null;		
		return finalPairs;
	}
	
	private static int[] chooseStringToEncode() {
		System.out.println("Enter word to encode. It must conatin onlly letters");
		String stringToEncode = chooseStringWithSpaces();
		while(stringToEncode.equals("@")) {
			wrongInput();
			stringToEncode = chooseStringWithSpaces();
		}
		return HelpFunctions.stringToInt(stringToEncode);
	}
	
	
	
	

	
	private static void newWord(int[] roters,int[] settings, int[]offset,int[]finalPairs) {
		int[] toEncode = chooseStringToEncode();		
		//Create enigma machine
		@SuppressWarnings("unused")
		Enigma enigma = new Enigma(roters, settings, offset,finalPairs,toEncode);
	}

	
	
	private static int chooseInteger() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
			try {
				return Integer.parseInt(in.readLine());
			} catch (IOException | NumberFormatException exception) {
				return -1;
			}
	}
	
	private static String chooseString() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
			try {
				String setting = in.readLine();
				if(setting.matches("[A-Za-z]+"))
					return setting;
				else return "@";
			} catch (IOException | NumberFormatException exception) {
				return "@";
			}
	}
	
	private static String chooseStringWithSpaces() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
		try {
			String setting = in.readLine();
			if(setting.matches("[A-Za-z ]+"))
				return setting;
			else return "@";
		} catch (IOException | NumberFormatException exception) {
			return "@";
		}
	}
	
	
	private static boolean checkRoters(int intRoters) {
		int[] roters = new int[3];
		roters[0] = (intRoters/10)/10;
		if(roters[0]<1 || roters[0]>5)
			return false;
		roters[1] = (intRoters/10)%10;
		if(roters[1]<1 || roters[1]>5 || roters[0] == roters[1])
			return false;
		roters[2] = intRoters%10;
		if(roters[2]<1 || roters[2]>5 || roters[0] == roters[2] || roters[1] == roters[2])
			return false;
		return true;
	}
	
	private static void loop() {
		Random rand = new Random();
		for(int i=0; i<1000; i++) {
			int[] roters = createListForRandom(1, 5, 3);
			int[] settings = createListForRandom(0, 25, 3);
			int[] offset = createListForRandom(0, 25, 3);
			int size;
			do {
				size = (rand.nextInt(11))+1;
			}while(size%2 != 0);
			int[] finalPairs = createListForRandom(0, 25, size);
			int[] toEncode = createListForRandom(0, 25,(rand.nextInt(7))+1);
			@SuppressWarnings("unused")
			Enigma enigma = new Enigma(roters, settings, offset,finalPairs,toEncode);
		}
		EndInterfaceForLoop();
	}
	
	private static int[] createListForRandom(int start, int end, int size){
		int[] range = IntStream.rangeClosed(start, end).toArray();
		java.util.List<Integer> listForRand = Arrays.stream(range).boxed().collect(Collectors.toList());
		return randomFill(listForRand,size);
	}
	
	private static int[] randomFill(java.util.List<Integer>listForRand,int size) {
		int rand;
		Random generator = new Random();
		ArrayList<Integer> data = new ArrayList<>();
		do {
			rand = generator.nextInt(listForRand.size()); 
			data.add(listForRand.get(rand));
			listForRand.remove(rand);
		}while(data.size() != size);
		return data.stream().mapToInt(i -> i).toArray();
	}
	
	private static void wrongInput() {
		System.out.println("Wrong input,try again");
	}


}
