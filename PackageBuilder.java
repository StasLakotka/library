import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PackageBuilder {

    private final List<Param> params = new ArrayList<>();
    private final StringBuilder body = new StringBuilder();
    private String extItem;
    private String returnType;
    private String languageType;
    @Getter
    private String schemaName;
    private String templateFuncBody = "create or replace function ${functionName}" +
            "  ${functionBody} \nend;";
    private String templateFuncBodyValue = "if ${expression} then return ${value}; end if; \n";

    public static PackageBuilder builder() {
        return new PackageBuilder();
    }

    @AllArgsConstructor
    private static class Param {

        private final String name;
        private final String type;

        public String getParam() {
            return name + " " + type;
        }
    }

    public String buildFunctionBody() {
        String sql = templateFuncBody;
        sql = sql.replace("${functionName}", schemaName)
                .replace("${functionBody}", buildBody());
        return sql;
    }

    public PackageBuilder languageType(String languageType) {
        this.languageType = languageType;
        return this;
    }

    public PackageBuilder param(String name, String type) {
        params.add(new Param(name, type));
        return this;
    }

    public PackageBuilder schemaName(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    public PackageBuilder returnType(String returnType) {
        this.returnType = returnType;
        return this;
    }

    public PackageBuilder extItem(String extItem) {
        this.extItem = extItem;
        return this;
    }

    public String buildFuncHeader(String tail) {
        return " (" + params.stream().map(Param::getParam).collect(Collectors.joining(", ")) +
                ") \n" + "returns " + returnType + "\nlanguage " + languageType + " \n" + tail;
    }

    public String buildBody() {
        return buildFuncHeader("as begin \n") +
                body + "return " + extItem + ";";
    }

    public PackageBuilder bodyGroupValue(String expression, String value) {
        String sql = templateFuncBodyValue;
        sql = sql.replace("${expression}", expression)
                .replace("${value}", value);
        body.append("   " + sql);
        return this;

    }
}