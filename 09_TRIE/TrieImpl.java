package 09_TRIE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TrieImpl {
    private static TrieNode root;

    public static void createTrie(String[] words){
        root = new TrieNode();
        for(String word: words)
            insertWord(word);
    }

    public static void insertWord(String word){
        TrieNode node = root;
        for(char ch : word.toCharArray()){
           if(!node.children.containsKey(ch)){
               node.children.put(ch, new TrieNode());
           }
           node = node.children.get(ch);
        }

        node.isEndOfWord = true;
    }

    public static void searchWord(String word){
        System.out.println("Search : " + word);
        TrieNode node = root;
        for(char ch : word.toCharArray()){
            if(node.children.containsKey(ch)){
                System.out.println(ch + " : ");
                node = node.children.get(ch);
            } else {
                System.out.println("Not Found");
                return;
            }
        }
        if(node.isEndOfWord == true)
            System.out.println("Found");
        else
            System.out.println("DELETED");
    }

    public static boolean deleteWord(TrieNode node, String word, int index){
        if(index == word.length()){
            if(!node.isEndOfWord)
                return false;

            node.isEndOfWord = false;
            return node.children.isEmpty();
        }
        char ch = word.charAt(index);
        if(!node.children.containsKey(ch))
            return false;

        boolean shouldDeleteChild = deleteWord(node.children.get(ch), word, index + 1);
        if(shouldDeleteChild)
            node.children.remove(ch);

        return !node.isEndOfWord && node.children.isEmpty();

    }
    public static List<String> prefix(String word) {
        /**
         * Return a list of all words in the trie that start with the given prefix.
         */
        TrieNode node = root;
        for(char ch : word.toCharArray()){
            if(!node.children.containsKey(ch)){
                return new ArrayList<String>();
            }
            node = node.children.get(ch);
        }
        List<String> result = new ArrayList<>();
        getAllWords(node, word, result);

        //recursively find all isEndWord = true node
        return result;
    }

    private static void getAllWords(TrieNode node, String word, List<String> result){
        if(node.isEndOfWord)
            result.add(word);

        Set<Character> keySet = node.children.keySet();
        keySet.forEach(System.out::println);

        node.children.forEach((ch, childNode) -> {
            getAllWords(childNode, word + ch, result);
        });
    }

    public static void main(String[] args) {
        String[] words = {"APPLE", "APE", "BALL", "BALLON","CAT"};
        createTrie(words);
        searchWord("APPLE");
        deleteWord(root, "APPLE", 0);
        searchWord("APPLE");
        System.out.println(prefix("BA"));
    }
}


/**
 * APP
 * prefix(APP)
 * node = root
 * root, root.children.contains('A')
 * node = A
 * node.children.contains('P')
 * node = P(1)
 * node.children.contains('P')
 * node = P(2)
 *
 *
 *
 */