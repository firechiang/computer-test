package com.firecode.computer.test.theoretical.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * 迭代流数据
 * 1 2 3 4 5 6 7 8 9
 * next()    函数用游标取出下一个元素
 * peek()    保留并返回下一个元素，使下一次调用 next() 函数返回当前元素
 * putBack() 使游标向前移动一个元素，使下一次调用 next() 函数返回游标所指向的元素（注意：每调用一次next()函数，取出一个元素，游标就会向后移动一个）
 *  
 *
 * @param <T>
 */
public class PeekIterator<T> implements Iterator<T> {
	
    private Iterator<T> it;

    private LinkedList<T> queueCache = new LinkedList<>();
    private LinkedList<T> stackPutBacks = new LinkedList<>();
    private final static int CACHE_SIZE = 10;
    /**
     * 最后一个元素
     */
    private T _endToken = null;


    public PeekIterator(Stream<T> stream){
        it = stream.iterator();
    }

    public PeekIterator(Iterator<T> _it, T endToke) {
        this.it = _it;
        this._endToken = endToke;
    }


    public PeekIterator(Stream<T> stream, T endToken){
        it = stream.iterator();
        _endToken = endToken;
    }

    /**
     * 保留当前待处理的下一个元素（就是下一次再next()函数，还是返回这个元素），并返回待处理的下一个元素
     * @return
     */
    public T peek(){
    	// 如果有保留元素就返回第一个保留元素
        if(this.stackPutBacks.size() > 0) {
            return this.stackPutBacks.getFirst();
        }
        if(!it.hasNext()) {
            return _endToken;
        }
        // 带处理的下一个元素（注意：这个带处理的下一个元素会在queueCache缓存里面）
        T val = next();
        // 将这个在queueCache缓存里面的待处理的下一个元素放到stackPutBacks缓存里面
        this.putBack();
        return val;
    }

    
    /**
     * 每next()函数一次，会将取出一个元素，游标就会往后移动一格
     * 当前这个函数就是将游标往前挪一格，使next()函数指向前一个元素
     * 删除queueCache里面的最后一个元素并将它放到stackPutBacks缓存里面的最前面
     * 缓存:A -> B -> C -> D
     * 放回:D -> C -> B -> A
     */
    public void putBack(){

        if(this.queueCache.size() > 0) {
            this.stackPutBacks.push(this.queueCache.pollLast());
        }

    }



    @Override
    public boolean hasNext() {
        return _endToken != null || this.stackPutBacks.size() > 0 || it.hasNext();
    }

    /**
     * 返回流里面的下一个元素，并将下还一个元素放到queueCache缓存
     */
    @Override
    public T next() {
        T val = null;
        if(this.stackPutBacks.size() > 0) {
        	// 弹出并删除第一个元素
            val = this.stackPutBacks.pop();
        } else {
            if(!this.it.hasNext()) {
                T tmp = _endToken;
                _endToken = null;
                return tmp;
            }
            val = it.next();
        }
        // 保证缓存里面的元素个数（将多余的元素删除）
        while(queueCache.size() > CACHE_SIZE - 1) {
        	// 去掉首个元素（就是把前面处理过的数据都删除）
            queueCache.poll();
        }
        queueCache.add(val);
        return val;
    }
}
