package service;

import strategy.decodingAlgoStrategies.DecodingAlgoStrategy;
import strategy.encodingAlgoStrategies.EncodingAlgoStrategy;
import strategy.encodingAlgoStrategies.ExtendHammingEncodeAlgoStrategy;
import strategy.encodingAlgoStrategies.HammingEncodeAlgoStrategy;
import utility.MathUtility;

import java.util.*;

public class EncodingDecodingService {

    private static EncodingDecodingService instance;
    private EncodingAlgoStrategy encodingAlgoStrategy;
    private DecodingAlgoStrategy decodingAlgoStrategy;

    private EncodingDecodingService () {

    }

    public static EncodingDecodingService getInstance(){
        if (instance == null) {
            instance = new EncodingDecodingService();
        }
        return instance;
    }

    public EncodingAlgoStrategy getEncodingAlgoStrategy() {
        return encodingAlgoStrategy;
    }

    public DecodingAlgoStrategy getDecodingAlgoStrategy() {
        return decodingAlgoStrategy;
    }
    
    public void setEncodingAlgoStrategy (EncodingAlgoStrategy encodingAlgoStrategy) {
        this.encodingAlgoStrategy = encodingAlgoStrategy;
    }

    public void setDecodingAlgoStrategy(DecodingAlgoStrategy decodingAlgoStrategy) {
        this.decodingAlgoStrategy = decodingAlgoStrategy;
    }

    public boolean[] executeEncoding(byte[] dataArr) {
        return encodingAlgoStrategy.execute(convertByteArrToBitSet(dataArr));
    }

    public boolean[] executeEncoding(boolean[] dataBitSet) {
        return encodingAlgoStrategy.execute(dataBitSet);
    }

    public boolean[] executeEncoding(String dataString) {
        return encodingAlgoStrategy.execute(convertStringToBitSet(dataString));
    }

    public boolean[] executeDecoding(byte[] dataArr) {
        return decodingAlgoStrategy.execute(convertByteArrToBitSet(dataArr));
    }

    public boolean[] executeDecoding(boolean[] dataBitSet) {
        return decodingAlgoStrategy.execute(dataBitSet);
    }

    public boolean[] executeDecoding(String dataString) {
        return decodingAlgoStrategy.execute(convertStringToBitSet(dataString));
    }

    public boolean[] executeDividedEncoding(String dataString, int codedSequenceLength, boolean codeIsExtended) {
        boolean[] dataBitArr = convertStringToBitSet(dataString);
        return executeDividedEncoding(dataString, codedSequenceLength, codeIsExtended);
    }

    public boolean[] executeDividedEncoding(byte[] dataArr, int codedSequenceLength, boolean codeIsExtended) {
        boolean[] dataBitArr = convertByteArrToBitSet(dataArr);
        return executeDividedEncoding(dataBitArr, codedSequenceLength, codeIsExtended);
    }

    public boolean[] executeDividedEncoding (boolean[] dataBitSet, int codedSequenceLength, boolean codeIsExtended) {
        EncodingAlgoStrategy hammingStrategy = new HammingEncodeAlgoStrategy();
        EncodingAlgoStrategy extHammingStrategy = new ExtendHammingEncodeAlgoStrategy();
        int dataLength = dataBitSet.length;
        List<Boolean> resCodeList = new ArrayList<>();
        boolean[] curCodedSequence = new boolean[codedSequenceLength];
        boolean[] curCode;
        int curStartPos = 0;
        int curCodeSeqLength = countHammingEncodedSequenceLength(codedSequenceLength, codeIsExtended);
        int codeSeqNum = 0;
        while (curStartPos < dataLength) {
            if (curStartPos + codedSequenceLength > dataLength) {
                break; // last code sequence with length smaller than given length should be ignored
            }
            for (int i = 0; i < codedSequenceLength; i++) {
                curCodedSequence[i] = dataBitSet[curStartPos + i];
            }
            curCode = executeEncoding(curCodedSequence);
            if (codeIsExtended) {
                setEncodingAlgoStrategy(extHammingStrategy);
                curCode = executeEncoding(curCode);
                setEncodingAlgoStrategy(hammingStrategy); // return from extension to coding
            }
            for (boolean curBit : curCode) {
                resCodeList.add(curBit);
            }
            curStartPos += codedSequenceLength;
            codeSeqNum++;
        }
        boolean[] resBitArray = new boolean[codeSeqNum * curCodeSeqLength];
        int i = 0;
        for (boolean curBit : resCodeList) {
            resBitArray[i] = curBit;
            i++;
        }
        return resBitArray;
    }

