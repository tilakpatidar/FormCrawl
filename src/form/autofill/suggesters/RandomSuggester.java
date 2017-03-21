package form.autofill.suggesters;

import form.Form;
import form.Input;
import form.autofill.data.Record;
import form.inputs.Group;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.*;

import static form.util.TextUtil.*;

public class RandomSuggester extends Suggester {
  private final HashMap<Input, List<String>> suggestionTokens = new HashMap<>();

  @Override
  public Record getSuggestedRecord(Form form) {
    List<Input> unboundedFields = form.getUnboundedFields();
    if (suggestionTokens.isEmpty()) {
      //init with unbounded elements
      unboundedFields.forEach(field -> suggestionTokens.put(field, new ArrayList<>()));
    }

    Record record = new Record();
    Element resultsDiv = form.getPreviousResults();
    if (resultsDiv != null) {
      populateTokensForUnboundedFields(unboundedFields, resultsDiv);
      System.out.println("Suggestions added for unbounded fields");
    }
    unboundedFields.forEach(field -> {
      try {
        record.put(field, suggestionTokens.get(field).remove(0));
      } catch (IndexOutOfBoundsException e) {
        record.put(field, "");
      }
    });

    //filling groupable and bounded elements
    for (Group group : form.getInputGroups()) {
      if (randomTruth()) {
        ArrayList<Input> inputs = group.getElements();
        boolean filled = false;
        for (Input input : inputs) {
          if (randomTruth() && !filled) {
            filled = true;
            record.put(input, "");
          }
        }
      }
    }

    //filling non groupAble but bounded elements
    //basically select tags
    for (Input input : form.getNonGroupableBoundedInputs()) {
      By option = By.tagName("option");
      List<WebElement> optionTags = input.getWebElement().findElements(option);
      Random randomize = new Random();
      int randomIndex = randomize.nextInt(optionTags.size());
      WebElement randomOption = optionTags.get(randomIndex);
      record.put(input, randomOption.getText());
    }
    System.out.println(record.toString());
    return record;
  }
  private void populateTokensForUnboundedFields(List<Input> unboundedFields, Element resultsDiv) {
    Input titleInput = unboundedFields.get(0);
    Set<String> tokenGrams = new HashSet<>();
    String title = filterText(removePunctuations(resultsDiv.text()));
    title = title.toLowerCase();
    System.out.printf("Title: %s%n", title);
    tokenGrams.addAll(ngrams(2, title));
    List<String> trigrams = ngrams(3, title);
    tokenGrams.addAll(trigrams);
    tokenGrams.add(title);
    this.suggestionTokens.get(titleInput).addAll(tokenGrams);
    System.out.printf("BiGrams generated : %d%n", tokenGrams.size());
  }

  private boolean randomTruth() {
    int randomNumber = (int) (Math.random() * 100);
    return randomNumber < 20 || randomNumber > 80;
  }
}
