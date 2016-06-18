package core.utilities;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Avoperties extends Properties {
	private static final long serialVersionUID = 1L;

	public Avoperties(String fileName) {
		String propFileName = fileName;
		try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
			if (inputStream != null) {
				load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
