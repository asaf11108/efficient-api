package com.example.api.qs;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern HEX_PATTERN = Pattern.compile("%([0-9A-Fa-f]{2})");
    private static final char[] HEX_TABLE = "0123456789ABCDEF".toCharArray();

    public static boolean hasOwnProperty(Map<String, Object> object, String key) {
        if (object == null || key == null) {
            return false;
        }
        return object.containsKey(key);
    }

    public static Map<String, Object> arrayToObject(List<Object> source, Map<String, Object> options) {
        Map<String, Object> obj = options != null && options.containsKey("plainObjects")
                ? new HashMap<>()
                : new LinkedHashMap<>();

        for (int i = 0; i < source.size(); i++) {
            Object item = source.get(i);
            if (item != null) {
                obj.put(Integer.toString(i), item);
            }
        }

        return obj;
    }

    public static Map<String, Object> assign(Map<String, Object> target, Map<String, Object> source) {
        target.putAll(source);
        return target;
    }

    public static Object compact(Object value) {
        LinkedList<QueueItem> queue = new LinkedList<>();
        List<Object> refs = new ArrayList<>();

        queue.add(new QueueItem(new HashMap<>(Map.of("o", value)), "o"));

        while (!queue.isEmpty()) {
            QueueItem item = queue.removeLast();
            Map<String, Object> obj = (Map<String, Object>) item.obj.get(item.prop);

            Set<String> keys = obj.keySet();
            for (String key : keys) {
                Object val = obj.get(key);
                if (val != null && val instanceof Map && !refs.contains(val)) {
                    queue.add(new QueueItem(obj, key));
                    refs.add(val);
                }
            }
        }

        return compactQueue(queue);
    }

    private static Object compactQueue(LinkedList<QueueItem> queue) {
        Object obj = null;

        while (!queue.isEmpty()) {
            QueueItem item = queue.removeLast();
            Object value = item.obj.get(item.prop);

            if (value instanceof List) {
                List<Object> compacted = new ArrayList<>();

                List<Object> list = (List<Object>) value;
                for (Object itemValue : list) {
                    if (itemValue != null) {
                        compacted.add(itemValue);
                    }
                }

                item.obj.put(item.prop, compacted);
            }

            obj = item.obj.get(item.prop);
        }

        return obj;
    }

    public static String decode(String str) {
        try {
            return java.net.URLDecoder.decode(str.replace("+", " "), StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return str;
        }
    }

    public static String encode(String str) {
        if (str.length() == 0) {
            return str;
        }

        StringBuilder out = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (isUnreserved(c)) {
                out.append(c);
            } else {
                byte[] bytes = String.valueOf(c).getBytes(StandardCharsets.UTF_8);
                for (byte b : bytes) {
                    out.append('%');
                    out.append(HEX_TABLE[(b & 0xF0) >> 4]);
                    out.append(HEX_TABLE[b & 0x0F]);
                }
            }
        }

        return out.toString();
    }

    private static boolean isUnreserved(char c) {
        return c == '-' || c == '.' || c == '_' || c == '~'
                || (c >= '0' && c <= '9')
                || (c >= 'A' && c <= 'Z')
                || (c >= 'a' && c <= 'z');
    }

    public static boolean isBuffer(Object obj) {
        return obj instanceof byte[];
    }

    public static boolean isRegExp(Object obj) {
        return obj instanceof Pattern;
    }

    public static Object merge(Object target, Object source, Map<String, Object> options) {
        if (source == null) {
            return target;
        }

        if (!(source instanceof Map) && !(source instanceof List)) {
            if (target instanceof List) {
                ((List<Object>) target).add(source);
            } else if (target instanceof Map) {
                boolean isPlainObjects = options != null && options.containsKey("plainObjects") && (boolean) options.get("plainObjects");
                boolean allowPrototypes = options != null && options.containsKey("allowPrototypes") && (boolean) options.get("allowPrototypes");
                if (isPlainObjects || allowPrototypes || !Object.class.getDeclaredFields()[0].isAnnotationPresent(Override.class)) {
                    ((Map<String, Object>) target).put(source.toString(), true);
                }
            } else {
                List<Object> resultList = new ArrayList<>();
                resultList.add(target);
                resultList.add(source);
                return resultList;
            }

            return target;
        }

        if (!(target instanceof Map) || !(source instanceof Map)) {
            List<Object> resultList = new ArrayList<>();
            resultList.add(target);
            if (source instanceof List) {
                resultList.addAll((List<Object>) source);
            } else {
                resultList.add(source);
            }
            return resultList;
        }

        Map<String, Object> mergeTarget = (Map<String, Object>) target;
        if (target instanceof List && !(source instanceof List)) {
            mergeTarget = arrayToObject((List<Object>) target, options);
        }

        if (target instanceof List && source instanceof List) {
            List<Object> sourceList = (List<Object>) source;
            for (int i = 0; i < sourceList.size(); i++) {
                if (((Map<?, ?>) target).containsKey(i)) {
                    Object targetItem = ((Map<?, ?>) target).get(i);
                    Object item = sourceList.get(i);
                    if (targetItem != null && targetItem instanceof Map && item != null && item instanceof Map) {
                        ((Map<Object, Object>) target).put(((Object) i), merge(targetItem, item, options));
                    } else {
                        ((List<Object>) target).add(item);
                    }
                } else {
                    ((List<Object>) target).add(sourceList.get(i));
                }
            }
            return target;
        }

        Map<String, Object> sourceMap = (Map<String, Object>) source;
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (((Map<?, ?>) target).containsKey(key)) {
                ((Map<String, Object>) target).put(key, merge(((Map<?, ?>) target).get(key), value, options));
            } else {
                ((Map<String, Object>) target).put(key, value);
            }
        }

        return mergeTarget;
    }

    private static class QueueItem {
        private final Map<String, Object> obj;
        private final String prop;

        public QueueItem(Map<String, Object> obj, String prop) {
            this.obj = obj;
            this.prop = prop;
        }
    }
}