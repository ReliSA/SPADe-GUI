package ostatni;

import java.time.LocalDate;
import java.util.Date;

public class Iterace {
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;

    public Iterace(LocalDate startDate, LocalDate endDate, String name) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
