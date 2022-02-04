/*------------------------*
*  A Pattern Locator      *
*  Kobe McManus (@Xenixi) *
* ------------------------*/
package apatloc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.ini4j.Ini;
import org.ini4j.Wini;
import org.ini4j.Profile.Section;

public class Main {

	static final long version = 100l;
	static final File configFile = new File("settings.ini");
	static Ini config;

	public static void main(String[] args) throws InterruptedException {

		Scanner input = new Scanner(System.in);

		try {
			// configuration

			if (!configFile.exists() || configFile.isDirectory()) {
				configFile.createNewFile();
			}

			config = new Ini(configFile);

			config.put("System", "Version", version);
			if (config.get("System", "TimesRun") == null) {
				config.put("System", "TimesRun", 1);
			} else {
				config.put("System", "TimesRun", config.get("System", "TimesRun", Integer.class) + 1);
			}

			if(config.get("Run.Settings") == null) 
				config.add("Run.Settings");
			
			if(config.get("CSV.Settings") == null) 
				config.add("CSV.Settings");
			
			if(config.get("RunConstraints.Settings") == null) 	
				config.add("RunConstraints.Settings");
			
			if(config.get("MultiFileComparison.Settings") == null) 
				config.add("MultiFileComparison.Settings");
			
			if(config.get("FileLabels.Settings") == null) 
				config.add("FileLabels.Settings");

			// Run Settings
			Ini.Section runSettings = config.get("Run.Settings");
			runSettings.putIfAbsent("Mode.CSV", "True");
			runSettings.putIfAbsent("Mode.Constraints", "False");
			runSettings.putIfAbsent("Mode.MultiFileComparison", "False");
			runSettings.putIfAbsent("Mode.LabelFiles", "False");

			config.put("Run.Settings", runSettings);

			// CSV Settings
			Ini.Section modeCSV = config.get("CSV.Settings");
			modeCSV.putIfAbsent("BypassFirstLine", "True");
			modeCSV.putIfAbsent("LinkResultsWithHeaderCategories", "True");
			

			config.put("CSV.Settings", modeCSV);

			// RunConstraint Settings
			Ini.Section modeConstraints = config.get("RunConstraints.Settings");
			modeConstraints.putIfAbsent("EnableLineConstraints", "True");
			modeConstraints.putIfAbsent("EnableCategoryConstraints", "True");

			modeConstraints.putIfAbsent("Line.Start", "1");
			modeConstraints.putIfAbsent("Line.End", "100");
			modeConstraints.putIfAbsent("Line.SpecifiedOnly", "False");
			modeConstraints.putIfAbsent("Line.SpecificLines", "1,5,17,29,107");
			modeConstraints.putIfAbsent("Category.Start", "1");
			modeConstraints.putIfAbsent("Category.End", "4");
			modeConstraints.putIfAbsent("Category.SpecifiedOnly", "False");
			modeConstraints.putIfAbsent("Category.SpecificCategories", "1,5,6");

			config.put("RunConstraints.Settings", modeConstraints);

			// MultiFileComparison Settings
			Ini.Section modeMultiFile = config.get("MultiFileComparison.Settings");
			modeMultiFile.putIfAbsent("MultiFile.ValidTypes", "CSV,TXT,LOG");
			modeMultiFile.putIfAbsent("MultiFile.InnerFolders", "False");

			config.put("MultiFileComparison.Settings", modeMultiFile);

			// FileLabels Settings
			Ini.Section modeLabelFiles = config.get("FileLabels.Settings");

			modeLabelFiles.putIfAbsent("Dataset.Title", "Default Title");
			modeLabelFiles.putIfAbsent("Dataset.Author", "Default Author");
			modeLabelFiles.putIfAbsent("Dataset.ProjectName", "Default Project Name");
			modeLabelFiles.putIfAbsent("Dataset.Number", "1");

			config.put("FileLabels.Settings", modeLabelFiles);

			config.store();

			// main loop
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
