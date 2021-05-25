import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PostgresBuilder  {

    private final static String CONFIG_FILE_NAME = "db.postgres.properties";
    private String schemaName;
    private final List<Function> functions = new ArrayList<>();

    public static PostgresBuilder builder() {
        return new PostgresBuilder();
    }

    public String properties(String properties) throws IOException {
        File file = new File(CONFIG_FILE_NAME);
        Properties properties1 = new Properties();
        properties1.load(new FileReader(file));
        return properties1.getProperty(properties);
    }

    public String buildPackage() throws IOException {
        String sql = properties("template.schema");
        sql = sql.replace("${schemaName}", schemaName);
        String packageInterface = functions.stream().map(Function::buildDesc).collect(Collectors.joining("\n"));
        sql = sql.replace("${packageInterface}", packageInterface);
        return sql;
    }

    public String buildBody() throws IOException {
        String sql = properties("template.schema.Body");
        sql = sql.replace("${schemaName}", schemaName);
        String packageBody = functions.stream().map(Function::body).collect(Collectors.joining("\n"));
        sql = sql.replace("${packageBody}", packageBody);
        return sql;
    }

    public PostgresBuilder schemaName(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    public Function function() {
        Function f = new Function(this);
        functions.add(f);
        return f;
    }

    public static class Function extends PostgresBuilder{

        private final PostgresBuilder pb;
        public String functionName;
        private String returnType;
        private String languageType;
        private final StringBuilder body = new StringBuilder();
        private final List<Param> params = new ArrayList<>();
        private String extItem;

        public Function(PostgresBuilder pb) {
            this.pb = pb;
        }

        public PostgresBuilder end() {
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
            String plSql = properties("template.function.body.groupValue");
            plSql = plSql.replace("${expression}", expression)
                    .replace("${value}", value);
            body.append("   " + plSql);
            return this;
        }

        public Function bodyGroupKlUni(String field, String[] items, String value) throws IOException {
            String plSql = properties("template.function.body.groupKlUni");
            plSql = plSql.replace("${field}", field)
                    .replace("${items}", String.join(", ", items))
                    .replace("${value}", value);
            body.append("   " + plSql);
            return this;
        }
    }
}