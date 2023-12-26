package services.stepin.home.lexic.ui.card;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.Phrase;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.ui.card.strategy.mode.CheckForeignMode;
import services.stepin.home.lexic.ui.card.strategy.mode.EditMode;
import services.stepin.home.lexic.ui.card.strategy.mode.Mode;
import services.stepin.home.lexic.ui.card.strategy.mode.ModeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.KeyModifier.CONTROL;
import static services.stepin.home.lexic.model.LanguageCode.DE;
import static services.stepin.home.lexic.model.RepetitionFrequency.DAYLY;
import static services.stepin.home.lexic.ui.CardsList.FOREIGN_LANGUAGE;
import static services.stepin.home.lexic.ui.CardsList.LOCAL_LANGUAGE;
import static services.stepin.home.lexic.ui.card.CardFormEvent.*;
import static services.stepin.home.lexic.ui.card.strategy.mode.ModeType.*;

public class CardForm extends FormLayout {

    private static final String LOCAL_EXAMPLE_LABEL = "Example (" + LOCAL_LANGUAGE + "):";
    private static final String FOREIGN_EXAMPLE_LABEL = "Example (" + FOREIGN_LANGUAGE + "):";
    private final Binder<Card> cardBinder = new BeanValidationBinder<>(Card.class);

    private final Map<ModeType, Mode> modes = new HashMap<>();

    @Getter
    private final List<LanguageCode> languageCodes;
    @Getter
    private final List<RepetitionFrequency> repetitionFrequencies;
    @Getter
    private final Button saveButton = new Button("Save");
    @Getter
    private final Button deleteButton = new Button("Delete");
    @Getter
    private final Button cancelButton = new Button("Cancel");
    @Getter
    private final RadioButtonGroup<ModeType> modesGroup = new RadioButtonGroup<>();
    @Getter
    private final ComboBox<LanguageCode> languageCode = new ComboBox<>("Language");
    @Getter
    private final ComboBox<RepetitionFrequency> repetitionFrequency = new ComboBox<>("Repeat");
    @Getter
    private final TextField localWord = new TextField("Local");
    @Getter
    private final TextField foreignWord = new TextField("Foreign");
    @Getter
    private final TextField checkWord = new TextField("Check");
    @Getter
    private final TextField localFirstExample = new TextField();
    @Getter
    private final TextField foreignFirstExample = new TextField();
    @Getter
    private final TextField checkFirstExample = new TextField();
    @Getter
    private final TextField localSecondExample = new TextField();
    @Getter
    private final TextField foreignSecondExample = new TextField();
    @Getter
    private final TextField checkSecondExample = new TextField();
    private final TextField localThirdExample = new TextField();
    @Getter
    private final TextField foreignThirdExample = new TextField();
    @Getter
    private final TextField checkThirdExample = new TextField();

    private Card card;

    public CardForm(
            List<LanguageCode> languageCodes,
            List<RepetitionFrequency> repetitionFrequencies) {

        this.languageCodes = languageCodes;
        this.repetitionFrequencies = repetitionFrequencies;

        addClassName("card-form-class");

        cardBinder.bindInstanceFields(this);

        languageCode.setItems(languageCodes);
        repetitionFrequency.setItems(repetitionFrequencies);

        languageCode.setValue(DE);
        repetitionFrequency.setValue(DAYLY);

        addLayout();

        modesGroup.setValue(EDIT);
    }

    private void addLayout() {

        add(createToolbar());

        HorizontalLayout propertiesLayout = new HorizontalLayout(languageCode, repetitionFrequency);
        add(propertiesLayout);

        add(localWord);
        add(foreignWord);
        add(checkWord);

        addExamples();;

        setResponsiveSteps(new ResponsiveStep("0", 2));
    }

    private void addExamples(){

        add(new Span(LOCAL_EXAMPLE_LABEL));
        add(new Span(FOREIGN_EXAMPLE_LABEL));

        add(localFirstExample);
        add(foreignFirstExample);
        add(checkFirstExample);

        add(localSecondExample);
        add(foreignSecondExample);
        add(checkSecondExample);

        add(localThirdExample);
        add(foreignThirdExample);
        add(checkThirdExample);

        setAutocomplete();
        setExampleToolTips();
    }

    private void setAutocomplete() {
        checkWord.getElement().setAttribute("autocomplete", "off");
        checkFirstExample.getElement().setAttribute("autocomplete", "off");
        checkSecondExample.getElement().setAttribute("autocomplete", "off");
        checkThirdExample.getElement().setAttribute("autocomplete", "off");
    }

