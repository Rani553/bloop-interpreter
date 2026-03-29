import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final String source;
    private int pos;        // current character index
    private int line;       // current line number (1-based)

    public Tokenizer(String source) {
        this.source = source;
        this.pos    = 0;
        this.line   = 1;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            skipSpacesAndTabs();        // skip horizontal whitespace only

            if (pos >= source.length()) break;

            char c = source.charAt(pos);

            // ── Newline ─────────────────────────────────────────────────────
            if (c == '\n') {
                tokens.add(new Token(TokenType.NEWLINE, "\\n", line));
                line++;
                pos++;
                continue;
            }

            // ── Carriage return (Windows line endings) ───────────────────────
            if (c == '\r') {
                pos++;
                continue;
            }

            // ── Comments: # to end of line ───────────────────────────────────
            if (c == '#') {
                while (pos < source.length() && source.charAt(pos) != '\n') pos++;
                continue;
            }

            // ── String literals ──────────────────────────────────────────────
            if (c == '"') {
                tokens.add(readString());
                continue;
            }

            // ── Numbers ──────────────────────────────────────────────────────
            if (Character.isDigit(c)) {
                tokens.add(readNumber());
                continue;
            }

            // ── Identifiers and keywords ─────────────────────────────────────
            if (Character.isLetter(c) || c == '_') {
                tokens.add(readWord());
                continue;
            }

            // ── Operators and punctuation ────────────────────────────────────
            switch (c) {
                case '+': tokens.add(new Token(TokenType.PLUS,  "+", line)); pos++; break;
                case '-': tokens.add(new Token(TokenType.MINUS, "-", line)); pos++; break;
                case '*': tokens.add(new Token(TokenType.STAR,  "*", line)); pos++; break;
                case '/': tokens.add(new Token(TokenType.SLASH, "/", line)); pos++; break;
                case '>': tokens.add(new Token(TokenType.GREATER, ">", line)); pos++; break;
                case '<': tokens.add(new Token(TokenType.LESS,    "<", line)); pos++; break;
                case ':':
                    // the colon at the end of  "then:"  — just skip it
                    pos++;
                    break;
                case '=':
                    if (pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                        tokens.add(new Token(TokenType.EQUAL_EQUAL, "==", line));
                        pos += 2;
                    } else {
                        // bare '=' is not part of BLOOP syntax; skip
                        pos++;
                    }
                    break;
                default:
                    // Unknown character — skip silently
                    pos++;
                    break;
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /** Skip spaces and tabs (but NOT newlines). */
    private void skipSpacesAndTabs() {
        while (pos < source.length()) {
            char c = source.charAt(pos);
            if (c == ' ' || c == '\t') pos++;
            else break;
        }
    }

    /** Read a double-quoted string literal. */
    private Token readString() {
        int startLine = line;
        pos++; // skip opening "
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && source.charAt(pos) != '"') {
            sb.append(source.charAt(pos));
            pos++;
        }
        pos++; // skip closing "
        return new Token(TokenType.STRING, sb.toString(), startLine);
    }

    /** Read a numeric literal (integer or decimal). */
    private Token readNumber() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '.')) {
            sb.append(source.charAt(pos));
            pos++;
        }
        return new Token(TokenType.NUMBER, sb.toString(), startLine);
    }

    /**
     * Read an identifier or keyword.
     * BLOOP keywords: put, into, print, if, then, repeat, times
     */
    private Token readWord() {
        int startLine = line;
        StringBuilder sb = new StringBuilder();
        while (pos < source.length() && (Character.isLetterOrDigit(source.charAt(pos)) || source.charAt(pos) == '_')) {
            sb.append(source.charAt(pos));
            pos++;
        }
        String word = sb.toString();

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
}
