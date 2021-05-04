package model.dataStructures;

import model.MessageElementContainer;

import java.util.Arrays;
import java.util.List;

public class SFAlgoTrie<T> {

    List<MessageElementContainer<T>> allElementsList;
    TrieNode<T> root;
    TrieNode<T> curBitNode; // Node which represents way to current code bit in Trie

    public SFAlgoTrie(List<MessageElementContainer<T>> allElementsList) {
        this.allElementsList = allElementsList;
        this.root = new TrieNode<>(null, null);
        for (MessageElementContainer<T> curMEl : allElementsList) {
            System.out.println(convertMElToTrieNode(curMEl));
            insert(convertMElToTrieNode(curMEl));
        }
        this.curBitNode = root;// begin from root
    }

    public SFAlgoTrie() {

    }

    public void insert(TrieNode<T> trieNode) {
        boolean[] key = trieNode.getKey();
        T value = trieNode.getValue();

        TrieNode<T> curNode;
        if (this.root == null) {
            this.root = new TrieNode<>(null, null);
        }
        curNode = root;
        for (int i = 0; i < key.length; i++) {
            if (key[i]) {
                if (curNode.getRight() == null) {
                    curNode.setRight(new TrieNode<>(null, Arrays.copyOfRange(key, 0, i + 1)));
                }
                curNode = curNode.getRight();
            } else {
                if (curNode.getLeft() == null) {
                    curNode.setLeft(new TrieNode<>(null, Arrays.copyOfRange(key, 0, i + 1)));
                }
                curNode = curNode.getLeft();
            }
        }
        curNode.setValue(value);
    }

    public T makeOneMoveInTrie(boolean curBit) throws IllegalArgumentException{
        if (curBit) {
            this.curBitNode = this.curBitNode.getRight();
        } else {
            this.curBitNode = this.curBitNode.getLeft();
        }
        if (curBitNode == null) { // lack of right way means that trie was built wrong or isn't intended for given code
            throw new IllegalArgumentException("Trie isn't intended for that code or was built wrong");
        }
        T valueToRetrieve = this.curBitNode.getValue();
        if (this.curBitNode.getLeft() == null && this.curBitNode.getRight() == null) { // we have reached the leaf
            if (valueToRetrieve != null) { // we have found coded message element
                this.curBitNode = root; // we return to root
            } else { // the trie was built wrong cause all leafs should be coded elements
                throw new IllegalArgumentException("Trie isn't intended for that code or was built wrong");
            }
        }
        return valueToRetrieve;
    }

    public TrieNode<T> convertMElToTrieNode(MessageElementContainer<T> mesElCont) {
        int curSFCode = mesElCont.getElementSFCode();
        int curSFCodeLength = mesElCont.getElSFCodeLength();

        TrieNode<T> trieNode;
        T value = mesElCont.getElement();
        boolean[] key = new boolean[curSFCodeLength];

        int curDivResult = curSFCode;
        int curDivNum = 0;
        int curBit;
        while (curDivResult != 0) { // while we have bits, we divide
            curBit = curDivResult % 2;
            curDivResult /= 2;
            if (curBit == 1) key[curDivNum] = true;
            else key[curDivNum] = false;
            curDivNum++;
        }

        for (int i = curDivNum; i < curSFCodeLength; i++) {
            key[i] = false;
        }

        return new TrieNode<>(value, key);

    }

    public static class TrieNode<T> {
        private boolean[] key;
        private T value;
        private TrieNode<T> left;
        private TrieNode<T> right;

        public TrieNode(T value, boolean[] key) {
            this.value = value;
            this.key = key;
        }

        public TrieNode() {

        }

        public boolean[] getKey() {
            return key;
        }

        public void setKey(boolean[] key) {
            this.key = key;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public TrieNode<T> getLeft() {
            return left;
        }

        public void setLeft(TrieNode<T> left) {
            this.left = left;
        }

        public TrieNode<T> getRight() {
            return right;
        }

        public void setRight(TrieNode<T> right) {
            this.right = right;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("TrieNode:\n" +
                    "value: " + value +
                    "\nkey: ");

            for (boolean curBit : key) {
                if (curBit) sb.append(1);
                else sb.append(0);
            }
            sb.append(".\n");

            return sb.toString();
        }
    }

}
