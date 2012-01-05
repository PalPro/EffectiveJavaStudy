package asakichy.第02章オブジェクトの生成と消滅;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import asakichy.rule.OutLine;
import asakichy.rule.WholeOutLine;

public class 項目02数多くのコンストラクタパラメータに直面した時にはビルダーを検討する {

	@Test
	public void 数が多いコンストラクタパラメータとは() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("使いにくい。").e();
		o.l2("問題１").s("何番目が何のパラメータかわかりにくい。").e();
		o.l2("問題２").s("必須パラメータかオプションパラメータがわかりにくい。").e();
	}

	@Test
	public void 解決策１_テレスコーピング・コンストラクタ() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("必須パラメータだけを受け取るコンストラクタを１つ提供し、");
		o.__("オプションパラメータを受け取る別のコンストラクタを提供する。").e();
		o.l2("が、非採用。").e();

		o.l1("【どうして？】").e();
		o.l2("多少使い勝手はよくなるが、問題点はいずれも解消されていない。").e();

		o.l1("【たとえば？】").e();
		o.l2("NutritionFacts_Telescopeクラス。").e();
		NutritionFacts_Telescope cocaCola;// NutritionFacts＝栄養成分

		// 必須のみ指定
		cocaCola = new NutritionFacts_Telescope(240, 8);
		assertThat(cocaCola.servingSize, is(240));
		assertThat(cocaCola.servings, is(8));
		assertThat(cocaCola.calories, is(0));
		assertThat(cocaCola.fat, is(0));
		assertThat(cocaCola.sodium, is(0));
		assertThat(cocaCola.carbohydrate, is(0));

		// 全パラメータ指定
		cocaCola = new NutritionFacts_Telescope(240, 8, 100, 0, 35, 27);
		assertThat(cocaCola.servingSize, is(240));
		assertThat(cocaCola.servings, is(8));
		assertThat(cocaCola.calories, is(100));
		assertThat(cocaCola.fat, is(0));
		assertThat(cocaCola.sodium, is(35));
		assertThat(cocaCola.carbohydrate, is(27));

	}

	private static class NutritionFacts_Telescope {
		private final int servingSize; // (mL) <必須>
		private final int servings; // (容器ごと) <必須>
		private final int calories; // カロリー <オプション>
		private final int fat; // 脂肪(g) <オプション>
		private final int sodium; // 塩分(mg) <オプション>
		private final int carbohydrate; // 炭水化物(g) <オプション>

		public NutritionFacts_Telescope(int servingSize, int servings) {
			this(servingSize, servings, 0);
		}

		public NutritionFacts_Telescope(int servingSize, int servings, int calories) {
			this(servingSize, servings, calories, 0);
		}

		public NutritionFacts_Telescope(int servingSize, int servings, int calories, int fat) {
			this(servingSize, servings, calories, fat, 0);
		}

		public NutritionFacts_Telescope(int servingSize, int servings, int calories, int fat, int sodium) {
			this(servingSize, servings, calories, fat, sodium, 0);
		}

		public NutritionFacts_Telescope(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
			this.servingSize = servingSize;
			this.servings = servings;
			this.calories = calories;
			this.fat = fat;
			this.sodium = sodium;
			this.carbohydrate = carbohydrate;
		}
	}

	@Test
	public void 解決策２_JavaBeansパターン() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("引数なしのコンストラクタにして、パラメータはすべてsetter経由で渡す。").e();
		o.l2("が、非採用。").e();

		o.l1("【どうして？】").e();
		o.l2("必須パラメータがどれだかわからない。").e();
		o.l2("必須パラメータが設定されていない不整合な状態を許容している。").e();
		o.l2("イミュータブルにできない。").e();

		o.l1("【たとえば？】").e();
		o.l2("NutritionFacts_JavaBeansクラス。").e();
		NutritionFacts_JavaBeans cocaCola = new NutritionFacts_JavaBeans();
		// この時点では不整合な状態
		assertThat(cocaCola.servingSize, is(-1));
		assertThat(cocaCola.servings, is(-1));
		assertThat(cocaCola.calories, is(0));
		assertThat(cocaCola.fat, is(0));
		assertThat(cocaCola.sodium, is(0));
		assertThat(cocaCola.carbohydrate, is(0));

		// すべてsetterで
		cocaCola.setServingSize(240);
		cocaCola.setServings(8);
		cocaCola.setCalories(100);
		cocaCola.setSodium(35);
		cocaCola.setCarbohydrate(27);

		assertThat(cocaCola.servingSize, is(240));
		assertThat(cocaCola.servings, is(8));
		assertThat(cocaCola.calories, is(100));
		assertThat(cocaCola.fat, is(0));
		assertThat(cocaCola.sodium, is(35));
		assertThat(cocaCola.carbohydrate, is(27));
	}

	@SuppressWarnings("unused")
	private static class NutritionFacts_JavaBeans {
		private int servingSize = -1; // 必須（だがデフォルトなし）
		private int servings = -1; // 必須（だがデフォルトなし）
		private int calories = 0;
		private int fat = 0;
		private int sodium = 0;
		private int carbohydrate = 0;

		public NutritionFacts_JavaBeans() {
		}

		public void setServingSize(int val) {
			servingSize = val;
		}

		public void setServings(int val) {
			servings = val;
		}

		public void setCalories(int val) {
			calories = val;
		}

		public void setFat(int val) {
			fat = val;
		}

		public void setSodium(int val) {
			sodium = val;
		}

		public void setCarbohydrate(int val) {
			carbohydrate = val;
		}
	}

	@Test
	public void 解決策３_ビルダーパターン() throws Exception {
		o.l1("【どういうこと？】").e();
		o.l2("Expression Builderパターンを採用する。").e();

		o.l1("【どうして？】").e();
		o.l2("問題がオールクリアする。").e();
		o.l2("使いやすいし、読みやすい。").e();
		o.l2("イミュータブルが実現できる。").e();
		o.l2("可変長パラメータが実現できる。").e();
		o.l2("Abstract Factoryになりうる。（Class#newInstanceの代替となりうる。）").e();

		o.l1("【たとえば？】").e();
		o.l2("NutritionFacts_Builderクラス。").e();
		NutritionFacts_Builder cocaCola = new NutritionFacts_Builder.Builder(240, 8).calories(100).sodium(35).carbohydrate(27)
				.build();
		assertThat(cocaCola.servingSize, is(240));
		assertThat(cocaCola.servings, is(8));
		assertThat(cocaCola.calories, is(100));
		assertThat(cocaCola.fat, is(0));
		assertThat(cocaCola.sodium, is(35));
		assertThat(cocaCola.carbohydrate, is(27));

		{
			/** 【補】ビルダー動作を分解 */
			// NutritionFacts_Builderをコンストラクトするために、
			// まず、NutritionFacts_Builder.Builderをコンストラクトする。
			// NutritionFacts_Builder.Builderをコンストラクタは必須パラメータのみ受け取る。
			NutritionFacts_Builder.Builder builder = new NutritionFacts_Builder.Builder(240, 8);

			// NutritionFacts_Builder.Builderのsetterで適宜オプションパラメータを設定する。
			// NutritionFacts_Builder.Builderのsetterは自身のオブジェクトを返すので、
			// チェインして書くことができる。
			builder.calories(100).sodium(35).carbohydrate(27);

			// 最後にNutritionFacts_Builder.Builder#build()で、
			// NutritionFacts_Builderのインスタンスを受け取る。
			@SuppressWarnings("unused")
			NutritionFacts_Builder pepsiCola = builder.build();
		}

	}

	@SuppressWarnings("unused")
	private static class NutritionFacts_Builder {
		private final int servingSize;
		private final int servings;
		private final int calories;
		private final int fat;
		private final int sodium;
		private final int carbohydrate;

		public static class Builder {
			// 必須
			private final int servingSize;
			private final int servings;

			// オプション（デフォルトで初期化）
			private int calories = 0;
			private int fat = 0;
			private int carbohydrate = 0;
			private int sodium = 0;

			// 必須はコンストラクタで
			public Builder(int servingSize, int servings) {
				this.servingSize = servingSize;
				this.servings = servings;
			}

			// オプションは自身を返すsetterで
			public Builder calories(int val) {
				calories = val;
				return this;
			}

			public Builder fat(int val) {
				fat = val;
				return this;
			}

			public Builder carbohydrate(int val) {
				carbohydrate = val;
				return this;
			}

			public Builder sodium(int val) {
				sodium = val;
				return this;
			}

			public NutritionFacts_Builder build() {
				return new NutritionFacts_Builder(this);
			}
		}

		private NutritionFacts_Builder(Builder builder) {
			servingSize = builder.servingSize;
			servings = builder.servings;
			calories = builder.calories;
			fat = builder.fat;
			sodium = builder.sodium;
			carbohydrate = builder.carbohydrate;
		}

	}

	@Test
	public void まとめ() throws Exception {
		o.l1("コンストラクタのパラメータの数が多いときにはビルダ－を検討する。").e();
		o.l1("ビルダーは、JavaにおけるLLの名前付パラメータの模倣である。").e();
	}

	@Rule
	public OutLine o = new OutLine();

	@ClassRule
	public static WholeOutLine wo = new WholeOutLine();

}
