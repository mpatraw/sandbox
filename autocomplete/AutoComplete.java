
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
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
	private String saveWord = null;
	private Node saveTop = null;
	private Node saveIter = null;

	public AutoComplete() {
	}

	public void loadWord(String word) {
		Node iter = root;
		
		for (int i = 0; i < word.length(); i++) {
			Node next = iter.letters.get(word.charAt(i));
			if (next == null) {
				next = new Node();
				next.parent = iter;
				iter.letters.put(word.charAt(i), next);
			}
			iter = next;
		}

		iter.isWord = true;
	}

	public void loadWordFile(String filename) throws IOException {
		FileInputStream fin = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(fin));

		for (String line = br.readLine(); line != null; line = br.readLine()) {
			loadWord(line);
		}

		br.close();
	}

	private void getWordsAt(Node iter, String run, ArrayList<String> list, int count) {
		if (list.size() >= count) {
			return;
		} else if (iter == null) {
			return;
		} else {
			for (Map.Entry<Character, Node> entry : iter.letters.entrySet()) {
				run += entry.getKey();
				getWordsAt(entry.getValue(), run, list, count);
				run = run.substring(0, run.length() - 1);
			}
			if (iter.isWord) {
				saveIter = iter;
				list.add(new String(run));
			}
		}
	}

	public String[] getWords(String start, int count) {
		ArrayList<String> list = new ArrayList<String>();
		String run = new String(start);

		if (saveWord != start) {
			saveWord = start;
			saveTop = root;
			saveIter = saveTop;

			for (int i = 0; i < start.length(); i++) {
				saveTop = saveTop.letters.get(start.charAt(i));
				if (saveTop == null) {
					return list.toArray(new String[list.size()]);
				}
			}
		}

		getWordsAt(saveTop, run, list, count);

		return (String[])list.toArray(new String[list.size()]);
	}

	class Node {
		public boolean isWord = false;
		public Node parent = null;
		public Map<Character, Node> letters = new HashMap<Character, Node>();

		public Node() {}
	}
}
