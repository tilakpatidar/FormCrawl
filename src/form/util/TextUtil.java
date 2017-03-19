package form.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class TextUtil {

  public static ArrayList<String> splitCamelCaseString(String s) {
    ArrayList<String> result = new ArrayList<>();
    for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
      result.add(w);
    }
    return result;
  }

  public static String concatList(List<String> li, String delim) {
    return li.stream().reduce((a, b) -> a + delim + b).orElse("");
  }

  public static String filterText(String text){
    return removeMultipleSpaces(text.replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " "));
  }

  public static String removePunctuations(String text){
    return text.replaceAll("[^a-zA-Z\\s]", "");
  }

  public static String removeMultipleSpaces(String text){
    return text.replaceAll("\\s+", " ").trim();
  }

  public static String filterLabelText(String text) {
    text = StringUtils.stripAccents(text);
    text = filterText(text).replaceAll("\\*|\\:", " ").toLowerCase();
    return text.trim();
  }
}
