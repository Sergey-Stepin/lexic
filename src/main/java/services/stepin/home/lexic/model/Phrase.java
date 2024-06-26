package services.stepin.home.lexic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

import static services.stepin.home.lexic.model.LanguageCode.DE;

@Embeddable
@Data
@NoArgsConstructor
public class Phrase {

    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode = DE;

    private String localPhrase;

    private String foreignPhrase;

    @Transient
    @JsonIgnore
    private String phraseExam;

    public Phrase(LanguageCode languageCode, String localPhrase, String foreignPhrase) {
        this.languageCode = languageCode;
        this.localPhrase = localPhrase;
        this.foreignPhrase = foreignPhrase;
    }
}
