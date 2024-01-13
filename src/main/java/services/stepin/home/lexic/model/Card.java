package services.stepin.home.lexic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(nullable = false)
    private PartOfSpeechType partOfSpeech = PartOfSpeechType.OTHER;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String localWord;

    private String foreignWord;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> foreignWords;

    @ElementCollection(fetch = FetchType.EAGER)

    private List<Phrase> phraseList = new ArrayList<>();

    @Transient
    @JsonIgnore
    private String localFirstExample;

    @Transient
    @JsonIgnore
    private String localSecondExample;

    @Transient
    @JsonIgnore
    private String localThirdExample;

    @Transient
    @JsonIgnore
    private String foreignFirstExample;

    @Transient
    @JsonIgnore
    private String foreignSecondExample;

    @Transient
    @JsonIgnore
    private String foreignThirdExample;

}
