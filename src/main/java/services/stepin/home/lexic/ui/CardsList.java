package services.stepin.home.lexic.ui;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.service.CardService;
import services.stepin.home.lexic.service.ExportService;
import services.stepin.home.lexic.ui.card.CardFormEvent;
import services.stepin.home.lexic.ui.card.CardForm;

import java.util.List;

import static com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT;
import static services.stepin.home.lexic.model.LanguageCode.DE;
import static services.stepin.home.lexic.model.LanguageCode.RU;
import static services.stepin.home.lexic.model.RepetitionFrequency.ALL;
import static services.stepin.home.lexic.model.RepetitionFrequency.DAILY;

@Route(value = "lexic")
@PageTitle("Lexic | cards")
public class CardsList extends VerticalLayout {

    public static final LanguageCode FOREIGN_LANGUAGE = DE;
    public static final LanguageCode LOCAL_LANGUAGE = RU;
    private static final int DEFAULT_PAGE_SIZE = 100;

    private final CardService cardService;
    private final ExportService exportService;

    private final ComboBox<RepetitionFrequency> repetitionFrequencyFilter = new ComboBox<>();
    private final TextField localWordFilter = new TextField();
    private final Button addCardButton = new Button("Add card");
    private final Button exportButton = new Button("Export");

    private Grid<Card> cardsGrid;
    private CardForm cardForm;

    public CardsList(
            CardService cardService, ExportService exportService) {

        this.cardService = cardService;
        this.exportService = exportService;

        addClassName("cards-class");
        setSizeFull();

        add(prepareToolbar());
        add(prepareContent());

        updateList();
        closeEditor();
    }

    private Component prepareToolbar() {


        HorizontalLayout toolBar = new HorizontalLayout();
        toolBar.addClassName("toolbar-class");

        toolBar.add(prepareWordFilter());
        toolBar.add(prepareFrequencyFilter());
        toolBar.add(prepareAddCardButton());
        toolBar.add(prepareExportButton());

        return toolBar;
    }

    private Component prepareWordFilter() {
        localWordFilter.setPlaceholder("find by word ...");
        localWordFilter.setClearButtonVisible(true);
        localWordFilter.setValueChangeMode(TIMEOUT);
        localWordFilter.setValueChangeTimeout(2000);
        localWordFilter.addValueChangeListener(this::onWordChanged);
        return localWordFilter;
    }

    private void onWordChanged(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        updateList();
    }

    private Component prepareFrequencyFilter() {
        repetitionFrequencyFilter.setItems(RepetitionFrequency.values());
        repetitionFrequencyFilter.setValue(DAILY);
        repetitionFrequencyFilter.addValueChangeListener(this::onRepetitionFrequencyChange);
        return repetitionFrequencyFilter;
    }

    private void onRepetitionFrequencyChange(
            AbstractField.ComponentValueChangeEvent<ComboBox<RepetitionFrequency>, RepetitionFrequency> event) {

        updateList();
    }

    private Button prepareAddCardButton() {
        addCardButton.addClickListener(event -> addCard());
        return addCardButton;
    }

    private Button prepareExportButton() {
        exportButton.addClickListener(this::onExportButtonClicked);
        return exportButton;
    }

    private Component prepareContent() {

        this.cardsGrid = createCardsGrid();
        this.cardForm = createCardForm();

        HorizontalLayout contentLayout = new HorizontalLayout(cardsGrid, cardForm);
        contentLayout.addClassName("content-class");

        cardsGrid.setSizeFull();
        cardsGrid.setMinWidth("200px");
        //contentLayout.setFlexGrow(1, cardsGrid);

        cardsGrid.setMinWidth("600px");
        //contentLayout.setFlexGrow(3, cardForm);

        contentLayout.setSizeFull();

        return contentLayout;

    }

    private Grid<Card> createCardsGrid() {

        Grid<Card> cardsGrid = new Grid<>(Card.class);
        cardsGrid.addClassName("card-grid-class");

        cardsGrid.setColumns("localWord");
        cardsGrid.asSingleSelect().addValueChangeListener(event -> editCard(event.getValue()));

        cardsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        cardsGrid.setPageSize(DEFAULT_PAGE_SIZE);

        return cardsGrid;
    }

    private CardForm createCardForm() {

        CardForm cardForm = new CardForm();

        cardForm.addSaveListener(this::saveCard);
        cardForm.addDeleteListener(this::deleteCard);
        cardForm.addCloseListener(event -> closeEditor());

        return cardForm;
    }

    private void addCard() {

        if (cardForm != null) {
            cardForm.validateAndSave();
        }

        cardsGrid.asSingleSelect().clear();
        editCard(new Card());
    }

    private void saveCard(CardFormEvent.CardFormSaveEvent event) {
        cardService.save(event.getCard());
        updateList();
        closeEditor();
    }

    private void deleteCard(CardFormEvent.CardFormDeleteEvent event) {
        cardService.delete(event.getCard());
        updateList();
        closeEditor();
    }

    private void updateList() {

        RepetitionFrequency frequency = repetitionFrequencyFilter.getValue();
        if (ALL.equals(frequency))
            frequency = null;

        String startsWith = localWordFilter.getValue();
        List<Card> foundCards = cardService.find(FOREIGN_LANGUAGE, frequency, startsWith);
        cardsGrid.setItems(foundCards);
    }

    private void editCard(Card card) {

        if (card == null) {
            closeEditor();
        }

        cardForm.setCard(card);
        cardForm.setVisible(true);

        addClassName("editing-class");
    }

    private void closeEditor() {
        cardForm.setCard(null);
        cardForm.setVisible(false);
        removeClassName("editing-class");
    }

    private void onExportButtonClicked(ClickEvent<Button> event) {
        exportService.exportAll();
    }

}
