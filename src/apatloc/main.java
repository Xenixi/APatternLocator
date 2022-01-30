/*------------------------*
*  A Pattern Locator      *
*  Kobe McManus (@Xenixi) *
* ------------------------*/
package apatloc;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class main {
	public static void main(String[] args) {
		try {
			long version = 100l;

			System.out.println("A Pattern Locator | Version: " + (double) version / 100);

			// add file stuff here

			File f = new File("S:/DATASETS/kaggleavocadoshort.csv");

			// line mode

			Scanner reader = new Scanner(f);

			List<String[]> fileLines = new ArrayList<>();

			while (reader.hasNextLine()) {
				fileLines.add(reader.nextLine().split(","));
			}
			reader.close();

			// get longest data section
			int length = 0;

			for (String[] dataSec : fileLines) {
				if (dataSec.length > length)
					length = dataSec.length;
			}

			Map<String, List<Integer[]>> foundItemsGlobal = new HashMap<>();
			// probably issues with the smaller strings eventually - will check ****
			for (int i = 1; i < length; i++) {
				Map<String, List<Integer[]>> items = new HashMap<>();
				int lineNum = 0;
				for (String[] dataSec : fileLines) {
					int secNum = 0;
					for (String string : dataSec) {
						for (int j = 0; (j + i) <= string.length(); j++) {
							String grab = string.substring(j, j + i);

							/*
							 * System.out.println("Excerpt Length: " + i + " Start Pos: " + j + " End Pos: "
							 * + j + i + "||| '" + grab + "'");
							 */

							boolean alreadyHasKey = false;
							int index = 0;
							for (Object o : items.keySet()) {
								// case sensitivity should be added as an option later!

								if (((String) o).equalsIgnoreCase(grab)) {
									alreadyHasKey = true;
									for (Entry<String, List<Integer[]>> e : items.entrySet()) {
										if (((String) e.getKey()).equalsIgnoreCase(grab)) {
											items.get(e.getKey()).add(new Integer[] { lineNum, secNum, j, j + i });
										}
									}

									System.err.println("Existing: " + grab);
								}

								index++;
							}
							if (!alreadyHasKey) {
								System.err.println("New Entry: " + grab);
								List<Integer[]> newList = new ArrayList<>();
								newList.add(new Integer[] { lineNum, secNum, j, j + i });
								items.put(grab, newList);

							}
						}
						secNum++;
					}
					lineNum++;
				}

				// sift through 'items'
				for (Entry<String, List<Integer[]>> e : items.entrySet()) {
					if (((List<Integer[]>) e.getValue()).size() > 1) {
						foundItemsGlobal.put(((String) e.getKey()), ((List<Integer[]>) e.getValue()));
					}
				}

			}
			for (Entry<String, List<Integer[]>> e : foundItemsGlobal.entrySet()) {
				System.out.println("*****");
				System.out.println("Occurences " + ((List<Integer[]>) e.getValue()).size() + " // '"
						+ ((String) e.getKey()) + "'");
				
				StringBuilder sb = new StringBuilder();
				List<Integer[]> details = ((List<Integer[]>)e.getValue());
				for(Integer[] detail : details) {
					sb.append("::");
					sb.append("\nLine " + detail[0]);
					sb.append("\nSection" + detail[1]);
					sb.append("\nStarts At " + detail[2]);
					sb.append("\nEnds At " + detail[3]);
					
				}
				System.out.println("Found at: ");
				System.out.println("*****\n");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
