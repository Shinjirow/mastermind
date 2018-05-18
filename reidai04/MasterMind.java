public class MasterMind {

    private static int[] seikai = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
    private static int limit = (int) Math.pow(10.0D, 6.0D);
    private static int numcheck = 0;
    private static int[] answer = new int[2];
    private static long start;
    private static long end;

    public MasterMind() {
    }

    public static void main(String[] var0) {
        start = System.currentTimeMillis();
        Solver.answer();
        System.out.println("*** No answer submitted. ***");
    }

    public static int getlimit() {
        return limit;
    }

    public static int getzigen() {
        return seikai.length;
    }

    public static int count() {
        return numcheck;
    }

    public static void submit(int[] var0) {
        end = System.currentTimeMillis();
        --numcheck;
        int[] var1 = new int[2];
        var1 = evaluate(var0);
        System.out.println(end - start + "msecs.");
        System.out.print("Your answer = ");

        int var2;
        for (var2 = 0; var2 < seikai.length; ++var2) {
            System.out.print(var0[var2]);
        }

        System.out.println();
        System.out.print("Seikai      = ");

        for (var2 = 0; var2 < seikai.length; ++var2) {
            System.out.print(seikai[var2]);
        }

        System.out.println();
        System.out.print("hint0, hint1 = " + var1[0] + ", " + var1[1]);
        System.exit(0);
    }

    public static int[] evaluate(int[] var0) {
        if (numcheck > limit) {
            System.out.println("*** Too many checks. ***");
            System.exit(0);
        }

        if (var0.length != seikai.length) {
            System.out.println("*** Invalid array length. ***");
            System.exit(0);
        }

        for (int var1 = 0; var1 < seikai.length; ++var1) {
            if (var0[var1] < 0) {
                System.out.println("*** Invalid value. ***");
                System.exit(0);
            }

            if (var0[var1] > 9) {
                System.out.println("*** Invalid value. ***");
                System.exit(0);
            }
        }

        ++numcheck;
        answer[0] = 0;
        answer[1] = 0;
        int[] var5 = new int[seikai.length];
        int[] var2 = new int[seikai.length];

        int var3;
        for (var3 = 0; var3 < seikai.length; ++var3) {
            var5[var3] = var0[var3];
            var2[var3] = seikai[var3];
        }

        for (var3 = 0; var3 < seikai.length; ++var3) {
            if (var5[var3] == var2[var3]) {
                ++answer[0];
                var5[var3] = -1;
                var2[var3] = -2;
            }
        }

        for (var3 = 0; var3 < seikai.length; ++var3) {
            for (int var4 = 0; var4 < seikai.length; ++var4) {
                if (var5[var3] == var2[var4]) {
                    ++answer[1];
                    var5[var3] = -1;
                    var2[var4] = -2;
                }
            }
        }

        return answer;
    }
}