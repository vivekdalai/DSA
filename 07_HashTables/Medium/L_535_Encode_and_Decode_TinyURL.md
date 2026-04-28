# Hash Table Notes

## 535 - Encode and Decode TinyURL

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/encode-and-decode-tinyurl/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Note: This is a companion problem to the System Design problem: Design TinyURL.

TinyURL is a URL shortening service where you enter a URL such as `https://leetcode.com/problems/design-tinyurl` and it returns a short URL such as `http://tinyurl.com/4e9iAk`. Design a class to encode a URL and decode a tiny URL.

There is no restriction on how your encode/decode algorithm should work. You just need to ensure that a URL can be encoded to a tiny URL and the tiny URL can be decoded to the original URL.

Implement the `Solution` class:

- `Solution()` Initializes the object of the system.

- `String encode(String longUrl)` Returns a tiny URL for the given `longUrl`.

- `String decode(String shortUrl)` Returns the original long URL for the given `shortUrl`. It is guaranteed that the given `shortUrl` was encoded by the same object.

**Example 1:**

```text
Input: url = "https://leetcode.com/problems/design-tinyurl"
Output: "https://leetcode.com/problems/design-tinyurl"

Explanation:
Solution obj = new Solution();
string tiny = obj.encode(url); // returns the encoded tiny url.
string ans = obj.decode(tiny); // returns the original url after decoding it.
```

**Constraints:**

- `1 <= url.length <= 104`

- `url` is guranteed to be a valid URL.

**Additional revision examples**



**Revision Example 1**

```text
Input: url = "https://leetcode.com/problems/design-tinyurl"
Output: "http://tinyurl.com/<6-char-key>" then decoding returns the original URL
```

**Revision Example 2**

```text
Input: encode two different URLs
Output: two different short URLs, each mapped back to its own original URL
```

------------------------------------------------------------------------

## 2. First Intuition

A short URL only needs to be a key. The real URL can live in memory, and decoding just looks the key back up.

------------------------------------------------------------------------

## 3. Random Key to URL Map

- The file stores `shortKey -> longUrl` in a map.
- Encoding generates a random six-character key from letters and digits.
- If a generated key already exists, it retries until it finds an unused key.
- Decoding extracts the last six characters from the short URL and returns the stored original URL.

------------------------------------------------------------------------

## 4. Short Dry Run

Encode `https://leetcode.com/problems/design-tinyurl`: suppose the key is `a1B2c3`; the method stores `a1B2c3 -> longUrl` and returns `http://tinyurl.com/a1B2c3`. Decoding that short URL extracts `a1B2c3` and returns the original.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
private Map<String, String> map = new HashMap<>();
private static final String PREFIX = "http://tinyurl.com/";
private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
private Random random = new Random();

public String encode(String longUrl) {
    String key = generateKey();
    while (map.containsKey(key)) {
        key = generateKey();
    }
    map.put(key, longUrl);
    return PREFIX + key;
}

public String decode(String shortUrl) {
    String key = shortUrl.substring(shortUrl.length() - 6);
    return map.get(key);
}

private String generateKey() {
    StringBuilder key = new StringBuilder();
    for (int i = 0; i < 6; i++) {
        key.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
    }
    return key.toString();
}
```

------------------------------------------------------------------------

## 6. Complexity

- Average time: `O(1)` for encode/decode, ignoring rare collision retries.
- Space: `O(n)` for stored URL mappings.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Design problems can be judged by behavior, not a single fixed answer.
- The object instance must preserve state between `encode` and `decode`.

------------------------------------------------------------------------

## End of Notes
