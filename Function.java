import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Function {

    private PostgresBuilder pb;
    private OracleBuilder ob;
    public String functionName;
    private String returnType;
    private String languageType;
    private final StringBuilder body = new StringBuilder();
    private final List<Param> params = new ArrayList<>();
    private String extItem;

    public Function(PostgresBuilder pb) {
        this.pb = pb;
    }

    public Function(OracleBuilder ob) {
        this.ob = ob;
    }

    public OracleBuilder endOracle() {
        return ob;
    }

    public PostgresBuilder endPostgres() {
        return pb;
    }

    public Function languageType(String languageType) {
        this.languageType = languageType;
        return this;
    }

    private String buildFuncHeader(String tail) {
        return "FUNCTION " + functionName + " (" +
                params.stream().map(Param::getParam).collect(Collectors.joining(", ")) +
                ") RETURNS " + returnType +
                "\nLANGUAGE " + languageType + "\n" + tail;
    }

    public String buildDesc() {
        return buildFuncHeader("");
    }

    public String body() {
        return buildFuncHeader("AS BEGIN\n") +
                body +
                "\nRETURN " + extItem +
                ";\nEND; \n";
    }

    public Function returnType(String returnType) {
        this.returnType = returnType;
        return this;
    }

    public Function functionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    public Function param(String name, String type) {
        params.add(new Param(name, type));
        return this;
    }

    public Function extItem(String extItem) {
        this.extItem = extItem;
        return this;
    }

    public Function bodyGroupValue(String expression, String value) throws IOException {
        String plSql = ob.properties("template.function.body.groupValue");
        plSql = plSql.replace("${expression}", expression)
                .replace("${value}", value);
        body.append("   " + plSql);
        return this;
    }

    public Function bodyGroupKlUni(String field, String[] items, String value) throws IOException {
        String plSql = ob.properties("template.function.body.groupKlUni");
        plSql = plSql.replace("${field}", field)
                .replace("${items}", String.join(", ", items))
                .replace("${value}", value);
        body.append("   " + plSql);
        return this;
    }
}