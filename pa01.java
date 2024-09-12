
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class pa01 {
	//begin main
		public static void main(String[] args) {
			
			//creating the key matrix and printing it to the console 
			int[][] keyMatrix = createKeyMatrix(args[0]);
			
			//creating the plain text and printing it to the console
			String plainText = createPlainText(args[1], keyMatrix[0].length);
			String encryptedText = encryptPlainText(plainText, keyMatrix);
			
			//echo to console with proper format
			formatOutput(plainText, "Plaintext");
			
			System.out.println("\n");
			
			formatOutput(encryptedText, "Ciphertext");
			
			System.out.println();
		}//end main

		//create matrix method
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
			        System.out.print("   " + keyMatrix[i][j]);
			    }//end j loop
				System.out.println("\n");
			}//end i loop
			
			return keyMatrix;
		}//end create matrix
		
		public static String createPlainText(String fileName, int n) {
			
			StringBuilder plainText = null;
			
			try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				
				plainText = new StringBuilder();
				String input;
				while((input = br.readLine()) != null) {
					plainText.append(input);
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
