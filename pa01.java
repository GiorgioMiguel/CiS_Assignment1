/*============================================================================
| Assignment: pa01 - Encrypting a plaintext file using the Hill cipher
|
| Author: Giorgio Torregrosa
| Language: Java
| To Compile: javac pa01.java
| To Execute: java -> java pa01 kX.txt pX.txt
| where kX.txt is the keytext file
| and pX.txt is plaintext file
| Note:
| All input files are simple 8 bit ASCII input
| All execute commands above have been tested on Eustis
|
| Class: CIS3360 - Security in Computing - Summer 2024
| Instructor: McAlpin
| Due Date: 09/15/24
+===========================================================================*/
package pa01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class pa01 {

	public static void main(String[] args) {
		
		//creating the key matrix and printing it to the console 
		int[][] keyMatrix = createKeyMatrix("k5.txt");
		
		//creating the plain text and printing it to the console
		String plainText = createPlainText("p5.txt", keyMatrix[0].length);
		String encryptedText = encryptPlainText(plainText, keyMatrix);
		
		//echo to console with proper format
		formatOutput(plainText, "Plaintext");
		
		System.out.println("\n");
		
		formatOutput(encryptedText, "Ciphertext");
		
		System.out.println();
	}//end main

	public static int[][] createKeyMatrix(String fileName) {
		int n = 0;
		int[][] keyMatrix = { };
		
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String input = br.readLine();
			
			n = Integer.parseInt(input);
			keyMatrix = new int[n][n];
			
			for(int i = 0; i < n; i++) {
				
				input = br.readLine();
				StringTokenizer st = new StringTokenizer(input, "\t");
				
				for(int j = 0; j < n; j++) {
					keyMatrix[i][j] = Integer.parseInt(st.nextToken());
				}//end j loop
				
			}//end i loop
		}//end try 
		catch(Exception e) {
			e.printStackTrace();
		}//end catch
		
		System.out.println("Key matrix:");
		for (int i = 0; i < n; i++) {
		    for (int j = 0; j < n; j++) {
		        System.out.printf("%4d", keyMatrix[i][j]);
		    }//end j loop
			System.out.println("\n");
		}//end i loop
		
		return keyMatrix;
	}//end create matrix
	
	public static String createPlainText(String fileName, int n) {
		
		StringBuilder plainText = null;
		
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			
			plainText = new StringBuilder(br.readLine());
			
			while(br.readLine()!=null) {
				plainText.append(br.readLine());
			}
			
			for(int i = 0; i < plainText.length(); i++) {
				if((int)plainText.charAt(i) >= 97 && (int)plainText.charAt(i) <= 122 || (int)plainText.charAt(i) >= 65 && (int)plainText.charAt(i) <= 90) {
					plainText.setCharAt(i, Character.toLowerCase(plainText.charAt(i)));
				}
				else {
					plainText.deleteCharAt(i);
					i--;
				}
			}//end i loop
			
			/*
			
			for(int i = 0; i < plainText.length(); i++) {
				if(!Character.isLetter(plainText.charAt(i))) {
					plainText.deleteCharAt(i);
					i--;
				}
				else {
					plainText.setCharAt(i, Character.toLowerCase(plainText.charAt(i)));
				}
			}//end i loop
			
			*/
		}//end try
		catch(Exception e) {
			e.printStackTrace();
		}//end catch
		
		
		int paddingLength = n - plainText.length() % n; 
		
		//padding the plain text string to match the key size
		for(int i = 0; i < paddingLength; i++) {
			plainText.append("x");
		}//end i loop
		
		return plainText.toString();
	}//end create matrix
	
	public static void formatOutput(String output, String header) {
		System.out.println(header + ":");
		for(int i = 0; i < output.length(); i++) {
			if(i != 0 && i % 80 == 0) {
				System.out.print("\n" + output.charAt(i));
			}//end if
			else {
				System.out.print(output.charAt(i));
			}//end else
		}//end loop i
	}//end format output
	
	public static String encryptPlainText(String plainText, int[][] keyMatrix) {
	
		int length = plainText.length();
		int idx = 0;
		int n = keyMatrix[0].length;
		int[] encryptBlock = new int[n];
		int[] encryptBlockPost = new int[n];
		
		StringBuilder encryptedText = new StringBuilder("");
		
		while(length > 0) {
			
			for(int x = 0; x < n; x++) {
				encryptBlock[x] = plainText.charAt(x + idx) - 'a';
				encryptBlockPost[x] = 0;
			}//end x loop
			
			for(int i = 0; i < n; i++) {
				for(int j= 0; j < n; j++) {
					encryptBlockPost[i] += (keyMatrix[i][j]*encryptBlock[j]);
				}//end j loop
				encryptBlockPost[i] %= 26;
			}//end i loop
			
			for(int x = 0; x < n; x++) {
				encryptedText.append((char)(encryptBlockPost[x]+'a'));
			}//end x loop
			
			length -= n;
			idx += n;
		}//end while
		
		return encryptedText.toString();
	}//end encrypt method
}//end class



/*=============================================================================
| I Giorgio Torregrosa (gi795211) affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+=============================================================================*/

