package com.example.book_web.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class DataUtil {

    private DataUtil() {
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    ModelMapper modelMapper;

    public static boolean isNullOrZero(Long value) {
        return (value == null || value.equals(0L));
    }


    public static boolean isNullOrZero(Double value) {
        return (value == null || value == 0);
    }


    public static boolean isNullOrZero(String value) {
        return (value == null || safeToLong(value).equals(0L));
    }


    public static boolean isNullOrZero(Integer value) {
        return (value == null || value.equals(0));
    }

    public static boolean isNullOrZero(BigDecimal value) {
        return (value == null || value.compareTo(BigDecimal.ZERO) == 0);
    }

    public static Long safeToLong(Object obj1, Long defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        if (obj1 instanceof BigDecimal) {
            return ((BigDecimal) obj1).longValue();
        }
        if (obj1 instanceof BigInteger) {
            return ((BigInteger) obj1).longValue();
        }
        if (obj1 instanceof Double) {
            return ((Double) obj1).longValue();
        }

        try {
            return Long.parseLong(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    /**
     * @param obj1 Object
     * @return Long
     */
    public static Long safeToLong(Object obj1) {
        return safeToLong(obj1, 0L);
    }

    public static Double safeToDouble(Object obj1, Double defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    public static Double safeToDouble(Object obj1) {
        return safeToDouble(obj1, 0.0);
    }

    public static Short safeToShort(Object obj1, Short defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return Short.parseShort(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    public static Short safeToShort(Object obj1) {
        return safeToShort(obj1, (short) 0);
    }

    public static int safeToInt(Object obj1, int defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            String data = obj1.toString();
            if (data.contains(".")) {
                data = data.substring(0, data.indexOf("."));
            }

            if (data.contains(",")) {
                data = data.substring(0, data.indexOf(","));
            }

            return Integer.parseInt(data);
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    public static Instant toInstant(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof java.sql.Date) {
            return Instant.ofEpochMilli(((java.sql.Date) object).getTime());
        } else if (object instanceof Timestamp) {
            return Instant.ofEpochMilli(((Timestamp) object).getTime());
        } else if (object instanceof String) {
            return Instant.parse((String) object);
        } else if (object instanceof Instant) {
            return (Instant) object;
        } else if (object instanceof Long) {
            return Instant.ofEpochMilli((Long) object);
        }
        return null;
    }

    public static int safeToInt(Object obj1) {
        return safeToInt(obj1, 0);
    }

    public static int safeToIntBoolean(Object obj1) {
        if (obj1 == null) {
            return 0;
        }
        String data = obj1.toString();
        if ("false".equalsIgnoreCase(data)) {
            return 0;
        }
        if ("true".equalsIgnoreCase(data)) {
            return 1;
        }
        return safeToInt(obj1, 0);
    }

    public static String safeToString(Object obj1, String defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }

        return obj1.toString().trim();
    }

    /**
     * @param obj1 Object
     * @return String
     */
    public static String safeToString(Object obj1) {
        return safeToString(obj1, "");
    }

    public static Date safeToDate(Object obj1) {
        if (obj1 == null) {
            return null;
        }
        return (Date) obj1;
    }

    /**
     * safe equal
     *
     * @param obj1 String
     * @param obj2 String
     * @return boolean
     */
    public static boolean safeEqual(String obj1, String obj2) {
        return (obj1 != null) && obj1.equals(obj2);
    }

    public static boolean safeEqual(Object obj1, Object obj2) {
        return ((obj1 != null) && (obj2 != null) && obj2.toString().equals(obj1.toString()));
    }

    /**
     * check null or empty
     * Su dung ma nguon cua thu vien StringUtils trong apache common lang
     *
     * @param cs String
     * @return boolean
     */
    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(final Object[] collection) {
        return collection == null || collection.length == 0;
    }

    public static boolean isNullOrEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNullOrEmpty(final Object obj) {
        return obj == null || obj.toString().isEmpty();
    }

    /**
     * Ham nay mac du nhan tham so truyen vao la object nhung gan nhu chi hoat dong cho doi tuong la string
     * Chuyen sang dung isNullOrEmpty thay the
     *
     * @param obj1
     * @return
     */
    public static boolean isStringNullOrEmpty(Object obj1) {
        return obj1 == null || "".equals(obj1.toString().trim());
    }


    /**
     * @param obj1 Object
     * @return BigDecimal
     */
    public static BigDecimal safeToBigDecimal(Object obj1) {
        if (obj1 == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return BigDecimal.ZERO;
        }
    }

    public static BigInteger safeToBigInteger(Object obj1, BigInteger defaultValue) {
        if (obj1 == null) {
            return defaultValue;
        }
        try {
            return new BigInteger(obj1.toString());
        } catch (final NumberFormatException nfe) {
            log.error(nfe.getMessage(), nfe);
            return defaultValue;
        }
    }

    /**
     * add
     *
     * @param obj1 BigDecimal
     * @param obj2 BigDecimal
     * @return BigDecimal
     */
    public static BigInteger add(BigInteger obj1, BigInteger obj2) {
        if (obj1 == null) {
            return obj2;
        } else if (obj2 == null) {
            return obj1;
        }

        return obj1.add(obj2);
    }

    public static Character safeToCharacter(Object value) {
        return safeToCharacter(value, '0');
    }

    public static Character safeToCharacter(Object value, Character defaulValue) {
        if (value == null) return defaulValue;
        return String.valueOf(value).charAt(0);
    }


    public static Collection<String> objLstToStringLst(List<Object> list) {
        Collection<String> result = new ArrayList<>();
        if (!list.isEmpty()) {
            result.addAll(list.stream().map(DataUtil::safeToString).collect(Collectors.toList()));
        }

        return result;
    }

    /**
     * Khong dung ham nay nua ma chuyen sang check thang == null
     */
    public static boolean isNullObject(Object obj1) {
        if (obj1 == null) {
            return true;
        }
        if (obj1 instanceof String) {
            return isNullOrEmpty(obj1.toString());
        }
        return false;
    }


    /**
     * Trim string
     *
     * @param alt: thay the khi str null
     */
    public static String trim(String str, String alt) {
        if (str == null) {
            return alt;
        }
        return str.trim();
    }

    public static BigDecimal defaultIfSmallerThanZero(BigDecimal value, BigDecimal defaultValue) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            return defaultValue;
        }
        return value;
    }

    public static Long safeAbs(Long number, Long defaultValue) {
        if (number == null) {
            if (defaultValue == null) {
                return 0L;
            }
            return defaultValue < 0 ? -defaultValue : defaultValue;
        }

        return number < 0 ? -number : number;
    }

    public static Boolean safeToBoolean(Double data) {
        if (data == null) return false;
        return data == 1;
    }

    public static Boolean safeToBoolean(Long data) {
        if (data == null) return false;
        return data == 1;
    }

    public static Boolean safeToBoolean(Integer data) {
        if (data == null) return false;
        return data == 1;
    }

    public static Boolean safeToBoolean(String data) {
        if (data == null) return false;
        return data.equals("true");
    }

    public static Boolean safeToBoolean(Object data) {
        String toString = safeToString(data);
        if ("".equals(toString)) return false;
        return toString.equals("true") || toString.equals("1");
    }

    public static Integer pageSize(Pageable pageable) {
        return (pageable.getPageNumber()) * pageable.getPageSize();
    }



    public static Pageable getPageable(Integer page, Integer size) {
        if (page == null || page <= 0) {
            page = 0;
        } else {
            page = page - 1;
        }
        if (size == null || size <= 0 || size > 100000) {
            size = 10;
        }
        return Pageable.ofSize(size).withPage(page);
    }


    public static <T> List<T> convertList(Object data) {
        return mapper.convertValue(data, new TypeReference<>() {
        });
    }

    public static <T> T convertTo(Object data, Class<T> clazz) {
        return mapper.convertValue(data, clazz);
    }

    public static <T> T convertTo(Object data, TypeReference<T> type) {
        return mapper.convertValue(data, type);
    }

    public static List<Integer> convertIntegerArr(Object input) {
        String[] idArr = ((String) input).split(",");
        List<Integer> lstInt = new ArrayList<>();
        for (String s : idArr) {
            try {
                lstInt.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException e) {
                log.error("Error parsing interger", e);
            }
        }
        return lstInt;
    }

    public static List<Long> convertLongArr(Object input) {
        String[] idArr = ((String) input).split(",");
        List<Long> lstLong = new ArrayList<>();
        for (int i = 0; i < idArr.length; i++) {
            try {
                lstLong.add(Long.parseLong(idArr[i].trim()));
            } catch (NumberFormatException e) {
                log.error("Error parsing long", e);
            }
        }
        return lstLong;
    }

    public static List<Double> convertDoubleArr(Object input) {
        String[] idArr = ((String) input).split(",");
        List<Double> lstDouble = new ArrayList<>();
        for (int i = 0; i < idArr.length; i++) {
            try {
                lstDouble.add(Double.parseDouble(idArr[i].trim()));
            } catch (NumberFormatException e) {
                log.error("Error parsing double", e);
            }
        }
        return lstDouble;
    }

    public static List<String> convertToStringArr(Object input) {
        if (input == null)
            return new ArrayList<>();
        String[] idArr = ((String) input).split(",");
        List<String> lstString = new ArrayList<>();
        for (String s : idArr) {
            try {
                lstString.add((s.trim()));
            } catch (NumberFormatException e) {
                log.error("Error parsing string", e);
            }
        }
        return lstString;
    }

    public static String generateTempPwd(int length) {

        String numbers = "012345678";
        char[] otp = new char[length];
        Random getOtpNum = new SecureRandom();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        StringBuilder optCode = new StringBuilder();
        for (char c : otp) {
            optCode.append(c);
        }
        return optCode.toString();
    }

    public static <T> T cloneBean(T source) {
        try {
            if (source == null) {
                return null;
            }
            T dto = (T) source.getClass().getConstructor().newInstance();
            BeanUtils.copyProperties(source, dto);
            return dto;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            log.error("Error parsing double", e);
            return null;
        }
    }

    public static <T> List<T> cloneBean(Iterable<T> sourceList) {
        final List<T> destList = Lists.newArrayList();
        if (sourceList == null) {
            return destList;
        }
        sourceList.forEach(t -> {
            T destObject = cloneBean(t);
            if (destObject != null) {
                destList.add(cloneBean(t));
            }
        });
        return destList;
    }


    public static String log(String message) {
        return String.format("%s: %s%n", new Date(), message);
    }

    public static Map<Long, String> getMapData(Object list, String keyIndex, String valueIndex) {
        var result = new HashMap<Long, String>();
        if (DataUtil.isNullOrEmpty(list)) {
            return result;
        }
        List<Map<String, Object>> listObj = mapper.convertValue(list, new TypeReference<>() {
        });
        for (Map<String, Object> data : listObj) {
            result.putIfAbsent(DataUtil.safeToLong(data.get(keyIndex)), DataUtil.safeToString(data.get(valueIndex)));
        }
        return result;
    }


    public static Object defaultIfNull(Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }


    public static boolean isValidDate(String dateStr, String formatDate) {
        DateFormat sdf = new SimpleDateFormat(formatDate);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

//    public static String getLogData(Object rawData, String key) {
//        Gson gson = new Gson();
//        Map<String, Object> logData = new HashMap<>();
//        logData.put(key, rawData);
//        return gson.toJson(logData);
//    }

    public static String listLongToString(List<Long> listLong) {
        if (CollectionUtils.isEmpty(listLong)) {
            return null;
        }
        return listLong.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public static String listIntegerToString(List<Integer> listLong) {
        if (CollectionUtils.isEmpty(listLong)){
            return null;
        }
        return listLong.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public static String listStringToString(List<String> listString) {
        if (CollectionUtils.isEmpty(listString)){
            return null;
        }
        return String.join(",", listString);
    }

    public static <T, R> List<R> convertList(List<T> list, Function<T, R> func) {
        return list.stream().map(func).collect(Collectors.toList());
    }

    public static <T, R> R convertObject(T t, Function<T, R> func) {
        return func.apply(t);
    }
    public static String uppercaseFirstLetter(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);

    }
}
