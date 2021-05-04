package model.dataStructures;

import model.MessageElementContainer;
import model.MessageInfo;
import model.comparator.MessageElementNumberComparator;
import model.dataStructures.SFAlgoTrieContainer.ListSFAlgoTrieContainer;
import utility.PropertyFileUtility;

import java.util.*;

public class ShannonFanoAlgoDataContainer<T> {

    private List<MessageElementContainer<T>> allElementsList;
    private List<T> message;
    private Map<T, MessageElementContainer<T>> messageElementMap;
    private BitSet messageBitSet;
    private int messageLength;
    private int SFCodeLength;
    private static final int ONE_CHAR_CAPACITY =
            Integer.valueOf((String) PropertyFileUtility.getPropertyFromFile("oneCharCapacity"));

    public ShannonFanoAlgoDataContainer(MessageInfo<T> messageInfo) {
        this.allElementsList = messageInfo.getMapElementList();
        this.allElementsList.sort(new MessageElementNumberComparator());
        this.message = messageInfo.getMessageDataList();
        this.messageLength = messageInfo.getMessageLength();
        this.SFCodeLength = 0;
        this.messageElementMap = new HashMap<>();
    }

    public ShannonFanoAlgoDataContainer() {

    }

    public BitSet executeShannonFanoEncoding() {
        executeOneDivision(0, allElementsList.size() - 1, 0);
        for (MessageElementContainer<T> curEl : allElementsList) {
            messageElementMap.put(curEl.getElement(), curEl); //add all elements with their codes to hashMap
        }
        messageBitSet = convertMessageToBitSet();
        return messageBitSet;
    }

    public static List executeShannonFanoDecoding(String bitCodeString, SFAlgoTrie trie)
            throws IllegalArgumentException{
        char[] bitChars = bitCodeString.toCharArray();
        boolean[] bitSet = new boolean[bitChars.length];
        List resList = new ArrayList();
        for (int i = 0; i < bitChars.length; i++) {
            if (bitChars[i] == '1'){
                bitSet[i] = true;
            } else if (bitChars[i] == '0') {
                bitSet[i] = false;
            } else {
                throw new IllegalArgumentException("Code should contain only 0 or 1 symbols");
            }
        }

        Object curElement = null;
        for (boolean curBit: bitSet) {
            curElement = trie.makeOneMoveInTrie(curBit);
            if (curElement != null) {
                resList.add(curElement);
                System.out.println(curElement);
            }
        }
        if (curElement == null) { // if last visited node is null then code is wrong cause we should reach
            // the last node in the end
            throw new IllegalArgumentException("The given code couldn't be decoded via given trie");
        }
        return resList;
    }

    public static byte[] executeShannonFanoDecoding(byte[] byteCodeArr, ListSFAlgoTrieContainer trieList) {
        int lastByteCodeBitsNumber = byteCodeArr[byteCodeArr.length - 2];
        int decodingTrieId = byteCodeArr[byteCodeArr.length - 1];
        SFAlgoTrie<Byte> trie = trieList.get(decodingTrieId);
        StringBuilder resCodeSB = new StringBuilder();
        List<Byte> resList = new ArrayList<>();
        for (int i = 0; i < byteCodeArr.length - 2; i++) {
            resCodeSB.append(String.format("%8s", Integer.toBinaryString(byteCodeArr[i] & 0xFF)).replace(' ', '0'));
        }
        String resCodeString = resCodeSB.substring(0, resCodeSB.length() - (8 - lastByteCodeBitsNumber));
        char[] bitChars = resCodeString.toCharArray();
        boolean[] bitSet = new boolean[bitChars.length];
        for (int i = 0; i < bitChars.length; i++) {
            if (bitChars[i] == '1'){
                bitSet[i] = true;
            } else if (bitChars[i] == '0') {
                bitSet[i] = false;
            } else {
                throw new IllegalArgumentException("Code should contain only 0 or 1 symbols");
            }
        }
        Object curElement = null;
        for (boolean curBit: bitSet) {
            curElement = trie.makeOneMoveInTrie(curBit);
            if (curElement != null) {
                resList.add((Byte)curElement);
            }
        }

        if (curElement == null) { // if last visited node is null then code is wrong cause we should reach
            // the last node in the end
            throw new IllegalArgumentException("The given code couldn't be decoded via given trie");
        }

        Byte[] resWrappedByteArr = resList.toArray(new Byte[resList.size()]);
        byte[] resByteArr = new byte[resList.size()];
        for (int i = 0; i < resWrappedByteArr.length; i++) {
            resByteArr[i] = resWrappedByteArr[i];
        }

        return resByteArr;
    }

