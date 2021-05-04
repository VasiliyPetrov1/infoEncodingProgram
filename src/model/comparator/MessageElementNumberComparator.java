package model.comparator;

import model.MessageElementContainer;

import java.util.Comparator;

public class MessageElementNumberComparator implements Comparator<MessageElementContainer> {

    @Override
    public int compare(MessageElementContainer o1, MessageElementContainer o2) {
        return o2.getElementNumber() - o1.getElementNumber();
    }

}
