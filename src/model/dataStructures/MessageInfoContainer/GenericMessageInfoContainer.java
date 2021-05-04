package model.dataStructures.MessageInfoContainer;

import model.MessageInfo;

import java.util.Collection;

public interface GenericMessageInfoContainer {

    Collection<MessageInfo> getContainer();

    MessageInfo pop();

    void add(MessageInfo messageInfo);

    default int getSize() {
        return 0;
    }

}
