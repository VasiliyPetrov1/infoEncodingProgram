package strategy.encodingAlgoStrategies;

public class ExtendHammingEncodeAlgoStrategy implements EncodingAlgoStrategy {

    @Override
    public boolean[] execute(boolean[] dataBitArray) {
        int dataLength = dataBitArray.length;
        boolean[] extendedBitArray = new boolean[dataLength + 1];
        boolean overallParity = false;
        for (boolean curBit : dataBitArray) {
            overallParity = overallParity ^ curBit; // evaluate overall(including parity bits) parity of Hamming code
        }
        extendedBitArray[0] = overallParity ? true : false; // extend Hamming code via adding zero overall parity bit
        for (int i = 0; i < dataLength; i++) {
           extendedBitArray[i + 1] = dataBitArray[i];
        }
        return extendedBitArray;
    }
}
