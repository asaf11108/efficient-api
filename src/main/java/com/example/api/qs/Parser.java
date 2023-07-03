package com.example.api.qs;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.api.qs.Utils.*;

public class Parser {

    private static class Defaults {
        public static final boolean allowDots = false;
        public static final boolean allowPrototypes = false;
        public static final int arrayLimit = 20;
        public static final String delimiter = "&";
        public static final int depth = 5;
        public static final int parameterLimit = 1000;
        public static final boolean plainObjects = false;
        public static final boolean strictNullHandling = false;
        public static final Decoder decoder = Utils::decode;
    }

    private interface Decoder {
        String decode(String encodedString);
    }

    public static class Options {
        public boolean ignoreQueryPrefix = false;
        public String delimiter = Defaults.delimiter;
        public int depth = Defaults.depth;
        public int arrayLimit = Defaults.arrayLimit;
        public boolean parseArrays = true;
        public Decoder decoder = Defaults.decoder;
        public boolean allowDots = Defaults.allowDots;
        public boolean plainObjects = Defaults.plainObjects;
        public boolean allowPrototypes = Defaults.allowPrototypes;
        public int parameterLimit = Defaults.parameterLimit;
        public boolean strictNullHandling = Defaults.strictNullHandling;
    }

    private static Map<String, Object> parseValues(String str, Options options) {
        Map<String, Object> obj = new LinkedHashMap<>();
        String cleanStr = options.ignoreQueryPrefix ? str.replaceFirst("^\\?", "") : str;
        int limit = options.parameterLimit == Integer.MAX_VALUE ? -1 : options.parameterLimit;
        String[] parts = cleanStr.split(Pattern.quote(options.delimiter), limit);

        for (String part : parts) {
            int bracketEqualsPos = part.indexOf("]=");
            int pos = bracketEqualsPos == -1 ? part.indexOf('=') : bracketEqualsPos + 1;

            String key, val;
            if (pos == -1) {
                key = options.decoder.decode(part);
                val = options.strictNullHandling ? null : "";
            } else {
                key = options.decoder.decode(part.substring(0, pos));
                val = options.decoder.decode(part.substring(pos + 1));
            }

            if (obj.containsKey(key)) {
                Object existingValue = obj.get(key);
                List<String> valueList;
                if (existingValue instanceof List) {
                    valueList = (List<String>) existingValue;
                } else {
                    valueList = new ArrayList<>();
                    valueList.add((String) existingValue);
                    obj.put(key, valueList);
                }
                valueList.add(val);
            } else {
                obj.put(key, val);
            }
        }

        return obj;
    }

    private static Object parseObject(List<String> chain, String val, Options options) {
        Object leaf = val;

        for (int i = chain.size() - 1; i >= 0; --i) {
            Object obj;
            String root = chain.get(i);

            if (root.equals("[]") && options.parseArrays) {
                List<String> list = new ArrayList<>();
                list.add((String) leaf);
                obj = list;
            } else {
                if (options.plainObjects) {
                    obj = new LinkedHashMap<>();
                } else {
                    obj = new HashMap<>();
                }

                String cleanRoot = root.charAt(0) == '[' && root.charAt(root.length() - 1) == ']'
                        ? root.substring(1, root.length() - 1)
                        : root;
                try {
                    int index = Integer.parseInt(cleanRoot);
                    if (!options.parseArrays && cleanRoot.equals("")) {
                        obj = new LinkedHashMap<>();
                        ((Map<Integer, Object>) obj).put(0, leaf);
                    } else if (!Double.isNaN(index) && !root.equals(cleanRoot) && Integer.toString(index).equals(cleanRoot)
                            && index >= 0 && (options.parseArrays && index <= options.arrayLimit)) {
                        List<Object> list = new ArrayList<>(Collections.nCopies(index + 1, null));
                        list.set(index, leaf);
                        obj = list;
                    } else if (!cleanRoot.equals("__proto__")) {
                        obj = new LinkedHashMap<>();
                        ((Map<String, Object>) obj).put(cleanRoot, leaf);
                    }
                } catch (NumberFormatException ignored) {
                    obj = new LinkedHashMap<>();
                    ((Map<String, Object>) obj).put(cleanRoot, leaf);
                }
            }

            leaf = obj;
        }

        return leaf;
    }