    private void setExampleToolTips(){
        setCheckWordTooltip();
        setFirstExampleTooltip();
        setSecondExampleTooltip();
        setThirdExampleTooltip();
    }

    private void setCheckWordTooltip() {
        checkWord.getTooltip().setManual(true);
        checkWord.addKeyDownListener(
                Key.SPACE,
                event -> checkWord.getTooltip().setOpened(true),
                CONTROL);
        checkWord.addKeyUpListener(
                Key.SPACE,
                event -> checkWord.getTooltip().setOpened(false),
                CONTROL);
    }

    private void setFirstExampleTooltip(){
        checkFirstExample.getTooltip().setManual(true);
        checkFirstExample.addKeyDownListener(
                Key.SPACE,
                event -> checkFirstExample.getTooltip().setOpened(true),
                CONTROL);
        checkFirstExample.addKeyUpListener(
                Key.SPACE,
                event -> checkFirstExample.getTooltip().setOpened(false),
                CONTROL);
    }

    private void setSecondExampleTooltip(){
        checkSecondExample.getTooltip().setManual(true);
        checkSecondExample.addKeyDownListener(
                Key.SPACE,
                event -> checkSecondExample.getTooltip().setOpened(true),
                CONTROL);
        checkSecondExample.addKeyUpListener(
                Key.SPACE,
                event -> checkSecondExample.getTooltip().setOpened(false),
                CONTROL);
    }

    private void setThirdExampleTooltip(){
        checkThirdExample.getTooltip().setManual(true);
        checkThirdExample.addKeyDownListener(
                Key.SPACE,
                event -> checkThirdExample.getTooltip().setOpened(true),
                CONTROL);
        checkThirdExample.addKeyUpListener(
                Key.SPACE,
                event -> checkThirdExample.getTooltip().setOpened(false),
                CONTROL);
    }

    private Component createToolbar() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new CardFormEvent.CardFormDeleteEvent(this, card)));
        cancelButton.addClickListener(event -> fireEvent(new CardFormEvent.CardFormCloseEvent(this)));

        Component modesGroup = createModesGroup();

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, deleteButton, cancelButton, modesGroup);
        add(buttonLayout);

        return buttonLayout;
    }

    private Component createModesGroup(){
        modesGroup.setItems(
                EDIT,
                CHECK_FOREIGN,
                CHECK_LOCAL);

        modesGroup.setItemLabelGenerator(ModeType::getTitle);

        add(modesGroup);

        modesGroup.addValueChangeListener(this::onModeChanged);

        return modesGroup;
    }

    private void onModeChanged(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<ModeType>, ModeType> event) {
        ModeType modeType = event.getValue();
        setMode(modeType);
    }

    private void setMode(ModeType modeType){
        Mode mode = loadMode(modeType);
        mode.on();
    }

    private Mode loadMode(ModeType modeType) {

        if(EDIT.equals(modeType))
            return modes.computeIfAbsent(modeType, key -> new EditMode(this));

        else if (CHECK_FOREIGN.equals(modeType))
            return modes.computeIfAbsent(modeType, key -> new CheckForeignMode(this));

        else if (CHECK_LOCAL.equals(modeType))
            throw new UnsupportedOperationException("CHECK_LOCAL mode is not implemented yet");

        else
            throw  new IllegalArgumentException(" Unexpected mode type: " + modeType);
    }

    public void setCard(Card card) {

        this.card = card;

        if(card != null)
            setExamples(card);

        clearExamples();
        autoSetMode(card);

        cardBinder.readBean(card);
    }

    private void clearExamples() {
        checkWord.setValue("");
        checkFirstExample.setValue("");
        checkSecondExample.setValue("");
        checkThirdExample.setValue("");
    }

    private void autoSetMode(Card card) {
        if(card == null)
            modesGroup.setValue(EDIT);
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

        card.setLocalFirstExample(phrase.getLocalPhrase());
        card.setForeignFirstExample(phrase.getForeignPhrase());
    }

    private void setSecondExample(List<Phrase> phrases){

        if(phrases.size() < 2)
            return;

        Phrase phrase = phrases.get(1);
        if(phrase == null)
            return;

        card.setLocalSecondExample(phrase.getLocalPhrase());
        card.setForeignSecondExample(phrase.getForeignPhrase());
    }

    private void setThirdExample(List<Phrase> phrases){

        if(phrases.size() < 3)
            return;

        Phrase phrase = phrases.get(2);
        if(phrase == null)
            return;

        card.setLocalThirdExample(phrase.getLocalPhrase());
        card.setForeignThirdExample(phrase.getForeignPhrase());
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
