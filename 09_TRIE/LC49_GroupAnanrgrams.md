class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        TrieNode root = new TrieNode();
        for(String str : strs)
            insertWord(root, str);
        
        List<List<String>> result = new ArrayList<>();
        dfs(root, result);

        return result;
        
    }

    private void dfs(TrieNode node, List<List<String>> result){
        if(node.words.size() > 0){
            result.add(node.words);
        }

        node.children.forEach((key, childNode) -> {
            dfs(childNode, result);
        });
    }

    private void insertWord(TrieNode root, String str){
        TrieNode node = root;
        char[] arr = str.toCharArray();
        Arrays.sort(arr);
        for(char ch : arr){
            if(!node.children.containsKey(ch)){
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }

        node.words.add(str);
    }
}

class TrieNode{
    Map<Character, TrieNode> children;
    List<String> words;

    TrieNode(){
        children = new HashMap<>();
        words = new ArrayList<>();

    }

}