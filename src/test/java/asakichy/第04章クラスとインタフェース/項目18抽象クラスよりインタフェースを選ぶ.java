package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目18抽象クラスよりインタフェースを選ぶ {
	@Test
	public void インターフェイスと抽象クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複数の実装を許す型を定義するために、インタフェースと抽象クラスの２つの仕組みがある。").e();
	}

	@Test
	public void インターフェイスの優位性_既存クラスに実装() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("既存クラスを、新たなインタフェースを実装するように変更することは容易。").e();

		o.l1("【どうして？】").e();
		o.l2("既存クラスを、新たな抽象クラスに拡張するのは不可能。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Comparableは後から導入され、JDK各クラスに実装された。").e();
	}

	@Test
	public void インターフェイスの優位性_ミックスイン可能() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("「本来の型」に加えて、なんらかの振る舞いを提供していることを宣言できる。").e();

		o.l1("【どうして？】").e();
		o.l2("多重継承が許されないため、抽象クラスはミックスイン不可能。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Comparable。").e();
	}

	@Test
	public void インターフェイスの優位性_階層を持たない型フレームワーク構築() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("階層を持たない型フレームワークを構築することができる。").e();

		o.l1("【どうして？】").e();
		o.l2("抽象クラスは、クラス階層を構築するが、クラスの属性が複数合った場合は、");
		o.__("組み合わせの爆発が発生する。").e();
		o.l2("インターフェイスは、クラス階層に拠らず、クラス属性を複数もつクラスを定義できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("SingerSongWriterインターフェイス。").e();
		SingerSongWriter singerSongWriter = new SingerSongWriter() {
			@Override
			public String compose() {
				return "作曲します。";
			}

			@Override
			public String sing() {
				return "歌います。";
			}

			@Override
			public String play() {
				return "弾き語りします。";
			}
		};
		assertThat(singerSongWriter.play(), is("弾き語りします。"));
	}

	interface Singer {
		String sing();
	}

	interface SongWriter {
		String compose();
	}

	interface SingerSongWriter extends Singer, SongWriter {
		String play();
	}

	@Test
	public void インターフェイスの優位性_安全かつ強力な拡張() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ラッパークラスイデオムを通して、安全で強力な機能エンハンスを可能に。").e();

		o.l1("【どうして？】").e();
		o.l2("抽象クラスを使用すると、継承を使う以外の選択肢がなくなる。").e();
	}

	@Test
	public void 抽象クラスの優位性_発展させやすい() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("適切なデフォルトの実装を含んでいる具象メソッドを、いつでも追加できる。").e();

		o.l1("【どうして？】").e();
		o.l2("インターフェイスにメソッドを追加すると、実装クラスがコンパイルエラー。").e();
		o.l2("一旦リリースされ、広く使用されていたら、直すのはほぼ不可能。").e();
	}

	@Test
	public void インターフェイスと抽象クラスのいいとこどり_抽象骨格実装() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インターフェイスと、そのデフォルト実装抽象クラス（＝抽象骨格実装）を提供する。").e();

		o.l1("【どうして？】").e();
		o.l2("デフォルトの動作でほぼ良ければ、抽象骨格実装を継承してカスタマイズすればいいし、");
		o.__("気に入らなければ、インターフェイスを直接実装すればいい。").e();

		o.l1("【どうすれば？】").e();
		o.l2("抽象骨格実装の命名は「Abstract{インターフェイス名}」とする。").e();

		o.l1("【たとえば？】").e();
		o.l2("１").s("java.util.AbstractList 抽象骨格実装を利用したIntArraysクラス。").e();
		int[] a = new int[10];
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		List<Integer> list = IntArrays.intArrayAsList(a);
		Collections.reverse(list);
		assertThat(list.get(0), is(9));
		{
			/** 【補】抽象骨格実装の応用・疑似多重継承 */
			// 既存クラスで、抽象骨格実装を継承できない場合、
			// インターフェイスを実装し、かつ、
			// 抽象骨格実装を拡張したインスタンスをメンバに持ち、
			// インターフェイスへのメッセージをそこに転送する。
		}
		{
			/** 【補】抽象骨格実装の応用・単純実装 */
			// 骨格実装だが、抽象ではないクラス。
			// 継承しなくてもインスタンス化できるし、継承もできる。
			// java.util.AbstractMap.SimpleEntryなど。
		}

		o.l2("２").s("java.util.Map.Entryの抽象骨格実装であるAbstractMapEntryクラス。").e();
		class SimpleMapEntry<K, V> extends AbstractMapEntry<K, V> {
			@Override
			public K getKey() {
				return null;
			}

			@Override
			public V getValue() {
				return null;
			}
		}
		SimpleMapEntry<String, String> entry = new SimpleMapEntry<String, String>();
		assertThat(entry.getKey(), nullValue());
		assertThat(entry.getValue(), nullValue());
	}

	public static class IntArrays {
		static List<Integer> intArrayAsList(final int[] a) {
			if (a == null) {
				throw new NullPointerException();
			}

			// intとIntegerのアダプタ
			return new AbstractList<Integer>() {
				public Integer get(int i) {
					return a[i];
				}

				@Override
				public Integer set(int i, Integer val) {
					int oldVal = a[i];
					a[i] = val;
					return oldVal;
				}

				public int size() {
					return a.length;
				}
			};
		}
	}

	public static abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {
		public abstract K getKey();

		public abstract V getValue();

		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry<?, ?> arg = (Map.Entry<?, ?>) o;
			return equals(getKey(), arg.getKey()) && equals(getValue(), arg.getValue());
		}

		private static boolean equals(Object o1, Object o2) {
			return o1 == null ? o2 == null : o1.equals(o2);
		}

		@Override
		public int hashCode() {
			return hashCode(getKey()) ^ hashCode(getValue());
		}

		private static int hashCode(Object obj) {
			return obj == null ? 0 : obj.hashCode();
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("インタフェースは、複数の実装を許す型を定義する最善の方法。").e();
		o.l1("ただし、リリース後の変更は困難なため、徹底的にテストする。").e();
		o.l2("自分で複数実装してみるのがよい。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
