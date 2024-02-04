package services.stepin.home.lexic.ui.card;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.Phrase;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.ui.card.strategy.mode.CheckForeignMode;
import services.stepin.home.lexic.ui.card.strategy.mode.EditMode;
import services.stepin.home.lexic.ui.card.strategy.mode.Mode;
import services.stepin.home.lexic.ui.card.strategy.mode.ModeType;
import services.stepin.home.lexic.ui.card.strategy.speech.Noun;
import services.stepin.home.lexic.ui.card.strategy.speech.OtherPartOfSpeech;
import services.stepin.home.lexic.ui.card.strategy.speech.PartOfSpeech;
import services.stepin.home.lexic.ui.card.strategy.speech.PartOfSpeechType;
import services.stepin.home.lexic.ui.card.strategy.speech.Verb;

import java.util.*;
import java.util.stream.Stream;

import static com.vaadin.flow.component.KeyModifier.CONTROL;
import static org.springframework.util.StringUtils.hasText;
import static services.stepin.home.lexic.model.Card.Gender.*;
import static services.stepin.home.lexic.model.LanguageCode.DE;
import static services.stepin.home.lexic.model.RepetitionFrequency.ALL;
import static services.stepin.home.lexic.model.RepetitionFrequency.DAILY;
import static services.stepin.home.lexic.ui.CardsList.FOREIGN_LANGUAGE;
import static services.stepin.home.lexic.ui.CardsList.LOCAL_LANGUAGE;
import static services.stepin.home.lexic.ui.card.CardFormEvent.*;
import static services.stepin.home.lexic.ui.card.strategy.mode.ModeType.*;
import static services.stepin.home.lexic.ui.card.strategy.speech.PartOfSpeechType.*;

@Log4j2
public class CardForm extends FormLayout {

    private static final String LOCAL_EXAMPLE_LABEL = "Example (" + LOCAL_LANGUAGE + "):";
    private static final String FOREIGN_EXAMPLE_LABEL = "Example (" + FOREIGN_LANGUAGE + "):";

    private static final String MASCULINUM_COLOUR_NAME = "blue";
    private static final String FEMININUM_COLOUR_NAME = "red";
    private static final String NEUTRUM_COLOUR_NAME = "mediumseagreen";
    private static final String DEFAULT_COLOUR_NAME = "black";
    private final Binder<Card> cardBinder = new BeanValidationBinder<>(Card.class);

    private final EnumMap<ModeType, Mode> modeMap = new EnumMap<>(ModeType.class);
    private final EnumMap<PartOfSpeechType, PartOfSpeech> partsOfSpeechMap = new EnumMap<>(PartOfSpeechType.class);

    @Getter
    private final Button saveButton = new Button("Save");
    @Getter
    private final Button deleteButton = new Button("Delete");
    @Getter
    private final Button cancelButton = new Button("Cancel");
    @Getter
    private final RadioButtonGroup<ModeType> modes = new RadioButtonGroup<>();
    @Getter
    private final RadioButtonGroup<PartOfSpeechType> partOfSpeech = new RadioButtonGroup<>();
    @Getter
    private final RadioButtonGroup<Card.Gender> gender = new RadioButtonGroup<>();
    @Getter
    private final ComboBox<LanguageCode> languageCode = new ComboBox<>("Language");
    @Getter
    private final ComboBox<RepetitionFrequency> repetitionFrequency = new ComboBox<>("Repeat");
    @Getter
    private final TextField localWord = new TextField("Local");
    @Getter
    private final TextField foreignWord = new TextField("Foreign");
    @Getter
    private final TextField foreignPlural = new TextField("Plural");
    @Getter
    private final TextField imperativeDu = new TextField("Imperativ (du)");
    @Getter
    private final TextField indikativIhr = new TextField("Indikativ (ihr)");
    @Getter
    private final TextField indikativErSieEs = new TextField("Indikativ (er/sie/es)");
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
    @Getter
    private final TextField localThirdExample = new TextField();
    @Getter
    private final TextField foreignThirdExample = new TextField();
    @Getter
    private final TextField checkThirdExample = new TextField();

    private Card card;


    public CardForm() {

        addClassName("card-form-class");

        languageCode.setItems(LanguageCode.values());
        repetitionFrequency.setItems(getRepetitionFrequencies());

        languageCode.setValue(DE);
        repetitionFrequency.setValue(DAILY);

        addLayout();

        cardBinder.bindInstanceFields(this);
    }

    private List<RepetitionFrequency> getRepetitionFrequencies() {
        return Stream.of(RepetitionFrequency.values())
                .filter(frequency -> !ALL.equals(frequency))
                .toList();
    }

