package form.autofill.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

/**
 * Collection of Records
 */
public class Data {

  private static String[] tags = {"div", "td"}; // tags which are possible candidates of being data rows

  public static Record[] extractRecords(String html) {
    Document doc = Jsoup.parse(html);

    //remove unwanted tags
    doc.select("script, style, head, .hidden, noscript, img, iframe, header, footer, br, code, nav").remove();

    String final_key = "";
    int max_c = 0;

    for (String tag : tags) {
      Elements divs = doc.getElementsByTag(tag);
      HashMap<String, Integer> classCount = new HashMap<String, Integer>();

      String maxKey = "";
      int max_count = 0;

      for (Element div : divs) {

        String className = div.attr("class");
        Integer count = classCount.get(className);
        if (count == null) {
          //first time class occurred
          classCount.put(className, 1);
          count = 1;
        } else {
          classCount.put(className, ++count);
        }

        if (count > max_count) {
          maxKey = className;
          max_count = count;
        }
      }

      if (max_count > max_c) {
        final_key = maxKey;
      }
    }

    Elements records = doc.getElementsByClass(final_key);

    Record[] recs = new Record[records.size()];

    int i = 0;
    for (Element record : records) {
      recs[i] = new Record();
      ++i;
    }

    return recs;
  }
}
