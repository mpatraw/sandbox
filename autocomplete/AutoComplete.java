
import java.lang.StringBuilder;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

/**
 * AutoComplete -- Returns a list of words starting with a string
 *
 * 
 *
 */
public class AutoComplete {

	private Node root = new Node();
	private String savedWord = null;
	private Stack<Node> saves = new Stack<Node>();

	public AutoComplete() {
	}

	/**
	 * Loads a single word into the tree.
	 */
	public void loadWord(String word) {
		Node iter = root;
		
		for (int i = 0; i < word.length(); i++) {
			boolean found = false;
			for (Node entry : iter.children) {
				if (entry.letter == word.charAt(i)) {
					iter = entry;
					found = true;
					break;
				}
			}

			if (!found) {
				Node next = new Node();
				next.letter = word.charAt(i);
				next.parent = iter;
				iter.children.add(next);
				iter = next;
			}
		}

		iter.isWord = true;
	}

	/**
	 * Loads a file by name. The file should have a list of words
	 * separated by a newline.
	 */
	public void loadWordFile(String filename) throws IOException {
		FileInputStream fin = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));

		for (String line = br.readLine(); line != null; line = br.readLine()) {
			loadWord(line);
		}

		br.close();
	}

	/**
	 * Returns a string at a given node by traversing up the
	 * tree collecting letters.
	 */
	private String stringAt(Node node) {
		StringBuilder sb = new StringBuilder();
		while (node != root) {
			sb.append(node.letter);
			node = node.parent;
		}
		return sb.reverse().toString();
	}

	/**
	 * Returns a list of words that begin with some characters up
	 * to a given count. Will save the traversal location so subsequent
	 * calls return the next words.
	 */
	public String[] getCompleteWords(String beginWith, int count) {
		if (saves.size() == 0 || !savedWord.equals(beginWith)) {
			savedWord = new String(beginWith);
			Node top = root;
			for (int i = 0; i < beginWith.length(); i++) {
				boolean found = false;
				for (Node entry : top.children) {
					if (entry.letter == beginWith.charAt(i)) {
						top = entry;
						found = true;
						break;
					}
				}

				if (!found) {
					return new String[0];
				}
			}

			saves.push(top);
		}

		ArrayList<String> list = new ArrayList<String>();

		while (saves.size() != 0) {
			Node top = saves.pop();

			for (Node entry : top.children) {
				saves.push(entry);
			}

			if (top.isWord) {
				list.add(stringAt(top));
			}

			if (list.size() >= count) {
				break;
			}
		}

		return (String[])list.toArray(new String[list.size()]);
	}

	/**
	 * Clears the state of the traversal, starting over from the
	 * beginning.
	 */
	public void clearState() {
		savedWord = null;
		saves.clear();
	}

	/**
	 * Empties the tree of all the words.
	 */
	public void clearWords() {
		root = null;
		clearState();
	}

	/**
	 * A node in the tree. Each node has a letter and N children.
	 */
	class Node {
		public char letter = '\0';
		public boolean isWord = false;
		public Node parent = null;
		public ArrayList<Node> children = new ArrayList<Node>();

		public Node() {}
	}
}
