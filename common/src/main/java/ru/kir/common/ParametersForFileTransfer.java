package ru.kir.common;

public class ParametersForFileTransfer {
    public static final int MAX_FRAME_LENGTHS = 1024 * 1024;
    public static final int LENGTH_FIELD_OFFSET = 0;
    public static final int LENGTH_FIELD_LENGTHS = 4;
    public static final int LENGTH_ADJUSTMENT = 0;
    public static final int INITIAL_BYTES_TO_STRIP = 4;


    public static final String SAVE_FILE_ON_CLIENT = "./cloud-client/src/main/resources/files/testServer.txt";
    public static final String SAVE_FILE_ON_SERVER = "./cloud-server/src/main/resources/client_files/user/testClient.txt";

    public static final String PATH_DOWNLOAD_FILE = "./cloud-server/src/main/resources/client_files/admin/testServer.txt";
    public static final String PATH_UPLOAD_FILE = "./cloud-client/src/main/resources/testClient.txt";

}