    public boolean[] executeDividedDecoding (boolean[] dataBitSet, int codedSequenceLength, boolean codeIsExtended) {
        int dataLength = dataBitSet.length;
        int encodedSequenceLength = countHammingEncodedSequenceLength(codedSequenceLength, codeIsExtended);
        int curStartPos = 0;
        List<Boolean> correctedBitList = new ArrayList<>();
        boolean[] curCodedSequence = new boolean[encodedSequenceLength];
        boolean[] curCorrectedSequence;
        while (curStartPos < dataLength) {
            if (curStartPos + encodedSequenceLength > dataLength) {
                break; // last code sequence with length smaller than given length should be ignored
            }
            for (int i = 0; i < encodedSequenceLength; i++) {
                curCodedSequence[i] = dataBitSet[curStartPos + i];
            }
            curCorrectedSequence = executeDecoding(curCodedSequence);
            for (boolean curBit : curCorrectedSequence) {
                correctedBitList.add(curBit);
            }
            curStartPos += encodedSequenceLength;
        }
        boolean[] resBitSet = new boolean[correctedBitList.size()];
        for (int i = 0; i < correctedBitList.size(); i++) {
            resBitSet[i] = correctedBitList.get(i);
        }
        return resBitSet;
    }

    public boolean[] damageHammingCodeBitSet (boolean[] dataBitSet, int codedSequenceLength, boolean codeIsExtended,
                                              int damagedBitsNumber) {
        int dataLength = dataBitSet.length;
        int encodedSequenceLength = countHammingEncodedSequenceLength(codedSequenceLength, codeIsExtended);
        int curStartPos = 0;
        List<Integer> curDamageBitIndexes;
        while (curStartPos < dataLength) {
            if (curStartPos + encodedSequenceLength > dataLength) {
                break; // last code sequence with length smaller than given length should be ignored
            }
            curDamageBitIndexes = chooseRandomHammingIndexes(damagedBitsNumber, encodedSequenceLength, codeIsExtended);
            for (int curIndex : curDamageBitIndexes) {
                dataBitSet[curStartPos + curIndex] = !dataBitSet[curStartPos + curIndex];
                System.out.println("Bit number " + (curIndex) + " was damaged.");
            }
            curStartPos += encodedSequenceLength;
        }
        return dataBitSet;
    }

    public List<Integer> chooseRandomHammingIndexes (int indexNum, int encodedSequenceLength, boolean codeIsExtended) {
        Random random = new Random();
        List<Integer> indexes = new ArrayList<>();
        int i = 0;
        int curRandInd = 0;
        while (i < indexNum) {
            curRandInd = random.nextInt(encodedSequenceLength); // from 0 to codedSequenceLength - 1
            if (!indexes.contains(curRandInd)) {
                if (!codeIsExtended && curRandInd != 0 && (curRandInd + 1) % 2 != 0) { // check for preventing damaging parity bits
                    indexes.add(curRandInd);
                    i++;
                }
                if (codeIsExtended && curRandInd != 0 && curRandInd != 1 && curRandInd % 2 != 0) {
                    indexes.add(curRandInd);
                    i++;
                }
            }
        }
        return indexes;
    }

    public static int countHammingEncodedSequenceLength (int codedSequenceLength, boolean codeIsExtended) {
        int numOfParityBits = (int) (Math.ceil(MathUtility.calculateLog(codedSequenceLength, 2.0)));
        if (codedSequenceLength + numOfParityBits >= Math.pow(2, numOfParityBits)) { // if overall size of code sequence is more than number of next parity bit
            numOfParityBits += 1;
        }
        int curCodeSeqLength = codedSequenceLength + numOfParityBits;
        if (codeIsExtended) {
            ++curCodeSeqLength;
        }
        return curCodeSeqLength;
    }

    public boolean[] convertByteArrToBitSet(byte[] dataArr) {
        boolean[] resBitArray = new boolean[dataArr.length * 8];
        StringBuilder resBitsSB = new StringBuilder();
        for (int i = 0; i < dataArr.length; i++) {
            resBitsSB.append(String.format("%8s", Integer.toBinaryString(dataArr[i] & 0xFF)).replace(' ', '0'));
        }
        char[] resBitChars = resBitsSB.toString().toCharArray();
        for (int i = 0; i < resBitChars.length; i++) {
            resBitArray[i] = resBitChars[i] == '1' ? true : false;
        }
        return resBitArray;
    }

