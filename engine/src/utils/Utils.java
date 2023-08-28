package utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class Utils {
    public static String getCurrDate() {
        String pattern = "dd.mm.yyyy-hh:mm:ss:sss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String dateStr = sdf.format(new Date());

        return dateStr;
    }

    public static String getSha1(String data) {
        return SharedUtils.getSha1(data);
    }

    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
