import java.util.Arrays;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 * Solver
 * マスターマインドに回答するクラス
 */
public class Solver extends Object {

	/**
	 * デバッグ用変数
	 */
	private static boolean DEBUG = true;

	/**
	 * 回答のチェック回数の上限値
	 */
	private static int limit = MasterMind.getlimit();
	
	/**
	 * マスターマインドの問題の桁数
	 */
	private static int zigen = MasterMind.getzigen();
	
	/**
	 * 回答をチェックした時のレスポンスを束縛する変数
	 */
	private static int[] hint = new int[2];
	
	/**
	 * 回答提出用の変数
	 */
	private static int[] testAnswer = new int[zigen];
	
	/**
	 * 0 ~ 9で、どの値が何度出現したかを記録しておく連想配列
	 * 途中でソートして挿入順に取り出されることを保証したいのでLinkedHashMapである
	 */
	private static Map<Integer, Integer> frequency = new LinkedHashMap<Integer, Integer>();
	
	/**
	 * 1つ手前の提出でヒットした回数を代入しておく変数
	 */
	private static int prevHit;

	/**
	 * 1つ手前の提出でその桁に入ってた値
	 */
	private static int prevDigit;

	/**
	 * 提出回数を記録しておく変数.
	 */
	private static int submission = 0;

	/**
	 * マスターマインドに回答するメインメソッド
	 * 前処理10回で連想配列にどれが何度出現するかを記録しておき、
	 * 次に各桁の値を順番に決めていく
	 * この時、提案する順番を出現頻度の高い順にしていくことにより、
	 * O(桁数 * 各桁がとりうる値)からO(1/2 * 桁数 * 各桁がとりうる値の種類数)になってるはず
	 */
	public static void answer() {
		Solver.countEachValue();

		Solver.decideNumber();

		Solver.submit();
	}

	/**
	 * 前処理10回を行うメソッド
	 * 全てi(0 <= i < 10)で埋めて提出しヒットの数を測る
	 */
	private static void countEachValue() {
		for(int i = 0;i < 10;i++){
			Arrays.fill(testAnswer, i);
			Solver.evaluateFirst(i);
		}

		return;
	}

	/**
	 * 実際に正解を求めていくメソッド
	 * そんなに改良してないので遅い
	 * N=桁数, M=それぞれの桁が取りうる値の範囲とすると
	 * 提出回数はO(1/2 * N * M)であるはず
	 * JavaのソートがO(NlogN)なら
	 * 計算量自体はO(1/2 * N * M * MlogM)
	 */
	private static void decideNumber() {
		for(int i = 0;i < zigen;i++){
			Solver.sortFrequency();

			int j = 0;
			for(Map.Entry<Integer, Integer> entry : frequency.entrySet()){
				testAnswer[i] = entry.getKey();
				if(Solver.evaluateSecond(i, j)) {
					frequency.put(testAnswer[i], frequency.get(testAnswer[i]) - 1);
					if(frequency.get(testAnswer[i]) == 0) frequency.remove(testAnswer[i]);
					break;
				}
				j++;
			}
		}

		return;
	}

	/**
	 * Frequencyテーブルを作成する前処理段階での評価メソッド
	 * 一旦提出して帰ってきた値が0じゃなかったら、
	 * 連想配列に追加する
	 * @param i : int 提出する際に埋めた値
	 */
	private static void evaluateFirst(int i) {
		Solver.submission++;
		hint = MasterMind.evaluate(testAnswer);
		if(hint[0]!=0) frequency.put(i, hint[0]);
		if(hint[0] == zigen) Solver.submit();

		return;
	}

	/**
	 * 各桁ごとに値を変えたあと、実際に評価するメソッド
	 * ちょっといい加減な実装なので余計な評価をさせている可能性がある
	 * @param digit : int 今どの桁を求めているか
	 * @param index : int その桁について、今何度目の提出か(0-indexed)
	 * @return その桁の値がわかった場合true
	 */
	private static boolean evaluateSecond(int digit, int index) {
		Solver.submission++;
		hint = MasterMind.evaluate(testAnswer);
		if(hint[0] == zigen) Solver.submit();
		if(index > 0){
			if(index == 1){
				if(prevHit > hint[0]){
					testAnswer[digit] = prevDigit;
					return true;
				}
			}
			if(prevHit < hint[0]){
				return true;
			}
		}

		prevHit = hint[0];
		prevDigit = testAnswer[digit];
		return false;
	}

	/**
	 * 出現頻度テーブルをソートするメソッド
	 * 連想配列のvalueを降順にソートする
	 * JavaのMapインタフェースは挿入順に取り出すことを保証していないので、
	 * ポリモーフィズムを失うあまりよくないプログラムだがLinkedHashMapに指定している
	 */
	private static void sortFrequency() {
		frequency = frequency.entrySet().stream()
		.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		.collect(Collectors.toMap(
			e -> e.getKey(),
			e -> e.getValue(),
			(s,a) -> a,
			LinkedHashMap::new
		));

		return;
	}

	/**
	 * 実際にファイナルアンサーを行うメソッド
	 * DEBUG変数がtrueだった場合、それまでに提出した回数を標準エラー出力に出力する
	 */
	private static void submit() {
		if(DEBUG) System.err.println("submission = " +  submission);
		MasterMind.submit(testAnswer);

		return;
	}
}
