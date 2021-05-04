package model.dataStructures.SFAlgoTrieContainer;

import model.dataStructures.SFAlgoTrie;

public interface ListSFAlgoTrieContainer extends GenericSFAlgoTrieContainer {

    int add(int index, SFAlgoTrie element);

    SFAlgoTrie get(int index);

}
