
package id.ac.itb.if5020.t2018.components;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import id.ac.itb.if5020.t2018.helpers.RuleTokenization;

public class BNFSingleRule {
    public final String rule;

    public final List<BNFSymbol> tokenRule;

    public BNFSingleRule(String _rule) throws ParseException {
        rule = _rule;
        tokenRule = tokenize(_rule);

        if (tokenRule.isEmpty()) {
            throw new InvalidParameterException("Token rules cannot be empty");
        }
    }

    private static List<BNFSymbol> tokenize(String _rule) throws ParseException {
        String token;
        RuleTokenization tokenization = new RuleTokenization(_rule);
        List<BNFSymbol> _tokenRule = new ArrayList<BNFSymbol>();

        while (tokenization.hasNext()) {
            token = tokenization.readNextToken();
            _tokenRule.add(BNFSymbol.create(token));
        }

        return _tokenRule;
    }

    /**
     * Caching first list.
     */
    private Set<String> firstlist;

    public Set<String> first() {
        if (firstlist == null) {
            firstlist = new HashSet<>();
            // TODO Find list first from rules?
        }

        if (firstlist.isEmpty()) {
            return null;
        }
        return firstlist;
    }
}