    public byte[] convertBitSetToByteArray(BitSet messageBitSet, int SFCodeLength, int SFAlgoTrieId) {
        // number of bytes in array - numOfBits / 8 + 1 for remainder if it is not equal to 0
        int byteArrLen = SFCodeLength % 8 != 0 ? SFCodeLength / 8 + 1 : SFCodeLength / 8;
        byteArrLen += 2; // for labeling the end of code in last byte and for saving id of decoding tree
        byte[] byteArr = new byte[byteArrLen];
        int formByte = -128; // we make right shift for 1 which sets up new 0 and 1 | 10000000
        int resByte = 0; // | 00000000
        int byteLimitCounter = 0;
        int byteNumberCounter = 0;
        for (int i = 0; i < SFCodeLength; i++) {
            if (byteLimitCounter == 8) { // if we have formed one byte add it to array and return form and res Bytes in prev state
                byteArr[byteNumberCounter] = (byte) resByte;
                formByte = -128;
                resByte = 0;
                byteLimitCounter = 0;
                byteNumberCounter++;
            }
            if (messageBitSet.get(i)) { // if curBit == 1
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
        byteArr[byteNumberCounter + 2] = (byte) SFAlgoTrieId; // write Id of trie for decoding
        return byteArr;
    }

    public BitSet convertMessageToBitSet() {
        BitSet messageBitSet = new BitSet();
        int bitPosition = 0;
        MessageElementContainer<T> curElCont;
        int curSFCode;
        int curSFCodeLength;
        int curBit;
        int curDivResult;
        int curDivNum;
        for (T curEl : message) {
            curElCont = messageElementMap.get(curEl);
            curSFCode = curElCont.getElementSFCode();
            curSFCodeLength = curElCont.getElSFCodeLength();

            curDivResult = curSFCode;
            curDivNum = 0;
            while (curDivResult != 0) { // while we have bits, we divide
                curBit = curDivResult % 2;
                curDivResult /= 2;
                if (curBit == 1) messageBitSet.set(bitPosition, true);
                else messageBitSet.set(bitPosition, false);
                curDivNum++;
                bitPosition++;
            }

            for (int i = curDivNum; i < curSFCodeLength; i++) {
                messageBitSet.set(bitPosition, false);
                bitPosition++;
            }

        }

        SFCodeLength = bitPosition; // Length of SF message representation is equal to index of the last retrieved bit + 1

        return messageBitSet;
    }

    public void executeOneDivision(int startIndex, int endIndex, int curDivisionNum) {

        if (startIndex == endIndex) {
            return;
        }

        int selectionHalfNumberSum = 0;
        int totalSum = 0;
        int selectionHalfIndex = startIndex;
        int i;

        MessageElementContainer<T> tempEl;
        for (i = startIndex; i <= endIndex; i++) { // count total amount of elements in allElemList part we want to divide
            tempEl = allElementsList.get(i);
            selectionHalfNumberSum += tempEl.getElementNumber();
            tempEl.setElSFCodeLength(curDivisionNum + 1); // set code length on each recursive call when we add 1 or 0 to code
        }

        selectionHalfNumberSum /= 2;

        while (totalSum < selectionHalfNumberSum) {
            totalSum += allElementsList.get(selectionHalfIndex).getElementNumber();
            selectionHalfIndex++;
        }

        selectionHalfIndex--;

        for (i = selectionHalfIndex + 1; i <= endIndex; i++) { // write 1 to radix equal to number of division
            int prevElSFCode = allElementsList.get(i).getElementSFCode();
            allElementsList.get(i).setElementSFCode(prevElSFCode + (int) Math.pow(2, curDivisionNum));
        }
        executeOneDivision(startIndex, selectionHalfIndex, curDivisionNum + 1);
        executeOneDivision(selectionHalfIndex + 1, endIndex, curDivisionNum + 1);
    }

    public SFAlgoTrie<T> buildSFAlgoTrie() {
        return new SFAlgoTrie(allElementsList);
    }

    public double countAvCodeCombLength() {
        int allCodesLengthSum = 0;
        for (MessageElementContainer<T> curEl : allElementsList) {
            allCodesLengthSum += curEl.getElSFCodeLength();
        }
        return (double) allCodesLengthSum / allElementsList.size();
    }

    public double countCompressionRatio() {
        return ((double) SFCodeLength) / (messageLength * ONE_CHAR_CAPACITY) * 100;
    }

    public void printSFCode() {
        for (int i = 0; i < SFCodeLength; i++) {
            if (messageBitSet.get(i)) {
                System.out.print(1);
            } else {
                System.out.print(0);
            }
        }
        System.out.println();
    }

    public List<MessageElementContainer<T>> getAllElementsList() {
        return allElementsList;
    }

    public void setAllElementsList(List<MessageElementContainer<T>> allElementsList) {
        this.allElementsList = allElementsList;
    }

    public List<T> getMessage() {
        return message;
    }

    public void setMessage(List<T> message) {
        this.message = message;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public BitSet getMessageBitSet() {
        return messageBitSet;
    }

    public void setMessageBitSet(BitSet messageBitSet) {
        this.messageBitSet = messageBitSet;
    }

    public int getSFCodeLength() {
        return SFCodeLength;
    }

    public void setSFCodeLength(int SFCodeLength) {
        this.SFCodeLength = SFCodeLength;
    }
}
