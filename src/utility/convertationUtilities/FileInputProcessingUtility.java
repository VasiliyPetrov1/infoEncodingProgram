package utility.convertationUtilities;

import model.MessageInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileInputProcessingUtility implements InputProcessingUtility<Byte, String> {

    private static FileInputProcessingUtility fileUtility;

    private FileInputProcessingUtility() {

    }

    public static FileInputProcessingUtility getInstance() {
        if (fileUtility == null) {
            fileUtility = new FileInputProcessingUtility();
        }
        return fileUtility;
    }

    @Override
    public MessageInfo<Byte> retrieveInputInfo(String... inputFilePath) throws IllegalArgumentException{
        String filePath = null;
        MessageInfo<Byte> messageInfo;
        List<Byte> inputByteList = new ArrayList<>();
        byte[] data;

        for (String сurPath : inputFilePath) {
            filePath = сurPath;
        }

        Path fileLocation = Paths.get(filePath);
        try {
            data = Files.readAllBytes(fileLocation);
        } catch (IOException e) {
            throw new IllegalArgumentException("Can not open file for reading");
        }

        for (int i = 0; i < data.length; i++) {
            inputByteList.add(data[i]);
        }

        messageInfo = new MessageInfo<>(inputByteList);
        return messageInfo;
    }
}
