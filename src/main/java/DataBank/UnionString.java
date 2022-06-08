package DataBank;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Evgeny Dybov
 */

@AllArgsConstructor
@Data
public class UnionString {
    private final String nameAgents;
    private final String nameProjects;
    private final Double profit;
}
