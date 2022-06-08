package DataBank;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Evgeny Dybov
 */

@Data
@AllArgsConstructor
public class ActivePOJO {
    private String nameAgent;
    private double active;
    private double highBound;
    private double lowBound;

}
