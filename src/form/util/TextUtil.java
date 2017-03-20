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

  public static String filterText(String text) {
    return removeMultipleSpaces(text.replaceAll("(\\n+)|(\\t+)|(\\s+)|(\\r+)", " "));
  }

  public static List<String> ngrams(int n, String str) {
    List<String> ngrams = new ArrayList<>();
    String[] words = str.split(" ");
    for (int i = 0; i < words.length - n + 1; i++)
      ngrams.add(concat(words, i, i + n));
    return ngrams;
  }

  public static String concat(String[] words, int start, int end) {
    StringBuilder sb = new StringBuilder();
    for (int i = start; i < end; i++)
      sb.append((i > start ? " " : "") + words[i]);
    return sb.toString();
  }

  public static String removePunctuations(String text) {
    return text.replaceAll("[^a-zA-Z\\s]", "");
  }

  public static String removeMultipleSpaces(String text) {
    return text.replaceAll("\\s+", " ").trim();
  }

  public static String filterLabelText(String text) {
    text = StringUtils.stripAccents(text);
    text = filterText(text).replaceAll("\\*|\\:", " ").toLowerCase();
    return text.trim();
  }
}
