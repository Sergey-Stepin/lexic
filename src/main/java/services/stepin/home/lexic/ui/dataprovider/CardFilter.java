package services.stepin.home.lexic.ui.dataprovider;

import lombok.Data;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;

import java.util.Set;

@Data
public class CardFilter {
    private Set<Card> checkAgainCards;
    private boolean checkAgain;
    private LanguageCode languageCode;
    private RepetitionFrequency repetitionFrequency;
    private String localWordContains;
    private String foreignWordContains;

}
