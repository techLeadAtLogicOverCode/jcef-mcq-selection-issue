package jcef.user.selection.actions;

public interface LogCommons {
    default String logMsgPrefix(){
        String name = Thread.currentThread().getName() ;
        long t = System.currentTimeMillis() ;
        return "[" + name + "-" + t + "] : " ;
    }
}
