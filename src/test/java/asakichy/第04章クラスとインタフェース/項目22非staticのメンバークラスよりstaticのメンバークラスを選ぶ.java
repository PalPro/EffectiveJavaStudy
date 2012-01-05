package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目22非staticのメンバークラスよりstaticのメンバークラスを選ぶ {

	@Test
	public void ネストしたクラスとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("他のクラス内に定義されたクラス。").e();
		o.l2("４種類ある。").e();
		o.l3("staticのメンバークラス。").e();
		o.l3("非staticのメンバークラス。").e();
		o.l3("無名クラス。").e();
		o.l3("ローカルクラス。").e();
		o.l2("「staticのメンバークラス」以外は「インナークラス」という。").e();

		o.l1("【どうすれば？】").e();
		o.l2("エンクロージングクラスに対して仕えるためだけに存在すべき。").e();
		o.l2("エンクロージングクラス以外のクラスに有用ならば、トップレベルにするべき。").e();
	}

	@Test
	public void ネストしたクラス_staticのメンバークラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("「静的ネストクラス」ともいう。").e();
		o.l2("通常のクラスとほぼ同じで、エンクロージングクラスとのアクセス制限がなくなるだけ。").e();
		o.l3("ネストクラスから、エンクロージングクラスのprivateにアクセスできる。").e();
		o.l3("エンクロージングクラスから、ネストクラスのprivateにアクセスできる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Outer4StaticMemberエンクロージングクラス。").e();
		o.l2("StaticMemberClassクラスはstaticのメンバークラス。").e();

		Outer4StaticMember outer = new Outer4StaticMember();
		Outer4StaticMember.StaticMemberClass staticNestedClass = new Outer4StaticMember.StaticMemberClass();
		// ネストクラスのprivateが呼び出せる
		assertThat(staticNestedClass.getOuterField(outer), is("outer"));

		o.l1("【どうすれば？】").e();
		o.l2("publicの場合、エンクロージングクラスと一緒に使用すると有用なヘルパークラスとして。").e();
		o.l3("たとえば、計算クラスにおいて、オペレーションクラスを提供する場合。").e();
		o.l2("privateの場合、エンクロージングクラスの内部的な構成要素として。").e();
		o.l3("たとえば、java.util.Mapに対する、java.util.Map.Entry。").e();
	}

	private static class Outer4StaticMember {
		private String outerField = "outer";

		private static class StaticMemberClass {
			private String getOuterField(Outer4StaticMember outer) {
				// エンクロージングクラスのprivateにアクセスできる
				return outer.outerField;
			}
		}
	}

	@Test
	public void ネストしたクラス_非staticのメンバークラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("狭義の「インナークラス」。").e();
		o.l2("エンクロージングインスタンスと暗黙に関連付けされる。").e();
		o.l3("関連付けされたインスタンスをインナークラスから参照できる。").e();
		o.l2("エンクロージングクラスとのアクセス制限がなくなる。").e();
		o.l3("ネストクラスから、エンクロージングクラスのprivateにアクセスできる。").e();
		o.l3("エンクロージングクラスから、ネストクラスのprivateにアクセスできる。").e();

		o.l1("【たとえば？】").e();
		o.l2("Outer4NonStaticMemberエンクロージングクラス。").e();
		o.l2("NonStaticMemberClassは非staticのメンバークラス。").e();

		// エンクロージングインスタンスと自動で関連付く
		Outer4NonStaticMember outer = new Outer4NonStaticMember();
		assertThat(outer.inner.getOuterField(), is("outer"));

		// 明示的に関連付けを指定することもできる
		outer = new Outer4NonStaticMember();
		Outer4NonStaticMember.NonStaticMemberClass inner = outer.new NonStaticMemberClass();
		assertThat(inner.getOuterField(), is("outer"));
		assertThat(inner.getOuterFieldByOuter(), is("outer"));

		o.l1("【どうすれば？】").e();
		o.l2("エンクロージングクラスのインスタンスを、");
		o.__("関係のないクラスのインスタンスとして見なすことを可能にするアダプター。").e();
		o.l3("たとえば、java.util.Map#keySet()から返される、コレクションビュー。").e();
	}

	private static class Outer4NonStaticMember {
		private String outerField = "outer";
		// エンクロージングインスタンスと自動で関連付く
		private NonStaticMemberClass inner = new NonStaticMemberClass();

		private class NonStaticMemberClass {
			private String getOuterField() {
				// 暗黙に関連付いたエンクロージングインスタンスのprivateフィールドを取得
				return outerField;
			}

			private String getOuterFieldByOuter() {
				// thisはインナーインスタンスで、
				// {アウタークラス}.thisがエンクロージングインスタンス
				return Outer4NonStaticMember.this.outerField;
			}
		}
	}

	@Test
	public void ネストしたクラス_無名クラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("式が書けるところに書ける。").e();
		o.l2("クラス宣言と同時にインスタンス化。").e();
		o.l2("手軽だが、多くの制限がある。").e();
		o.l3("後からインスタンス化できない。").e();
		o.l3("instance ofで検査できない。").e();
		o.l3("staticメンバー持てない。").e();
		o.l3("１つしか実装（implements）できない。").e();
		o.l3("スーパータイプのメソッドしか呼べない。").e();
		o.l3("短くないと読みにくい。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.util.Comparatorを実装した無名クラス。").e();
		String[] stringArray = new String[] { "333", "4444", "1", "22" };

		// 無名クラス：静的ネストクラス版
		Arrays.sort(stringArray, Outer4Noname.NONAME_CLASS_STATIC);
		assertThat(stringArray, arrayContaining("1", "22", "333", "4444"));

		// 無名クラス：インナークラス版
		stringArray = new String[] { "333", "4444", "1", "22" };
		Outer4Noname outer = new Outer4Noname();
		Arrays.sort(stringArray, outer.nonameClass);
		assertThat(stringArray, arrayContaining("4444", "333", "22", "1"));

		o.l1("【どうすれば？】").e();
		o.l2("関数オブジェクトに使用。").e();
		o.l3("Strategyパターンで。").e();
		o.l2("プロセスオブジェクトに使用。").e();
		o.l3("java.lang.Runnableで。").e();
		o.l2("staticファクトリメソッドに使用。").e();
		o.l3("newするクラスが無名クラス。").e();
	}

	private static class Outer4Noname {
		// 無名クラス：静的ネストクラス版
		private static Comparator<String> NONAME_CLASS_STATIC = new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.length() - s2.length();
			}

		};

		enum SortDirection {
			ASCEND, DESCEND
		}

		private SortDirection sortDirection = SortDirection.DESCEND;
		// 無名クラス：インナークラス版
		private Comparator<String> nonameClass = new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				// 無名でも、非staticの文脈なら、暗黙エンクロージングインスタンスを持つ
				if (sortDirection == SortDirection.DESCEND) {
					return s2.length() - s1.length();
				} else {
					return s1.length() - s2.length();
				}
			}

		};
	}

	@Test
	public void ネストしたクラス_ローカルクラス() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("ローカル変数が定義できるところに定義できる。").e();
		o.l2("使用頻度は少ないし、制限もある。").e();
		o.l3("staticメンバー持てない。").e();
		o.l3("短くないと読みにくい。").e();

		o.l1("【たとえば？】").e();
		o.l2("メソッド内に定義したLocalClassクラス。").e();
		class LocalClass {
			private String getOuterField() {
				// 暗黙に関連付いたエンクロージングインスタンスのprivateフィールドを取得
				return outerField4LocalClass;
			}
		}
		LocalClass localClass = new LocalClass();
		assertThat(localClass.getOuterField(), is("outer"));
	}

	private String outerField4LocalClass = "outer";

	@Test
	public void ネストしたクラス_選択基準() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("それぞれの特徴があり、選択にはガイドラインがある。").e();

		o.l1("【どうすれば？】").e();
		o.l2("メソッドの外からも見える必要があったり、メソッド内では長すぎなら、「メンバー」へ。").e();
		o.l2("「メンバー」を選んだ場合。").e();
		o.l2("エンクロージングインスタンスヘの参照が必要ならば「非static」、でなければ「static」へ。").e();
		o.l2("「メンバー」を選ばなかった場合。").e();
		o.l2("メソッド内に属しているべきであり、１ケ所からのみインスタンスを生成する必要があり、");
		o.__("そのクラスを特徴付ける型がすでに存在していれば「無名クラス」、でなければ「ローカルクラス」。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("エンクロージングインスタンスをいじらないなら、必ず「static」の方を使う。").e();
		o.l1("エンクロージングとの関連がないので、インスタンス化が容易だし、メモリも節約できるので。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