    public boolean[] convertStringToBitSet(String dataString) {
        char[] dataChars = dataString.toCharArray();
        boolean[] resBitArray = new boolean[dataChars.length * 16];
        StringBuilder resBitsSB = new StringBuilder();
        for (int i = 0; i < dataChars.length; i++) {
            resBitsSB.append(String.format("%16s", Integer.toBinaryString(dataChars[i] & 0xFFFF)).replace(' ', '0'));
        }
        dataChars = resBitsSB.toString().toCharArray();
        for (int i = 0; i < dataChars.length; i++) {
            resBitArray[i] = dataChars[i] == '1' ? true : false;
        }
        return resBitArray;
    }

    public static boolean[] retrieveHammingCodeFromFileByteArr (byte[] byteArr) {
        int byteDataLength = byteArr.length;
        int lastByteBitsLength = byteArr[byteDataLength - 1]; // last byte is in charge of number of significant bits in last byte
        String lastByte = String.format("%8s", Integer.toBinaryString(byteArr[byteDataLength - 2] & 0xFF)).
                replace(' ', '0'); // retrieving last not full byte
        lastByte = lastByte.substring(lastByteBitsLength);
        boolean[] resCodeArr = new boolean[(byteDataLength - 2) * 8 + lastByteBitsLength];
        StringBuilder resBitsSB = new StringBuilder();
        for (int i = 0; i < byteDataLength - 2; i++) {
            resBitsSB.append(String.format("%8s", Integer.toBinaryString(byteArr[i] & 0xFF)).replace(' ', '0'));
        }
        if (lastByteBitsLength != 0) {
            resBitsSB.append(lastByte);
        }
        char[] resBitChars = resBitsSB.toString().toCharArray();
        for (int i = 0; i < resBitChars.length; i++) {
            resCodeArr[i] = resBitChars[i] == '1' ? true : false;
        }
        return resCodeArr;
    }

    public byte[] convertBitSetToByteArr (boolean[] dataBitSet) {
        int bitSetLength = dataBitSet.length;
        int byteArrLen = bitSetLength % 8 != 0 ? bitSetLength / 8 + 1 : bitSetLength / 8;
        ++byteArrLen; // add byte for writing significant symbols number in last byte
        byte[] byteArr = new byte[byteArrLen];
        int formByte = -128; // we make right shift for 1 which sets up new 0 and 1 | 10000000
        int resByte = 0; // | 00000000
        int byteLimitCounter = 0;
        int byteNumberCounter = 0;
        for (int i = 0; i < bitSetLength; i++) {
            if (byteLimitCounter == 8) { // if we have formed one byte add it to array and return form and res Bytes in prev state
                byteArr[byteNumberCounter] = (byte) resByte;
                formByte = -128;
                resByte = 0;
                byteLimitCounter = 0;
                byteNumberCounter++;
            }
            if (dataBitSet[i]) { // if curBit == 1
                resByte = formByte | resByte; // saves all 1 and 0 and writes 1 to curBit
            } else {
                formByte = ~formByte;
                resByte = formByte & resByte; // saves all 1 and 0 and writes 0 to curBit
                formByte = ~formByte; // returns formByte in previous state
            }
            formByte = formByte & 0xff; // for making all bits besides last bite 1
            formByte = (byte) (formByte >>> 1);
            byteLimitCounter++;
        }

        byteArr[byteNumberCounter] = (byte) resByte; // write the last formed byte, not formed bits of it are 0
        byteArr[byteNumberCounter + 1] = (byte) (byteLimitCounter); // amount of bits responsible fo code in last byte
        return byteArr;
    }

    public byte[] convertBitSetToAliquotByteArr (boolean[] dataBitSet) {
        int bitSetLength = dataBitSet.length;
        int byteArrLen = bitSetLength / 8;
        byte[] byteArr = new byte[byteArrLen];
        int formByte = -128; // we make right shift for 1 which sets up new 0 and 1 | 10000000
        int resByte = 0; // | 00000000
        int byteLimitCounter = 0;
        int byteNumberCounter = 0;
        for (int i = 0; i < bitSetLength; i++) {
            if (byteLimitCounter == 8) { // if we have formed one byte add it to array and return form and res Bytes in prev state
                byteArr[byteNumberCounter] = (byte) resByte;
                formByte = -128;
                resByte = 0;
                byteLimitCounter = 0;
                byteNumberCounter++;
            }
            if (dataBitSet[i]) { // if curBit == 1
                resByte = formByte | resByte; // saves all 1 and 0 and writes 1 to curBit
            } else {
                formByte = ~formByte;
                resByte = formByte & resByte; // saves all 1 and 0 and writes 0 to curBit
                formByte = ~formByte; // returns formByte in previous state
            }
            formByte = formByte & 0xff; // for making all bits besides last bite 1
            formByte = (byte) (formByte >>> 1);
            byteLimitCounter++;
        }
        return byteArr;
    }
}
