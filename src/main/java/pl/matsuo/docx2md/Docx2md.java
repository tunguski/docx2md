package pl.matsuo.docx2md;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


/**
 * Created by marek on 08.07.15.
 */
public class Docx2md {


  public static void main(String[] args) throws Exception {
    File source = new File(args[0]);

    if (!source.exists()) {
      throw new RuntimeException("File not found");
    }

    CommandLine cli = new DefaultParser().parse(options(), args);

    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
    if (cli.hasOption("o")) {
      File outFile = new File(cli.getOptionValue("o"));

      outFile.getParentFile().mkdirs();
      outFile.createNewFile();

      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
    }

    try {
      new Docx2md().process(source, out);
    } finally {
      out.flush();
      out.close();
    }
  }

  public void process(File source, BufferedWriter out) throws Exception {
    //Blank Document
    XWPFDocument document = new XWPFDocument(new FileInputStream(source));

    document.getBodyElements().forEach(element -> {
      try {
        if (element instanceof XWPFParagraph) {
          XWPFParagraph paragraph = (XWPFParagraph) element;

          boolean bold = false;
          boolean italic = false;
          String text = "";

          for (XWPFRun run : paragraph.getRuns()) {
            if (run.text().trim().isEmpty()) {
              continue;
            }

            if (bold != run.isBold()) {
              text += "**";
              bold = run.isBold();
            }

            if (italic != run.isItalic()) {
              text += "*";
              italic = run.isItalic();
            }

            text += run.text();
          }

          if (bold) {
            text += "**";
          }
          if (italic) {
            text += "*";
          }

          if (text.isEmpty()) {
            return;
          }

          // Heading1   - nagłówek 1-go rzędu
          // Heading2   - nagłówek 2-go rzędu
          // null       - zwykły tekst
          // TableTitle - nagłówek tabeli
          // TableText  - tekst w tabeli
          // ListBullet2 - lista wypunktowana
          // ListBullet3 - lista wypunktowana
          if (paragraph.getStyle() == null) {
            out.write(text);
            out.newLine();
            out.newLine();
          } else if (paragraph.getStyle().equals("Heading1")) {
            out.write("# " + text);
            out.newLine();
            out.newLine();
          } else if (paragraph.getStyle().equals("Heading2")) {
            out.write("## " + text);
            out.newLine();
            out.newLine();
          } else if (paragraph.getStyle().equals("ListBullet1")) {
            out.write("* " + text);
            out.newLine();
          } else if (paragraph.getStyle().equals("ListBullet2")) {
            out.write("* " + text);
            out.newLine();
          } else if (paragraph.getStyle().equals("ListBullet3")) {
            out.write("    * " + text);
            out.newLine();
//          } else if (paragraph.getStyle().equals("TableTitle")) {
//          } else if (paragraph.getStyle().equals("TableText")) {
          } else {
            out.write(paragraph.getText());
            out.newLine();
            out.write("========== paragraph " + paragraph.getStyle());
            out.newLine();
          }

        } else {
          out.write(element.toString());
          out.newLine();
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }


  protected static Options options() {
    Options options = new Options();

    options.addOption("o", "output-file", true, "output file path");

    return options;
  }
}

