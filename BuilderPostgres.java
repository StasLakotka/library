import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BuilderPostgres implements BuilderInterface{

    private final List<Param> params = new ArrayList<>();
    private final StringBuilder body = new StringBuilder();
    private String extItem;
    private String returnType;
    private String languageType;

    public BuilderPostgres returnType(String returnType){
        this.returnType = returnType;
        return this;
    }

    public BuilderPostgres languageType(String languageType) {
        this.languageType = languageType;
        return this;
    }

    public BuilderPostgres extItem(String extItem) {
        this.extItem = extItem;
        return this;
    }

    @Override
    public String buildPackageBody() {
        return null;
    }

    @Override
    public String buildBody() {
        return  buildFuncHeader("as begin \n") +
                body + "return " + extItem + ";";
    }

    @Override
    public String buildFuncHeader(String tail) {
        return " (" + params.stream().map(BuilderPostgres.Param::getParam).collect(Collectors.joining(", ")) +
                ") \n" + "returns " + returnType + "\nlanguage " + languageType + " \n" + tail;
    }

    @Override
    public String buildPackage() {
        return null;
    }
}
