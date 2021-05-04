package model.dataStructures.MessageInfoContainer;

import model.MessageInfo;

public interface ListMessageInfoContainer extends GenericMessageInfoContainer {

    MessageInfo get(int index);

    void insert(int index, MessageInfo messageInfo);

}
