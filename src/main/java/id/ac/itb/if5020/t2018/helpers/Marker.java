package id.ac.itb.if5020.t2018.helpers;

import java.util.Objects;

public class Marker {
    public final int lineNumber;
    public final int colNumber;
    public final int shiftNumber;
    public final String token;
    public final int currentTokenIndex;

    public Marker(int _lineNumber, int _colNumber, int _shiftNumber, String _token, int _currentTokenIndex) {
        lineNumber = _lineNumber;
        colNumber = _colNumber;
        shiftNumber = _shiftNumber;
        token = _token;
        currentTokenIndex = _currentTokenIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            Integer.valueOf(lineNumber),
            Integer.valueOf(colNumber),
            Integer.valueOf(shiftNumber),
            token
        );
    }
}