package org.asyou.redis.exception;

/**
 * Created by steven on 2017/4/23.
 */
public class CodeException extends Exception{
    public CodeException(){
        super();
    }

    public CodeException(String message) {
        super(message);
    }

    public String getExceptionString(){
        StackTraceElement[] stes = this.getStackTrace();
        StackTraceElement ste = (stes != null && stes.length > 0) ? stes[0] : null;
        if (ste != null) {
            return String.format("%s - [%s(%s)]", this.getMessage(), ste.getFileName(), ste.getLineNumber());
        } else {
            return this.getMessage();
        }
    }

    public static String getExceptionString(Exception e){
        StackTraceElement[] stes = e.getStackTrace();
        StackTraceElement ste = (stes != null && stes.length > 0) ? stes[0] : null;
        if (ste != null)
//            return String.format("%s - [%s] [%s.%s] [%s]", e.getMessage(), ste.getFileName(), ste.getClassName(), ste.getMethodName(), ste.getLineNumber());
        {
            return String.format("%s - [%s(%s)]", e.getMessage(), ste.getFileName(), ste.getLineNumber());
        } else {
            return e.getMessage();
        }
    }
}
