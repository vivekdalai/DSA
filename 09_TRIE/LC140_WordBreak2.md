class Solution {
public List<String> wordBreak(String s, List<String> wordDict) {
TrieDS trie = new TrieDS(wordDict);
// trie.printTrie();
Map<Integer, List<String>> dp = new HashMap<>();

        return dfs(0, s, trie.root, dp);

    }

    private List<String> dfs(int start, String s, TrieNode root, Map<Integer, List<String>> dp){
        if(dp.containsKey(start))
            return dp.get(start);
        
        List<String> result = new ArrayList<>();
        if(start == s.length()){
            result.add("");
            return result;
        }

        TrieNode node = root;
        for(int end = start; end < s.length(); end++){
            char ch = s.charAt(end);
            if(!node.children.containsKey(ch)){
                break;
            }
            node = node.children.get(ch);
            if(node.isEndOfWord){
                List<String> suffixes = dfs(end + 1, s, root, dp);
                for(String suffix : suffixes){
                    if(suffix.isEmpty())
                        result.add(s.substring(start, end + 1));
                    else
                        result.add(s.substring(start, end + 1) + " " + suffix);
                }
            }
        }

        dp.put(start, result);

        return result;
    }
}

class TrieDS {
TrieNode root;
public TrieDS(List<String> words){
root = new TrieNode('X');
words.forEach((word) -> {
insertWord(word);
});
}
public void insertWord(String word){
TrieNode node = root;
for(char ch : word.toCharArray()){
if(!node.children.containsKey(ch)){
node.children.put(ch, new TrieNode(ch));
}
node = node.children.get(ch);
}
node.isEndOfWord = true;
}

    public boolean checkWord(String word){
        TrieNode node = root;
        for(char ch : word.toCharArray()){
            if(!node.children.containsKey(ch))
                return false;
            
            node = node.children.get(ch);
        }
        if(node.isEndOfWord == true)
            return true;
        
        return false;
    }

    public void printTrie(){
        Deque<TrieNode> queue = new ArrayDeque<>();
        queue.add(root);
        while(!queue.isEmpty()){
            int size = queue.size();
            for(int i = 0; i < size; i++){
                TrieNode curr = queue.pollFirst();
                System.out.print(curr.ch + " ");
                curr.children.forEach((key, value) -> queue.add(value));
            }
            System.out.println();
        }
    }
}

class TrieNode {

    char ch;
    Map<Character, TrieNode> children; //stores next possible word from this node
    boolean isEndOfWord;


    public TrieNode(char ch){
        this.ch = ch;
        children = new HashMap<>();
        isEndOfWord = false;
    }

}