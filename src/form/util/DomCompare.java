package form.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.outerj.daisy.diff.helper.NekoHtmlParser;
import org.outerj.daisy.diff.html.HTMLDiffer;
import org.outerj.daisy.diff.html.HtmlSaxDiffOutput;
import org.outerj.daisy.diff.html.TextNodeComparator;
import org.outerj.daisy.diff.html.dom.DomTreeBuilder;
import org.xml.sax.InputSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashSet;
import java.util.Locale;

public final class DomCompare {
  private final InputSource oldSource;
  private static final String[] REMOVED_TAGS = {"head", "script", "style", "meta", "link", "title", "nav", "og", "#comment", "br"};
  public DomCompare(String basePage) {
    Document originalDOM = filterDOM(basePage);
    this.oldSource = new InputSource(new StringReader(originalDOM.html()));
  }
  public Element getResultsDoc(String resultsHTML) {
    try {
      Document resultDOM = filterDOM(resultsHTML);
      StringWriter finalResult = new StringWriter();
      SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
      TransformerHandler result = tf.newTransformerHandler();
      result.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      result.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
      result.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
      result.getTransformer().setOutputProperty(OutputKeys.ENCODING, "utf");
      result.setResult(new StreamResult(finalResult));

      Locale locale = Locale.getDefault();
      String prefix = "diff";

      NekoHtmlParser cleaner = new NekoHtmlParser();

      InputSource newSource = new InputSource(new StringReader(resultDOM.html()));

      DomTreeBuilder oldHandler = new DomTreeBuilder();
      cleaner.parse(oldSource, oldHandler);
      TextNodeComparator leftComparator = new TextNodeComparator(
          oldHandler, locale);

      DomTreeBuilder newHandler = new DomTreeBuilder();
      cleaner.parse(newSource, newHandler);
      TextNodeComparator rightComparator = new TextNodeComparator(
          newHandler, locale);

      HtmlSaxDiffOutput output = new HtmlSaxDiffOutput(result,
          prefix);

      HTMLDiffer differ = new HTMLDiffer(output);
      differ.diff(leftComparator, rightComparator);

      String diffedHTML = finalResult.toString();
      Element resultSet = getCommonParent(diffedHTML);
      Elements spans = resultSet.getElementsByClass("diff-html-added");
      spans.forEach(span -> {
        String text = span.text();
        span.replaceWith(new TextNode(text, span.baseUri()));
      });
      return resultSet;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  private static Element getCommonParent(String diffedHTML) {
    Document diffed = Jsoup.parse(diffedHTML);
    int majorDiff = getMajorDiffId(diffed);
    System.out.println("Major diff id: " + majorDiff);
    String changeIdValue = "added-diff-" + majorDiff;
    Elements elementsByClass = diffed.getElementsByAttributeValue("changeid", changeIdValue);
    elementsByClass.first().remove();
    elementsByClass.last().remove();
    elementsByClass.remove(0);
    elementsByClass.remove(elementsByClass.size() - 1);
    HashSet<Element> addedElements = new HashSet<>(elementsByClass);
    Element firstAddedElement = elementsByClass.get(0);
    Element parent = firstAddedElement.parent();
    while (true) {
      Elements children = parent.getElementsByAttributeValue("changeid", changeIdValue);
      HashSet<Element> childrenSet = new HashSet<>(children);
      if (childrenSet.containsAll(addedElements)) {
        return parent;
      }
      parent = parent.parent();
    }
  }
  private static int getMajorDiffId(Document diffed) {
    int count = 0;
    int id = 0;
    int index = 0;
    while (true) {
      String changeIdValue = "added-diff-" + index;
      Elements elementsByClass = diffed.getElementsByAttributeValue("changeid", changeIdValue);
      if (elementsByClass.isEmpty()) {
        break;
      }
      if (elementsByClass.size() > count) {
        count = elementsByClass.size();
        id = index;
      }
      index += 1;
    }
    return id;
  }

  private static Document filterDOM(String html) {
    Document doc = Jsoup.parse(html);
    for (String tag : REMOVED_TAGS) {
      doc.getElementsByTag(tag).forEach(e -> e.remove());
    }
    return doc;
  }

  private static String readFile(String path) {
    File file = new File(path);
    try {
      FileReader reader = new FileReader(file);
      BufferedReader buff = new BufferedReader(reader);
      String line;
      String data = "";
      while ((line = buff.readLine()) != null) {
        data = data + "\n" + line;
      }
      return data;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static void main(String[] args) {
    String orig = readFile("/Users/ptilak/test/orig1.html");
    String results = readFile("/Users/ptilak/test/results1.html");
    DomCompare compare = new DomCompare(orig);
    Element resultDoc = compare.getResultsDoc(results);
    System.out.println(resultDoc.html());
  }
}
