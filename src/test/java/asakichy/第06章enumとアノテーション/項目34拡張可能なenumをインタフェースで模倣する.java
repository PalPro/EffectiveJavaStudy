package asakichy.第06章enumとアノテーション;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目34拡張可能なenumをインタフェースで模倣する {

	@Test
	public void enum型の拡張とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("enum型はextendsできない。").e();
		o.l2("enum型はimplementsできる。").e();

		o.l1("【どうして？】").e();
		o.l2("enum型の実装拡張は、ほとんどの場合、間違った設計。").e();
		o.l2("言語仕様として抑制されている。").e();
	}

	@Test
	public void 拡張可能なenum型_オペコード() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("何らかのマシンに対する操作を表す要素を持つ列挙型。").e();
		o.l2("拡張可能な列挙型を使わざるを得ない数少ない例。").e();

		o.l1("【どうすれば？】").e();
		o.l2("オペコードに関するインタフェースを定義。").e();
		o.l2("そのインタフェースの標準実装であるenumを定義。").e();
		o.l2("そのインタフェースの拡張実装であるenumを定義。").e();

		o.l1("【たとえば？】").e();
		o.l2("インターフェイスOperation。").e();
		o.l2("インターフェイスを基本実装したBasicOperation列挙型。").e();
		o.l2("インターフェイスを拡張実装したExtendedOperation列挙型。").e();

		List<Double> result;
		double x = 10D;
		double y = 2D;
		// Operationを実装しているので、BasicOperation/ExtendedOperationどちらも渡せる
		result = calsAll(BasicOperation.class, x, y);
		assertThat(result, contains(12D, 8D, 20D, 5D));
		result = calsAll(ExtendedOperation.class, x, y);
		assertThat(result, contains(100D, 0D));
	}

	private static <T extends Enum<T> & Operation> List<Double> calsAll(Class<T> opSet, double x, double y) {
		List<Double> result = new ArrayList<Double>();
		for (Operation op : opSet.getEnumConstants()) {
			result.add(op.apply(x, y));
		}
		return result;
	}

	interface Operation {
		double apply(double x, double y);
	}

	enum BasicOperation implements Operation {
		PLUS("+") {
			public double apply(double x, double y) {
				return x + y;
			}
		},
		MINUS("-") {
			public double apply(double x, double y) {
				return x - y;
			}
		},
		TIMES("*") {
			public double apply(double x, double y) {
				return x * y;
			}
		},
		DIVIDE("/") {
			public double apply(double x, double y) {
				return x / y;
			}
		};
		private final String symbol;

		BasicOperation(String symbol) {
			this.symbol = symbol;
		}

		@Override
		public String toString() {
			return symbol;
		}
	}

	public enum ExtendedOperation implements Operation {
		EXP("^") {
			public double apply(double x, double y) {
				return Math.pow(x, y);
			}
		},
		REMAINDER("%") {
			public double apply(double x, double y) {
				return x % y;
			}
		};

		private final String symbol;

		ExtendedOperation(String symbol) {
			this.symbol = symbol;
		}

		@Override
		public String toString() {
			return symbol;
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("enum型はインタフェース継承なら可能。").e();
		o.l1("enum型をインタフェースを使って書いておくと、拡張を模倣できる。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
