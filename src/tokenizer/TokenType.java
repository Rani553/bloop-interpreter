public enum TokenType {
    // Keywords
    PUT,        // put
    INTO,       // into
    PRINT,      // print
    IF,         // if
    THEN,       // then
    REPEAT,     // repeat
    TIMES,      // times

    // Literals
    NUMBER,     // e.g. 10, 3.14
    STRING,     // e.g. "hello"
    IDENTIFIER, // variable names like x, result

    // Arithmetic operators
    PLUS,       // +
    MINUS,      // -
    STAR,       // *
    SLASH,      // /

    // Comparison operators
    GREATER,    // >
    LESS,       // <
    EQUAL_EQUAL,// ==

    // Structure
    NEWLINE,    // end of a line
    EOF         // end of file
}
