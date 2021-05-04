package model.dataStructures.SFAlgoTrieContainer;

import model.dataStructures.SFAlgoTrie;

public interface GenericSFAlgoTrieContainer {

    Object add(SFAlgoTrie element); // returns id of current added element

    SFAlgoTrie pop(); // returns last added Trie

    int getSize(); // returns number of Tries from 0 to 127

    void clear(); // deletes all elements(Tries) in container

}
