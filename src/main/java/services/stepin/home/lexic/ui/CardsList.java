package services.stepin.home.lexic.ui;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import services.stepin.home.lexic.model.Card;
import services.stepin.home.lexic.model.LanguageCode;
import services.stepin.home.lexic.model.RepetitionFrequency;
import services.stepin.home.lexic.service.CardService;
import services.stepin.home.lexic.service.ExportService;
import services.stepin.home.lexic.ui.card.CardFormEvent;
import services.stepin.home.lexic.ui.card.CardForm;
import services.stepin.home.lexic.ui.dataprovider.CardFilter;
import services.stepin.home.lexic.ui.dataprovider.CardDataProvider;

import java.util.HashSet;
import java.util.Set;

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
    private static final int DEFAULT_PAGE_SIZE = 50;

    private final ConfigurableFilterDataProvider<Card, Void, CardFilter> filteredDataProvider;
    private final CardFilter cardFilter = new CardFilter();
    private final CardService cardService;
    private final ExportService exportService;

    private final TextField localWordFilter = new TextField();
    private final TextField foreignWordFilter = new TextField();
    private final ComboBox<RepetitionFrequency> repetitionFrequencyFilter = new ComboBox<>();

    private final Button addCardButton = new Button("Add card");
    private final Button exportButton = new Button("Export");

    private Grid<Card> cardsGrid;
    private CardForm cardForm;

    private final Set<Card> checkAgainCards = new HashSet<>();
    private final Checkbox checkAgainFilter = new Checkbox("again");
    private final Button clearRepeatAgainButton = new Button("Clear");

    public CardsList(
            CardDataProvider dataProvider,
            CardService cardService,
            ExportService exportService) {

        this.filteredDataProvider = dataProvider.withConfigurableFilter();

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

        toolBar.add(prepareLocalWordFilter());
        toolBar.add(prepareForeignWordFilter());
        toolBar.add(prepareRepeatAgainToolbar());
        toolBar.add(prepareFrequencyFilter());
        toolBar.add(prepareAddCardButton());
        toolBar.add(prepareExportButton());

        return toolBar;
    }

    private Component prepareLocalWordFilter() {
        localWordFilter.setPlaceholder("find by local word ...");
        localWordFilter.setClearButtonVisible(true);
        localWordFilter.setValueChangeMode(TIMEOUT);
        localWordFilter.setValueChangeTimeout(2000);
        localWordFilter.addValueChangeListener(event -> updateList());
        return localWordFilter;
    }

    private Component prepareForeignWordFilter() {
        foreignWordFilter.setPlaceholder("find by foreign word ...");
        foreignWordFilter.setClearButtonVisible(true);
        foreignWordFilter.setValueChangeMode(TIMEOUT);
        foreignWordFilter.setValueChangeTimeout(2000);
        foreignWordFilter.addValueChangeListener(event -> updateList());
        return foreignWordFilter;
    }

    private Component prepareRepeatAgainToolbar() {

        HorizontalLayout repeatAgainToolbar = new HorizontalLayout();
        repeatAgainToolbar.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.NONE,
                LumoUtility.Margin.NONE);

        repeatAgainToolbar.setAlignItems(Alignment.BASELINE);

        repeatAgainToolbar.getStyle().set("border-width", "thin");
        repeatAgainToolbar.getStyle().set("border-style", "solid");
        repeatAgainToolbar.getStyle().set("border-color", "var(--lumo-contrast-20pct)");
        repeatAgainToolbar.getStyle().set("border-radius", "var(--lumo-border-radius-l)");

        repeatAgainToolbar.add(prepareClearRepeatAgainButton());

        return repeatAgainToolbar;
    }

    private Component prepareCheckAgainFilter(){
        checkAgainFilter.setValue(false);
        checkAgainFilter.addValueChangeListener(event -> updateList());
        return checkAgainFilter;
    }

    private Component prepareClearRepeatAgainButton() {
        clearRepeatAgainButton.addClickListener(event -> clearRepeatAgain());
        return clearRepeatAgainButton;
    }

    private Component prepareFrequencyFilter() {
        repetitionFrequencyFilter.setItems(RepetitionFrequency.values());
        repetitionFrequencyFilter.setValue(DAILY);
        repetitionFrequencyFilter.addValueChangeListener(event -> updateList());
        return repetitionFrequencyFilter;
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

        cardForm.setMinWidth("800px");

        contentLayout.setSizeFull();

        return contentLayout;

    }

    private Grid<Card> createCardsGrid() {

        Grid<Card> cardsGrid = new Grid<>(Card.class);
        cardsGrid.addClassName("card-grid-class");

        cardsGrid.setColumns("localWord");

        Grid.Column<Card> checkAgainColumn = cardsGrid
                .addComponentColumn(this::generateCheckAgainComponent)
                .setHeader("Again")
                .setKey("again")
                .setWidth("70px")
                .setFlexGrow(0);

        cardsGrid.setColumnOrder(
                cardsGrid.getColumnByKey("again"),
                cardsGrid.getColumnByKey("localWord"));

        cardsGrid.asSingleSelect().addValueChangeListener(event -> editCard(event.getValue()));

        cardsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        cardsGrid.setDataProvider(filteredDataProvider);
        cardsGrid.setPageSize(DEFAULT_PAGE_SIZE);

        return cardsGrid;
    }

    private Component generateCheckAgainComponent(Card card) {

        boolean checkAgain = checkAgainCards.contains(card);
        Checkbox againCheckBox = new Checkbox(checkAgain);
        againCheckBox.addValueChangeListener(event ->  onAgainCheckboxChange(event, card));

        return againCheckBox;
    }

    private void clearRepeatAgain() {
        checkAgainCards.clear();
        checkAgainFilter.setValue(false);
        updateList();
    }

    private void onAgainCheckboxChange(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event, Card card) {

        boolean checkAgain = event.getValue();

        if(checkAgain)
            checkAgainCards.add(card);
        else
            checkAgainCards.remove(card);
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

        Card card = event.getCard();

        cardService.delete(card);
        checkAgainCards.remove(card);

        updateList();
        closeEditor();
    }

    private void updateList() {

        updateCardFilter();
        filteredDataProvider.setFilter(cardFilter);
    }

    private void updateCardFilter() {
        updateRepetitionFrequencyFilter();
        updateWordContainsFilters();
        updateCheckAgain();
    }

    private void updateRepetitionFrequencyFilter(){
        RepetitionFrequency frequency = repetitionFrequencyFilter.getValue();
        if (ALL.equals(frequency))
            frequency = null;

        cardFilter.setRepetitionFrequency(frequency);
    }

    private void updateWordContainsFilters() {
        cardFilter.setLocalWordContains(localWordFilter.getValue());
        cardFilter.setForeignWordContains(foreignWordFilter.getValue());
    }

    private void updateCheckAgain() {
        cardFilter.setCheckAgain(checkAgainFilter.getValue());
        cardFilter.setCheckAgainCards(checkAgainCards);
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
