# IF5020-HW3

Abstract `BNFRule`
- Variable
  - Left (As reference)
  - Right -> List of List of BNFSymbol
    - `"terminal"`
    - `"<non terminal>"`
    - `"{}"` Repetition
    - `"[]"` Optional
    Example `["package", " ", "<QualifiedIdentifier>"]`
- Method

Abstract Class `BNFSymbol` (Abstract Factory)
- Class `TerminalSymbol`
- Class `NonTerminalSymbol`
- Class `RepetitionSymbol`
  - Variable
    - List of `BNFSymbol`
- Class `OptionalSymbol`
  - Variable
    - List of `BNFSymbol`

Class `FileParser`
