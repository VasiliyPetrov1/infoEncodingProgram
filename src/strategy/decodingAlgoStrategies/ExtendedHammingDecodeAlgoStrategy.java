package strategy.decodingAlgoStrategies;

import java.util.ArrayList;
import java.util.List;

public class ExtendedHammingDecodeAlgoStrategy implements DecodingAlgoStrategy{

    @Override
    public boolean[] execute(boolean[] dataBitArray) {
        int dataLength = dataBitArray.length;
        boolean expectedParityBit = dataBitArray[0]; // retrieve bit which is in charge of overall Hamming code parity
        int paritySyndrome = 0;
        for (int i = 1; i < dataLength; i++) {
            if (dataBitArray[i]) { // check indexes of all ones in the message
                paritySyndrome = paritySyndrome ^ i; // evaluate position of defected bit
            }
        }
        boolean realParityBit = false;
        for (int i = 1; i < dataLength; i++) {
            realParityBit = realParityBit ^ dataBitArray[i];
        }
        String responseStatus;
        if (paritySyndrome == 0 && expectedParityBit == realParityBit) { // if overall parity is equal to expected value
            // and parity syndrome is equal to 0 (no bits was changed)
            responseStatus = "The coded message hasn't been defected";
        } else if (paritySyndrome != 0 && expectedParityBit != realParityBit) { // if overall parity isn't equal to expected value
            // and parity syndrome is not equal to 0 (it is equal to position of defected bit)
            dataBitArray[paritySyndrome] = !dataBitArray[paritySyndrome]; // correct bit
            responseStatus = "The defected bit is on position " + paritySyndrome + ". Correcting...";
        } else if (paritySyndrome != 0 && expectedParityBit == realParityBit) { // if overall parity is equal to expected value
            // and parity syndrome is not equal to 0 (it is not equal to position of defected bit now because it is
            // definetely more than one defected bit)
            responseStatus = "There are two defected bits but their positions can't be determined";
        } else {
            responseStatus = "Undefined amount of defected bits";
        }
        System.out.println(responseStatus);
        List<Boolean> resBitList = new ArrayList<>(); // create empty result list
        int nextParBitPos = 1; // the position of the next parity bit
        for (int i = 1; i < dataLength; i++) { // int that loop we remove redundant parity bits from needed response
            if (i == nextParBitPos) {
                nextParBitPos *= 2;
            } else {
                resBitList.add(dataBitArray[i]);
            }
        }
        boolean[] resBitArray = new boolean[resBitList.size()];
        for (int i = 0; i < resBitList.size(); i++) {
            resBitArray[i] = resBitList.get(i);
        }

        return resBitArray;
    }
}
