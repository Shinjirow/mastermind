import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Collections;

public class Solver {

	private static boolean DEBUG = true;

	private static int limit = MasterMind.getlimit();
	private static int zigen = MasterMind.getzigen();
	private static int[] x = new int[zigen];
	private static int[] hint = new int[2];
	private static int[] bestAnswer = new int[zigen];
	private static int[] bestscore = { 0, 0 };

	private static int[] testAnswer = new int[zigen];
	private static Map<Integer, Integer> frequency = new LinkedHashMap<Integer, Integer>();
	private static int[] prevHint = new int[2];
	private static int[] prevAnswer = new int[zigen];
	private static int prevHit;
	private static int prevDigit;

	private static int submission = 0;

	public static void answer() {
		Solver.countEachValue();

		Solver.decideNumber();

		Solver.submit();
	}

	private static void countEachValue(){
		for(int i = 0;i < 10;i++){
			Arrays.fill(testAnswer, i);
			Solver.evaluateFirst(i);
		}
	}

	private static void decideNumber(){
		for(int i = 0;i < zigen;i++){
			Solver.sortFrequency();

			int j = 0;
			for(Map.Entry<Integer, Integer> entry : frequency.entrySet()){
				// if(DEBUG) System.err.println("entry = " + entry);
				testAnswer[i] = entry.getKey();
				// if(DEBUG) {System.err.print("Submission = ");for(int k : testAnswer) System.err.print(k); System.err.println();}
				if(Solver.evaluateSecond(i, j)) {
					// if(DEBUG) System.err.println("わかったので減らす");
					frequency.put(testAnswer[i], frequency.get(testAnswer[i]) - 1);
					if(frequency.get(testAnswer[i]) == 0) frequency.remove(testAnswer[i]);
					break;
				}
				j++;
			}
		}

		// if(DEBUG) frequency.entrySet().forEach(System.err::println);
	}

	private static void evaluateFirst(int i){
		Solver.submission++;
		hint = MasterMind.evaluate(testAnswer);
		if(hint[0]!=0) frequency.put(i, hint[0]);
		if(hint[0] == zigen) Solver.submit();
	}

	private static boolean evaluateSecond(int digit, int index){
		Solver.submission++;
		hint = MasterMind.evaluate(testAnswer);
		if(hint[0] == zigen) Solver.submit();
		// if(DEBUG) {for(int i : hint) {System.err.print(i);} System.err.println();}
		if(index > 0){
			// if(DEBUG) System.err.println("prevHit = " + prevHit);
			if(index == 1){
				if(prevHit > hint[0]){
					//0回目の提案が正しかった
					testAnswer = prevAnswer;
					testAnswer[digit] = prevDigit;
					return true;
				}
			}
			if(prevHit < hint[0]){
				//n回目の提案が正しかった
				return true;
			}
		}

		prevHint = hint;
		prevHit = hint[0];
		prevAnswer = testAnswer;
		prevDigit = testAnswer[digit];
		return false;
	}

	private static void sortFrequency(){
		frequency = frequency.entrySet().stream()
		.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		.collect(Collectors.toMap(
			e -> e.getKey(),
			e -> e.getValue(),
			(s,a) -> a,
			LinkedHashMap::new
		));
	}

	private static void submit(){
		if(DEBUG) System.err.println("submission = " +  submission);
		MasterMind.submit(testAnswer);
	}
}
