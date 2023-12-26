package services.stepin.home.lexic.ui.card;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.Phrase;
import services.stepin.home.lexic.model.RepetitionFrequency;

import java.util.ArrayList;
import java.util.List;

import static services.stepin.home.lexic.model.LanguageCode.DE;
import static services.stepin.home.lexic.model.RepetitionFrequency.DAYLY;
import static services.stepin.home.lexic.ui.CardsList.FOREIGN_LANGUAGE;
import static services.stepin.home.lexic.ui.CardsList.LOCAL_LANGUAGE;
import static services.stepin.home.lexic.ui.card.CardFormEvent.*;

public class CardForm extends FormLayout {

    private static final String LOCAL_EXAMPLE_LABEL = "Example (" + LOCAL_LANGUAGE + "):";
    private static final String FOREIGN_EXAMPLE_LABEL = "Example (" + FOREIGN_LANGUAGE + "):";

    private final List<LanguageCode> languageCodes;
    private final List<RepetitionFrequency> repetitionFrequencies;
    private final Binder<Card> cardBinder = new BeanValidationBinder<>(Card.class);
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");
    private final ComboBox<LanguageCode> languageCode = new ComboBox<>("Language");
    private final ComboBox<RepetitionFrequency> repetitionFrequency = new ComboBox<>("Repeat");
    private final TextField localWord = new TextField("Local");
    private final TextField foreignWord = new TextField("Foreign");
    private final TextField localFirstExample = new TextField();
    private final TextField localSecondExample = new TextField();
    private final TextField localThirdExample = new TextField();
    private final TextField foreignFirstExample = new TextField();
    private final TextField foreignSecondExample = new TextField();
    private final TextField foreignThirdExample = new TextField();

    private Card card;

    public CardForm(List<LanguageCode> languageCodes, List<RepetitionFrequency> repetitionFrequencies) {

        this.languageCodes = languageCodes;
        this.repetitionFrequencies = repetitionFrequencies;

        addClassName("card-form-class");

        cardBinder.bindInstanceFields(this);

        languageCode.setItems(languageCodes);
        repetitionFrequency.setItems(repetitionFrequencies);

        languageCode.setValue(DE);
        repetitionFrequency.setValue(DAYLY);

        addLayout();
    }

    private void addLayout() {

        add(createToolbar());

        HorizontalLayout propertiesLayout = new HorizontalLayout(languageCode, repetitionFrequency);
        add(propertiesLayout);

        add(localWord);
        add(foreignWord);

        add(new Span(LOCAL_EXAMPLE_LABEL));
        add(new Span(FOREIGN_EXAMPLE_LABEL));

        add(localFirstExample);
        add(foreignFirstExample);

        add(localSecondExample);
        add(foreignSecondExample);

        add(localThirdExample);
        add(foreignThirdExample);

        setResponsiveSteps(new ResponsiveStep("0", 2));
    }

    private Component createToolbar() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new CardFormEvent.CardFormDeleteEvent(this, card)));
        cancel.addClickListener(event -> fireEvent(new CardFormEvent.CardFormCloseEvent(this)));

        HorizontalLayout buttonLayout = new HorizontalLayout(save, delete, cancel);
        add(buttonLayout);

        return buttonLayout;
    }

    public void setCard(Card card) {

        this.card = card;

        if(card != null)
            setExamples(card);

        cardBinder.readBean(card);
    }

    private void setExamples(Card card) {

        List<Phrase> phrases = card.getPhraseList();
        setFirstExample(phrases);
        setSecondExample(phrases);
        setThirdExample(phrases);
    }

    private void setFirstExample(List<Phrase> phrases){

        if(phrases.size() < 1)
            return;

        Phrase phrase = phrases.get(0);
        if(phrase == null)
            return;

        localFirstExample.setValue(phrase.getLocalPhrase());
        foreignFirstExample.setValue(phrase.getForeignPhrase());
    }

    private void setSecondExample(List<Phrase> phrases){

        if(phrases.size() < 2)
            return;

        Phrase phrase = phrases.get(1);
        if(phrase == null)
            return;

        localSecondExample.setValue(phrase.getLocalPhrase());
        foreignSecondExample.setValue(phrase.getForeignPhrase());
    }

    private void setThirdExample(List<Phrase> phrases){

        if(phrases.size() < 3)
            return;

        Phrase phrase = phrases.get(2);
        if(phrase == null)
            return;

        localThirdExample.setValue(phrase.getLocalPhrase());
        foreignThirdExample.setValue(phrase.getForeignPhrase());
    }

    private void validateAndSave() {
        try {

            setPhrases(card);

            cardBinder.writeBean(card);
            fireEvent(new CardFormEvent.CardFormSaveEvent(this, card));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPhrases(Card card){

        List<Phrase> phrases = new ArrayList<>();

        Phrase firstPhrase = createPhrase(localFirstExample.getValue(), foreignFirstExample.getValue());
        phrases.add(firstPhrase);

        Phrase secondPhrase = createPhrase(localSecondExample.getValue(), foreignSecondExample.getValue());
        phrases.add(secondPhrase);

        Phrase thirdPhrase = createPhrase(localThirdExample.getValue(), foreignThirdExample.getValue());
        phrases.add(thirdPhrase);

        card.setPhraseList(phrases);
    }

    private Phrase createPhrase(String local, String foreign){
        return new Phrase(languageCode.getValue(), local, foreign);
    }

    public Registration addDeleteListener(ComponentEventListener<CardFormDeleteEvent> listener) {
        return addListener(CardFormDeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<CardFormSaveEvent> listener) {
        return addListener(CardFormSaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CardFormCloseEvent> listener) {
        return addListener(CardFormCloseEvent.class, listener);
    }

}