    private void addLayout() {

        add(createToolbar());

        add(createModesGroup());
        add(preparePartOfSpeechGroup());

        add(preparePropertiesToolbar());
        add(prepareNounComponent());

        add(prepareVerbComponent());

        add(localWord);
        add(foreignWord);
        add(checkWord);

        addExamples();

        setFonts();
        setResponsiveSteps(new ResponsiveStep("0", 2));
    }

    private void setFonts() {

        localWord.getStyle().set("font-size", "var(--lumo-font-size-xl)");

        foreignWord.getStyle().set("font-size", "var(--lumo-font-size-xl)");

        checkWord.getStyle().set("font-size", "var(--lumo-font-size-xl)");
    }

    private Component preparePropertiesToolbar() {
        return new HorizontalLayout(languageCode, repetitionFrequency);
    }

    private Component preparePartOfSpeechGroup() {
        partOfSpeech.setItems(
                NOUN,
                VERB,
                OTHER);

        partOfSpeech.setItemLabelGenerator(this::generatePartOfSpeechLabel);
        partOfSpeech.addValueChangeListener(this::onPartOfSpeechChangedChanged);

        return partOfSpeech;
    }

    private Component prepareVerbComponent() {

        HorizontalLayout partOfSpeechDependent = new HorizontalLayout();

        partOfSpeechDependent.add(imperativeDu);
        partOfSpeechDependent.add(indikativIhr);
        partOfSpeechDependent.add(indikativErSieEs);

        return partOfSpeechDependent;
    }

    private Component prepareNounComponent() {

        HorizontalLayout nounComponent = new HorizontalLayout();
        nounComponent.setAlignItems(FlexComponent.Alignment.BASELINE);

        gender.setItems(Card.Gender.values());
        gender.setItemLabelGenerator(this::generateGenderLabel);
        gender.addValueChangeListener(this::onGenderChanged);
        nounComponent.add(gender);

        nounComponent.add(foreignPlural);

        return nounComponent;
    }

