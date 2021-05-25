import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class OracleBuilder implements BuilderInterface {

    private final static String CONFIG_FILE_NAME = "db.oracle.properties";
    private String packageName;
    private final  List<Function> functions = new ArrayList<>();

    public static OracleBuilder builder() {
        return new OracleBuilder();
    }

    public String properties(String properties) throws IOException {
        File file = new File(CONFIG_FILE_NAME);
        Properties properties1 = new Properties();
        properties1.load(new FileReader(file));
        return properties1.getProperty(properties);
    }

    public String buildPackage() throws IOException {
        String sql = properties("template.package");
        sql = sql.replace("${packageName}", packageName);
        String packageInterface = functions.stream().map(Function::buildDesc).collect(Collectors.joining("\n"));
        sql = sql.replace("${packageInterface}", packageInterface);
        return sql;
    }

    public String buildBody() throws IOException {
        String sql = properties("template.package.body");
        sql = sql.replace("${packageName}", packageName);
        String packageBody = functions.stream().map(Function::body).collect(Collectors.joining("\n"));
        sql = sql.replace("${packageBody}", packageBody);
        return sql;
    }

    public OracleBuilder schemaName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public Function function() {
        Function f = new Function(this);
        functions.add(f);
        return f;
    }

    public static class Function  {

        private final OracleBuilder ob;
        private String functionName;
        private String returnType;
        private final StringBuilder body = new StringBuilder();
        private final List<Param> params = new ArrayList<>();
        private String extItem;

        private Function(OracleBuilder ob) {
            this.ob = ob;
        }

        public static Function builder(OracleBuilder ob) {
            return new Function(ob);
        }

        public OracleBuilder end() {
            return ob;
        }

        private String buildFuncHeader(String tail) {
            return "FUNCTION " + functionName + " (" +
                    params.stream().map(Param::getParam).collect(Collectors.joining(", ")) +
                    ") RETURN " + returnType + tail;
        }

        public String buildDesc() {
            return buildFuncHeader(";");
        }

        public String body() {
            return buildFuncHeader(" IS\nBEGIN\n") +
                    body +
                    "\nRETURN " + extItem +
                    ";\nEND " + functionName + ";\n";
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

        public Function extItem(String value) {
            extItem = value;
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
}
