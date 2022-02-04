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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		final long version = 100l;
		
		Scanner input = new Scanner(System.in);
		try {
			//config
			ConfigurationManager.initiate();
			ConfigurationManager.put("version", Long.toString(version));
			ConfigurationManager.put("dogs", "cow");
			ConfigurationManager.put("zzzz", "yes");
			ConfigurationManager.put("aaa", "hello");
			
			while (true) {

				

				System.out.println("A Pattern Locator | Version: " + (double) version / 100);

				File f = new File("NONEXISTANT");

				while (true) {

					System.out.println("Enter a file/directory(not currently supported) path to get started:\n");
					String path = input.nextLine();
					f = new File(path);
					if (f.exists()) {
						System.out.println("Reading...");
						break;
					}

					System.out.println("Invalid entry - please try again.");

				}

				// check if file or directory
				if (f.exists() && f.isDirectory()) {

					File[] dirFiles = f.listFiles();
					if (dirFiles.length > 0) {
						System.out.println("Performing bulk operation on all valid files.");
						Thread.sleep(2000);
					} else {
						System.out.println("No valid files found in this directory.");
					}

					for (File file : dirFiles) {
						if (file.getName().endsWith(".csv") || file.getName().endsWith(".txt")
								|| file.getName().endsWith(".log")) {
							mainStage(file);
						}
					}

					System.out.println("Completed operations for all found valid files.");
					continue;
				}

				mainStage(f);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void mainStage(File f) throws FileNotFoundException, IOException {
		// line mode
		long beginTime = System.currentTimeMillis();
		Scanner reader = new Scanner(f);

		Map<String, List<Integer[]>> foundItemsGlobal = PatternFinder.execute(reader);

		// print out
		File output = new File(
				f.getParentFile().getAbsolutePath() + "/op_" + f.getName() + "_" + System.currentTimeMillis() + ".log");
		if (!output.exists()) {
			output.createNewFile();
		}
		FileWriter fw = new FileWriter(output);

		fw.append("APatternLocator Log:\n");

		for (Entry<String, List<Integer[]>> e : foundItemsGlobal.entrySet()) {
			// System.out.println("~~~~~\n");
			System.out.println(
					"Occurences " + ((List<Integer[]>) e.getValue()).size() + " // '" + ((String) e.getKey()) + "'");

			fw.append("Occurences " + ((List<Integer[]>) e.getValue()).size() + " // '" + ((String) e.getKey()) + "'"
					+ "\n");

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
}
