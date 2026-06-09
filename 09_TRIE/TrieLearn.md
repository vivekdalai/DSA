# Trie Notes

## Trie / Prefix Tree

**Generated on:** 2026-06-09 IST

------------------------------------------------------------------------

## 1. What is a Trie?

A Trie, also called a Prefix Tree, is a tree-like data structure used to store strings.

Each node represents one character, and a path from the root to some node represents a prefix or a complete word.

Example words:

```text
APPLE, APE, BALL, BALLON, CAT
```

The trie shares common prefixes:

```text
root
+-- A
|   +-- P
|       +-- E     -> APE ends here
|       +-- P
|           +-- L
|               +-- E -> APPLE ends here
+-- B
|   +-- A
|       +-- L
|           +-- L -> BALL ends here
|               +-- O
|                   +-- N -> BALLON ends here
+-- C
    +-- A
        +-- T -> CAT ends here
```

------------------------------------------------------------------------

## 2. Why Use a Trie?

Trie is useful when many words share prefixes.

Common use cases:

- autocomplete suggestions
- dictionary word search
- prefix matching
- spell checker
- word games
- IP routing / binary trie variations

For normal search in a list of words, we may need to scan many words. In a trie, search depends mainly on the length of the word.

------------------------------------------------------------------------

## 3. Trie Node Structure

In this folder, `TrieNode.java` has:

```java
class TrieNode {
    Map<Character, TrieNode> children;
    TrieNode next;
    boolean isEndOfWord;
}
```

Important fields:

- `children`: stores the next possible characters from the current node.
- `isEndOfWord`: tells whether a complete word ends at this node.

For example, if the trie contains `APE` and `APPLE`, the node for second `P` is only a prefix node for `APPLE`, but the node for `E` in `APE` has `isEndOfWord = true`.

`next` is currently not needed for this trie implementation because child links are already stored inside the `children` map.

------------------------------------------------------------------------

## 4. First Intuition

Think of a trie like walking character by character.

For word `APPLE`:

```text
root -> A -> P -> P -> L -> E
```

When inserting:

- if the character path already exists, reuse it
- if the character path does not exist, create a new node
- after the last character, mark `isEndOfWord = true`

When searching:

- if every character path exists, the prefix exists
- the word exists only if the last node has `isEndOfWord = true`

This distinction is important because `APP` may be a prefix of `APPLE`, but that does not mean `APP` is stored as a word.

------------------------------------------------------------------------

## 5. Insert Operation

Current method:

```java
public static void insertWord(String word) {
    TrieNode node = root;

    for (char ch : word.toCharArray()) {
        if (!node.children.containsKey(ch)) {
            node.children.put(ch, new TrieNode());
        }
        node = node.children.get(ch);
    }

    node.isEndOfWord = true;
}
```

Methodology:

- Start from `root`.
- Read one character at a time.
- If the character is missing in `children`, create a new `TrieNode`.
- Move to the child node.
- After processing all characters, mark the last node as a word-ending node.

Dry run for `APE`:

```text
root
root.children does not have A -> create A
A.children does not have P -> create P
P.children does not have E -> create E
mark E.isEndOfWord = true
```

------------------------------------------------------------------------

## 6. Search Operation

Current method:

```java
public static void searchWord(String word) {
    TrieNode node = root;

    for (char ch : word.toCharArray()) {
        if (node.children.containsKey(ch)) {
            node = node.children.get(ch);
        } else {
            System.out.println("Not Found");
            return;
        }
    }

    if (node.isEndOfWord)
        System.out.println("Found");
    else
        System.out.println("DELETED");
}
```

Methodology:

- Start from `root`.
- Move character by character.
- If any character path is missing, the word is not present.
- If all character paths exist, check `isEndOfWord`.

Possible results:

- `Found`: complete word exists.
- `Not Found`: some character path does not exist.
- `DELETED`: character path exists, but the final node is not marked as a complete word.

Example:

If only `APPLE` exists:

```text
search("APPLE") -> Found
search("APP")   -> DELETED / not a complete word
search("APX")   -> Not Found
```

