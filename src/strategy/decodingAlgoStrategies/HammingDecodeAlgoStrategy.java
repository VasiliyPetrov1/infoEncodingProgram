package strategy.decodingAlgoStrategies;

import java.util.ArrayList;
import java.util.List;

public class HammingDecodeAlgoStrategy implements DecodingAlgoStrategy{
    @Override
    public boolean[] execute(boolean[] dataBitArray) {
        int dataLength = dataBitArray.length;
        int paritySyndrome = 0;
        for (int i = 0; i < dataLength; i++) {
            if (dataBitArray[i]) { // check indexes of all ones in the message
                paritySyndrome = paritySyndrome ^ (i + 1); // evaluate position of defected bit
            }
        }
        String responseStatus;
        if (paritySyndrome == 0) {
            responseStatus = "The coded message hasn't been defected";
        } else {
            dataBitArray[paritySyndrome - 1] = !dataBitArray[paritySyndrome - 1]; // correct defected bit
            responseStatus = "The defected bit is on position " + paritySyndrome + ". Correcting...";
        }
        System.out.println(responseStatus);
        List<Boolean> resBitList = new ArrayList<>(); // create empty result list
        int nextParBitPos = 1; // the position of the next parity bit
        for (int i = 0; i < dataLength; i++) { // int that loop we remove redundant parity bits from needed response
            if (i == nextParBitPos - 1) {
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
