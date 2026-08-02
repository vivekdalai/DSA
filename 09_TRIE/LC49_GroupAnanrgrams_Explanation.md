# Trie Notes

## 49 - Group Anagrams Using Trie

------------------------------------------------------------------------

## 1. Problem Understanding

Given an array of strings `strs`, group all anagrams together.

Two words are anagrams if they contain the same characters with the same frequency, but possibly in different order.

Example:

```text
Input:  ["eat","tea","tan","ate","nat","bat"]
Output: [["eat","tea","ate"],["tan","nat"],["bat"]]
```

The order of groups and the order of words inside each group can be different.

------------------------------------------------------------------------

## 2. First Intuition

Anagrams become identical after sorting their characters.

```text
eat -> aet
tea -> aet
ate -> aet

tan -> ant
nat -> ant

bat -> abt
```

So the sorted word can be treated as the unique signature for an anagram group.

In a normal hash map solution, this sorted signature is used as the map key.

In this trie solution, the sorted signature is inserted character by character into a trie. All original words that end at the same trie node belong to the same anagram group.

------------------------------------------------------------------------

## 3. Why Trie Works Here

A trie stores character paths.

For each word:

1. Sort the characters.
2. Insert the sorted characters into the trie.
3. At the final node, store the original unsorted word.

If two words are anagrams, their sorted version is the same, so they follow the exact same trie path and end at the same node.

That final node keeps the list of all words in that anagram group.

------------------------------------------------------------------------

## 4. Trie Node Structure

The solution uses this node:

```java
class TrieNode {
    Map<Character, TrieNode> children;
    List<String> words;

    TrieNode() {
        children = new HashMap<>();
        words = new ArrayList<>();
    }
}
```

Important fields:

- `children`: stores the next character path in the sorted word.
- `words`: stores original strings that finish at this node.

Unlike a normal dictionary trie, this node does not need `isEndOfWord`.

Reason:

- If `words.size() > 0`, then some sorted word ends at this node.
- That is enough to identify an anagram group.

------------------------------------------------------------------------

## 5. Insert Operation

Current method:

```java
private void insertWord(TrieNode root, String str) {
    TrieNode node = root;
    char[] arr = str.toCharArray();
    Arrays.sort(arr);

    for (char ch : arr) {
        if (!node.children.containsKey(ch)) {
            node.children.put(ch, new TrieNode());
        }
        node = node.children.get(ch);
    }

    node.words.add(str);
}
```

Methodology:

- Start from `root`.
- Convert the word to a character array.
- Sort the character array.
- Walk through the sorted characters.
- Create missing trie nodes when needed.
- After the last character, add the original word to `node.words`.

Example:

```text
insert "eat"
sorted = "aet"
path = root -> a -> e -> t
store "eat" at node t
```

Then:

```text
insert "tea"
sorted = "aet"
path = root -> a -> e -> t
store "tea" at same node t
```

So `"eat"` and `"tea"` are grouped together.

------------------------------------------------------------------------

## 6. DFS Collection

Current method:

```java
private void dfs(TrieNode node, List<List<String>> result) {
    if (node.words.size() > 0) {
        result.add(node.words);
    }

    node.children.forEach((key, childNode) -> {
        dfs(childNode, result);
    });
}
```

After inserting all words, every anagram group is stored at some trie node.

DFS visits all trie nodes. Whenever it finds a node with a non-empty `words` list, that list is added to the answer.

------------------------------------------------------------------------

## 7. Full Dry Run

Input:

```text
["eat","tea","tan","ate","nat","bat"]
```

Insert each word:

```text
eat -> sorted aet -> store at path a -> e -> t
tea -> sorted aet -> same path, store with eat
tan -> sorted ant -> store at path a -> n -> t
ate -> sorted aet -> same path, store with eat and tea
nat -> sorted ant -> same path, store with tan
bat -> sorted abt -> store at path a -> b -> t
```

Trie group nodes:

```text
path a -> e -> t : [eat, tea, ate]
path a -> n -> t : [tan, nat]
path a -> b -> t : [bat]
```

DFS result:

```text
[[bat], [eat, tea, ate], [tan, nat]]
```

Any group order is accepted.

------------------------------------------------------------------------

## 8. Clean Interview Version

```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        TrieNode root = new TrieNode();

        for (String str : strs) {
            insertWord(root, str);
        }

        List<List<String>> result = new ArrayList<>();
        dfs(root, result);
        return result;
    }

    private void insertWord(TrieNode root, String str) {
        TrieNode node = root;
        char[] chars = str.toCharArray();
        Arrays.sort(chars);

        for (char ch : chars) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }

        node.words.add(str);
    }

    private void dfs(TrieNode node, List<List<String>> result) {
        if (!node.words.isEmpty()) {
            result.add(node.words);
        }

        for (TrieNode child : node.children.values()) {
            dfs(child, result);
        }
    }
}

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    List<String> words = new ArrayList<>();
}
```

------------------------------------------------------------------------

## 9. Complexity

Let:

- `n` = number of strings
- `k` = maximum length of a string

Complexities:

- Sorting each word: `O(k log k)`
- Inserting one sorted word into trie: `O(k)`
- Total insertion time: `O(n * k log k)`
- DFS traversal: `O(total trie nodes)`
- Space: `O(total characters stored in trie + number of input strings)`

Overall:

```text
Time:  O(n * k log k)
Space: O(n * k)
```

------------------------------------------------------------------------

## 10. Pattern Recognition and Revision Notes

- For anagram grouping, first think of a canonical representation.
- Sorting creates the same key for all anagrams.
- A hash map is the most common solution for this problem.
- A trie can also group by storing the sorted key as a path.
- The final trie node stores original words because the sorted path only identifies the group.
- If input is only lowercase English letters, a 26-frequency count key can avoid sorting.

------------------------------------------------------------------------

## End of Notes
