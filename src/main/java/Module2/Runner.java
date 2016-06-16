package Module2;

import java.util.Arrays;
import java.util.List;

public class Runner {
    public static void main(String[] args) {
        Task<Integer> task1 = new IntegerTask(100);
        Task<Integer> task2 = new IntegerTask(-100);
        Task<Integer> task3 = new IntegerTask(400);
        Task<Integer> task4 = new IntegerTask(-10);

        List<Task<Integer>> intTasks = Arrays.asList(task1, task2, task3, task4);

        Executor<Number> numberExecutor = new ExecutorImpl();

        try {

            for (Task<Integer> intTask : intTasks) {
                numberExecutor.addTask(intTask);
            }
            numberExecutor.addTask(new LongTask(10L), new NumberValidator());
            numberExecutor.addTask(new LongTask(-10L), new NumberValidator());
            numberExecutor.addTask(new LongTask(180L), new NumberValidator());
            numberExecutor.addTask(new LongTask(-180L), new NumberValidator());
            numberExecutor.addTask(new FloatTask(-1203.34F));
            numberExecutor.addTask(new FloatTask(-123.34F), new NumberValidator());

            numberExecutor.execute();

            System.out.println("Valid results:");
            for (Number number : numberExecutor.getValidResults()) {
                System.out.println(number);
            }
            System.out.println("Invalid results:");
            for (Number number : numberExecutor.getInvalidResults()) {
                System.out.println(number);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
