package services.stepin.home.lexic.model;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.compress.utils.Sets;

import java.util.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static services.stepin.home.lexic.model.LanguageCode.DE;
import static services.stepin.home.lexic.model.RepetitionFrequency.WEEKLY;

@Entity
@Data
public class Card {

    public static final List<LanguageCode> LANGUAGE_CODES = Arrays.stream(LanguageCode.values()).toList();
    public static final List<RepetitionFrequency> REPETITION_FREQUENCIES = Arrays.stream(RepetitionFrequency.values()).toList();

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long cardId;

    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode = DE;

    @Enumerated(EnumType.STRING)
    private RepetitionFrequency repetitionFrequency = WEEKLY;

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
