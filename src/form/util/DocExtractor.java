package form.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class DocExtractor {
  public static Elements extractDocs(Element soup) {
    return findListOfSimilarChilds(soup);
  }
  private static Elements findListOfSimilarChilds(Element parent){
    Elements children = parent.children();
    for(Element firstChild: children){
      boolean different = false;
      String previousHead = extractHead(firstChild.toString());
      for(Element child : children){
        String head = extractHead(child.toString());
        if(!head.equalsIgnoreCase(previousHead)){
          different = true;
          break;
        }
      }
      if(different){
        for(Element child : children) {
          try{
            return findListOfSimilarChilds(child);
          }catch (IndexOutOfBoundsException e){

          }
        }
      }else if(children.size() > 1){
        return children;
      }else{
        return findListOfSimilarChilds(firstChild);
      }
    }
    throw new IndexOutOfBoundsException();
  }

  private static String extractHead(String html){
    return html.split(">")[0].replaceAll("<|>","");
  }

  public static void main(String[] args){
    String html = DomCompare.readFile("/Users/ptilak/test/res.html");
    Document document = Jsoup.parse(html);
    DocExtractor.findListOfSimilarChilds(document.body()).forEach(e -> System.out.println("#######\n" + e.toString() + "\n#######"));
  }
}
