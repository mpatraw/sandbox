
public class AutoCompleteMain {
	public static void main(String[] args) throws Exception {
		AutoComplete ac = new AutoComplete();
		ac.loadWordFile("/usr/share/dict/words");


		for (String word : ac.getWords("hell", 100)) {
			System.out.println(word);
		}
	}
}
