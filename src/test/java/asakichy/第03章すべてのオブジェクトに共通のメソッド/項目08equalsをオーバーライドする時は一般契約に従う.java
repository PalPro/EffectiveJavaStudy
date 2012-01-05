package asakichy.第03章すべてのオブジェクトに共通のメソッド;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目08equalsをオーバーライドする時は一般契約に従う {

	@Test
	public void equalsメソッドとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクト同士の論理的等価性を判断するメソッド。").e();

		o.l1("【どうすれば？】").e();
		o.l2("Object#equals()をオーバライド。").e();
	}

	@Test
	public void オーバーライドすべきではない場合_実体クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("値ではなく、実体を表しているクラス。").e();
		o.l2("クラスの個々のインスタンスが本質的に一意。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Thread。").e();
		o.l2("実装はObject#equals()で十分。").e();
		assertThat(new Thread(), is(not(new Thread())));
	}

	@Test
	public void オーバーライドすべきではない場合_論理的等価性不要クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラスが論理的等価性に興味がない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Random。").e();
		o.l2("実装はObject#equals()で十分。").e();
		assertThat(new Random(), is(not(new Random())));
	}

	@Test
	public void オーバーライドすべきではない場合_スーパクラス実装を使用() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("スーパクラスの振る舞いがこのクラスに対して適切。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.ArrayList。").e();
		o.l2("実装はjava.util.AbstractList#equals()を利用している。").e();
		List<String> list1 = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("a");
				add("b");
			}
		};
		List<String> list2 = Arrays.asList(new String[] { "a", "b" });
		assertThat(list1, is(list2));
	}

	@Test
	public void オーバーライドすべきではない場合_privateクラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("privateクラスであり、equalsが決して呼ばれない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("とはいえ、内部呼び出しの可能性はある。").e();
		o.l2("念のため、呼び出し時例外発行するなど、ガード実装しておく。").e();

		o.l1("【たとえば？】").e();
		o.l2("equals()をオーバライドして例外発行。").e();
		new Object() {
			@Override
			public boolean equals(Object obj) {
				throw new AssertionError();
			}
		};
	}

	@Test
	public void オーバーライドすべきではない場合_enum() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("列挙型は、論理的等価性とオブジェクトの等価性が同一。").e();

		o.l1("【たとえば？】").e();
		o.l2("Genderを表現するenum。").e();
		assertThat(Gender.MAN, is(Gender.MAN));
	}

	enum Gender {
		MAN, WOMAN
	};

	@Test
	public void オーバーライドすべき場合_値クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("論理的等価性という概念を持つ「値」クラス。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Integer。").e();
		assertThat(Integer.valueOf(1), is(Integer.valueOf(1)));

		o.l1("【どうすれば？】").e();
		o.l2("「一般契約」を厳守し、同値関係を実装する。").e();
		o.l2("契約を守らないと、契約を前提とした他のクラスとコラボできない。").e();
	}

	@Test
	public void 一般契約_反射的() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("reflexive。").e();
		o.l2("nullでない任意の参照値xに対して、");
		o.__("x.equals(x)はtrueを返さなければならない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.String。").e();
		String x = "abc";
		assertThat(x.equals(x), is(true));
	}

	@Test
	public void 一般契約_対称的() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("symmetric。").e();
		o.l2("nullでない任意の参照値xとyに対して、");
		o.__("y.equals(x)がtrueを返す場合のみ、");
		o.__("x.equals(y)はtrueを返さなければならない。").e();

		o.l1("【たとえば？】").e();
		o.l2("CaseInsensitiveStringクラスにおける対称性違反とその対応。").e();
		CaseInsensitiveString x = new CaseInsensitiveString("Polish");
		CaseInsensitiveString xx = new CaseInsensitiveString("polish");
		String y = "polish";
		assertThat(x.equals(xx), is(true));
		// 対称性違反！
		assertThat(x.equals(y), is(true));
		assertThat(y.equals(x), is(false));

		// 改善
		x = new CaseInsensitiveString("Polish") {
			@Override
			public boolean equals(Object o) {
				return o instanceof CaseInsensitiveString && ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
			}
		};
		assertThat(x.equals(xx), is(true));
		// 対称的！
		assertThat(x.equals(y), is(false));
		assertThat(y.equals(x), is(false));

	}

	private class CaseInsensitiveString {
		protected final String s;

		public CaseInsensitiveString(String s) {
			this.s = s;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof CaseInsensitiveString) {
				return s.equalsIgnoreCase(((CaseInsensitiveString) o).s);
			}
			if (o instanceof String) {
				return s.equalsIgnoreCase((String) o);
			}
			return false;
		}
	}

	@Test
	public void 一般契約_推移的() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("transitive。").e();
		o.l2("nullでない任意の参照値xとyとzに対して、");
		o.__("x.equals(y)とy.equals(z)がtrueを返すなら、");
		o.__("x.equals(z)はtrueを返さなければならない。").e();

		o.l1("【たとえば？】").e();
		o.l2("ColorPointクラスにおける推移性違反とその対応。").e();
		ColorPoint x = new ColorPoint(1, 2, Color.RED);
		Point y = new Point(1, 2);
		ColorPoint z = new ColorPoint(1, 2, Color.BLUE);
		assertThat(x.equals(y), is(true));
		assertThat(y.equals(z), is(true));
		assertThat(x.equals(z), is(false));// 推移性違反！

		{
			/** 【補】equalsにおけるgetClassによる型チェック */
			// リスコフの置換原則違反になるので、誤った実装方法。
		}

		// コンポジション設計で対応
		ColorPoint_Composition xx = new ColorPoint_Composition(1, 2, Color.RED);
		Point yy = new Point(1, 2);
		ColorPoint_Composition zz = new ColorPoint_Composition(1, 2, Color.BLUE);
		// Pointビューを使って、Pointとしての推移性を保つ
		assertThat(xx.asPoint().equals(yy), is(true));
		assertThat(yy.equals(zz.asPoint()), is(true));
		assertThat(xx.asPoint().equals(zz.asPoint()), is(true));
		// ColorPointは、色まで比較
		assertThat(xx.equals(zz), is(false));

		o.l1("【どうすれば？】").e();
		o.l2("オブジェクト指向における抽象化の恩恵をあきらめずに、");
		o.__("インスタンス化可能な(＝abstractではない)クラスを拡張して、");
		o.__("equals契約を守ったまま値要素を追加する方法は「ない」。").e();
		o.l2("java.sql.Timestampは「悪例」。").e();
		o.l3("java.util.Date を 継承して「nanoseconds」を加えてしまった。").e();
	}

	private static class Point {
		private final int x;
		private final int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Point)) {
				return false;
			}
			Point p = (Point) o;
			return p.x == x && p.y == y;
		}

		@Override
		public int hashCode() {
			return 31 * x + y;
		}
	}

	enum Color {
		RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
	}

	private static class ColorPoint extends Point {
		private final Color color;

		public ColorPoint(int x, int y, Color color) {
			super(x, y);
			this.color = color;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof ColorPoint)) {
				return o.equals(this); // 色を無視した比較
			}
			return super.equals(o) && ((ColorPoint) o).color == color;
		}
	}

	private static class ColorPoint_Composition {
		private final Point point;
		private final Color color;

		public ColorPoint_Composition(int x, int y, Color color) {
			point = new Point(x, y);
			this.color = color;
		}

		public Point asPoint() {
			return point;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof ColorPoint_Composition)) {
				return false;
			}
			ColorPoint_Composition cp = (ColorPoint_Composition) o;
			return cp.point.equals(point) && cp.color.equals(color);
		}

		@Override
		public int hashCode() {
			return point.hashCode() * 33 + color.hashCode();
		}
	}

	@Test
	public void 一般契約_整合的() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("consistent。").e();
		o.l2("nullでない任意の参照値xとyに対して、");
		o.__("equals比較に使用されている情報が変更されなければ、");
		o.__("x.equals(y)は終始一貫して同一の結果を返さなければならない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.String。").e();
		String x = "abc";
		String y = "abc";
		assertThat(x.equals(y), is(true));
		assertThat(x.equals(y), is(true));

		o.l1("【どうすれば？】").e();
		o.l2("信頼できない外部リソースに依存しないこと。").e();
		o.l2("外の資源の状態に依らず、メモリ内の情報だけで計算を行うべき。").e();
		o.l2("例えば、ホスト名とIPアドレスの比較は、");
		o.__("ネットワークの状態によって、恒久的に同じ意味とならない場合がある。").e();
	}

	@Test
	public void 一般契約_非null的() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("non-null。").e();
		o.l2("nullでない任意の参照値xに対して、");
		o.__("x.equals(null)はfalseを返さなければならない。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.String。").e();
		String x = "abc";
		assertThat(x.equals(null), is(false)); // NullPointerExceptionを返さない
	}

	@Test
	public void 正しい実装_手順() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("一般契約を受け、具体的な実装手順。").e();
		o.l2("１").s("引数が自分自身かチェック。").e();
		o.l2("２").s("引数が正しい型かチェック。").e();
		o.l2("３").s("引数を正しい型にキャスト。").e();
		o.l2("４").s("意味のあるフィールドを比較。").e();
		o.l2("５").s("「対称的？」「推移的？」「整合的？」と自問する。").e();

		o.l1("【たとえば？】").e();
		o.l2("Personクラス。").e();
		final class Person {
			private String name;

			public Person(String name) {
				this.name = name;
			}

			@Override
			public boolean equals(Object o) {
				// １ 引数が自分自身かチェック。
				if (o == this) {
					return true;
				}
				// ２ 引数が正しい型かチェック。
				if (!(o instanceof Person)) {
					return false;// nullチェックも兼ねている
				}
				// ３ 引数を正しい型にキャスト。
				Person p = (Person) o;
				// ４ 意味のあるフィールドを比較。
				return name.equals(p.name);
			}
		}
		// ５ 「対称的？」「推移的？」「整合的？」と自問する。
		assertThat(new Person("asakichy"), is(new Person("asakichy")));

	}

	@Test
	public void 正しい実装_フィールド比較() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("フィールドの型ごとに、適切な比較を行う。").e();

		o.l1("【たとえば？】").e();
		o.l2("Typeクラス。").e();
		final class Type {
			private String s = "s";
			private float f = 1.1F;
			private int i = 1;

			@Override
			public boolean equals(Object o) {
				// ...
				Type hoge = (Type) o;
				// オブジェクト比較のイディオム
				boolean sameS = (s == null ? hoge.s == null : s.equals(hoge.s));
				// -> 若干速い版
				sameS = (s == hoge.s || s != null && s.equals(hoge.s));

				// プリミティブは通常「==」でよいが、float/doubleはラッパを使用
				// Float.NaN同士の比較等を正しく行ってくれる
				// http://java.sun.com/javase/ja/6/docs/ja/api/java/lang/Float.html#equals%28java.lang.Object%29
				boolean sameI = (i == hoge.i);
				boolean sameF = (Float.compare(f, hoge.f) == 0);

				return sameS && sameI && sameF;
			}

		}
		assertThat(new Type(), is(new Type()));
	}

	@Test
	public void 間違った実装_hashCodeをオーバーライドしていない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("equalsをオーバライドしているのに、hashCodeをオーバーライドしていない。").e();

		o.l1("【どうして？】").e();
		o.l2("hashCodeの契約違反となり、それを前提としたクラスとコラボできなくなる。");
	}

	@Test
	public void 間違った実装_妙に賢い() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("同値性を調べるのに、賢すぎるとトラブルに陥りやすい。").e();

		o.l1("【どういうこと？】").e();
		o.l2("例えば、Fileクラスは、シンボリックリンクを追いかけてはいけない。").e();
	}

	@Test
	public void 間違った実装_引数をObject以外に変更() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("引数シグニチャを他の型で置き換える。").e();

		o.l1("【どうして？】").e();
		o.l2("オーバーロードになって、呼ばれなくなる。").e();

		o.l1("【たとえば？】").e();
		o.l2("シグニチャをObject以外に変えたequals()。").e();
		@SuppressWarnings("unused")
		Object o = new Object() {
			// 本当は、@Overrideを付ければ誤りを検知できる。
			public boolean equals(String obj) {
				return true;
			}
		};
		// 実装したequalsは呼ばれない！
		assertThat(o, is(not(new Object())));
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("equalsの実装は契約とルールだらけ。").e();
		o.l1("インスタンス化できるクラスの継承が絡むときには諦めも肝心。").e();

		o.l1("品質向上・工数削減のため、ツールやライブラリの仕様を検討する。").e();
		o.l2("org.apache.commons.lang.builder.EqualsBuilder。").e();
		o.l2("EclipseのSource Generate機能。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
