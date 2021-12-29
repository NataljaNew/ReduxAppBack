package resto;

public interface ApiPath {
    String ID_VARIABLE = "id";
    String NAME_VARIABLE = "name";

    String ROOT = "/api";
    String ITEMS = "/items";
    String ITEM = "/{" + ID_VARIABLE + "}";
    String SEARCH = "/search";
    String FILES = "/files";
    String FILE_BY_NAME = "/{" + NAME_VARIABLE + "}";
    String BLOBS = "/blobs";
    String GET_BLOB = BLOBS + "/{" + ID_VARIABLE + "}";
}
