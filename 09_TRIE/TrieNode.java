package 09_TRIE;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    Map<Character, TrieNode> children;
    TrieNode next;
    boolean isEndOfWord;

    public TrieNode(){
        children = new HashMap<>();
        isEndOfWord = false;
    }
}
