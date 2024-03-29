package id.ac.itb.if5020.t2018.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import id.ac.itb.if5020.t2018.JavaEngine;
import id.ac.itb.if5020.t2018.components.RuleNotMatchException;

public class TextFileParser implements TextFileParserInterface {

    private FileReader filereader;

    private final BufferedReader bufferReader;

    private int lineNumber = 0;

    private int currentCol = 0;

    private boolean _isEndOfFile = false;

    public TextFileParser(String _filename) throws FileNotFoundException, IOException {
        filereader = new FileReader(_filename);
        bufferReader = new BufferedReader(filereader);
        bufferReader.mark(0);
    }

    public TextFileParser(File file) throws FileNotFoundException, IOException {
        filereader = new FileReader(file);
        bufferReader = new BufferedReader(filereader);
        bufferReader.mark(0);
    }

    public TextFileParser(FileReader reader) throws IOException {
        this(new BufferedReader(reader));
    }

    public TextFileParser(BufferedReader br) throws IOException {
        bufferReader = br;
        bufferReader.mark(0);
    }

    public int getCurrentLineNumber() {
        return lineNumber;
    }

    public int getCurrentCol() {
        return currentCol - shiftedSymbol;
    }

    @Override
    public void reset() throws IOException {
        bufferReader.reset();
    }

    private String currentToken;

    @Override
    public String getCurrentToken() throws ParseException {
        return currentToken;
    }

    private String currentLine;

    @Override
    public String getCurrentLineString() {
        return currentLine;
    }

