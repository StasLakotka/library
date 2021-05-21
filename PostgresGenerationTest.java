
public class PostgresGenerationTest {


    public static void main(String[] args) {
        PackageBuilder pb = PackageBuilder.builder();
        pb.schemaName("func1")
                .param("par1", "int")
                .param("param2", "int")
                .returnType("int")
                .languageType("plpgsql")
                .extItem("0")
                .bodyGroupValue("1500", "1")
                .bodyGroupValue("1501", "2");

        String sql = pb.buildFunctionBody();

        System.out.println(sql);

    }

}
