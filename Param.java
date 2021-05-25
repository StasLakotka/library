import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Param {

    private final String name;
    private final String type;

    public String getParam() {
        return name + " " + type;
    }
}
