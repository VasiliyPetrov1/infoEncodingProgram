package model.dataStructures.MessageInfoContainer;

import model.MessageInfo;
import model.dataStructures.MessageInfoContainer.ListMessageInfoContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListMessageInfoContainerImpl implements ListMessageInfoContainer {

    private List<MessageInfo> messageInfoList = new ArrayList<>();

    @Override
    public Collection<MessageInfo> getContainer() {
        return messageInfoList;
    }

    @Override
    public MessageInfo pop() {
        return messageInfoList.get(getSize() - 1);
    }

    @Override
    public void add(MessageInfo messageInfo) {
        messageInfoList.add(messageInfo);
    }

    @Override
    public MessageInfo get(int index) {
        return messageInfoList.get(index);
    }

    @Override
    public void insert(int index, MessageInfo messageInfo) {
        messageInfoList.add(index, messageInfo);
    }

    @Override
    public int getSize() {
        return messageInfoList.size();
    }

}
