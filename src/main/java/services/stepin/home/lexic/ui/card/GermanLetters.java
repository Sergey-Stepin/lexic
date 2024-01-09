package services.stepin.home.lexic.ui.card;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;
import lombok.Setter;

public class GermanLetters extends HorizontalLayout {

    private final Button button_ä = new Button("ä");
    private final Button button_Ä = new Button("Ä");
    private final Button button_ö = new Button("ö");
    private final Button button_Ö = new Button("Ö");
    private final Button button_ü = new Button("ü");
    private final Button button_Ü = new Button("Ü");
    private final Button button_ß = new Button("ß");


    @Getter
    @Setter
    private CardForm source;

    public GermanLetters() {
        addLayout();
    }

    private void addLayout() {

        prepareButton_ä();
        prepareButton_Ä();

        prepareButton_ö();
        prepareButton_Ö();

        prepareButton_ü();
        prepareButton_Ü();

        prepareButton_ß();
    }

    private void prepareButton_ä() {
        button_ä.addClickListener(this::listen_ä);
        add(button_ä);
    }

    private void prepareButton_Ä() {
        button_Ä.addClickListener(this::listen_Ä);
        add(button_Ä);
    }

    private void prepareButton_ö() {
        button_ö.addClickListener(this::listen_ö);
        add(button_ö);
    }

    private void prepareButton_Ö() {
        button_Ö.addClickListener(this::listen_Ö);
        add(button_Ö);
    }

    private void prepareButton_ü() {
        button_ü.addClickListener(this::listen_ü);
        add(button_ü);
    }

    private void prepareButton_ß() {
        button_ß.addClickListener(this::listen_ß);
        add(button_ß);
    }

    private void prepareButton_Ü() {
        button_Ü.addClickListener(this::listen_Ü);
        add(button_Ü);
    }

    private void listen_ä (ClickEvent < Button > event) {
        var field = source.getFocusedComponent();
        field.setValue(field.getValue() + 'ä');
    }

    private void listen_Ä (ClickEvent < Button > event) {
        var field = source.getFocusedComponent();
        field.setValue(field.getValue() + 'Ä');
    }

    private void listen_ö (ClickEvent < Button > event) {
        var field = source.getFocusedComponent();
        field.setValue(field.getValue() + 'ö');
    }

    private void listen_Ö (ClickEvent < Button > event) {
        var field = source.getFocusedComponent();
        field.setValue(field.getValue() + 'Ö');
    }

    private void listen_ü (ClickEvent < Button > event) {
        var field = source.getFocusedComponent();
        field.setValue(field.getValue() + 'ü');
    }

    private void listen_Ü (ClickEvent < Button > event) {
        var field = source.getFocusedComponent();
        field.setValue(field.getValue() + 'Ü');
    }

    private void listen_ß (ClickEvent < Button > event) {
        var field = source.getFocusedComponent();
        field.setValue(field.getValue() + 'ß');
    }
}
