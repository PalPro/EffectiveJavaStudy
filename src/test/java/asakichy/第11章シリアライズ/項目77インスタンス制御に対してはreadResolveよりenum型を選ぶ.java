package asakichy.第11章シリアライズ;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Arrays;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目77インスタンス制御に対してはreadResolveよりenum型を選ぶ {

	@Test
	public void シリアライズ可能シングルトンの問題点() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("シングルトンにシリアライズを実装すると、もはやシングルトンではなくなる。").e();

		o.l1("【どうして？】").e();
		o.l2("readObjectメソッドは新たに生成されたインスタンスを返し、");
		o.__("そのインスタンスはクラスの初期化時に生成されたインスタンスと同一ではない。").e();

		o.l1("【たとえば？】").e();
		o.l2("複数のインスタンスが生成可能な、シリアライズ可能シングルトンElvisNotSingletonクラス。").e();

		ElvisNotSingleton originalInstance = ElvisNotSingleton.INSTANCE;

		// シリアライズ
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(originalInstance);
		byte[] serialized = os.toByteArray();

		// デシリアライズ
		ByteArrayInputStream is = new ByteArrayInputStream(serialized);
		ElvisNotSingleton desirializedInstance = (ElvisNotSingleton) new ObjectInputStream(is).readObject();

		// シングルトンなのに、複数のインスタンスが発生！
		assertThat(desirializedInstance, not(sameInstance(originalInstance)));
	}

	private static class ElvisNotSingleton implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final ElvisNotSingleton INSTANCE = new ElvisNotSingleton();

		private ElvisNotSingleton() {
		}
	}

	@Test
	public void 解決策_readResolve() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("readObjectで生成されたインスタンスを、他のインスタンスと置換できる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("デシリアライズされた後に、新たに生成されたオブジェクトに対してreadResolveが呼び出される。").e();
		o.l2("readResolveで、新たに生成されたオブジェクトの代わりのオブジェクト参照を返す。").e();

		o.l1("【たとえば？】").e();
		o.l2("デシリアライズ後も同じインスタンスを返す、シリアライズ可能シングルトンElvisByReadResolveクラス。").e();

		ElvisByReadResolve originalInstance = ElvisByReadResolve.INSTANCE;

		// シリアライズ
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(originalInstance);
		byte[] serialized = os.toByteArray();

		// デシリアライズ
		ByteArrayInputStream is = new ByteArrayInputStream(serialized);
		ElvisByReadResolve desirializedInstance = (ElvisByReadResolve) new ObjectInputStream(is).readObject();

		// デシリアライズ後も同じインスタンス
		assertThat(desirializedInstance, sameInstance(originalInstance));

		{
			/** 【補】readResolveのメソッド可視性 */
			// readResolveをprivateにすると、サブクラスには適用されない。
			// よって、クラスがfinalならprivateにするべき。

			// クラスがfinalでない場合、protectedかpublicにして、サブクラスがオーバライドしていないと
			// サブクラスのインスタンスのデシリアライズでスーパークラスのでシリアライズがそのまま呼ばれてしまい、
			// スーパークラスが生成され、ClassCastExceptionが発生しやすいので注意。
		}
	}

	private static class ElvisByReadResolve implements Serializable {
		private static final long serialVersionUID = 1L;
		public static final ElvisByReadResolve INSTANCE = new ElvisByReadResolve();

		private ElvisByReadResolve() {
		}

		private Object readResolve() throws ObjectStreamException {
			return INSTANCE;
		}
	}

	@Test
	public void 解決策_readResolveかつtransient() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("インスタンス制御のためにreadResolveに依存するのであれば、");
		o.__("オブジェクト参照型のすべてのインスタンスフィールドはtransientでなければならない。").e();

		o.l1("【どうして？】").e();
		o.l2("readResolveメソッドが実行される前に、");
		o.__("デシリアライズされたオブジェクトへの参照を取得される恐れがある。").e();

		o.l1("【たとえば？】").e();
		o.l2("transientでないオブジェクト参照を含むシングルトンElvisクラス。").e();
		o.l2("Elvisに忍び込み、そのオブジェクト参照を「盗む」ElvisStealerクラス。").e();

		// ElvisStealerを忍ばせたElvisのシリアライズバイナリストリーム
		final byte[] serializedForm = new byte[] { (byte) 0xac, (byte) 0xed, (byte) 0x00, (byte) 0x05, (byte) 0x73, (byte) 0x72,
				(byte) 0x00, (byte) 0x7a, (byte) 0x61, (byte) 0x73, (byte) 0x61, (byte) 0x6b, (byte) 0x69, (byte) 0x63,
				(byte) 0x68, (byte) 0x79, (byte) 0x2e, (byte) 0xe7, (byte) 0xac, (byte) 0xac, (byte) 0x31, (byte) 0x31,
				(byte) 0xe7, (byte) 0xab, (byte) 0xa0, (byte) 0xe3, (byte) 0x82, (byte) 0xb7, (byte) 0xe3, (byte) 0x83,
				(byte) 0xaa, (byte) 0xe3, (byte) 0x82, (byte) 0xa2, (byte) 0xe3, (byte) 0x83, (byte) 0xa9, (byte) 0xe3,
				(byte) 0x82, (byte) 0xa4, (byte) 0xe3, (byte) 0x82, (byte) 0xba, (byte) 0x2e, (byte) 0xe9, (byte) 0xa0,
				(byte) 0x85, (byte) 0xe7, (byte) 0x9b, (byte) 0xae, (byte) 0x37, (byte) 0x37, (byte) 0xe3, (byte) 0x82,
				(byte) 0xa4, (byte) 0xe3, (byte) 0x83, (byte) 0xb3, (byte) 0xe3, (byte) 0x82, (byte) 0xb9, (byte) 0xe3,
				(byte) 0x82, (byte) 0xbf, (byte) 0xe3, (byte) 0x83, (byte) 0xb3, (byte) 0xe3, (byte) 0x82, (byte) 0xb9,
				(byte) 0xe5, (byte) 0x88, (byte) 0xb6, (byte) 0xe5, (byte) 0xbe, (byte) 0xa1, (byte) 0xe3, (byte) 0x81,
				(byte) 0xab, (byte) 0xe5, (byte) 0xaf, (byte) 0xbe, (byte) 0xe3, (byte) 0x81, (byte) 0x97, (byte) 0xe3,
				(byte) 0x81, (byte) 0xa6, (byte) 0xe3, (byte) 0x81, (byte) 0xaf, (byte) 0x72, (byte) 0x65, (byte) 0x61,
				(byte) 0x64, (byte) 0x52, (byte) 0x65, (byte) 0x73, (byte) 0x6f, (byte) 0x6c, (byte) 0x76, (byte) 0x65,
				(byte) 0xe3, (byte) 0x82, (byte) 0x88, (byte) 0xe3, (byte) 0x82, (byte) 0x8a, (byte) 0x65, (byte) 0x6e,
				(byte) 0x75, (byte) 0x6d, (byte) 0xe5, (byte) 0x9e, (byte) 0x8b, (byte) 0xe3, (byte) 0x82, (byte) 0x92,
				(byte) 0xe9, (byte) 0x81, (byte) 0xb8, (byte) 0xe3, (byte) 0x81, (byte) 0xb6, (byte) 0x24, (byte) 0x45,
				(byte) 0x6c, (byte) 0x76, (byte) 0x69, (byte) 0x73, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x4c,
				(byte) 0x00, (byte) 0x0d, (byte) 0x66, (byte) 0x61, (byte) 0x76, (byte) 0x6f, (byte) 0x72, (byte) 0x69,
				(byte) 0x74, (byte) 0x65, (byte) 0x53, (byte) 0x6f, (byte) 0x6e, (byte) 0x67, (byte) 0x73, (byte) 0x74,
				(byte) 0x00, (byte) 0x12, (byte) 0x4c, (byte) 0x6a, (byte) 0x61, (byte) 0x76, (byte) 0x61, (byte) 0x2f,
				(byte) 0x6c, (byte) 0x61, (byte) 0x6e, (byte) 0x67, (byte) 0x2f, (byte) 0x4f, (byte) 0x62, (byte) 0x6a,
				(byte) 0x65, (byte) 0x63, (byte) 0x74, (byte) 0x3b, (byte) 0x78, (byte) 0x70, (byte) 0x73, (byte) 0x72,
				(byte) 0x00, (byte) 0x81, (byte) 0x61, (byte) 0x73, (byte) 0x61, (byte) 0x6b, (byte) 0x69, (byte) 0x63,
				(byte) 0x68, (byte) 0x79, (byte) 0x2e, (byte) 0xe7, (byte) 0xac, (byte) 0xac, (byte) 0x31, (byte) 0x31,
				(byte) 0xe7, (byte) 0xab, (byte) 0xa0, (byte) 0xe3, (byte) 0x82, (byte) 0xb7, (byte) 0xe3, (byte) 0x83,
				(byte) 0xaa, (byte) 0xe3, (byte) 0x82, (byte) 0xa2, (byte) 0xe3, (byte) 0x83, (byte) 0xa9, (byte) 0xe3,
				(byte) 0x82, (byte) 0xa4, (byte) 0xe3, (byte) 0x82, (byte) 0xba, (byte) 0x2e, (byte) 0xe9, (byte) 0xa0,
				(byte) 0x85, (byte) 0xe7, (byte) 0x9b, (byte) 0xae, (byte) 0x37, (byte) 0x37, (byte) 0xe3, (byte) 0x82,
				(byte) 0xa4, (byte) 0xe3, (byte) 0x83, (byte) 0xb3, (byte) 0xe3, (byte) 0x82, (byte) 0xb9, (byte) 0xe3,
				(byte) 0x82, (byte) 0xbf, (byte) 0xe3, (byte) 0x83, (byte) 0xb3, (byte) 0xe3, (byte) 0x82, (byte) 0xb9,
				(byte) 0xe5, (byte) 0x88, (byte) 0xb6, (byte) 0xe5, (byte) 0xbe, (byte) 0xa1, (byte) 0xe3, (byte) 0x81,
				(byte) 0xab, (byte) 0xe5, (byte) 0xaf, (byte) 0xbe, (byte) 0xe3, (byte) 0x81, (byte) 0x97, (byte) 0xe3,
				(byte) 0x81, (byte) 0xa6, (byte) 0xe3, (byte) 0x81, (byte) 0xaf, (byte) 0x72, (byte) 0x65, (byte) 0x61,
				(byte) 0x64, (byte) 0x52, (byte) 0x65, (byte) 0x73, (byte) 0x6f, (byte) 0x6c, (byte) 0x76, (byte) 0x65,
				(byte) 0xe3, (byte) 0x82, (byte) 0x88, (byte) 0xe3, (byte) 0x82, (byte) 0x8a, (byte) 0x65, (byte) 0x6e,
				(byte) 0x75, (byte) 0x6d, (byte) 0xe5, (byte) 0x9e, (byte) 0x8b, (byte) 0xe3, (byte) 0x82, (byte) 0x92,
				(byte) 0xe9, (byte) 0x81, (byte) 0xb8, (byte) 0xe3, (byte) 0x81, (byte) 0xb6, (byte) 0x24, (byte) 0x45,
				(byte) 0x6c, (byte) 0x76, (byte) 0x69, (byte) 0x73, (byte) 0x53, (byte) 0x74, (byte) 0x65, (byte) 0x61,
				(byte) 0x6c, (byte) 0x65, (byte) 0x72, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x01, (byte) 0x4c, (byte) 0x00,
				(byte) 0x07, (byte) 0x70, (byte) 0x61, (byte) 0x79, (byte) 0x6c, (byte) 0x6f, (byte) 0x61, (byte) 0x64,
				(byte) 0x74, (byte) 0x00, (byte) 0x7c, (byte) 0x4c, (byte) 0x61, (byte) 0x73, (byte) 0x61, (byte) 0x6b,
				(byte) 0x69, (byte) 0x63, (byte) 0x68, (byte) 0x79, (byte) 0x2f, (byte) 0xe7, (byte) 0xac, (byte) 0xac,
				(byte) 0x31, (byte) 0x31, (byte) 0xe7, (byte) 0xab, (byte) 0xa0, (byte) 0xe3, (byte) 0x82, (byte) 0xb7,
				(byte) 0xe3, (byte) 0x83, (byte) 0xaa, (byte) 0xe3, (byte) 0x82, (byte) 0xa2, (byte) 0xe3, (byte) 0x83,
				(byte) 0xa9, (byte) 0xe3, (byte) 0x82, (byte) 0xa4, (byte) 0xe3, (byte) 0x82, (byte) 0xba, (byte) 0x2f,
				(byte) 0xe9, (byte) 0xa0, (byte) 0x85, (byte) 0xe7, (byte) 0x9b, (byte) 0xae, (byte) 0x37, (byte) 0x37,
				(byte) 0xe3, (byte) 0x82, (byte) 0xa4, (byte) 0xe3, (byte) 0x83, (byte) 0xb3, (byte) 0xe3, (byte) 0x82,
				(byte) 0xb9, (byte) 0xe3, (byte) 0x82, (byte) 0xbf, (byte) 0xe3, (byte) 0x83, (byte) 0xb3, (byte) 0xe3,
				(byte) 0x82, (byte) 0xb9, (byte) 0xe5, (byte) 0x88, (byte) 0xb6, (byte) 0xe5, (byte) 0xbe, (byte) 0xa1,
				(byte) 0xe3, (byte) 0x81, (byte) 0xab, (byte) 0xe5, (byte) 0xaf, (byte) 0xbe, (byte) 0xe3, (byte) 0x81,
				(byte) 0x97, (byte) 0xe3, (byte) 0x81, (byte) 0xa6, (byte) 0xe3, (byte) 0x81, (byte) 0xaf, (byte) 0x72,
				(byte) 0x65, (byte) 0x61, (byte) 0x64, (byte) 0x52, (byte) 0x65, (byte) 0x73, (byte) 0x6f, (byte) 0x6c,
				(byte) 0x76, (byte) 0x65, (byte) 0xe3, (byte) 0x82, (byte) 0x88, (byte) 0xe3, (byte) 0x82, (byte) 0x8a,
				(byte) 0x65, (byte) 0x6e, (byte) 0x75, (byte) 0x6d, (byte) 0xe5, (byte) 0x9e, (byte) 0x8b, (byte) 0xe3,
				(byte) 0x82, (byte) 0x92, (byte) 0xe9, (byte) 0x81, (byte) 0xb8, (byte) 0xe3, (byte) 0x81, (byte) 0xb6,
				(byte) 0x24, (byte) 0x45, (byte) 0x6c, (byte) 0x76, (byte) 0x69, (byte) 0x73, (byte) 0x3b, (byte) 0x78,
				(byte) 0x70, (byte) 0x71, (byte) 0x00, (byte) 0x7e, (byte) 0x00, (byte) 0x02 };

		InputStream is = new ByteArrayInputStream(serializedForm);
		ObjectInputStream ois = new ObjectInputStream(is);
		Elvis elvis = (Elvis) ois.readObject();

		assertThat(elvis.getFavorites(), is("[Hound Dog, Heartbreak Hotel]"));

		// 盗難成功！
		Elvis impersonator = ElvisStealer.impersonator;
		assertThat(impersonator.getFavorites(), is("[A Fool Such as I]"));

		{
			/** 【補】ElvisStealerの攻撃の仕組み */
			// 「盗人」クラスElvisStealerは、
			// readResolveとシリアライズされたシングルトンを参照するpayloadフィールドを持つ。

			// そのシリアライズされたシングルトンの中に盗人は隠れる。
			// つまり、そのシリアライズストリームの中のシングルトンの、
			// 非transientなfavoriteSongsフィールドを、その盗人のインスタンスで置き換える。
			// これで参照は循環し、シングルトンは盗人を含んでおり、盗人はそのシングルトンを参照している。

			// シングルトンは盗人を含んでいるので、
			// シングルトンがデシリアライズされる際に、盗人のreadBesolvｅが最初に実行される。
			// 結果、盗人のreadResolveが実行された時に、
			// payloadは部分的にデシリアライズされているシングルトンをいまだに参照してる。
			// （この時点で、シングルトンのほうのrealResolveは、また呼ばれていない。）
			// 盗人のreadResolveは、payloadからその参照をstaticフィールドimpersonatorに保管しておく。
			// それから、自分が隠れているフィールドfavoriteSongsに対する正しい型の値を返す。
			// これをしないと、そのフィールドに対して盗人の参照を復元しようとした時に、
			// JVMはClassCastExceptionをスローする。
		}

	}

	private static class Elvis implements Serializable {
		public static final Elvis INSTANCE = new Elvis();

		private Elvis() {
		}

		// 非transientなオブジェクト参照フィールドを持っている
		private String[] favoriteSongs = { "Hound Dog", "Heartbreak Hotel" };

		public String getFavorites() {
			return Arrays.toString(favoriteSongs);
		}

		// 同一インスタンスを返す対策は行っている
		private Object readResolve() throws ObjectStreamException {
			return INSTANCE;
		}

		private static final long serialVersionUID = 0;
	}

	private static class ElvisStealer implements Serializable {
		static Elvis impersonator;
		private Elvis payload;

		private Object readResolve() {
			// まだ未解決（Elvis#readResolve()前）のデシリアライズElvisを保管しておく
			impersonator = payload;

			// favoritesを乗っ取る
			return new String[] { "A Fool Such as I" };
		}

		private static final long serialVersionUID = 0;
	}

	public void 補_攻撃用バイナリデータ作成に使用したプログラム() throws Exception {
		// パッケージが異なるので、サンプルのバイナリはそのまま使えない。

		// バイナリ作成用Elvis
		// private static class Elvis implements Serializable {
		// public static final Elvis INSTANCE = new Elvis();
		//
		// private Elvis() {
		// }
		//
		// private Object favoriteSongs = new ElvisStealer();
		//
		// private static final long serialVersionUID = 0;
		// }

		// バイナリ作成用ElvisStealer
		// private static class ElvisStealer implements Serializable {
		// private Elvis payload;
		//
		// private static final long serialVersionUID = 0;
		// }

		// 上述クラスをシリアライズ
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		new ObjectOutputStream(os).writeObject(Elvis.INSTANCE);
		byte[] serialized = os.toByteArray();

		for (int i = 0; i < serialized.length; i++) {
			System.out.printf("(byte) 0x%02x,", serialized[i]);
			System.out.println();
		}

		// このままだとpayloadがnullなので、最後の、

		// (byte) 0x78, (byte) 0x70, (byte) 0x70

		// を、以下のように変更して、payloadをElvisへの参照にする

		// (byte) 0x78, (byte) 0x70, (byte) 0x71, (byte) 0x00, (byte) 0x7e,
		// (byte)0x00, (byte) 0x02
	}

	@Test
	public void 解決策_enum型() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("最小の努力で、宣言された定数の他にインスタンスが存在しないという完全な保証を得ることができる。").e();
		o.l2("JDK1.5以降のベストプラクティス。").e();

		o.l1("【たとえば？】").e();
		o.l2("シリアライズの問題をクリアしている、完全無欠なシングルトンElvisByEnumクラス。").e();
		assertThat(ElvisByEnum.INSTANCE.getFavorites(), is("[Hound Dog, Heartbreak Hotel]"));

		{
			/** 【補】readResolveの用途 */
			// 通常、シングルトンの実装にはenum型を使うべきだが、
			// インスタンス制御に対するreadResolveの使用は、廃れたわけではない。

			// コンパイル時にインスタンスが分かっていない、
			// シリアライズ可能なインスタンス制御されたクラスを書く必要がある場合には、
			// そのクラスをenum型として表現することはできない。

			// 例えば、どんなインスタンスがあるかはXMLに書いてあって
			// 実行時にそれを読んでインスタンス化するなど。

		}
	}

	enum ElvisByEnum {
		INSTANCE;
		private String[] favoriteSongs = { "Hound Dog", "Heartbreak Hotel" };

		public String getFavorites() {
			return Arrays.toString(favoriteSongs);
		}
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("シングルトンはenumで。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
