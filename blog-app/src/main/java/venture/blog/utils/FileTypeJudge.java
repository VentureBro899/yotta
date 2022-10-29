package venture.blog.utils;



import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Method;

/**
 * 文件类型操作类
 * @author zhihao.du
 *
 */
public final class FileTypeJudge {

    /**
     * Constructor
     */
    private FileTypeJudge() {
    }

    /**
     * 将文件头转换成16进制字符串
     * @param
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到文件头
     * @param is 文件路径
     * @return 文件头
     * @throws IOException
     */
    private static String getFileContent(InputStream is) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        try {
            is.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }

    /**
     * 获取文件类型类
     * @param is 文件路径
     * @return 文件类型
     */
    public static FileType getType(InputStream is) throws IOException {
        String fileHead = getFileContent(is);
        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();
        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }
        return null;
    }

    /**
     * 获取文件类型
     * @param is
     * @return
     * @throws Exception
     */
    public static String getFileType(InputStream is) throws Exception{
        FileType fileType = getType(is);
        if(fileType!=null){
            return fileType.getValue();
        }
        return null;
    }


    public static boolean isPng(InputStream is){
        if(is == null){
            return false;
        }
        try {
            String fileType = getFileType(is);
            return  FileType.PNG.getValue().equals(fileType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isjpg(InputStream is){
        if(is == null){
            return false;
        }
        try {
            String fileType = getFileType(is);
            return  FileType.JPEG.getValue().equals(fileType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isGif(InputStream is){
        if(is == null){
            return false;
        }
        try {
            String fileType = getFileType(is);
            return  FileType.GIF.getValue().equals(fileType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     *  判断文件是否是指定类型
     * @return boolean
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2022/8/9 18:59
     */
    public static boolean isSpecify(InputStream is,String type) {
        if (is == null) {
            return false;
        }
        if(type.equalsIgnoreCase("jpg")){
            type = "JPEG";
        }
        try {
            // 期待类型头
            String wantType = null;
            // 传入文件类型头
            String inType = FileTypeJudge.getFileType(is);
            Class<?> clazz = FileType.class;
            // 获取所有枚举变量
            Object[] enumConstants = clazz.getEnumConstants();
            Method getValue = clazz.getMethod("getValue");
            for (Object enumConstant : enumConstants) {
                if (enumConstant.toString().equalsIgnoreCase(type)){
                    // getValue方法获取枚举对应的值
                    wantType = getValue.invoke(enumConstant).toString();
                    break;
                }
            }
            return  wantType.equals(inType);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
