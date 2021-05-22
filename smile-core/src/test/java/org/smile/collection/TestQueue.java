package org.smile.collection;

import java.util.Iterator;

import org.smile.collection.QueueBuffer.EventHandler;


public class TestQueue {
	
	public static void main(String[] a){
		test2();
	}
	
	private static void test3(){
		QueueBuffer<String> buffer=new QueueBuffer<String>(100);
		for(int i=0;i<100;i++){
			buffer.offer("index-"+i);
		}
		buffer.poll();
		Iterator<String> iterator=buffer.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
	}
	
	private static void test2(){
		final QueueBuffer<String> queue=QueueBuffer.allocate(1000, new EventHandler<String>() {
			@Override
			public void onEvent(String event) {
				System.out.println(event);
			}
		});
		for(int i=0;i<10;i++){
			final int n=i;
			Thread t=new Thread(new Runnable() {
				@Override
				public void run() {
					for(int j=0;j<10;j++){
						queue.offer(n+"-"+j);
//						try {
//							Thread.sleep(10);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
					}
				}
			});
			t.start();
		}
	}
	
	private static void test1(){
		final QueueBuffer queue=new QueueBuffer(1000);
		for(int i=0;i<3;i++){
			final int n=i;
			Thread t=new Thread(new Runnable() {
				@Override
				public void run() {
					for(int j=0;j<1000;j++){
						queue.offer(n+"-"+j);
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			t.start();
		}
		
		
		
		for(int i=0;i<10;i++){
			Thread t=new Thread(new Runnable() {
				@Override
				public void run() {
					Object o;
					while((o=queue.poll())!=null){
						System.out.println(o);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			t.start();
		}
	}
}