    private void addExamples() {

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

    private String generatePartOfSpeechLabel(PartOfSpeechType partOfSpeechType) {
        return partOfSpeechType.name().toLowerCase();
    }

    private void onPartOfSpeechChangedChanged(
            AbstractField.ComponentValueChangeEvent<RadioButtonGroup<PartOfSpeechType>, PartOfSpeechType> event) {

        PartOfSpeechType partOfSpeechType = event.getValue();

        if (card == null)
            return;

        setPartOfSpeechMap(partOfSpeechType);
    }

    private void setPartOfSpeechMap(PartOfSpeechType partOfSpeechType) {
        PartOfSpeech part = loadPartOfSpeech(partOfSpeechType);
        part.on();
    }

    private PartOfSpeech loadPartOfSpeech(PartOfSpeechType partOfSpeechType) {

        if (NOUN.equals(partOfSpeechType))
            return partsOfSpeechMap.computeIfAbsent(partOfSpeechType, key -> new Noun(this));

        else if (VERB.equals(partOfSpeechType))
            return partsOfSpeechMap.computeIfAbsent(partOfSpeechType, key -> new Verb(this));

        else if (OTHER.equals(partOfSpeechType))
            return partsOfSpeechMap.computeIfAbsent(partOfSpeechType, key -> new OtherPartOfSpeech(this));

        else
            return partsOfSpeechMap.computeIfAbsent(partOfSpeechType, key -> new OtherPartOfSpeech(this));
    }

    private String generateGenderLabel(Card.Gender genderItem) {
        return genderItem.name().toLowerCase();
    }

    private void onGenderChanged(
            AbstractField.ComponentValueChangeEvent<RadioButtonGroup<Card.Gender>, Card.Gender> event) {

        Card.Gender selectedGender = event.getValue();

        if (MASCULINE.equals(selectedGender))
            setForMasculinum();
        else if (FEMININE.equals(selectedGender))
            setForFemininum();
        else if (NEUTER.equals(selectedGender))
            setForNeutrum();
        else
            setForUndetermined();
    }

    private void setForMasculinum() {
        foreignWord.getStyle().setColor(MASCULINUM_COLOUR_NAME);
    }

    private void setForFemininum() {
        foreignWord.getStyle().setColor(FEMININUM_COLOUR_NAME);
    }

    private void setForNeutrum() {
        foreignWord.getStyle().setColor(NEUTRUM_COLOUR_NAME);
    }
    private void setForUndetermined() {
        foreignWord.getStyle().setColor(DEFAULT_COLOUR_NAME);
    }


    private void setAutocomplete() {
        localWord.getElement().setAttribute("autocomplete", "off");
        foreignWord.getElement().setAttribute("autocomplete", "off");
        checkWord.getElement().setAttribute("autocomplete", "off");
        checkFirstExample.getElement().setAttribute("autocomplete", "off");
        checkSecondExample.getElement().setAttribute("autocomplete", "off");
        checkThirdExample.getElement().setAttribute("autocomplete", "off");
    }

    private void setExampleToolTips() {
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

    private void setFirstExampleTooltip() {
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

    private void setSecondExampleTooltip() {
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

    private void setThirdExampleTooltip() {
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

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, deleteButton, cancelButton);
        add(buttonLayout);

        return buttonLayout;
    }

    private Component createModesGroup() {
        modes.setItems(
                EDIT,
                CHECK_FOREIGN,
                CHECK_LOCAL);

        modes.setItemLabelGenerator(ModeType::getTitle);

        add(modes);

        modes.addValueChangeListener(this::onModeChanged);

        return modes;
    }

    private void onModeChanged(AbstractField.ComponentValueChangeEvent<RadioButtonGroup<ModeType>, ModeType> event) {
        ModeType modeType = event.getValue();

        if (card == null)
            return;

        setMode(modeType);
    }

    private void setMode(ModeType modeType) {
        Mode mode = loadMode(modeType);
        mode.on();
    }

    private Mode loadMode(ModeType modeType) {

        if (EDIT.equals(modeType))
            return modeMap.computeIfAbsent(modeType, key -> new EditMode(this));

        else if (CHECK_FOREIGN.equals(modeType))
            return modeMap.computeIfAbsent(modeType, key -> new CheckForeignMode(this));

        else if (CHECK_LOCAL.equals(modeType))
            throw new UnsupportedOperationException("CHECK_LOCAL mode is not implemented yet");

        else
            throw new IllegalArgumentException(" Unexpected mode type: " + modeType);
    }

    public void setCard(Card card) {

        this.card = card;

        if (card != null)
            setManuallyBoundField();

        clearExamples();

        cardBinder.readBean(card);
    }

    public void validateAndSave() {

        if (card == null) {
            return;
        }

        try {
            setPhrases(card);
            cardBinder.writeBean(card);
            fireEvent(new CardFormEvent.CardFormSaveEvent(this, card));

        } catch (ValidationException ex) {
            log.warn("Cannot save card: " + ex.getMessage());
        }
    }

    private void setManuallyBoundField() {
        setExamples();
        setPartOfSpeech();
        setGender();
        autoSetMode();
    }

    private void clearExamples() {
        checkWord.setValue("");
        checkFirstExample.setValue("");
        checkSecondExample.setValue("");
        checkThirdExample.setValue("");
    }

    private void autoSetMode() {
        if (modes.getValue() == null)
            modes.setValue(EDIT);
    }

    private void setExamples() {

        List<Phrase> phrases = card.getPhraseList();

        setFirstExample(phrases);
        setSecondExample(phrases);
        setThirdExample(phrases);
    }

    private void setPartOfSpeech() {
        PartOfSpeechType storedValue = card.getPartOfSpeech();
        partOfSpeech.setValue(storedValue);
    }

    private void setGender() {
        Card.Gender storedValue = card.getGender();
        gender.setValue(storedValue);
    }

    private void setFirstExample(List<Phrase> phrases) {

        if (phrases.size() < 1)
            return;

        Phrase phrase = phrases.get(0);
        if (phrase == null)
            return;

        card.setLocalFirstExample(phrase.getLocalPhrase());
        card.setForeignFirstExample(phrase.getForeignPhrase());
    }

    private void setSecondExample(List<Phrase> phrases) {

        if (phrases.size() < 2)
            return;

        Phrase phrase = phrases.get(1);
        if (phrase == null)
            return;

        card.setLocalSecondExample(phrase.getLocalPhrase());
        card.setForeignSecondExample(phrase.getForeignPhrase());
    }

    private void setThirdExample(List<Phrase> phrases) {

        if (phrases.size() < 3)
            return;

        Phrase phrase = phrases.get(2);
        if (phrase == null)
            return;

        card.setLocalThirdExample(phrase.getLocalPhrase());
        card.setForeignThirdExample(phrase.getForeignPhrase());
    }

    private void setPhrases(Card card) {

        List<Phrase> phrases = new ArrayList<>();

        Phrase firstPhrase = createPhrase(localFirstExample.getValue(), foreignFirstExample.getValue());
        if (firstPhrase != null)
            phrases.add(firstPhrase);

        Phrase secondPhrase = createPhrase(localSecondExample.getValue(), foreignSecondExample.getValue());
        if (secondPhrase != null)
            phrases.add(secondPhrase);

        Phrase thirdPhrase = createPhrase(localThirdExample.getValue(), foreignThirdExample.getValue());
        if (firstPhrase != null)
            phrases.add(thirdPhrase);

        card.setPhraseList(phrases);
    }

    private Phrase createPhrase(String localWord, String foreignWord) {
        if (hasText(localWord) && hasText(foreignWord))
            return new Phrase(languageCode.getValue(), localWord, foreignWord);
        else
            return null;
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
