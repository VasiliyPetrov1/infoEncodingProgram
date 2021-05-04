package strategy.encodingAlgoStrategies;

import utility.MathUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HammingEncodeAlgoStrategy implements EncodingAlgoStrategy {

    @Override
    public boolean[] execute(boolean[] dataBitArray) {
        System.out.println("Hamming encoding...");
        System.out.println(Arrays.toString(dataBitArray).replaceAll("true", "1").
                replaceAll("false", "0"));
        List<Boolean> resBitList = new ArrayList<>();
        for (boolean dataBit : dataBitArray) {
            resBitList.add(dataBit);
        }
        int dataLength = dataBitArray.length;
        int numOfParityBits = (int) (Math.ceil(MathUtility.calculateLog(dataLength, 2.0)));
        if (dataLength + numOfParityBits >= Math.pow(2, numOfParityBits)) { // if overall size of code sequence is more than number of next parity bit
            numOfParityBits += 1;
        }
        for(int curParBitPos : generateParBitPosInd(numOfParityBits)){
            resBitList.add(curParBitPos, false);
        }
        List<Integer> onesPosList = new ArrayList<>();
        for (int i = 0; i < resBitList.size(); i++) {
            if (resBitList.get(i)) {
                onesPosList.add(i + 1);
            }
        }
        int paritySyndrome = 0;
        for (int i = 0; i < onesPosList.size(); i++) {
            paritySyndrome = paritySyndrome ^ onesPosList.get(i);
        }
        String paritySyndromeString = String.format("%8s", Integer.toBinaryString(paritySyndrome)).
                replace(' ', '0'); // always > 0 if there is soma mistake and = 0 if no mistake
        paritySyndromeString = paritySyndromeString.substring(paritySyndromeString.length() - numOfParityBits);
        char[] paritySyndromeChars = paritySyndromeString.toCharArray();
        for (int i = 0; i < paritySyndromeChars.length; i++) {
            // set all impair radixes bits from 0 to 1 to make all binary radixes pair
            if (paritySyndromeChars[i] == '1') {
                resBitList.set((int)Math.pow(2, numOfParityBits - 1 - i) - 1, true);
            }
        }
        System.out.println(resBitList.toString().replaceAll("true", "1").
                replaceAll("false", "0"));
        System.out.println(paritySyndromeString);
        boolean[] resBitArray = new boolean[resBitList.size()];
        for (int i = 0; i < resBitList.size(); i++) {
            resBitArray[i] = resBitList.get(i);
        }
        return resBitArray;
    }

    public Integer[] generateParBitPosInd(int parBitsNum) {
        List<Integer> parPosList = new ArrayList<>();
        int curParPos = 1;
        for (int i = 0; i < parBitsNum; i++) {
            parPosList.add(curParPos - 1);
            curParPos *= 2;
        }
        return (Integer [])parPosList.toArray(new Integer[parPosList.size()]);
    }

}
