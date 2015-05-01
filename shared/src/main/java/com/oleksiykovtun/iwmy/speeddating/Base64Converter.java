package com.oleksiykovtun.iwmy.speeddating;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

/**
 * Apache Commons-based Base64 encoder/decoder with UTF-8 encoding
 */
public class Base64Converter {

    public static byte[] getBytesFromBase64String(String base64String) {
        return Base64.decodeBase64(StringUtils.getBytesUtf8(base64String));
    }

    public static String getStringFromBase64String(String base64String) {
        return StringUtils.newStringUtf8(getBytesFromBase64String(base64String));
    }

    public static String getBase64StringFromBytes(byte[] bytes) {
        return StringUtils.newStringUtf8(Base64.encodeBase64(bytes));
    }

    public static String getBase64StringFromString(String text) {
        return getBase64StringFromBytes(StringUtils.getBytesUtf8(text));
    }

}
