package services.stepin.home.lexic.ui.phrase;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import services.stepin.home.lexic.model.Phrase;

public class PhrasesComponent extends VerticalLayout {

    private final PhraseGrid phraseGrid = new PhraseGrid();

    public PhrasesComponent() {

        add(createToolbar());
        add(phraseGrid);

    }

    private Component createToolbar() {

        Button addPhraseButton = new Button(LumoIcon.PLUS.create());
        addPhraseButton.addClickListener(event -> phraseGrid.addPhrase(new Phrase()));

        addPhraseButton.addThemeVariants(
                ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_SMALL);

        HorizontalLayout toolBar = new HorizontalLayout(addPhraseButton);
        return toolBar;
    }

    public PhraseGrid getPhraseGrid() {
        return phraseGrid;
    }

}
