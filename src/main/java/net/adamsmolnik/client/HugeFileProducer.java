package net.adamsmolnik.client;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class HugeFileProducer {

    public static void main(String[] args) throws Exception {
        int size = 10_000_000;
        byte[] bytes = new byte[size];
        Arrays.fill(bytes, (byte) 65);
        Files.write(Paths.get("C:/temp/file_sizedOf" + size), bytes);
    }

}
