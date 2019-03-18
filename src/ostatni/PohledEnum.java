package ostatni;

public enum PohledEnum {
    ARTIFACT_VIEW("artifactView"),
    CONFIGURATION_VIEW("configurationView"),
    COMMITED_CONFIGURATION_VIEW("commitedConfigView"),
    COMMIT_VIEW("commitView"),
    WORK_UNIT_VIEW("workUnitView"),
    FIELD_CHANGE_VIEW("fieldChangeView");

    private final String viewName;

    PohledEnum(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return this.viewName;
    }
}