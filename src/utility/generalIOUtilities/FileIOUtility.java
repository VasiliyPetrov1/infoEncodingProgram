package utility.generalIOUtilities;

import utility.convertationUtilities.FileInputProcessingUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIOUtility {

    public static byte[] readBinary(String filePath) throws IllegalArgumentException{
        byte[] inputByteArr;
        byte curByte;

        Path fileLocation = Paths.get(filePath);
        try {
            inputByteArr = Files.readAllBytes(fileLocation);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can not open file for reading");
        }

        return inputByteArr;
    }

    public static boolean writeBinary(String filePath, byte[] data) throws IOException{
        Path file = Paths.get(filePath);
        try {
            Files.write(file, data);
        } catch (IOException e) {
            throw new IOException("Can't write byte array to file");
        }
        return true;
    }
}
