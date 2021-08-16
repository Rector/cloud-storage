package ru.kir.utils;

public class ParametersForFileTransfer {
    public static final int MAX_FRAME_LENGTHS = 1024 * 1024;
    public static final int LENGTH_FIELD_OFFSET = 0;
    public static final int LENGTH_FIELD_LENGTHS = 4;
    public static final int LENGTH_ADJUSTMENT = 0;
    public static final int INITIAL_BYTES_TO_STRIP = 4;

    public static final String SAVE_FILE_ON_CLIENT = "D:/JavaProject/cloud-storage/cloud-client/src/main/resources/files/testServer.txt";
    public static final String SAVE_FILE_ON_SERVER = "D:/JavaProject/cloud-storage/cloud-server/src/main/resources/client_files/user/testClient.txt";

    public static final String PATH_DOWNLOAD_FILE = "D:/JavaProject/cloud-storage/cloud-server/src/main/resources/client_files/admin/testServer.txt";
    public static final String PATH_UPLOAD_FILE = "D:/JavaProject/cloud-storage/cloud-client/src/main/resources/testClient.txt";
}
