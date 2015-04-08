package selector;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafer@gmail.com
 * @since 0.5.2
 */
public abstract class AbstractSelectable implements Selectable {

    protected abstract List<String> getSourceTexts();

    public Selectable css(String selector) {
        return $(selector);
    }

    public Selectable css(String selector, String attrName) {
        return $(selector, attrName);
    }

    protected Selectable select(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<String>();
        for (String string : strings) {
            String result = selector.select(string);
            if (result != null) {
                results.add(result);
            }
        }
        return new PlainText(results);
    }

    protected Selectable selectList(Selector selector, List<String> strings) {
        List<String> results = new ArrayList<String>();
        for (String string : strings) {
            List<String> result = selector.selectList(string);
            results.addAll(result);
        }
        return new PlainText(results);
    }

    public List<String> all() {
        return getSourceTexts();
    }

    public Selectable jsonPath(String jsonPath) {
        throw new UnsupportedOperationException();
    }

    public String get() {
        if (CollectionUtils.isNotEmpty(all())) {
            return all().get(0);
        } else {
            return null;
        }
    }

    public Selectable select(Selector selector) {
        return select(selector, getSourceTexts());
    }

    public Selectable selectList(Selector selector) {
        return selectList(selector, getSourceTexts());
    }

    public Selectable regex(String regex) {
        RegexSelector regexSelector = Selectors.regex(regex);
        return selectList(regexSelector, getSourceTexts());
    }

    public Selectable regex(String regex, int group) {
        RegexSelector regexSelector = Selectors.regex(regex, group);
        return selectList(regexSelector, getSourceTexts());
    }

    public Selectable replace(String regex, String replacement) {
        ReplaceSelector replaceSelector = new ReplaceSelector(regex,replacement);
        return select(replaceSelector, getSourceTexts());
    }

    public String getFirstSourceText() {
        if (getSourceTexts() != null && getSourceTexts().size() > 0) {
            return getSourceTexts().get(0);
        }
        return null;
    }

    @Override
    public String toString() {
        return get();
    }

    public boolean match() {
        return getSourceTexts() != null && getSourceTexts().size() > 0;
    }
}
