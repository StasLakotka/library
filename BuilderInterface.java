import java.io.IOException;

public interface BuilderInterface {

    String buildBody() throws IOException;

    String buildPackage() throws IOException;

    BuilderInterface schemaName(String schemaName);

    OracleBuilder.Function function();
}