    @Override
    public String readNextToken() throws ParseException {
        for (;;) {
            if (currentLine == null) {
                try {
                    currentLine = bufferReader.readLine();
                    lineNumber++;
                    currentCol = 0;
                    currentToken = null;

                    if (currentLine == null) {
                        _isEndOfFile = true;
                        return null;
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }

            updateCurrentToken();
            if (currentToken != null && currentToken != "") {
                break;
            }
        }

        return currentToken;
    }

    private int shiftedSymbol;

    private boolean inCommentTag = false;

    private void updateCurrentToken() throws ParseException {
        shiftedSymbol = 0;
        currentTokenIndex = 0;
        currentToken = "";

        for (;currentCol < currentLine.length();) {
            // Skip spacing
            if (currentLine.charAt(currentCol) <= ' ') {
                currentCol++;
                continue;
            }
            if (currentCol < currentLine.length() && handleTokenization()) {
                break;
            }
        }

        if (currentCol == currentLine.length()) {
            currentLine = null;
        }
    }

    /**
     * @return true when there is token, false otherwise.
     */
    private boolean handleTokenization() throws ParseException {
        // Separate reading possibility
        // - Start reading multiline or tagged comment
        if (inCommentTag || currentLine.substring(currentCol).matches("^/\\*.*")) {
            if (!inCommentTag) {
                currentCol += 2;
                inCommentTag = true;
            }
            while (currentCol < currentLine.length() && !currentLine.substring(currentCol).matches("^\\*/.*")) {
                currentCol++;
            }
            if (currentCol < currentLine.length() && currentLine.substring(currentCol).matches("^\\*/.*")) {
                currentCol += 2;
                inCommentTag = false;
            } else {
                return false;
            }
        }
        // - Start reading single line comment
        if (currentLine.substring(currentCol).matches("^//.*")) {
            // skip until end of line
            currentCol = currentLine.length();
            return false;
        }
        // - Start reading string value
        if (currentLine.substring(currentCol).matches("^\".*")) {
            shiftedSymbol = 1;
            while (currentCol + shiftedSymbol < currentLine.length() && currentLine.charAt(currentCol + shiftedSymbol) != '"') {
                if (currentLine.charAt(currentCol + shiftedSymbol) == '\\') {
                    shiftedSymbol++;
                }
                shiftedSymbol++;
            }

            if (currentCol + shiftedSymbol >= currentLine.length()) {
                throw new ParseException("Failed parsing string literal \"" + currentLine + "\". String has no end?", currentCol);
            }
            shiftedSymbol++;
            return updateAfterTokenization();
        }
        // - Start reading terminal with symbol count more than 1
        if (currentLine.substring(currentCol).matches("^=.*")) {
            if (currentLine.substring(currentCol).matches("^={2}[^=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            if (currentLine.substring(currentCol).matches("^={3}[^=].*")) {
                shiftedSymbol = 3;
                return updateAfterTokenization();
            }

            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^\\..*")) {
            if (currentLine.substring(currentCol).matches("^\\.{3}[^\\.].*")) {
                shiftedSymbol = 3;
                return updateAfterTokenization();
            } else if (currentLine.substring(currentCol).matches("^\\.[0-9]+(_[0-9]+)*([eE][+-]?[0-9_]+)?[fFdD]?.*")) {
				shiftedSymbol = 0;
				while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^[0-9_\\.eE+-fFdD]+")) {
					shiftedSymbol++;
					if (currentCol + shiftedSymbol >= currentLine.length()) {
						break;
					}
				}
				return updateAfterTokenization();
			}

            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        if (currentLine.substring(currentCol).matches("^\\[\\].*")) {
            shiftedSymbol = 2;
            return updateAfterTokenization();
        }

        if (currentLine.substring(currentCol).matches("^@.*")) {
            if (currentLine.substring(currentCol).matches("^@interface[^a-zA-Z0-9].*")) {
                shiftedSymbol = 10;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }

		// handle &, &&, and &=
		if (currentLine.substring(currentCol).matches("^&.*")) {
            if (currentLine.substring(currentCol).matches("^&{2}[^&].*")
				|| currentLine.substring(currentCol).matches("^&=[^&=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }

		// handle |, ||, |=
		if (currentLine.substring(currentCol).matches("^\\|.*")) {
			if (currentLine.substring(currentCol).matches("^\\|{2}[^\\|].*")
				|| currentLine.substring(currentCol).matches("^\\|=[^\\|=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
		}

		// handle ! and !=
		if (currentLine.substring(currentCol).matches("^!.*")) {
			if (currentLine.substring(currentCol).matches("^!=[^!=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
		}

		// handle +, ++, and +=
		if (currentLine.substring(currentCol).matches("^\\+.*")) {
            if (currentLine.substring(currentCol).matches("^\\+{2}[^\\+].*")
				|| currentLine.substring(currentCol).matches("^\\+=[^\\+=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }

		// handle -, --, and -=
		if (currentLine.substring(currentCol).matches("^\\-.*")) {
			if (currentLine.substring(currentCol).matches("^\\-{2}[^\\-].*")
				|| currentLine.substring(currentCol).matches("^\\-=[^\\-=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
		}

		// handle *, and *=
		if (currentLine.substring(currentCol).matches("^\\*.*")) {
			if (currentLine.substring(currentCol).matches("^\\*=[^\\*=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
		}

		// handle /, and /=
		if (currentLine.substring(currentCol).matches("^/.*")) {
			if (currentLine.substring(currentCol).matches("^/=[^/=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
		}

		// handle ^, and ^=
		if (currentLine.substring(currentCol).matches("^\\^.*")) {
			if (currentLine.substring(currentCol).matches("^\\^=[^\\^=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
		}

		// handle %, and %=
		if (currentLine.substring(currentCol).matches("^%.*")) {
			if (currentLine.substring(currentCol).matches("^%=[^%=].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
		}

		// handle <, <<, <=, <<=, and <>
		if (currentLine.substring(currentCol).matches("^<.*")) {
            if (currentLine.substring(currentCol).matches("^<{2}[^<].*")
				|| currentLine.substring(currentCol).matches("^<=[^<=].*")
				/*|| currentLine.substring(currentCol).matches("^<>[^<>].*")*/) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
			if (currentLine.substring(currentCol).matches("^<{2}=[^<=].*")) {
                shiftedSymbol = 3;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }

		// handle >, >>, >>>, >=, >>=, and >>>=
		if (currentLine.substring(currentCol).matches("^>.*")) {
            if (currentLine.substring(currentCol).matches("^>=[^>=].*")
				|| currentLine.substring(currentCol).matches("^>{2}[^>].*")) {
                shiftedSymbol = 2;
                return updateAfterTokenization();
            }
			if (currentLine.substring(currentCol).matches("^>{3}[^>].*")
				|| currentLine.substring(currentCol).matches("^>{2}=[^>=].*")) {
                shiftedSymbol = 3;
                return updateAfterTokenization();
            }
			if (currentLine.substring(currentCol).matches("^>{3}=[^>=].*")) {
                shiftedSymbol = 4;
                return updateAfterTokenization();
            }
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }

        if (currentLine.substring(currentCol).matches("^[,;:{}()$~`\\?\\\\\\[\\]].*")) {
            shiftedSymbol = 1;
            return updateAfterTokenization();
        }
        // - Start reading identifier
        if (currentLine.substring(currentCol).matches("^[a-zA-Z_].*")) {
            shiftedSymbol = 0;
            while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^[a-zA-Z0-9_]+")) {
                shiftedSymbol++;
                if (currentCol + shiftedSymbol >= currentLine.length()) {
                    break;
                }
            }
            return updateAfterTokenization();
        }
        // - start reading integer or float
        if (currentLine.substring(currentCol).matches("^[0-9].*")) {
            shiftedSymbol = 0;

            // Decimal Integer Zero Without Suffix
			if (currentLine.substring(currentCol).matches("^0([^0-9xXbBlL]|\\b).*")) {
				shiftedSymbol = 1;
            }

			// Decimal Integer Zero With Suffix
			if (currentLine.substring(currentCol).matches("^0[lL]([^0-9xXbB]|\\b).*")) {
				shiftedSymbol = 2;
            }

			// Decimal Integer Non Zero
			if (currentLine.substring(currentCol).matches("^[1-9].*")) {
				shiftedSymbol = 0;
				while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^[1-9][0-9_lL]*")) {
					shiftedSymbol++;
					if (currentCol + shiftedSymbol >= currentLine.length()) {
						break;
					}
				}
            }

			// Hex Integer
			if (currentLine.substring(currentCol).matches("^0[xX][^\\.]*")) {
				shiftedSymbol = 1;
				while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^0[xX][0-9A-Fa-f_lL]*")) {
					shiftedSymbol++;
					if (currentCol + shiftedSymbol >= currentLine.length()) {
						break;
					}
				}
            }

			// Octal Integer
			if (currentLine.substring(currentCol).matches("^0[0-7].*")) {
				shiftedSymbol = 1;
				while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^0[0-7][0-7_lL]*")) {
					shiftedSymbol++;
					if (currentCol + shiftedSymbol >= currentLine.length()) {
						break;
					}
				}
            }

			// Binary Integer
			if (currentLine.substring(currentCol).matches("^0[bB].*")) {
				shiftedSymbol = 1;
				while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^0[bB][01_lL]*")) {
					shiftedSymbol++;
					if (currentCol + shiftedSymbol >= currentLine.length()) {
						break;
					}
				}
            }

			// Decimal Floating
			if (currentLine.substring(currentCol).matches("^[0-9_]+\\.[0-9_]*([eE][+-]?[0-9_]+)?[fFdD]?.*")
				|| currentLine.substring(currentCol).matches("^[0-9_]+[eE][+-]?[0-9_]+[fFdD]?.*")
				|| currentLine.substring(currentCol).matches("^[0-9_]+[fFdD].*")) {
				while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^[0-9_\\.eE+\\-fFdD]+")) {
					shiftedSymbol++;
					if (currentCol + shiftedSymbol >= currentLine.length()) {
						break;
					}
				}
			}

			// Hex Floating
			if (currentLine.substring(currentCol).matches("^0[xX][0-9A-Fa-f_]+[\\.]?[pP][+-]?[0-9_]+[fFdD]?.*")
				|| currentLine.substring(currentCol).matches("^0[xX][0-9A-Fa-f_]*\\.[0-9A-Fa-f_]+[pP][+-]?[0-9_]+[fFdD]?.*")) {
				while (currentLine.substring(currentCol, currentCol + shiftedSymbol + 1).matches("^[0-9A-Fa-f_\\.pP+\\-dDxX]+")) {
					shiftedSymbol++;
					if (currentCol + shiftedSymbol >= currentLine.length()) {
						break;
					}
				}
			}

            return updateAfterTokenization();
        }

		// - Start reading character
		if (currentLine.substring(currentCol).matches("^'.*")) {
            if (currentLine.charAt(currentCol + 1) == '\\') {
				shiftedSymbol = 4;
			} else {
				shiftedSymbol = 3;
			}

			if (currentCol + shiftedSymbol > currentLine.length()) {
                throw new ParseException("Failed parsing character literal \"" + currentLine + "\". Character has no end?", currentCol);
            }

            return updateAfterTokenization();
        }

        return false;
    }

    private boolean updateAfterTokenization() {
        currentToken = currentLine.substring(currentCol, currentCol + shiftedSymbol);
        currentCol += shiftedSymbol;
        return true;
    }

    public void close() throws IOException {
        bufferReader.close();
        if (filereader != null) {
            filereader.close();
        }
    }

    private int currentTokenIndex = 0;

    @Override
    public char getCurrentTokenChar() throws ParseException {
        if (currentTokenIndex >= currentToken.length()) {
            readNextToken();
            return 0;
        }
        return currentToken.charAt(currentTokenIndex);
    }

    @Override
    public char readCurrentTokenChar() throws ParseException {
        if (currentTokenIndex >= currentToken.length()) {
            return 0;
        }
        char retval = currentToken.charAt(currentTokenIndex++);
        if (currentTokenIndex >= currentToken.length()) {
            readNextToken();
        }
        return retval;
    }

    @Override
    public boolean isEndOfFile() {
        return _isEndOfFile;
    }

    @Override
    public Marker getMarker() {
        return new Marker(lineNumber, currentCol, shiftedSymbol, currentToken, currentTokenIndex);
    }

    @Override
    public void resetToMarker(Marker marker) {
        if (JavaEngine.throwNonLL1) {
            throw new RuntimeException("This parser is not parsing with LL(1) rule.");
        } else {
            JavaEngine.LOGGER.warning("This parser is not parsing with LL(1) rule.");
        }

        if (marker.lineNumber != lineNumber) {
            throw new RuntimeException("Cannot reset no another line");
        }
        currentCol = marker.colNumber;
        shiftedSymbol = marker.shiftNumber;
        currentTokenIndex = marker.currentTokenIndex;
        currentToken = String.valueOf(marker.token);
    }

    private RuleNotMatchException errorException;

    private Marker errorMarker;

    @Override
    public void markError(RuleNotMatchException _exception) {
        errorException = _exception;
        if (errorMarker != null) {
            errorMarker = null;
        }

        errorMarker = new Marker(lineNumber, currentCol, shiftedSymbol, currentToken, currentTokenIndex);
    }

    @Override
    public void markError() {
        if (errorMarker != null) {
            errorMarker = null;
        }

        errorMarker = new Marker(lineNumber, currentCol, shiftedSymbol, currentToken, currentTokenIndex);
    }

    @Override
    public RuleNotMatchException getLastRuleException() {
        return errorException;
    }

    @Override
    public Marker getLatestError() {
        return errorMarker;
    }
}