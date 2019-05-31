package databaze;

import ostatni.Iteration;
import ostatni.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * Rozhraní pro výběr dat z databázových pohledů
 */
public interface IViewDAO {

    /**
     * Vratí 2 seznamy dat. První s hodnotami pro JOIN výsledků a druhý s číselnými výsledky agregačního dotazu
     * @param query dotaz na získání dat pro detekci
     * @return seznam dat
     */
    List<List<String>> runQuery(String query);

    /**
     * Vrátí číselný výsledek agregačního dotazu
     * @param query dotaz pro otestování dotazu na vytvoření proměnné
     * @return číselný výsledek
     */
    Long testVariable(String query);

    /**
     * Vrací seznam osob patřících do projektu
     * @param projektId id projektu pro výběr osob
     * @return seznam osob
     */
    ArrayList<String> getPeopleForProject(int projektId);

    /**
     * Vrací seznam iterací patřících do projektu
     * @param projektId id projektu pro výběr iterací
     * @return seznam iterací
     */
    ArrayList<Iteration> getIterationsForProject(int projektId);

    /**
     * Vrací strukturu pohledu jako seznam sloupců a jejich typů
     * @param viewName název pohledu
     * @return seznam sloupců v pohledu
     */
    List<Column> getViewStructure(String viewName);
}
