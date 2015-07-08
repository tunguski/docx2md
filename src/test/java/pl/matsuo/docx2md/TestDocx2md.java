package pl.matsuo.docx2md;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by marek on 08.07.15.
 */
public class TestDocx2md {


  @Test
  public void testDocx2md() throws Exception {
    new Docx2md().process(new File("src/test/resources/test_file.docx"),
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("target/testDock2md.md")))));
  }
}
