package pl.matsuo.docx2md;

import org.junit.Test;
import org.mockito.internal.util.io.IOUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by marek on 08.07.15.
 */
public class TestDocx2md {


  @Test
  public void testDocx2md() throws Exception {
    new Docx2md().process(new File("src/test/resources/test_file.docx"),
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("target/testDock2md.md")))));

    Collection<String> expected = IOUtil.readLines(getClass().getResourceAsStream("/test_file.md"));
    Collection<String> result = IOUtil.readLines(new FileInputStream(new File("target/testDock2md.md")));

    assertEquals(expected, result);
  }


  @Test
  public void testDocx2mdMain() throws Exception {
    Docx2md.main(new String[] {
        "src/test/resources/test_file.docx"
    });
  }
}
