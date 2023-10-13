package services.stepin.home.lexic.ui.person;

import lombok.Getter;
import services.stepin.home.lexic.model.Phrase;

import java.util.ArrayList;
import java.util.List;

import static services.stepin.home.lexic.model.LanguageCode.DE;

public class DataService {

    @Getter
    private final static List<Person> people;
    static {
        people = new ArrayList<>();
        people.add(new Person("Alex"));
        people.add(new Person("Ann"));
        people.add(new Person("James"));
    }

    @Getter
    private final static List<Phrase> phrases;
    static {
        phrases = new ArrayList<>();
        phrases.add(new Phrase(DE, "Привет", "Hello"));
        phrases.add(new Phrase(DE, "До свидания", "Aufweidersehen"));
        phrases.add(new Phrase(DE, "Добрый день", "Guten tag"));
    }
}
