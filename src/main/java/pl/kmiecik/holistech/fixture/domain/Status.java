package pl.kmiecik.holistech.fixture.domain;

public enum Status {
    NOK("FAIL"),
    OK("PASS");
    private final String displayName;

    private Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayFISStatusName() {
        return displayName;
    }
}