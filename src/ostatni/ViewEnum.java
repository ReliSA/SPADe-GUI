package ostatni;

/**
 * Seznam pohledů použitý při načítání struktur
 *
 * @author Patrik Bezděk
 */
public enum ViewEnum {
    ARTIFACT_VIEW("artifactView"),
    CONFIGURATION_VIEW("configurationView"),
    COMMITED_CONFIGURATION_VIEW("commitedConfigView"),
    COMMIT_VIEW("commitView"),
    WORK_UNIT_VIEW("workUnitView"),
    FIELD_CHANGE_VIEW("fieldChangeView");

    /**
     * Název pohledu
     */
    private final String viewName;

    /**
     * Konstuktor položek
     */
    ViewEnum(String viewName) {
        this.viewName = viewName;
    }

    /**
     * Vrací název pohledu
     * @return název pohledu
     */
    public String getViewName() {
        return this.viewName;
    }
}
