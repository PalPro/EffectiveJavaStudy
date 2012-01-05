package asakichy.第04章クラスとインタフェース;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目13クラスとメンバーへのアクセス可能性を最小限にする {
	@Test
	public void 情報隠蔽とは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("モジュールの実装の詳細をすべて隠蔽し、実装とAPIをはっきりと分離。").e();
		o.l2("モジュールはAPIを通してのみ他のモジュールと通信し、");
		o.__("各モジュールはお互いの内部の動きについては知らない。").e();

		o.l1("【どうして？】").e();
		o.l2("情報隠蔽がシステムを構成するモジュールを効果的に『分離』し、");
		o.__("モジュールを個別に開発、テスト、最適化、使用、理解、修正することを可能にする。").e();
	}

	@Test
	public void 指針_まずすべてのメンバをprivateに() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("原則、一旦privateで考えてみる。").e();

		o.l1("【どうして？】").e();
		o.l2("publicにすると、完全に公開API。").e();
		o.l2("protectedにすると、publicでなくても、もう公開API。").e();
		o.l2("公開APIは永久にサポートしなければならない。").e();
		
		o.l1("【どうすれば？】").e();
		o.l2("パッケージプライベートは、公開APIではない。").e();
		o.l2("近い仲間が必要とすれば、privateを取ってもよい。").e();
	}

	@Test
	public void 指針_テストのためにアクセス緩和しない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("テストを容易にするために、アクセスを緩和してはいけない。").e();

		o.l1("【どうして？】").e();
		o.l2("公開APIを、クライアント以外のために増やしてはいけない。").e();
		o.l2("テストのためだけに設計を変更するのは本末転倒。").e();

		o.l1("【どうすれば？】").e();
		o.l2("パッケージプライベートまでは許容範囲。").e();
	}

	@Test
	public void 指針_フィールドは絶対publicにしない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インスタンスフィールドは、決してpublicにすべきではない。").e();

		o.l1("【どうして？】").e();
		o.l2("保存できる値を制限する機会を放棄することになる。").e();
		o.l2("不変式を強要する機会を放棄することになる。").e();
		o.l2("内部データ表現を変更する機会を放棄することになる。").e();
	}

	@Test
	public void 指針_定数の公開のみpublicを許容する() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("public static finalの固定フィールドは使用してよい。").e();

		o.l1("【たとえば？】").e();
		o.l2("IMMUTABLE_STRING定数。").e();
		assertThat(IMMUTABLE_STRING, anything());

		o.l2("ただし、finalにしても、配列やその類は可変となる。").e();
		MUTABLE_ARRAY[0] = "b1";
		assertThat(MUTABLE_ARRAY, arrayContaining("b1", "a2"));

		o.l2("対策１").s("不変なリストを返す。").e();
		assertThat(IMMUTABLE_LIST, contains("a1", "a2"));

		o.l2("対策２").s("複製を返す。").e();
		assertThat(IMMUTABLE_CLONE, arrayContaining("a1", "a2"));
	}

	public static final String IMMUTABLE_STRING = "immutable_string";

	public static final String[] MUTABLE_ARRAY = { "a1", "a2" };

	private static final String[] IMMUTABLE_ARRAY = { "a1", "a2" };
	// 対策１
	public static final List<String> IMMUTABLE_LIST = Collections.unmodifiableList(Arrays.asList(IMMUTABLE_ARRAY));
	// 対策２
	public static final String[] IMMUTABLE_CLONE = IMMUTABLE_ARRAY.clone();

	@Test
	public void 制約_オーバライド時にアクセスを厳しくすることはできない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("protectedをオーバライドして、publicには変更できる。").e();
		o.l2("protectedをオーバライドして、privateやパッケージプライベートには変更できない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("protectedは子供に伝承し続けるため、慎重に公開。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("情報隠蔽はオブジェクト指向の遵守すべき教義の一つ。").e();
		o.l1("情報隠蔽をJava実装に落とし込む仕組みは、");
		o.__("アクセス指定子によるアクセス制限である。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
