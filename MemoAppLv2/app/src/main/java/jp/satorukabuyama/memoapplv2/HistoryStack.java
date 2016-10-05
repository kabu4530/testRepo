package jp.satorukabuyama.memoapplv2;

import java.util.Stack;

public class HistoryStack<T> {

    private final Stack<T> undoStack = new Stack<T>();
    private final Stack<T> redoStack = new Stack<T>();

    /**
     * アンドゥ
     * @return
     */
    public T undo(){

        T result = null;
        if( !undoStack.empty() ){
            result = undoStack.pop();
            redoStack.push(result);
        }

        return result;
    }

    /**
     * リドゥ
     * @return
     */
    public T redo(){

        T result = null;
        if( !redoStack.empty() ){
            result = redoStack.pop();
            undoStack.push(result);
        }

        return result;
    }

    /**
     * 履歴の追加
     * @param history
     */
    public void add(T history){
        undoStack.push(history);
        redoStack.clear();
    }

    /**
     * アンドゥの列挙
     * @return
     */
    public final Iterable<T> iterateUndo(){
        return undoStack;
    }
}
