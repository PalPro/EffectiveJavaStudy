package asakichy.第02章オブジェクトの生成と消滅;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目01コンストラクタの代わりにstaticファクトリーメソッドを検討する {
	@Test
	public void staticファクトリメソッドとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("クラスのインスタンスを返す単なるstaticのメソッド。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Boolean#valueOf()。").e();

		Boolean trueInstance = Boolean.valueOf(true);
		assertThat(trueInstance, is(Boolean.TRUE));
	}

	@Test
	public void 長所１_名前を持てる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コンストラクタのように、メソッド名に「クラス名」という縛りがなくなる。").e();

		o.l1("【どうして？】").e();
		o.l2("意図を伝える命名が可能となる。").e();
		o.l2("同じシグニチャを持てない、という制約からも解放される。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.BigIntegerで素数（prime）を作成。").e();

		BigInteger probablePrime;

		// コンストラクタでは意図が伝わらない
		probablePrime = new BigInteger(8, 100, new Random());
		assertThat(probablePrime, isA(BigInteger.class));

		// ファクトリメソッド名によって意図が伝わる
		probablePrime = BigInteger.probablePrime(8, new Random());
		assertThat(probablePrime, isA(BigInteger.class));
	}

	@Test
	public void 長所２_インスタンス使い回せる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("既存のインスタンスの使い回しが可能となる。").e();

		o.l1("【どうして？】").e();
		o.l2("無駄なインスタンス生成を回避できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("java.lang.Boolean#valueOf()。").e();

		Boolean trueInstance = Boolean.valueOf(true);

		// 実は同じインスタンス
		assertThat(trueInstance, sameInstance(Boolean.TRUE));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void 長所３_戻り値の型を任意のサブタイプにできる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("自分のクラスの型で返さず、状況に応じたサブタイプの型で返せる。");

		o.l1("【どうして？】").e();
		o.l2("どんなサブタイプも返せるという柔軟性を持ち、");
		o.__("かつ、戻す実装クラスを隠蔽できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("１").s("java.util.Collections#unmodifiableList。").e();
		o.l3("公開していない実装クラスのインスタンスを返している。").e();
		List<String> modifiableList = new ArrayList<String>();
		modifiableList.add("modify");
		List<String> unmodifiableList = Collections.unmodifiableList(modifiableList);
		assertThat(unmodifiableList.get(0), is("modify"));

		{
			/** 【補】unmodifiableList インスタンスについて */
			// 「UnmodifiableRandomAccessList」ないし「UnmodifiableList」という、
			// 『「List」のサブタイプ』の『privateなクラス』のオブジェクト。
			// （正確にはCollectionsクラスのパッケージプライベートなstaticインナークラス）
		}

		o.l2("２").s("java.util.EnumSet。").e();
		o.l3("publicなコンストラクタはなく、すべてstaticファクトリメソッド。").e();

		EnumSet<Color> selectedColor = EnumSet.of(Color.RED, Color.BLUE);
		assertThat(selectedColor.contains(Color.RED), is(true));

		o.l2("３").s("サービスプロバイダーフレームワーク。").e();
		o.l3("JDBCなどがそれにあたる。").e();

		// ３大構成要素
		assertThat("サービスインタフェース", describedAs("プロバイダーが実装するインターフェイス。", anything()));
		assertThat("プロバイダー登録API", describedAs("クライアントがアクセスする実装をシステムが登録。", anything()));
		assertThat("サービスアクセスAPI", describedAs("サービスのインスタンスをクライアントが取得。", anything()));

		// フレームワークのスケッチ
		Provider DEFAULT_PROVIDER = new Provider() {
			public Service newService() {
				return new Service() {
					@Override
					public String toString() {
						return "Default service";
					}
				};
			}
		};

		Provider COMP_PROVIDER = new Provider() {
			public Service newService() {
				return new Service() {
					@Override
					public String toString() {
						return "Complementary service";
					}
				};
			}
		};

		Provider ARMED_PROVIDER = new Provider() {
			public Service newService() {
				return new Service() {
					@Override
					public String toString() {
						return "Armed service";
					}
				};
			}
		};
		// 登録
		Services.registerDefaultProvider(DEFAULT_PROVIDER);
		Services.registerProvider("comp", COMP_PROVIDER);
		Services.registerProvider("armed", ARMED_PROVIDER);
		// 取得
		assertThat(Services.newInstance().toString(), is("Default service"));
		assertThat(Services.newInstance("comp").toString(), is("Complementary service"));
		assertThat(Services.newInstance("armed").toString(), is("Armed service"));

		// ----
		// 例１の例外アサーション
		unmodifiableList.clear(); // UnsupportedOperationException発生
	}

	enum Color {
		RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
	}

	public interface Service {
		// サービスインタフェース
	}

	public interface Provider {
		Service newService();
	}

	private static class Services {
		private Services() {
		}

		private static final Map<String, Provider> providers = new ConcurrentHashMap<String, Provider>();
		public static final String DEFAULT_PROVIDER_NAME = "<def>";

		// プロバイダー登録API
		public static void registerDefaultProvider(Provider p) {
			registerProvider(DEFAULT_PROVIDER_NAME, p);
		}

		public static void registerProvider(String name, Provider p) {
			providers.put(name, p);
		}

		// サービスアクセスAPI
		public static Service newInstance() {
			return newInstance(DEFAULT_PROVIDER_NAME);
		}

		public static Service newInstance(String name) {
			Provider p = providers.get(name);
			if (p == null) {
				throw new IllegalArgumentException("No provider registered with name: " + name);
			}
			return p.newService();
		}
	}

	@Test
	public void 長所４_パラメータ化された型のインスタンス生成を簡便化できる() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("パラメータ化された部分の多いクラスのコンストラクタは冗長になるが、");
		o.__("それをファクトリによって簡便化できる。").e();

		o.l1("【どうして？】").e();
		o.l2("右辺側のジェネリックパラメータを省略できる。").e();

		o.l1("【たとえば？】").e();
		o.l2("ジェネリックを多く含むコレクションのコンストラクト。").e();

		@SuppressWarnings("unused")
		HashMap<String, List<String>> parameterizedMap;

		// 面倒
		parameterizedMap = new HashMap<String, List<String>>();

		// 簡潔
		parameterizedMap = /* HashMap. */newInstance();// HashMapのメソッドという妄想

		// Java7では言語で型推論サポート（ダイヤモンド・オペレータ）
		// parameterizedMap = new HashMap<>(); // Java6環境なので...コメントアウト
	}

	private static <K, V> HashMap<K, V> newInstance() {
		return new HashMap<K, V>();
	}

	@Test
	public void 短所１_サブクラスが作れない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("staticファクトリメソッドのみ提供し、");
		o.__("publicコンストラクタを隠蔽してしまうと、");
		o.__("サブクラスが作れなくなる。").e();

		o.l1("【どうすれば？】").e();
		o.l2("継承ではなく、コンポジションで設計する。").e();
	}

	@Test
	public void 短所２_コンストラクトメソッドであることが目立たない() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("コンストラクタは特殊なメソッドなので、例えばAPIドキュメントでも目立つ存在。").e();
		o.l2("staticファクトリメソッドは、同様にオブジェクトを生成する役割を持つが、");
		o.__("他のstaticメソッドに埋もれ、コンストラクトすることがアピールできない。").e();

		o.l1("【どうすれば？】").e();
		o.l2("標準的な命名規則でファクトリメソッドであることをアピール。").e();

		o.l2("valueOf").e().l3("同じ値を持つインスタンスを返す。").e().l3("つまり、型変換メソッド。").e();
		o.l2("of").e().l3("valueOfのシンプル命名。").e();
		o.l2("getInstance").e().l3("返すインスタンスが再利用オブジェクトの可能性あり。").e();
		o.l2("newInstance").e().l3("返すインスタンスは新しく生成したオブジェクト。").e();
		o.l2("get{Type}").e().l3("getInstanceの、返すクラスがメソッド所有クラスと異なる版。").e();
		o.l2("new{Type}").e().l3("newInstanceの、返すクラスがメソッド所有クラスと異なる版。").e();
	}

	@Test
	public void まとめ() throws Exception {
		o.l1("思考停止してコンストラクタを使用/設計しない。").e();
		o.l1("staticファクトリメソッドを検討の棚へ。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
