package org.smile.collection;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.smile.math.MathUtils;
import org.smile.reflect.Unsafes;
/**
 * 一个缓冲的队列
 * @author 胡真山
 *
 * @param <E>
 */
public class QueueBuffer<E> extends AbstractQueue<E> {
	/**缓冲区数组*/
	private Object[] buffer;
	/**用于计算在缓冲数组中的索引*/
	private int indexMask;
	/**写索引*/
	private AtomicLong writeCount=new AtomicLong(0);
	/**读索引*/
	private AtomicLong readCount=new AtomicLong(0);
	
	/**
	 * 创建一个缓冲队列
	 * @param size 缓冲环的长度
	 */
	public QueueBuffer(int size){
		int maxSize=MathUtils.ceilPowerOf2(size);
		this.indexMask=maxSize-1;
		this.buffer=new Object[maxSize];
	}

	@Override
	public boolean offer(E e) {
		if(writeCount.get()-readCount.get()>indexMask){
			return false;
		}
		long wc=writeCount.getAndIncrement();
		int index=(int)(wc&indexMask);
		buffer[index]=e;
		return true;
	}

	@Override
	public E poll() {
		if(readCount.get()<writeCount.get()){
			long rc=readCount.getAndIncrement();
			int index=(int)(rc&indexMask);
			E e=(E)buffer[index];
			return e;
		}
		return null;
	}

	@Override
	public E peek() {
		long rc=readCount.get();
		int index=(int)(rc&indexMask);
		E e=(E)buffer[index];
		return e;
	}

	@Override
	public Iterator<E> iterator() {
		return new RingIterator();
	}

	@Override
	public int size() {
		return (int)(writeCount.get()-readCount.get());
	}
	
	class RingIterator implements Iterator<E>{
		/**当前读取的索引*/
		private long index=-1;
		
		@Override
		public boolean hasNext() {
			long write=writeCount.get();
			return index<write-1;
		}

		@Override
		public E next() {
			long nextindex=readCount.get();
			if(index==-1){
				index=nextindex;
			}else{
				if(index<nextindex){
					index=nextindex;
				}else{
					index=index+1;
				}
			}
			return (E)buffer[(int)(index&indexMask)];
		}

		@Override
		public void remove() {
			throw new IllegalArgumentException("not support this method ");
		}
	}
	
	public static interface EventHandler<T>
	{
	    void onEvent(T event);
	}
	
	
	public static class MultiEventHandler<T> implements EventHandler<T>{
		
		private List<EventHandler<T>> handlers=new LinkedList<EventHandler<T>>();
		
		public MultiEventHandler(EventHandler<T>... eventHandlers){
			CollectionUtils.addItem(handlers, eventHandlers);
		}

		@Override
		public void onEvent(T event) {
			for(EventHandler<T> handler:handlers){
				handler.onEvent(event);
			}
		}
	}
	/**
	 * 可以添加处理事件的buffer
	 * @author 胡真山
	 *
	 * @param <E>
	 */
	protected static class EventQueueBuffer<E> extends QueueBuffer<E>{
		/**运行中*/
		final int RUNNING=1;
		/**不在运行中*/
		final int NO_RUNNING=0;
		
		private EventHandler<E> handler;
		
		private ExecutorService executor=Executors.newScheduledThreadPool(2);
		
		private PollEventTask eventTask=new PollEventTask();
		/**是否在运行出队任务*/
		protected volatile int isRunning=NO_RUNNING;
		/**
		 * @param size 缓冲队列的大小
		 * @param handler 出列事件处理
		 */
		private EventQueueBuffer(int size,EventHandler<E> handler) {
			super(size);
			this.handler=handler;
		}
		
		@Override
		public boolean offer(E e) {
			if(super.offer(e)){
				if(UNSAFE.compareAndSwapInt(this, RUN_OFFSET,NO_RUNNING, RUNNING)){
					executor.execute(eventTask);
				}
				return true;
			}
			return false;
		}
		
		class PollEventTask implements Runnable{
			@Override
			public void run() {
				E e;
				while(true){
					if((e=poll())==null){
						isRunning=NO_RUNNING;
						break;
					}
					handler.onEvent(e);
				}
			}
		}
		
		private static final sun.misc.Unsafe UNSAFE;
		
		private static long RUN_OFFSET;
		
		static{
			UNSAFE=Unsafes.getUnsafe();
			try {
				RUN_OFFSET=UNSAFE.objectFieldOffset(EventQueueBuffer.class.getDeclaredField("isRunning"));
			} catch (Exception e) {
				throw new InternalError(e.getMessage());
			}
		}
	}
	
	public static <E> QueueBuffer<E> allocate(int size,EventHandler<E> handler){
		return new EventQueueBuffer<E>(size, handler);
	}
}
