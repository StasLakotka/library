import lombok.AllArgsConstructor;

public interface BuilderInterface {

    String buildPackageBody();

    String buildBody();

    String buildFuncHeader(String tail);

    String buildPackage();

    @AllArgsConstructor
    class Param {
        private final String name;
        private final String type;

        public String getParam() {
            return name + " " + type;
        }
    }

}
