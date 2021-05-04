package model.dataStructures.SFAlgoTrieContainer;

import model.dataStructures.SFAlgoTrie;

import java.util.ArrayList;
import java.util.List;

public class ListSFAlgoTrieContainerImpl implements ListSFAlgoTrieContainer{

    private List<SFAlgoTrie> trieList;
    private int curAddIndex;
    private int size;

    public ListSFAlgoTrieContainerImpl () {
        this.trieList = new ArrayList();
        this.curAddIndex = 0;
        this.size = 0;
    }

    @Override
    public Object add(SFAlgoTrie element) {
        trieList.add(curAddIndex, element);
        int prevCurArrIndex = curAddIndex;
        curAddIndex++;
        if (curAddIndex == 128) { // if we have reached the end of list we start to add elements from beginning
            curAddIndex = 0;
            return curAddIndex;
        }
        size++;
        return prevCurArrIndex;
    }

    @Override
    public SFAlgoTrie pop() {
        return trieList.get(curAddIndex); // returns the last consistently added element
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void clear() {
        trieList.clear();
        size = 0;
        curAddIndex = 0;
    }

    @Override
    public SFAlgoTrie get(int index) {
        return trieList.get(index);
    }

    @Override
    public int add(int index, SFAlgoTrie element) {
        if (index > 127) {
            throw new IllegalArgumentException("The max index for adding new Trie is 127");
        }
        trieList.add(element);
        return index;
    }
}
