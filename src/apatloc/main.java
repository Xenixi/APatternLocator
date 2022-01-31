/*------------------------*
*  A Pattern Locator      *
*  Kobe McManus (@Xenixi) *
* ------------------------*/
package apatloc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		try {
			while (true) {

				long version = 100l;

				System.out.println("A Pattern Locator | Version: " + (double) version / 100);
				
				
				File f = new File("NONEXISTANT");
				
				while(true) {
				
				System.out.println("Enter a file/directory(not currently supported) path to get started:\n");
				String path = input.nextLine();
				f = new File(path);
				if(f.exists()) {
					System.out.println("Reading...");
					break;
				} 
				
				System.out.println("Invalid entry - please try again.");
				
				
				
				}
			
				//check if file or directory
				if(f.exists() && f.isDirectory()) {
					
					
					
					System.out.println("This functionality is currently not supported, but it will be implemented soon!");
					System.out.println("Exiting to prevent issues");
					System.exit(0);
				}
				

				// line mode
				long beginTime = System.currentTimeMillis();
				Scanner reader = new Scanner(f);

				Map<String, List<Integer[]>> foundItemsGlobal = PatternFinder.execute(reader);
				
				
				// print out
				File output = new File("output_" + System.currentTimeMillis() + ".log");
				if (!output.exists()) {
					output.createNewFile();
				}
				FileWriter fw = new FileWriter(output);

				fw.append("APatternLocator Log:\n");

				for (Entry<String, List<Integer[]>> e : foundItemsGlobal.entrySet()) {
					// System.out.println("~~~~~\n");
					System.out.println("Occurences " + ((List<Integer[]>) e.getValue()).size() + " // '"
							+ ((String) e.getKey()) + "'");

					fw.append("Occurences " + ((List<Integer[]>) e.getValue()).size() + " // '" + ((String) e.getKey())
							+ "'" + "\n");

					StringBuilder sb = new StringBuilder();
					List<Integer[]> details = ((List<Integer[]>) e.getValue());
					for (Integer[] detail : details) {
						// add 1 to all of them to convert from starting at index 0 to starting at char
						// 1

						sb.append("::");
						sb.append("\nLine " + detail[0] + 1);
						sb.append("\nSection " + detail[1] + 1);
						sb.append("\nStarts At " + detail[2] + 1);
						sb.append("\nEnds At " + detail[3] + 1);
						sb.append("\n");

					}
					System.out.println("Found at:\n" + sb.toString());

					System.out.println("=====\n\n");

					fw.append("Found at:\n" + sb.toString() + "\n");
					fw.append("=====\n\n" + "\n");
				}
				long timeElapsed = System.currentTimeMillis() - beginTime;
				System.out.println("*****Operation Completed. Time Elapsed: " + timeElapsed + "ms\n");
				fw.append("*****Operation Completed. Time Elapsed: " + timeElapsed + "ms");
				fw.flush();
				fw.close();
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
