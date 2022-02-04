package apatloc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class ConfigurationManager {
	private static final Map<String, String> configuration = new HashMap<>();
	static final File config = new File("settings.ini");

	public static void initiate() throws IOException {

		if (!config.exists() || config.isDirectory()) {
			config.createNewFile();
		}
		
		reload();

	}

	public static void reload() throws FileNotFoundException {
		List<String> configLines = new ArrayList<>();

		Scanner configReader = new Scanner(config);
		while (configReader.hasNextLine()) {
			configLines.add(configReader.nextLine());
		}

		configReader.close();

		for (String line : configLines) {

			String[] parts = line.split("=");

			String variable, value;

			if (parts.length < 2) {
				continue;
			}

			variable = parts[0];

			if (parts.length > 2) {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < parts.length; i++) {
					sb.append(parts[i]);
				}
				value = sb.toString();
			} else {
				value = parts[1];
			}
			
			//remove any whitespace from value 
			value = value.replace(" ", "");
			
			configuration.clear();
			configuration.put(variable, value);

		}
	}

	public static Map<String, String> getConfigurationMap() {
		return configuration;
	}
	public static String get(String var) {
		return configuration.get(var);
	}
	public static boolean put(String var, String value) {
		configuration.put(var, value.replace(" ", " ").replace("=", ""));
		return writeToFile();
		
	}
	private static boolean writeToFile() {
		try {
			PrintWriter pw = new PrintWriter(config);
			
			for(Entry e : configuration.entrySet()) {
				pw.append(((String)e.getKey()) + "=" + ((String)e.getValue()) + "\n");
			}
			pw.flush();
			pw.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
