package test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
//import rs.etf.sab.tests.CityOperationsTest;
//import rs.etf.sab.tests.DistrictOperationsTest;
//import rs.etf.sab.tests.PublicModuleTest;
//import rs.etf.sab.tests.UserOperationsTest;
//import rs.etf.sab.tests.VehicleOperationsTest;

public final class TestRunner {
    private static final int MAX_POINTS_ON_PUBLIC_TEST = 10;
    private static final Class[] UNIT_TEST_CLASSES = new Class[]{CityOperationsTest.class, DistrictOperationsTest.class, UserOperationsTest.class, VehicleOperationsTest.class, PackageOperationsTest.class};
    private static final Class[] MODULE_TEST_CLASSES = new Class[]{PublicModuleTest.class};

    private static double runUnitTestsPublic() {
        double numberOfSuccessfulCases = 0.0;
        double numberOfAllCases = 0.0;
        double points = 0.0;
        JUnitCore jUnitCore = new JUnitCore();
        for (Class testClass : UNIT_TEST_CLASSES) {
            System.out.println("\n" + testClass.getName());
            Request request = Request.aClass(testClass);
            Result result = jUnitCore.run(request);
            numberOfAllCases = result.getRunCount();
            numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
            if (numberOfSuccessfulCases < 0.0) {
                numberOfSuccessfulCases = 0.0;
            }
            System.out.println("Successful: " + numberOfSuccessfulCases);
            System.out.println("All: " + numberOfAllCases);
            double points_curr = 2.0 * numberOfSuccessfulCases / numberOfAllCases;
            System.out.println("Points: " + points_curr);
            points += points_curr;
        }
        return points;
    }

    private static double runModuleTestsPublic() {
        double numberOfSuccessfulCases = 0.0;
        double numberOfAllCases = 0.0;
        double points = 0.0;
        JUnitCore jUnitCore = new JUnitCore();
        for (Class testClass : MODULE_TEST_CLASSES) {
            System.out.println("\n" + testClass.getName());
            Request request = Request.aClass(testClass);
            Result result = jUnitCore.run(request);
            numberOfAllCases = result.getRunCount();
            numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
            if (numberOfSuccessfulCases < 0.0) {
                numberOfSuccessfulCases = 0.0;
            }
            System.out.println("Successful: " + numberOfSuccessfulCases);
            System.out.println("All: " + numberOfAllCases);
            double points_curr = 2.0 * numberOfSuccessfulCases / numberOfAllCases;
            System.out.println("Points: " + points_curr);
            points += points_curr;
        }
        return points;
    }

    private static double runPublic() {
        double res = 0.0;
        res += TestRunner.runUnitTestsPublic();
        return res += TestRunner.runModuleTestsPublic();
    }

    public static void runTests() {
        double resultsPublic = TestRunner.runPublic();
        System.out.println("Points won on public tests is: " + resultsPublic + " out of 10");
    }
}