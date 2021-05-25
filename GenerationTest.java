import java.io.IOException;

public class GenerationTest {

    public static void main(String[] args) throws IOException {

       BuilderInterface ob = OracleBuilder.builder();
        ob.schemaName("oracle").function()
                .functionName("f1")
                .param("p1", "int")
                .returnType("int")
                .extItem("6")
                .bodyGroupValue("1", "2")
                .end()
                .function()
                .functionName("f2")
                .param("p2", "int")
                .returnType("int")
                .bodyGroupValue("5", "6")
                .end();
        String or = ob.buildBody();
        String or1 = ob.buildPackage();
        System.out.println(or);
        System.out.println();
//
//        BuilderInterface bi = PostgresBuilder.builder();
//        bi.schemaName("postgres").function()
//                .functionName("f1")
//                .param("p1", "int")
//                .returnType("int")
//                .languageType("plpgsql")
//                .extItem("6")
//                .bodyGroupValue("1", "2")
//                .endPostgres()
//                .function()
//                .functionName("f2")
//                .param("p2", "int")
//                .returnType("int")
//                .languageType("plpgsql")
//                .bodyGroupValue("4", "5")
//                .endPostgres();
//        String sql = bi.buildBody();
//        String sql1 = bi.buildPackage();
//        System.out.println(sql);
    }
}