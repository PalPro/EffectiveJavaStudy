package asakichy.第10章並行性;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目69waitとnotifyよりコンカレンシーユーティリティを選ぶ {

	@Test
	public void コンカレンシーユーティリティとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("並行プログラミングのための高レベルなユーティリティ。").e();
		o.l2("３つのカテゴリーに分類。").e();
		o.l3("エグゼキューターフレームワーク。").e();
		o.l3("コンカレントコレクション。").e();
		o.l3("シンクロナイザー。").e();

		o.l1("【どうすれば？】").e();
		o.l2("低レベルなwaitとnotifyを使用せず、高レベルなコンカレンシーユーティリティを使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("waitとnotifyを使用して正しくプログラムするのは困難。").e();
		o.l2("コンカレンシーユーティリティは、手作業で書いていたことの多くを行ってくれる。").e();
	}

	@Test
	public void コンカレントコレクションとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("標準コレクションインターフェイスのこうパフォーマンスな並列実装。").e();
		o.l2("独自の同期を内部的に管理している。").e();

		o.l1("【どうすれば？】").e();
		o.l2("外部で同期されたコレクションよりはコンカレントコレクションを使用する。").e();
		o.l2("java.util.Hashtableやjava.util.Collections.synchronizedMapよりも、");
		o.__("java.util.concurrent.ConcurrentHashMapを使う。").e();

		o.l1("【どうして？】").e();
		o.l2("内部同期なので高速。").e();
	}

	@Test
	public void コンカレントコレクション_状態変更操作() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複数の基本的な操作を、単一のアトミック操作にまとめた操作を提供。").e();

		o.l1("【どうして？】").e();
		o.l2("クライアント側から、コンカレントコレクションに対して、");
		o.l2("複数のメソッド呼び出しをアトミックにまとめることができない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.concurrent.ConcurrentMap#putIfAbsent()を使用して、String#intern()を模したintern()関数。").e();
		String one = intern("1");
		String two = intern("2");
		assertThat(one, is("1"));
		assertThat(two, is("2"));
		String one_2 = intern("1");
		assertThat(one_2, sameInstance(one));

		stringPool.clear();
		one = internSpeedy("1");
		two = internSpeedy("2");
		assertThat(one, is("1"));
		assertThat(two, is("2"));
		one_2 = internSpeedy("1");
		assertThat(one_2, sameInstance(one));

		{
			/** 【補】 ConcurrentMap#putIfAbsent() の動作 */
			// キーに対するマッピングが存在しなければ、マッピングを挿入してnullを返し、
			// マッピングが存在していれば、キーにすでに関連付けられた値を返す。
			// これにより、スレッドセーフな正規化マップを実装するのが容易になる。
		}
	}

	private static final ConcurrentMap<String, String> stringPool = new ConcurrentHashMap<String, String>();

	private static String intern(String s) {
		String previousValue = stringPool.putIfAbsent(s, s);
		return previousValue == null ? s : previousValue;
	}

	// 高速版
	private static String internSpeedy(String s) {
		String result = stringPool.get(s);
		if (result == null) {
			result = stringPool.putIfAbsent(s, s);
			if (result == null) {
				result = s;
			}
		}
		return result;
	}

	@Test
	public void コンカレントコレクション_ブロック操作() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("操作が完了するまで待つ操作を提供。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Queueを拡張した、java.util.concurrent.BlockingQueue。").e();
		o.l3("ワークキュー機能を実現。").e();
		o.l3("エグゼキューターフレームワークの内部でも使用されている。").e();

		Producer producer = new Producer(blockingQueue);
		Consumer consumer = new Consumer(blockingQueue);
		Thread producerThread = new Thread(producer);
		Thread consumerThread = new Thread(consumer);

		producerThread.start();
		consumerThread.start();

		producerThread.join();
		consumerThread.join();

		assertThat(result, containsInAnyOrder("p1", "p2", "p3", "p4", "p5"));
	}

	private static final BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<String>(1);
	private static final List<String> result = new CopyOnWriteArrayList<String>();

	private static class Producer implements Runnable {
		private final BlockingQueue<String> queue;

		Producer(BlockingQueue<String> q) {
			queue = q;
		}

		public void run() {
			try {
				for (int i = 0; i < 5; i++) {
					queue.put(produce(i));
				}
			} catch (InterruptedException ex) {
			}
		}

		public String produce(int number) {
			return "p" + (number + 1);
		}
	}

	private static class Consumer implements Runnable {
		private final BlockingQueue<String> queue;

		Consumer(BlockingQueue<String> q) {
			queue = q;
		}

		public void run() {
			try {
				while (result.size() < 5) {
					consume(queue.take());
				}
			} catch (InterruptedException ex) {
			}
		}

		void consume(String s) {
			result.add(s);
		}
	}

	@Test
	public void シンクロナイザーとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("スレッドが他のスレッドを待つことを可能にするオブジェクト。").e();
		o.l2("複数スレッドの活動を協調させることが可能。").e();

		o.l1("【どうすれば？】").e();
		o.l2("使用頻度が高いシンクロナイザー。").e();
		o.l3("CountDownLatch。").e();
		o.l3("Semaphore。").e();
		o.l2("使用頻度が低いシンクロナイザー。").e();
		o.l3("CyclicBarrier。").e();
		o.l3("Exchanger。").e();
	}

	@Test
	public void シンクロナイザー_カウントダウンラッチ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("使い捨てのバリアー。").e();
		o.l2("１つ以上のスレッドが何かを行うために、他の１つ以上のスレッドを待ち合わせ可能。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.concurrent.CountDownLatchを使用した、並行実行を計測するtime()関数。").e();
		o.l2("").e();

		ExecutorService executor = Executors.newCachedThreadPool();

		long nanoTime = time(executor, 5, new Runnable() {
			@Override
			public void run() {
				strings.add("called");
			}
		});

		assertThat(strings.size(), is(5));
		assertThat(nanoTime, anything());

		executor.shutdown();
	}

	private static final List<String> strings = new CopyOnWriteArrayList<String>();

	private static long time(Executor executor, int concurrency, final Runnable action) throws InterruptedException {
		final CountDownLatch ready = new CountDownLatch(concurrency);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch done = new CountDownLatch(concurrency);

		for (int i = 0; i < concurrency; i++) {
			executor.execute(new Runnable() {
				public void run() {
					ready.countDown(); // 各スレッドが、準備の完了を伝える
					try {
						start.await(); // いわゆる「堰」で、ここで待ち合わせる
						action.run(); // 堰が外れたら、一斉スタート
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} finally {
						done.countDown(); // 各スレッドが、処理の完了を伝える。
					}
				}
			});
		}

		ready.await(); // 全スレッドの、準備の完了を待つ
		long startNanos = System.nanoTime();
		start.countDown(); // 全スレッドの、堰を外す
		done.await(); // 全スレッドの、処理の完了を待つ
		return System.nanoTime() - startNanos;
	}

	@Test
	public void waitとnotify() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("低レベルな並行処理プログラミングで使用。").e();

		o.l1("【どうして？】").e();
		o.l2("もはや使用する理由はほとんどない。").e();
		o.l2("ただし、昔のコードを保守することもあるので、知識は持っておくべき。").e();

		o.l1("【どうすれば？】").e();
		o.l2("waitループイディオムを使用する。").e();
		o.l2("notifyでなく、notifyAllを使用する。").e();

		{
			/** 【補】 ループの中にwaitを入れる理由 */
			// 要するに、waitの「前後で」条件の検証を行う、ということ。

			// ※ 前で条件検証を行う理由は、活性を保証するため。

			// 条件がすでに成立していて、
			// スレッドが待つ前にnotifyが呼び出されてしまったいたら、
			// スレッドがその待ちから常に起きることが保証されない。

			// ※ 後で条件検証を行う理由は、安全性を保証するため。

			// 待ちから起こされたとしても、もう一度条件をチェックして、
			// 成立していなければ再び待ちに入ることは必須。
			// 条件が成立していないまま走ると、不変式が破壊され、不整合を引き起こす。

			// 条件が不成立なのに、スレッドが起こされてしまう可能性がいくつかある。
			// ・待ちスレッドが起きる前に、別のスレッドがロック取って成立条件の部分変えちゃう
			// ・条件成立してないのに、別のスレッドが悪意でnotifyしちゃう
			// ・スレッド起しに気前が良いスレッドがあり、notifyAllしちゃう
			// ・偽りの目覚め（まれに、notifyされなくても起きちゃうことがある）
		}

		{
			/** 【補】 notifyでなく、常にnotifyAllを使用する理由 */
			// notifyで、どれかのスレッドのみを狙い撃ちで起こすのは難しい。
			// notifyAllで、全部起こしにかかったほうが、確実。
			// 起きる必要のなかったスレッドは、条件チェックでまた眠りに入るので、問題ない。
		}

		o.l1("【たとえば？】").e();
		o.l2("java.util.concurrent.BlockingQueue相当の機能をwait/notifyで実現したRequestQueueクラス。").e();

		Client client = new Client(requestQueue);
		Server server = new Server(requestQueue);
		Thread clientThread = new Thread(client);
		Thread serverThread = new Thread(server);

		clientThread.start();
		serverThread.start();

		clientThread.join();
		serverThread.join();

		assertThat(accepts, containsInAnyOrder("r1", "r2", "r3", "r4", "r5"));
	}

	private static RequestQueue requestQueue = new RequestQueue();

	private static class RequestQueue {
		private final Queue<String> queue = new LinkedList<String>();

		public synchronized String getRequest() {
			// waitループイディオム
			while (queue.peek() == null) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			return queue.remove();
		}

		public synchronized void putRequest(String request) {
			queue.offer(request);
			// notifyでなく、notifyAllを使用する
			notifyAll();
		}
	}

	private static final List<String> accepts = new CopyOnWriteArrayList<String>();

	private static class Client implements Runnable {
		private final RequestQueue queue;

		Client(RequestQueue q) {
			queue = q;
		}

		public void run() {
			for (int i = 0; i < 5; i++) {
				queue.putRequest("r" + (i + 1));
			}
		}
	}

	private static class Server implements Runnable {
		private final RequestQueue queue;

		Server(RequestQueue q) {
			queue = q;
		}

		public void run() {
			while (accepts.size() < 5) {
				accepts.add(queue.getRequest());
			}
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("言語にたとえると、コンカレンシーユーティリティは高級言語で、wait/notifyはアセンブラ。").e();
		o.l1("新たなコードでwait/notifyを使用する理由はない。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
