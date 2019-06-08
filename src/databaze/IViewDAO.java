package databaze;

import ostatni.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * Rozhraní pro výběr dat z databázových pohledů
 *
 * @author Patrik Bezděk
 */
public interface IViewDAO {

    /**
     * Vrací data zadaného dotazu. První s hodnotami pro JOIN výsledků a druhý s číselnými výsledky agregačního dotazu
     * @param query dotaz na získání dat pro detekci
     * @return seznam dat
     */
    List<List<String>> runQuery(String query);

    /**
     * Vrací číselný výsledek agregačního dotazu
     * @param query dotaz pro otestování dotazu na vytvoření proměnné
     * @return číselný výsledek
     */
    Long testVariable(String query);

    /**
     * Vrací seznam jmen členů týmu patřících do projektu
     * @param projektId id projektu pro výběr osob
     * @return seznam osob
     */
    ArrayList<String> getPeopleForProject(int projektId);

    /**
     * Vrací seznam sloupců pohledu
     * @param viewName název pohledu
     * @return seznam sloupců v pohledu
     */
    List<Column> getViewStructure(String viewName);
}
