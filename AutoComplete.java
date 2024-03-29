import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

// With PQ of size 3 and storing all words on each node
// TC: O(nlog(k)) where k = 3 -> O(n) where n = number of words starting with the input prefix
class AutocompleteSystem {
    private class TrieNode {
        private TrieNode[] children;
        private List<String> words;

        public TrieNode() {
            // accounting for the 256 characters
            this.children = new TrieNode[256];
            this.words = new ArrayList<>();
        }
    }

    private TrieNode prefixTree;
    private StringBuilder inputString;
    private Map<String, Integer> frequencyMap;

    public AutocompleteSystem(String[] sentences, int[] times) {
        this.prefixTree = new TrieNode();
        this.inputString = new StringBuilder();
        this.frequencyMap = new HashMap<>();

        for (int i = 0; i < sentences.length; i++) {
            int val = frequencyMap.getOrDefault(sentences[i], 0);
            frequencyMap.put(sentences[i], times[i] + val);
        }

        for (String word : frequencyMap.keySet()) {
            insert(word);
        }
    }

    public void insert(String word) {
        TrieNode temp = prefixTree;

        for (char c : word.toCharArray()) {
            if (temp.children[c] == null) {
                temp.children[c] = new TrieNode();
            }
            temp = temp.children[c];
            temp.words.add(word);
        }
    }

    public List<String> getWords(StringBuilder word) {
        TrieNode temp = prefixTree;
        for (int i = 0; i < word.length(); i++) {
            char current = word.charAt(i);
            if (temp.children[current] == null) {
                return new ArrayList<>();
            }
            temp = temp.children[current];
        }
        return temp.words;
    }

    public List<String> input(char c) {
        if (c == '#') {
            String curr = inputString.toString();

            if (!frequencyMap.containsKey(curr)) {
                frequencyMap.put(curr, 0);
                insert(curr);
            }

            frequencyMap.put(curr, frequencyMap.get(curr) + 1);
            inputString.setLength(0);
            return new ArrayList<>();
        }

        inputString.append(c);

        Queue<String> pq = new PriorityQueue<>((a, b) -> {
            if (frequencyMap.get(a) == frequencyMap.get(b)) {
                return b.compareTo(a);
            }

            return frequencyMap.get(a) - frequencyMap.get(b);
        });

        for (String s : getWords(inputString)) {
            pq.add(s);
            if (pq.size() > 3) {
                pq.poll();
            }
        }

        List<String> res = new ArrayList<>();
        while (!pq.isEmpty()) {
            res.add(0, pq.poll());
        }

        return res;
    }
}

/**
 * Your AutocompleteSystem object will be instantiated and called as such:
 * AutocompleteSystem obj = new AutocompleteSystem(sentences, times);
 * List<String> param_1 = obj.input(c);
 */

// Hashmap only implementation
// TC: O(N * log(k)) where N = all elements in the dictionary and k is
// negligible
class AutocompleteSystem2 {

    private Map<String, Integer> frequencyMap;
    private StringBuilder inputString;

    public AutocompleteSystem2(String[] sentences, int[] times) {
        this.frequencyMap = new HashMap<>();
        this.inputString = new StringBuilder();

        for (int i = 0; i < sentences.length; i++) {
            int val = frequencyMap.getOrDefault(sentences[i], 0);
            frequencyMap.put(sentences[i], times[i] + val);
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            String curr = inputString.toString();

            if (!frequencyMap.containsKey(curr)) {
                frequencyMap.put(curr, 0);
            }

            frequencyMap.put(curr, frequencyMap.get(curr) + 1);
            inputString.setLength(0);
            return new ArrayList<>();
        }

        inputString.append(c);

        Queue<String> pq = new PriorityQueue<>((a, b) -> {
            if (frequencyMap.get(a) == frequencyMap.get(b)) {
                return b.compareTo(a);
            }

            return frequencyMap.get(a) - frequencyMap.get(b);
        });

        String curr = inputString.toString();
        for (String s : frequencyMap.keySet()) {
            if (s.startsWith(curr)) {
                pq.add(s);
            }
            if (pq.size() > 3) {
                pq.poll();
            }
        }

        List<String> res = new ArrayList<>();
        while (!pq.isEmpty()) {
            res.add(0, pq.poll());
        }

        return res;
    }
}

/**
 * Your AutocompleteSystem object will be instantiated and called as such:
 * AutocompleteSystem obj = new AutocompleteSystem(sentences, times);
 * List<String> param_1 = obj.input(c);
 */

// Trie with keeping only 3 max words per node
// TC: O(l) where N = all elements in the dictionary and k is
// negligible
class AutocompleteSystem3 {

    private class TrieNode {
        private TrieNode[] children;
        private List<String> words;

        public TrieNode() {
            this.children = new TrieNode[256];
            this.words = new ArrayList<>();
        }
    }

    TrieNode prefixTree;
    String inputString;
    Map<String, Integer> frequencyMap;

    public AutocompleteSystem3(String[] sentences, int[] times) {
        this.prefixTree = new TrieNode();
        this.inputString = "";
        this.frequencyMap = new HashMap<>();

        for (int i = 0; i < sentences.length; i++) {
            int val = frequencyMap.getOrDefault(sentences[i], 0);
            frequencyMap.put(sentences[i], val + times[i]);
        }

        for (String w : frequencyMap.keySet()) {
            insert(w);
        }
    }

    private void insert(String word) {
        TrieNode node = prefixTree;

        for (char c : word.toCharArray()) {
            if (node.children[c] == null) {
                node.children[c] = new TrieNode();
            }
            node = node.children[c];
            List<String> prefixWords = node.words;
            if (!prefixWords.contains(word)) {
                prefixWords.add(word);
            }

            Collections.sort(prefixWords, (a, b) -> {
                if (frequencyMap.get(a) == frequencyMap.get(b)) {
                    return a.compareTo(b);
                }

                return frequencyMap.get(b) - frequencyMap.get(a);
            });

            if (prefixWords.size() > 3) {
                prefixWords.remove(prefixWords.size() - 1);
            }
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            int val = frequencyMap.getOrDefault(inputString, 0);
            frequencyMap.put(inputString, val + 1);
            insert(inputString);
            inputString = "";
            return new ArrayList<>();
        }

        inputString += "" + c;
        // find the node
        TrieNode node = prefixTree;
        for (char ch : inputString.toCharArray()) {
            if (node.children[ch] == null) {
                return new ArrayList<>();
            }
            node = node.children[ch];
        }

        return node.words;
    }
}

/**
 * Your AutocompleteSystem object will be instantiated and called as such:
 * AutocompleteSystem obj = new AutocompleteSystem(sentences, times);
 * List<String> param_1 = obj.input(c);
 */