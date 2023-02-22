package com.project.cmn.http.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;

/**
 * Json String to Object 또는 Object to Json String을 위한 유틸
 */
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Object를 JSON String로 변환한다.
     *
     * @param obj 변환할 Object
     * @return String
     * @throws JsonProcessingException
     */
    public static String getJson(Object obj) throws JsonProcessingException {
        return getJson(null, obj);
    }

    /**
     * Object를 JSON을 들여쓰기한 String로 변환한다.
     *
     * @param obj 변환할 Object
     * @return String
     * @throws JsonProcessingException
     */
    public static String getJsonIndent(Object obj) throws JsonProcessingException {
        return getJson(SerializationFeature.INDENT_OUTPUT, obj);
    }

    /**
     * Object 을 JSON String 으로 변환 (pretty format)
     *
     * @param obj JSON String으로 변환할 객체
     * @return 변환된 JSON String
     * @throws JsonProcessingException
     */
    public static String getJsonPretty(Object obj) throws JsonProcessingException {
        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * JSON String을 pretty format 형태의 JSON String으로 변환
     *
     * @param jsonStr 변환할 JSON String
     * @return pretty format 형태의 JSON String
     * @throws IOException
     */
    public static String getJsonPretty(String jsonStr) throws IOException {
        Object obj = OBJECT_MAPPER.readValue(jsonStr, Object.class);

        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    /**
     * Object를 JSON String로 변환한다.
     *
     * @param feature SerializationFeature
     * @param obj     변환할 Object
     * @return JSON String
     * @throws JsonProcessingException
     */
    public static String getJson(SerializationFeature feature, Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        if (feature != null) {
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
            mapper.configure(feature, true);
        }

        return mapper.writeValueAsString(obj);
    }

    /**
     * JSON String를 Object로 변환한다.
     *
     * @param json  변환할 JSON String
     * @param clazz 변환할 Object class
     * @return Object
     * @throws IOException
     */
    public static <T> Object toObject(String json, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * JSON String을 Object로 변환한다.
     * <p>
     * TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
     * <p>
     * HashMap<String,Object> map = JsonUtils.toObject(json, typeRef);
     *
     * @param json         변환할 JSON string
     * @param valueTypeRef 변환할 Json Type
     * @return Object
     * @throws IOException
     */
    public static <T> Object toObject(String json, TypeReference<T> valueTypeRef) throws IOException {
        return OBJECT_MAPPER.readValue(json, valueTypeRef);
    }

    /**
     * JSON String을 List<Object>로 변환한다.
     *
     * @param json  변환할 JSON String
     * @param clazz 변환할 Object class
     * @return 변환된 List<Object>
     * @throws IOException
     */
    public static <T> List<T> toList(String json, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(json, TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
    }

    /**
     * {@link Object}를 {@link Class<T>}로 변환한다.
     *
     * @param obj   {@link Object} 소스
     * @param clazz {@link Class} 타켓
     * @param <T>   {@link T}
     * @return 변환된 {@link Class}
     */
    public static <T> Object convert(Object obj, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }
}
