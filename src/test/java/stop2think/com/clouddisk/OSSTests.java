package stop2think.com.clouddisk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author weihongyu
 */
@SpringBootTest
public class OSSTests {

    @Test
    void propertiesTest() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/application.properties"));
        for (String key : properties.stringPropertyNames()) {
            System.out.println(key + ": " + properties.getProperty(key));
        }
    }
}
