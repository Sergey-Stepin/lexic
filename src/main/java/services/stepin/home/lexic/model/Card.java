package services.stepin.home.lexic.model;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.compress.utils.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static services.stepin.home.lexic.model.LanguageCode.DE;

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
    private RepetitionFrequency repetitionFrequency;

    //@ElementCollection(fetch = FetchType.EAGER)
    //private Set<String> localWords;

    private String localWord;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> foreignWords;

//    @ElementCollection(fetch = FetchType.LAZY)
//    private List<Phrase> phraseList;

}