    private static Object parseKeys(String givenKey, String val, Options options) {
        if (givenKey == null || givenKey.isEmpty()) {
            return null;
        }

        // Transform dot notation to bracket notation
        String key = options.allowDots ? givenKey.replaceAll("\\.([^.\\[]+)", "[$1]") : givenKey;

        // The regex chunks
        Pattern brackets = Pattern.compile("(\\[[^\\[\\]]*])");
        Pattern child = Pattern.compile("(\\[[^\\[\\]]*])");

        // Get the parent
        Matcher segmentMatcher = brackets.matcher(key);
        int segmentStart = segmentMatcher.find() ? segmentMatcher.start() : -1;
        String parent = segmentStart != -1 ? key.substring(0, segmentStart) : key;

        // Stash the parent if it exists
        List<String> keys = new ArrayList<>();
        if (parent != null && !parent.isEmpty()) {
            // If we aren't using plain objects, optionally prefix keys
            // that would overwrite object prototype properties
            if (!options.plainObjects && false) {
                if (!options.allowPrototypes) {
                    return null;
                }
            }
            keys.add(parent);
        }

        // Loop through children appending to the array until we hit depth
        Matcher childMatcher = child.matcher(key);
        int i = 0;
        while (childMatcher.find() && i < options.depth) {
            i += 1;
            String segment = childMatcher.group(1);
            if (!options.plainObjects && false/* && Object.prototype.containsKey(segment.substring(1, segment.length() - 1))*/) {
                if (!options.allowPrototypes) {
                    return null;
                }
            }
            keys.add(segment);
        }

        // If there's a remainder, just add whatever is left
        // if (segmentMatcher.find()) {
        //     keys.add("[" + key.substring(segmentMatcher.start()) + "]");
        // }

        return parseObject(keys, val, options);
    }

    public static Map<String, Object> parse(String str, Options opts) throws Exception {
        Options options = opts != null ? opts : new Options();

        if (options.decoder != null && !(options.decoder instanceof Decoder)) {
            throw new Exception("Decoder has to be a function.");
        }

        options.ignoreQueryPrefix = options.ignoreQueryPrefix == true;
        options.delimiter = options.delimiter != null && !options.delimiter.isEmpty()
                ? options.delimiter
                : Defaults.delimiter;
        options.depth = options.depth >= 0 ? options.depth : Defaults.depth;
        options.arrayLimit = options.arrayLimit >= 0 ? options.arrayLimit : Defaults.arrayLimit;
        options.parseArrays = options.parseArrays != false;
        options.allowDots = options.allowDots == true;
        options.plainObjects = options.plainObjects == true;
        options.allowPrototypes = options.allowPrototypes == true;
        options.parameterLimit = options.parameterLimit >= 0 ? options.parameterLimit : Defaults.parameterLimit;
        options.strictNullHandling = options.strictNullHandling == true;

        if (str == null || str.isEmpty()) {
            return options.plainObjects ? new LinkedHashMap<>() : new HashMap<>();
        }

        Object tempObj = str instanceof String ? parseValues(str, options) : str;
        Map<String, Object> obj = options.plainObjects ? new LinkedHashMap<>() : new HashMap<>();

        // Iterate over the keys and setup the new object
        Set<String> keys = ((Map<String, Object>) tempObj).keySet();
        for (String key : keys) {
            Object newObj = parseKeys(key, ((Map<String, Object>) tempObj).get(key).toString(), options);
            obj = merge(obj, (Map<String, Object>) newObj, options);
        }

        return compact(obj);
    }

    private static Map<String, Object> merge(Map<String, Object> target, Map<String, Object> source, Options options) {
        for (String key : source.keySet()) {
            Object sourceValue = source.get(key);
            if (target.containsKey(key)) {
                Object targetValue = target.get(key);
                if (targetValue instanceof List && sourceValue instanceof List) {
                    List<Object> mergedList = new ArrayList<>();
                    mergedList.addAll((List<Object>) targetValue);
                    mergedList.addAll((List<Object>) sourceValue);
                    target.put(key, mergedList);
                } else if (targetValue instanceof Map && sourceValue instanceof Map) {
                    Map<String, Object> mergedMap = new LinkedHashMap<>();
                    mergedMap.putAll((Map<String, Object>) targetValue);
                    mergedMap.putAll((Map<String, Object>) sourceValue);
                    target.put(key, mergedMap);
                } else {
                    target.put(key, sourceValue);
                }
            } else {
                target.put(key, sourceValue);
            }
        }
        return target;
    }

    private static Map<String, Object> compact(Map<String, Object> obj) {
        Map<String, Object> compactedObj = new LinkedHashMap<>();
        for (String key : obj.keySet()) {
            Object value = obj.get(key);
            if (value != null) {
                compactedObj.put(key, value);
            }
        }
        return compactedObj;
    }
}