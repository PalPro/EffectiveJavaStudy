package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目20タグ付クラスよりクラス階層を選ぶ {

	@Test
	public void タグ付クラスとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インスタンスが2種類以上の特性持つ。").e();
		o.l2("その特性を特定するための「タグ」フィールドを持つ。").e();
		o.l2("ほとんどの場合、不適切な実装方法。").e();

		o.l1("【どうして？】").e();
		o.l2("複数の実装が単一クラスにあり、読みやすさが損なわれる。").e();
		o.l2("自分の特性以外の属性も保持することになり、メモリが増加。").e();
		o.l2("コンストラクタで自分以外の特性の属性まで初期化出来ないので、全部をfinalにできない。").e();
		o.l2("特性毎の処理のswitch文が多発する。").e();

		o.l1("【たとえば？】").e();
		o.l2("FigureTaggedクラス。").e();
		FigureTagged rectangle = new FigureTagged(10, 10);
		assertThat(rectangle.area(), is(100.0));
		FigureTagged circle = new FigureTagged(10);
		assertThat(circle.area(), is(314.1592653589793));
	}

	private static class FigureTagged {
		enum Shape {
			RECTANGLE, CIRCLE
		};

		// これが「タグ」フィールド
		final Shape shape;

		// タグが「RECTANGLE」のときだけ必要なフィールド
		double length;
		double width;

		// タグが「CIRCLE」のときだけ必要なフィールド
		double radius;

		// RECTANGLE用のコンストラクタ
		FigureTagged(double length, double width) {
			shape = Shape.RECTANGLE;
			this.length = length;
			this.width = width;
		}

		// CIRCLE用のコンストラクタ
		FigureTagged(double radius) {
			shape = Shape.CIRCLE;
			this.radius = radius;
		}

		// 面積を求めるクラスだが、switch文による分岐を含んでいる
		double area() {
			switch (shape) {
			case RECTANGLE:
				return length * width;
			case CIRCLE:
				return Math.PI * (radius * radius);
			default:
				throw new AssertionError();
			}
		}
	}

	@Test
	public void 代替_クラス階層() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("複数の特性のオブジェクトを表現できる単一のデータ型を定義するには、サブタイプ化を使用。").e();
		o.l2("タグ付クラスは、クラス階層の誤った模倣に過ぎない。").e();

		o.l1("【どうして？】").e();
		o.l2("データに、関連する操作がついていきやすい。").e();
		o.l2("無関係なフィールドや操作は同居せず、コードは簡潔で明瞭に。").e();
		o.l2("フィールドはすべてfinalに。").e();
		o.l2("型間の自然な階層を反映し、柔軟性が増大する。").e();
		o.l2("コンパイル時の型検査も可能。").e();

		o.l1("【たとえば？】").e();
		o.l2("Figureクラスをルートとしたクラス階層。").e();
		Figure rectangle = new Rectangle(10, 10);
		assertThat(rectangle.area(), is(100.0));
		Figure circle = new Circle(10);
		assertThat(circle.area(), is(314.1592653589793));
		{
			/** 【補】クラス階層の柔軟性 */
			// クラス階層に新たな特性を追加するのも簡単。
			class Square extends Rectangle {
				// 正方形
				Square(double side) {
					super(side, side);
				}
			}
			Figure squere = new Square(10);
			assertThat(squere.area(), is(100.0));
		}
	}

	private static abstract class Figure {
		abstract double area();
	}

	private static class Rectangle extends Figure {
		final double length;
		final double width;

		Rectangle(double length, double width) {
			this.length = length;
			this.width = width;
		}

		double area() {
			return length * width;
		}
	}

	private static class Circle extends Figure {
		final double radius;

		Circle(double radius) {
			this.radius = radius;
		}

		double area() {
			return Math.PI * (radius * radius);
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("オブジェクト指向は、クラス階層により、タグつきクラスを自然に表現する能力を持つ。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
