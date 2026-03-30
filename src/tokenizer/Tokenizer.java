import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String source;
    private int pos;
    private int line;

    public Tokenizer(String source) {
        this.source = source;
        this.pos = 0;
        this.line = 1;
    }
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < source.length()) {
            char c = source.charAt(pos);

            // Skip spaces and tabs
            if (c == ' ' || c == '\t') {
                pos++;
                continue;
            }

            // String literal — starts with "
            if (c == '"') {
                tokens.add(readString());
                continue;
            }

            // Number — starts with a digit
            if (Character.isDigit(c)) {
                tokens.add(readNumber());
                continue;
            }

            // Identifier or keyword — starts with a letter
            if (Character.isLetter(c) || c == '_') {
                tokens.add(readWord());
                continue;
            }
            pos++;
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }
    // method Read_words
    private Token readWord() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() &&
            (Character.isLetterOrDigit(source.charAt(pos)) 
            || source.charAt(pos) == '_')) {
            sb.append(source.charAt(pos));
            pos++;
        }
        String word = sb.toString();

    // Check if it's a keyword, otherwise it's an identifier
        switch (word) {
            case "put":    return new Token(TokenType.PUT,    word, startLine);
            case "into":   return new Token(TokenType.INTO,   word, startLine);
            case "print":  return new Token(TokenType.PRINT,  word, startLine);
            case "if":     return new Token(TokenType.IF,     word, startLine);
            case "then":   return new Token(TokenType.THEN,   word, startLine);
            case "repeat": return new Token(TokenType.REPEAT, word, startLine);
            case "times":  return new Token(TokenType.TIMES,  word, startLine);
            default:       return new Token(TokenType.IDENTIFIER, word, startLine);
        }
    }
    private Token readNumber() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();

        while (pos < source.length() &&
            (Character.isDigit(source.charAt(pos)) 
            || source.charAt(pos) == '.')) {
            sb.append(source.charAt(pos));
            pos++;
        }

        return new Token(TokenType.NUMBER, sb.toString(), startLine);
    }
    private Token readString() {
        int startLine = line;
        pos++; // skip the opening "

        StringBuilder sb = new StringBuilder();

        while (pos < source.length() && source.charAt(pos) != '"') {
            sb.append(source.charAt(pos));
            pos++;
        }
        pos++; // skip the closing "
        return new Token(TokenType.STRING, sb.toString(), startLine);
    }
    
}