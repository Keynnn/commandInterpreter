public class LexerException extends Exception {
	public LexerException(int line, int column) {
		super("Caractère non attendu à la ligne " + line + " colonne " + column + ".");
	}
}
