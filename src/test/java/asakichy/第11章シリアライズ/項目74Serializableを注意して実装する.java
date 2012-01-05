package asakichy.第11章シリアライズ;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目74Serializableを注意して実装する {

	@Test
	public void シリアライズとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("オブジェクトをバイトストリームとして符号化し、");
		o.__("バイトストリームの符号からオブジェクトを再構築するフレームワーク。").e();

		o.l1("【どうして？】").e();
		o.l2("オブジェクトがシリアライズされると、その符号化されたものは、");
		o.l2("実行中の仮想マシンから他の仮想マシンへ送信できるし、");
		o.l2("後でデシリアライズするためにディスクに保存できる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("クラスがSerializableを実装する。").e();

		{
			/** 【補】シリアライズのアーキテクチャ */
			// Serializableインターフェイスを実装しているクラスAのインスタンスがある。

			// ObjectOutputStream#writeObject()でシリアライズする。
			// ObjectOutputStream#writeObject()は、
			// クラスAにwriteObject()が定義されていればそれを呼び出し、
			// そうでなければObjectOutputStream#defaultWriteObject()が使用される。

			// ObjectInputStream#readObject()でデシリアライズする。
			// ObjectInputStream#writeObject()は、
			// クラスAにreadObject()が定義されていればそれを呼び出し、
			// そうでなければObjectInputStream#defaultReadObject()が使用される。
		}
	}

	@Test
	public void 実装コスト_柔軟性低下() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("一旦リリースされると、クラスの実装を変更する柔軟性を低下させる。").e();

		o.l1("【どうして？】").e();
		o.l2("シリアライズで符号化されたもの（＝シリアライズ形式）は、そのクラスの公開APIの一部となる。").e();
		o.l3("そのシリアライズ形式を永久にサポートしなければならない。").e();
		o.l2("デフォルトのシリアライズ形式なら、永久にそのクラスの最初の内部表現と結び付く。").e();
		o.l3("プライベートやパッケージプライベートすら公開APIの一部となし、情報隠蔽不可となる。").e();
		o.l3("内部表現を変更すると、互換性がなくなる。").e();
		o.l2("ストリーム一意識別子が必要。").e();
		o.l3("明示的に指定しない自動計算の場合、クラスに変更があると、互換性が失われる。").e();

		{
			/** 【補】互換性のある内部表現書き換え */
			// java.io.ObjectOutputStream#putFields()と
			// java.io.ObjectInputStream#readFields()を使用すれば、
			// 元のシリアライズ形式を保持しながら内部表現を変更することは可能。
			// ただし、難易度は高めで、ソースコードの見た目も悪くなる。
		}

		{
			/** 【補】ストリーム一意識別子について */
			// より一般的には「シリアルバージョンUID」のこと。
			// すべてのシリアライズ可能なクラスが持つ、そのクラスに関連付けられた一意の識別番号。

			// 明示的に付番する場合、serialVersionIDという名のstatic final longフィールドを宣言。
			// 明示的に付番しない場合、実行時にシステムが自動生成する。

			// 自動生成される値は、クラス名、クラスが実装しているインタフェース名、
			// クラスのすべてのpublicとprotectedのメンバーから影響を受ける。
			// これらのどれかを何らかの方法で変更したら、互換性は失われ、
			// 実行時にInvalidClassExceptionがスローされることになる。

		}
	}

	@Test
	public void 実装コスト_品質低下() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("バグやセキュリティホールの可能性を増大させる。").e();

		o.l1("【どうして？】").e();
		o.l2("オブジェクト生成に関して、「言語外の仕組み」にあたる。").e();
		o.l3("つまり、デシリアライズは「隠れたコンストラクタ」。").e();
		o.l3("不変式破壊や不正アクセスのリスクがある。").e();
	}

	@Test
	public void 実装コスト_テスト負荷() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("新しいバージョンのクラスをリリースすることに関連したテストの負荷を増大させる。").e();

		o.l1("【どうして？】").e();
		o.l2("新旧のシリアル・デシリアルを組み合わせてテストしなければならない。").e();
		o.l2("しかも、そのテストは自動化できない。").e();
		o.l3("バイナリ互換に加え、セマンティック互換も検証する必要がある。").e();
		o.l3("つまり、 異常なく読み込めることに加え、");
		o.l3("さらにプログラムが（意味的に）正しく動くことを確認する必要あり。").e();
	}

	@Test
	public void 適否基準() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("軽く考えないで、熟慮して決める。").e();

		o.l1("【どうすれば？】").e();
		o.l2("フレームワークが要求するなら実装する。").e();
		o.l2("値クラスやコレクションクラスは実装する。").e();
		o.l2("活動クラスは実装しない。").e();
		o.l3("java.lang.Threadなど。").e();
		o.l2("継承するためのクラスでは実装しない。").e();
		o.l3("継承実装者にかなりの負荷になる。").e();
		o.l2("Serializableの拡張をしない。").e();
		o.l2("内部クラスは実装しない。").e();
		o.l3("内部クラスはエンクロージングインスタンスの参照を持っていて、");
		o.__("それはコンパイラが作ったヤツなので、シリアライズ形式は常に不明瞭なモノになってしまう。").e();

		{
			/** 【補】 Serializableを実装した、継承するためのクラス */
			// JDKの中には、継承するためのクラスでも、
			// 理由があり、Serializableを実装しているクラスがある。
			// ＜java.lang.Throwable＞
			// RMI等で例外を送受信するため。
			// ＜java.awt.Component＞
			// GUIの保存や復元のため。
			// ＜javax.servlet.http.HttpServlet＞
			// セッション情報の保存や復元のため。
		}

		{
			/** 【補】readObjectNoDataについて */
			// シリアライズ可能かつ拡張可能で、インスタンスフィールドを持つクラスを実装する場合、
			// インスタンスフィールドがデフォルト値に初期化されたら成り立たなくなる不変式を
			// クラスが持っていたら、readObjectNoDataを追加しなければならない。

			// 既存のシリアライズ可能クラスに対して、
			// シリアライズ可能なスーパークラスが追加されることに対応するために必要。

			// 例
			// private void readObjectNoData() throws InvalidObjectException {
			// 　　throw new InvalidObjectException("Stream data required.");
			// }
		}
	}

	@Test
	public void シリアライズ非実装と継承() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("継承のために設計されたシリアライズ不可能なクラスに対して、").e();
		o.l2("パラメータなしコンストラクタを提供することを検討すべき。").e();

		o.l1("【どうして？】").e();
		o.l2("継承のために設計されたクラスがシリアライズ可能でなければ、").e();
		o.l2("シリアライズ可能なサブクラスを書くことは不可能になるかもしれない。").e();
		o.l2("特に、スーパークラスが、アクセス可能なパラメータなしコンストラクタを提供していなければ、不可能確定。").e();

		o.l1("【どうすれば？】").e();
		o.l2("自身がシリアライズ不可能でも、パラメータなしコンストラクタを提供し、");
		o.__("サブクラスがシリアライズ可能な実装が出来ることをアシストする。").e();
		o.l2("その際、自身の不変式は破られないようにする。").e();

		o.l1("【たとえば？】").e();
		o.l2("シリアライズ未実装の、継承のために設計されたAbstractFooクラス。").e();
		o.l2("AbstractFooクラスを継承して、シリアライズを実装したFooクラス。").e();

		Foo foo = new Foo(10, 10);

		// シリアライズ
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(foo);
		byte[] serializedFoo = os.toByteArray();

		// デシリアライズ
		ByteArrayInputStream is = new ByteArrayInputStream(serializedFoo);
		Foo deserializedFoo = (Foo) new ObjectInputStream(is).readObject();

		assertThat(deserializedFoo.getX(), is(10));
		assertThat(deserializedFoo.getY(), is(10));
	}

	// 状態をもち、シリアライズ可能なサブクラスを許容する、シリアライズ未実装クラス
	private static abstract class AbstractFoo {
		private int x, y;

		// 初期化状態管理用
		private enum State {
			NEW, INITIALIZING, INITIALIZED
		};

		private final AtomicReference<State> init = new AtomicReference<State>(State.NEW);

		// 元々のコンストラクタ
		public AbstractFoo(int x, int y) {
			initialize(x, y);
		}

		// サブクラスのシリアライズ可能化便宜に、パラメータなしコンストラクタ提供
		// サブクラスのreadObject()メソッドで使用
		@SuppressWarnings("unused")
		protected AbstractFoo() {
		}

		protected final void initialize(int x, int y) {
			if (!init.compareAndSet(State.NEW, State.INITIALIZING)) {
				// 初期化済み
				throw new IllegalStateException("Already initialized");
			}
			this.x = x;
			this.y = y;
			init.set(State.INITIALIZED);
		}

		// サブクラスのwriteObject()メソッドで使用する、getter
		protected final int getX() {
			checkInit();
			return x;
		}

		protected final int getY() {
			checkInit();
			return y;
		}

		// すべてのpublic/protectedメソッドから呼び出さなければならない
		private void checkInit() {
			if (init.get() != State.INITIALIZED) {
				throw new IllegalStateException("Uninitialized");
			}
		}
		// ....残りは省略
	}

	private static class Foo extends AbstractFoo implements Serializable {
		private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
			s.defaultReadObject();

			// 手動でスーパークラスのフィールドをデシリアライズ
			int x = s.readInt();
			int y = s.readInt();
			// 手動で初期化
			initialize(x, y);
		}

		private void writeObject(ObjectOutputStream s) throws IOException {
			s.defaultWriteObject();

			// 手動でスーパークラスの状態をでシリアライズ
			s.writeInt(getX());
			s.writeInt(getY());
		}

		public Foo(int x, int y) {
			super(x, y);
		}

		private static final long serialVersionUID = 1856835860954L;
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("Serializableの実装が簡単だというのは、「逆」見掛け倒し。").e();
		o.l1("Serializableの実装は負荷が大きいので、十分注意を払い、決定する。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
