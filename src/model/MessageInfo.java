package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageInfo<T> {
    List<T> messageDataList;
    private int alphabetSize;
    private int messageLength;
    private final int charCapacity = 8; // default
    private Map<T, MessageElementContainer<T>> elementAmountMap;
    private double informationAmount;
    private double entropy;
    private double maxEntropy;
    private int absRedundancy;
    private double relRedundancy;

    public MessageInfo(List<T> messageDataList, int alphabetSize, int messageLength,
                       Map<T, MessageElementContainer<T>> elementAmountMap,
                       double informationAmount, double entropy, double maxEntropy,
                       int absRedundancy, double relRedundancy) {
        this.messageDataList = messageDataList;
        this.alphabetSize = alphabetSize;
        this.messageLength = messageLength;
        this.elementAmountMap = elementAmountMap;
        this.informationAmount = informationAmount;
        this.entropy = entropy;
        this.maxEntropy = maxEntropy;
        this.absRedundancy = absRedundancy;
        this.relRedundancy = relRedundancy;
    }

    public MessageInfo() {

    }

    public MessageInfo(List<T> messageDataList) {
        this.messageDataList = messageDataList;
        setMessageLength(messageDataList.size());
        fillElementAmountMap(messageDataList);
        setAlphabetSize(elementAmountMap.size());
        setEntropy(calculateEntropy());
        setMaxEntropy(calculateMaxEntropy());
        setInformationAmount(calculateInformationAmount());
        setAbsRedundancy(calculateAbsRedundancy());
        setRelRedundancy(calculateRelRedundancy());
    }

    public int getAlphabetSize() {
        return alphabetSize;
    }

    public void setAlphabetSize(int alphabetSize) {
        this.alphabetSize = alphabetSize;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public Map<T, MessageElementContainer<T>> getElementAmountMap() {
        return elementAmountMap;
    }

    public void setElementAmountMap(Map<T, MessageElementContainer<T>> elementAmountMap) {
        this.elementAmountMap = elementAmountMap;
    }

    public double getInformationAmount() {
        return informationAmount;
    }

    public void setInformationAmount(double informationAmount) {
        this.informationAmount = informationAmount;
    }

    public double getEntropy() {
        return entropy;
    }

    public void setEntropy(double entropy) {
        this.entropy = entropy;
    }

    public double getMaxEntropy() {
        return maxEntropy;
    }

    public void setMaxEntropy(double maxEntropy) {
        this.maxEntropy = maxEntropy;
    }

    public int getAbsRedundancy() {
        return absRedundancy;
    }

    public void setAbsRedundancy(int absRedundancy) {
        this.absRedundancy = absRedundancy;
    }

    public double getRelRedundancy() {
        return relRedundancy;
    }

    public void setRelRedundancy(double relRedundancy) {
        this.relRedundancy = relRedundancy;
    }

    public List<T> getMessageDataList() {
        return messageDataList;
    }

    public void setMessageDataList(List<T> fileDataList) {
        this.messageDataList = fileDataList;
    }

    public int getCharCapacity() {
        return charCapacity;
    }

    public void fillElementAmountMap(List<T> fileDataList) {
        elementAmountMap = new HashMap<>();
        for (T element : fileDataList) {
            MessageElementContainer<T> prevElement = elementAmountMap.get(element); // obtain element and its previous amount
            if (prevElement == null) { // if there's no amount of this element(this element is going to be added firstly)
                elementAmountMap.put(element, new MessageElementContainer<>(element, 1)); // put element with amount 1
            } else {
                elementAmountMap.put(element, // put element with previous amount + 1
                        new MessageElementContainer<>(element, prevElement.getElementNumber() + 1));
            }
        }
    }

    public double calculateEntropy() {
        double entropy = 0;
        double elProbability;
        for (MessageElementContainer<T> element : elementAmountMap.values()) {
            elProbability = (double) element.getElementNumber() / messageLength;
            entropy -= elProbability * calculateLog(elProbability, 2);
        }
        return entropy;
    }

    public double calculateMaxEntropy() {
        return calculateLog(alphabetSize, 2);
    }

    public double calculateInformationAmount() {
        return messageLength * calculateEntropy();
    }

    public double calculateLog(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    public int calculateAbsRedundancy() {
        return charCapacity - (int) calculateEntropy() - 1;
    }

    public double calculateRelRedundancy() {
        return (double) calculateAbsRedundancy() / charCapacity;
    }

    public List<MessageElementContainer<T>> getMapElementList() {

        List<MessageElementContainer<T>> mapElementList = new ArrayList<>();

        for (MessageElementContainer<T> messageEl : elementAmountMap.values()) {
            mapElementList.add(messageEl);
        }

        return mapElementList;
    }

    public void calculateAllElementsSFCodes() {

    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "fileDataList=" + messageDataList +
                ",\n alphabetSize=" + alphabetSize +
                ",\n messageLength=" + messageLength +
                ",\n informationAmount=" + informationAmount +
                ",\n entropy=" + entropy +
                ",\n maxEntropy=" + maxEntropy +
                ",\n absRedundancy=" + absRedundancy +
                ",\n relRedundancy=" + relRedundancy +
                '}';
    }
}
