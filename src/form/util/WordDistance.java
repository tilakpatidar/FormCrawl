package form.util;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.PointerType;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.relationship.Relationship;
import net.sf.extjwnl.data.relationship.RelationshipList;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.FileNotFoundException;
import java.util.HashMap;

import static net.sf.extjwnl.data.PointerType.*;
import static net.sf.extjwnl.data.relationship.RelationshipFinder.findRelationships;
import static net.sf.extjwnl.dictionary.Dictionary.getDefaultResourceInstance;

public final class WordDistance {

  private static Dictionary dictionary;

  public static int compute(String firstWord, String secondWord) throws
      JWNLException, CloneNotSupportedException {
    int min = 99999999;
    PointerType minPointer = null;
    POS noun = POS.values()[0];
    IndexWord start = dictionary.lookupAllIndexWords(firstWord).getIndexWord(noun);
    IndexWord end = dictionary.lookupAllIndexWords(secondWord).getIndexWord(noun);
    for (Synset firstSet : start.getSenses()) {
      for (Synset secondSet : end.getSenses()) {
        for (PointerType pointerType : score.keySet()) {
          RelationshipList list = findRelationships(firstSet, secondSet, pointerType);
          for (Object aList : list) {
            ((Relationship) aList).getNodeList().print();
          }
          try {
            int depth = list.get(0).getDepth() * score.get(pointerType);
            if (depth < min) {
              min = depth;
              minPointer = pointerType;
            }
          } catch (IndexOutOfBoundsException ignored) {
          }
        }
      }
    }
    System.out.println(minPointer);
    return min;
  }

  public static final HashMap<PointerType, Integer> score = new HashMap<>();

  static {
    score.put(SIMILAR_TO, 1);
    score.put(HYPERNYM, 2);
    score.put(INSTANCE_HYPERNYM, 2);
    score.put(HYPONYM, 2);
    score.put(INSTANCES_HYPONYM, 2);
    score.put(ENTAILMENT, 3);
    score.put(ATTRIBUTE, 3);
    score.put(DOMAIN_ALL, 4);
    score.put(MEMBER_ALL, 4);
    score.put(CATEGORY, 4);
    score.put(CATEGORY_MEMBER, 4);
    score.put(USAGE_MEMBER, 4);
    score.put(REGION_MEMBER, 4);

    try {
      dictionary = getDefaultResourceInstance();
    } catch (JWNLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws FileNotFoundException, JWNLException, CloneNotSupportedException {
    int resultScore = compute("left", "right");
    System.out.println(resultScore);
  }
}