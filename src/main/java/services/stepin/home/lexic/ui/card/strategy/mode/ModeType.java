package services.stepin.home.lexic.ui.card.strategy.mode;

public enum ModeType {

    EDIT("Edit"),
    CHECK_LOCAL("Check local"),
    CHECK_FOREIGN("Check foreign");

    private final String title;

    ModeType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
