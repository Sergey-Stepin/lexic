package services.stepin.home.lexic.model;

import jakarta.persistence.*;
import lombok.Data;
import services.stepin.home.lexic.ui.card.strategy.speech.PartOfSpeechType;

import java.util.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static services.stepin.home.lexic.model.LanguageCode.DE;
import static services.stepin.home.lexic.model.RepetitionFrequency.DAILY;

@Entity
@Data
public class Card {

    public enum Gender{MASCULINE, FEMININE, NEUTER}

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long cardId;

    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode = DE;

    @Enumerated(EnumType.STRING)
    private RepetitionFrequency repetitionFrequency = DAILY;

    @Enumerated(EnumType.STRING)
    private PartOfSpeechType partOfSpeech;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String localWord;

    private String foreignWord;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> foreignWords;

    @ElementCollection(fetch = FetchType.EAGER)

    private List<Phrase> phraseList = new ArrayList<>();

    @Transient
    private String localFirstExample;

    @Transient
    private String localSecondExample;

    @Transient
    private String localThirdExample;

    @Transient
    private String foreignFirstExample;

    @Transient
    private String foreignSecondExample;

    @Transient
    private String foreignThirdExample;

}
