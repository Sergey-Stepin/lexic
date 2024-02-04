package services.stepin.home.lexic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty
    private String localWord;

    @NotEmpty
    private String foreignWord;

    private String foreignPlural;

    private String imperativeDu;

    private String indikativIhr;
    private String indikativErSieEs;

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

    @Transient
    @JsonIgnore
    private boolean again;

    @Transient
    @JsonIgnore
    private int hash = 0;

    @Transient
    @JsonIgnore
    private boolean isHashCalculated = false;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Card card = (Card) o;

        return cardId == card.cardId;
    }

    @Override
    public int hashCode() {

        if(isHashCalculated)
            return hash;

        hash = (int) (cardId ^ (cardId >>> 32));
        isHashCalculated = true;

        return hash;
    }
}
