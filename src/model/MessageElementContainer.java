package model;

import java.util.Objects;

public class MessageElementContainer<T> {
    private T element;
    private int elementNumber;
    private int elementSFCode;
    private int elSFCodeLength;

    public MessageElementContainer(T element, int elementNumber) {
        this.element = element;
        this.elementNumber = elementNumber;
        this.elementSFCode = 0;
        this.elSFCodeLength = 0;
    }

    public MessageElementContainer() {

    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public int getElementNumber() {
        return elementNumber;
    }

    public void setElementNumber(int elementNumber) {
        this.elementNumber = elementNumber;
    }

    public int getElementSFCode() { return elementSFCode; }

    public void setElementSFCode(int elementSFCode) { this.elementSFCode = elementSFCode; }

    public int getElSFCodeLength() {
        return elSFCodeLength;
    }

    public void setElSFCodeLength(int elSFCodeLength) {
        this.elSFCodeLength = elSFCodeLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageElementContainer<T> that = (MessageElementContainer<T>) o;
        return Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element);
    }

    @Override
    public String toString() {
        return "MessageElement{" +
                "\nelement=" + element +
                "\n, elementNumber=" + elementNumber +
                "\n, SFCode=" + elementSFCode +
                "\n, SFCodeLength=" + elSFCodeLength +
                '}';
    }
}
