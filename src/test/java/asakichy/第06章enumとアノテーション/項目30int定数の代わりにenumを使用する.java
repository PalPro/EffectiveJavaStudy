package asakichy.第06章enumとアノテーション;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目30int定数の代わりにenumを使用する {

	@Test
	public void 列挙型とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("固定数の定数からその値が成り立つ型。").e();

		o.l1("【たとえば？】").e();
		o.l2("季節、太陽系の惑星、トランプのデッキ内のスーツなど。").e();
	}

	@Test
	public void 実装方法_int定数() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("名前付きint定数のグループを宣言。").e();

		o.l1("【たとえば？】").e();
		o.l2("Fruitsクラス。").e();

		@SuppressWarnings("unused")
		class Fruits {
			public static final int APPLE_FUJI = 0;
			public static final int APPLE_PIPPIN = 1;
			public static final int APPLE_GRANNY_SMITH = 2;

			public static final int ORANGE_NAVEL = 0;
			public static final int ORANGE_TEMPLE = 1;
			public static final int ORANGE_BLOOD = 2;
		}
		assertThat(Fruits.APPLE_FUJI, is(0));

		o.l1("【どうすれば？】").e();
		o.l2("多くの欠点を持っているので、使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("コンパイラチェックが使えない。").e();
		o.l3("オレンジ渡すところにアップル渡しても、何も文句を言わない。").e();
		o.l2("名前空間がネーミングルールになってる。").e();
		o.l3("強制力がない。").e();
		o.l2("定数がコンパイル時に埋め込まれる。").e();
		o.l3("定数の方だけコンパイルしたら、クライアントの動作が不整合に陥る。").e();
		o.l2("表示可能な文字列に変換出来ない").e();
		o.l3("デバッグしても数字見えるだけで、直感的でなく、わかりにくい。").e();
		o.l2("定数をすべてイテレートする機能ない。").e();
		o.l3("数すら教えてくれない。").e();
	}

	@Test
	public void 実装方法_String定数() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("名前付きString定数のグループを宣言。").e();

		o.l1("【たとえば？】").e();
		o.l2("Fruitsクラス。").e();

		class Fruits {
			public static final String APPLE_FUJI = "AppleFuji";
			// ....
		}
		assertThat(Fruits.APPLE_FUJI, is("AppleFuji"));

		o.l1("【どうすれば？】").e();
		o.l2("int定数にプラスして悪いところがあるので、使用しない。").e();

		o.l1("【どうして？】").e();
		o.l2("パフォーマンスが悪くなる可能性あり。").e();
		o.l2("定数の内容をリテラルで書いてしまい、それがスペルミスしていたら、即バグ。").e();
	}

	@Test
	public void 実装方法_enum型() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("タイプセーフenumパターンのJava言語実装。").e();
		o.l2("要は、public static final フィールドを通して、");
		o.__("個々の列挙定数に対して1つのインスタンスを公開しているクラス。").e();
		o.l2("アクセス可能なコンスタラクタがないので、事実上finalクラス。").e();
		o.l3("インスタンス化できない。").e();
		o.l3("宣言されたenum定数以外のインスタンスは存在しない。").e();
		o.l2("extends出来ない。").e();

		o.l1("【たとえば？】").e();
		o.l2("Apple・Orangeの種類をenum型で。").e();

		assertThat(Apple.FUJI, is(not(Apple.PIPPIN)));
		assertThat(Orange.NAVEL, is(Orange.NAVEL));

		// 別enumの比較は、きちんとコンパイルエラーになる。
		// assertThat(Apple.FUJI, is(Orange.NAVEL));

		o.l1("【どうすれば？】").e();
		o.l2("JDK1.5以上であれば、必ずenum型を使用する。").e();

		o.l1("【どうして？】").e();
		o.l2("コンパイル時の型安全を提供。").e();
		o.l2("名前空間があるので、同一名の定数が共存可能。").e();
		o.l3("クライアントのリコンパイルなく、定数の追加・変更が可能。").e();
		o.l2("Objectのすべてのメソッドの高品質な実装の提供。").e();
		o.l2("Comparable/Serializableの高品質な実装の提供。").e();
		o.l2("フィールドやメソッドの追加が可能。").e();
		o.l2("インターフェイスの実装が可能。").e();
	}

	public enum Apple {
		FUJI, PIPPIN, GRANNY_SMITH
	}

	public enum Orange {
		NAVEL, TEMPLE, BLOOD
	}

	@Test
	public void enum型_フィールドとメソッドの追加() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("定数にデータを関連付けたフィールドを作成したり、");
		o.__("定数に関連した情報を取得するメソッドを作成できる。").e();

		o.l1("【どうして？】").e();
		o.l2("単なるenum定数のコレクションとして始まっても、後に、完全装備の抽象化へと発展できる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("データを受け取るコンストラクタを書いて、そのフィールドにデータを保存。").e();
		o.l2("enumは不変なので、すべてのフィールドはfinalであるべき。").e();
		o.l2("メソッドは、通常インスタンスメソッドと変わらない。").e();

		o.l1("【たとえば？】").e();
		o.l2("太陽系の８個の惑星をあらわすPlanet列挙型。").e();
		o.l3("質量と半径を持つ。").e();
		o.l3("惑星表面上での物体の重さを計算できる。").e();

		// フィールドデータを取得
		assertThat(Planet.EARTH.mass(), is(5.975e+24));
		assertThat(Planet.EARTH.radius(), is(6.378e6));

		// 重さの計算メソッド
		double earthWeight = 100D;
		double mass = earthWeight / Planet.EARTH.surfaceGravity();
		assertThat(Planet.EARTH.surfaceWeight(mass), is(earthWeight));

		// enum定数を列挙するvaluesメソッド（組み込み）
		List<String> planets = new ArrayList<String>();
		for (Planet p : Planet.values()) {
			planets.add(p.toString());
		}
		assertThat(planets, contains("MERCURY", "VENUS", "EARTH", "MARS", "JUPITER", "SATURN", "URANUS", "NEPTUNE"));
	}

	enum Planet {
		MERCURY(3.302e+23, 2.439e6),

		VENUS(4.869e+24, 6.052e6),

		EARTH(5.975e+24, 6.378e6),

		MARS(6.419e+23, 3.393e6),

		JUPITER(1.899e+27, 7.149e7),

		SATURN(5.685e+26, 6.027e7),

		URANUS(8.683e+25, 2.556e7),

		NEPTUNE(1.024e+26, 2.477e7);

		private final double mass; // 単位：キログラム
		private final double radius; // 単位：メートル
		private final double surfaceGravity; // 単位： m / s^2

		// 万有引力定数 単位： m^3 / kg s^2
		private static final double G = 6.67300E-11;

		// コンストラクタ
		Planet(double mass, double radius) {
			this.mass = mass;
			this.radius = radius;
			surfaceGravity = G * mass / (radius * radius);
		}

		public double mass() {
			return mass;
		}

		public double radius() {
			return radius;
		}

		public double surfaceGravity() {
			return surfaceGravity;
		}

		public double surfaceWeight(double mass) {
			return mass * surfaceGravity; // F = ma
		}
	}

	@Test
	public void enum型_クラスレベル() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("一般に有用なら、トップレベルにする。").e();
		o.l2("あるクラスでのみ使用されるなら、そのクラスのメンバクラスにする。").e();

		o.l1("【たとえば？】").e();
		o.l2("小数点部の丸めモードを列挙しているjava.math.RoundingMode。").e();
		o.l3("たしかに、java.math.BigDecimalのみが使用している。").e();
		o.l3("ただし、BigDecimalに結びつかない根本的な抽象を表しているので、トップレベルとなっている。").e();
	}

	@Test
	public void enum型_定数固有メソッド() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("定数ごとに別々のメソッドを定義できる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("抽象メソッドを宣言し、定数固有クラス本体オーバーライド。").e();

		o.l1("【たとえば？】").e();
		o.l2("四則演算の操作を表すOperation列挙型。").e();

		double x = 10D;
		double y = 10D;
		assertThat(Operation.PLUS.apply(x, y), is(20D));
		assertThat(Operation.MINUS.apply(x, y), is(0D));
		assertThat(Operation.TIMES.apply(x, y), is(100D));
		assertThat(Operation.DIVIDE.apply(x, y), is(1D));
	}

	enum Operation {
		PLUS {
			double apply(double x, double y) {
				return x + y;
			}
		},
		MINUS {
			double apply(double x, double y) {
				return x - y;
			}
		},
		TIMES {
			double apply(double x, double y) {
				return x * y;
			}
		},
		DIVIDE {
			double apply(double x, double y) {
				return x / y;
			}
		};

		abstract double apply(double x, double y);

	}

	@Test
	public void enum型_文字列表現() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("定数名を返すtoString()、定数名から定数を返すvalueOf()は自動生成されている。").e();
		o.l2("しかし、toString()について、定数名よりも、より適した文字列表現を返したい場合がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("toString()をオーバライドする。").e();
		o.l2("かつ、セットで、valueOf()相当のfromString()メソッドを追加する。").e();

		o.l1("【たとえば？】").e();
		o.l2("四則演算の操作を表すOperation列挙型のtoString()を改造し、");
		o.__("かつfromString()を追加したOperationEx列挙型。").e();

		// Operationの自動生成された、toString()とvalueOf()
		assertThat(Operation.PLUS.toString(), is("PLUS"));
		assertThat(Operation.valueOf("PLUS"), is(Operation.PLUS));

		// OperationのtoString()をカスタマイズし、より適した文字列表現へ
		assertThat(OperationEx.PLUS.toString(), is("+"));
		// カスタマイズした文字列表現と対になる、定数取得メソッド
		assertThat(OperationEx.fromString("+"), is(OperationEx.PLUS));
		// もともとある定数取得メソッドであるvalueOf()はそのまま残しておく
		assertThat(OperationEx.valueOf("PLUS"), is(OperationEx.PLUS));
	}

	enum OperationEx {
		PLUS("+") {
			double apply(double x, double y) {
				return x + y;
			}
		},
		MINUS("-") {
			double apply(double x, double y) {
				return x - y;
			}
		},
		TIMES("*") {
			double apply(double x, double y) {
				return x * y;
			}
		},
		DIVIDE("/") {
			double apply(double x, double y) {
				return x / y;
			}
		};
		private final String symbol;

		OperationEx(String symbol) {
			this.symbol = symbol;
		}

		@Override
		public String toString() {
			return symbol;
		}

		abstract double apply(double x, double y);

		private static final Map<String, OperationEx> stringToEnum = new HashMap<String, OperationEx>();
		static {
			for (OperationEx op : values())
				stringToEnum.put(op.toString(), op);
		}

		public static OperationEx fromString(String symbol) {
			return stringToEnum.get(symbol);
		}
	}

	@Test
	public void enum型_実装の共有() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("enum定数間で、実装を共有したい時がある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("戦略的enumパターンを使用する。").e();
		o.l3("計算部分をprivateのネストしたenumへ移動し、これがStrategy。").e();
		o.l3("このネストenumのインスタンスを、親enumコンストラクタへ渡す。").e();

		o.l1("【たとえば？】").e();
		o.l2("PayrollDay列挙型。").e();
		
		// 平日
		assertThat(PayrollDay.MONDAY.pay(10D, 1.5D), is(16.5D));
		// 休日
		assertThat(PayrollDay.SUNDAY.pay(10D, 1.5D), is(22.5D));
	}

	enum PayrollDay {
		MONDAY(PayType.WEEKDAY),

		TUESDAY(PayType.WEEKDAY),

		WEDNESDAY(PayType.WEEKDAY),

		THURSDAY(PayType.WEEKDAY),

		FRIDAY(PayType.WEEKDAY),

		SATURDAY(PayType.WEEKEND),

		SUNDAY(PayType.WEEKEND);

		private final PayType payType;

		PayrollDay(PayType payType) {
			this.payType = payType;
		}

		double pay(double hoursWorked, double payRate) {
			// 戦略enumに委譲
			return payType.pay(hoursWorked, payRate);
		}

		// 戦略的enum
		private enum PayType {
			// 平日は8時間を超えた部分が割り増しとなる
			WEEKDAY {
				double overtimePay(double hours, double payRate) {
					return hours <= HOURS_PER_SHIFT ? 0 : (hours - HOURS_PER_SHIFT) * payRate / 2;
				}
			},
			// 休日はすべて割り増しとなる
			WEEKEND {
				double overtimePay(double hours, double payRate) {
					return hours * payRate / 2;
				}
			};
			private static final int HOURS_PER_SHIFT = 8;

			abstract double overtimePay(double hrs, double payRate);

			double pay(double hoursWorked, double payRate) {
				double basePay = hoursWorked * payRate;
				return basePay + overtimePay(hoursWorked, payRate);
			}
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("従来のintやStringによる定数型より、必ずenum型を使用すること。").e();
		o.l1("enum型は、より読みやすく、安全で、拡張可能な定数型を実現できる。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
