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

## How to Compile
```
mvn package
```

## How to run
```
java -jar target/ll_checker-1.0.0.jar --filename <filename>
```

Run with example
```
java -jar target/ll_checker-1.0.0.jar --filename example.txt
```

## How to run with verbose log mode
```
java -jar target/ll_checker-1.0.0.jar --filename <filename> -debug -loglevel 2
```

Run with example
```
java -jar target/ll_checker-1.0.0.jar --filename example.txt -debug -loglevel 2
```