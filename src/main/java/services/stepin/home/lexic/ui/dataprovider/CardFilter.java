package services.stepin.home.lexic.ui.dataprovider;

import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;

public record CardFilter(
        LanguageCode languageCode,
        RepetitionFrequency repetitionFrequency,
        String localWordContains,
        String foreignWordContains) {
}
