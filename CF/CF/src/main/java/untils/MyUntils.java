package untils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyUntils {

	public static String FileLocation(){
		try (InputStream input = MyUntils.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            return prop.getProperty("file.loca");

        } catch (IOException ex) {
            ex.printStackTrace();
            
        }
		return null;
	}
	
	public static String FileLocationTest(){
		try (InputStream input = MyUntils.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            return prop.getProperty("file.locatest");

        } catch (IOException ex) {
            ex.printStackTrace();
            
        }
		return null;
	}
	
	public static String FileLocation1M(){
		try (InputStream input = MyUntils.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            return prop.getProperty("file.loca1M");

        } catch (IOException ex) {
            ex.printStackTrace();
            
        }
		return null;
	}
	
	public static String OutputLocation(){
		try (InputStream input = MyUntils.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            return prop.getProperty("file.output");

        } catch (IOException ex) {
            ex.printStackTrace();
            
        }
		return null;
	}
}