------------------------------------------------------------------------

## 7. Delete Operation

Current method:

```java
public static boolean deleteWord(TrieNode node, String word, int index) {
    if (index == word.length()) {
        if (!node.isEndOfWord)
            return false;

        node.isEndOfWord = false;
        return node.children.isEmpty();
    }

    char ch = word.charAt(index);
    if (!node.children.containsKey(ch))
        return false;

    boolean shouldDeleteChild = deleteWord(node.children.get(ch), word, index + 1);

    if (shouldDeleteChild)
        node.children.remove(ch);

    return !node.isEndOfWord && node.children.isEmpty();
}
```

Deletion in a trie has two jobs:

- unmark the last character node as a word-ending node
- remove extra nodes only if they are not shared with another word

Why not delete blindly?

If the trie contains both `BALL` and `BALLON`, deleting `BALL` should not delete the nodes used by `BALLON`.

Dry run for deleting `APPLE` when `APE` also exists:

```text
APPLE path: A -> P -> P -> L -> E
APE path:   A -> P -> E
```

The nodes after the second `P` can be removed if no other word uses them, but the shared `A -> P` path must remain because `APE` still needs it.

Return value meaning:

- `true`: current node became useless and can be removed by its parent.
- `false`: current node must stay because it is a complete word or has children.

------------------------------------------------------------------------

## 8. Prefix Search / Autocomplete

Current method:

```java
public static List<String> prefix(String word) {
    TrieNode node = root;

    for (char ch : word.toCharArray()) {
        if (!node.children.containsKey(ch)) {
            return new ArrayList<String>();
        }
        node = node.children.get(ch);
    }

    List<String> result = new ArrayList<>();
    getAllWords(node, word, result);
    return result;
}
```

Methodology:

- First reach the node where the prefix ends.
- From that node, run DFS.
- Whenever `isEndOfWord = true`, add the current formed word to the answer.

Helper method:

```java
private static void getAllWords(TrieNode node, String word, List<String> result) {
    if (node.isEndOfWord)
        result.add(word);

    node.children.forEach((ch, childNode) -> {
        getAllWords(childNode, word + ch, result);
    });
}
```

Example:

```text
words = APPLE, APE, BALL, BALLON, CAT
prefix("BA") -> [BALL, BALLON]
prefix("AP") -> [APE, APPLE]
prefix("DO") -> []
```

------------------------------------------------------------------------

## 9. Clean Interview Version

```java
import java.util.*;

class Trie {
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord;
    }

    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode node = root;

        for (char ch : word.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);
        }

        node.isEndOfWord = true;
    }

    public boolean search(String word) {
        TrieNode node = findNode(word);
        return node != null && node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        return findNode(prefix) != null;
    }

    private TrieNode findNode(String text) {
        TrieNode node = root;

        for (char ch : text.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return null;
            }
            node = node.children.get(ch);
        }

        return node;
    }
}
```

------------------------------------------------------------------------

## 10. Complexity

Let:

- `L` = length of the word
- `P` = length of the prefix
- `K` = number of characters visited while collecting all matching prefix words

Complexities:

- Insert: `O(L)`
- Search word: `O(L)`
- Check prefix exists: `O(P)`
- List all words with prefix: `O(P + K)`
- Space: `O(total characters stored)` in the trie

Using `HashMap<Character, TrieNode>` keeps the implementation flexible for different characters. If the input is only lowercase English letters, an array of size `26` can also be used.

------------------------------------------------------------------------

## 11. Pattern Recognition and Revision Notes

- Use Trie when the problem talks about prefixes.
- If the task asks for autocomplete, prefix count, dictionary search, or starts-with, think Trie.
- `isEndOfWord` is the key difference between a prefix and a complete stored word.
- Deletion must preserve shared prefixes.
- Prefix search usually has two steps: reach prefix node, then DFS from there.
- For lowercase `a-z`, array children are faster. For generic characters, `HashMap` is easier.

------------------------------------------------------------------------

## End of Notes
