package Module2;

import java.util.ArrayList;
import java.util.List;

public interface Executor<T> {
    // Добавить таск на выполнение. Результат таска будет доступен через метод getValidResults().
    // Бросает Эксепшн если уже был вызван метод execute()
    void addTask(Task<? extends T> task) throws Exception;

    // Добавить таск на выполнение и валидатор результата. Результат таска будет записан в ValidResults если validator.isValid вернет true для этого результата
    // Результат таска будет записан в InvalidResults если validator.isValid вернет false для этого результата
    // Бросает Эксепшн если уже был вызван метод execute()
    void addTask(Task<? extends T> task, Validator<T> validator) throws Exception;

    // Выполнить все добавленые таски
    void execute();

    // Получить валидные результаты. Бросает Эксепшн если не был вызван метод execute()
    List<T> getValidResults();

    // Получить невалидные результаты. Бросает Эксепшн если не был вызван метод execute()
    List<T> getInvalidResults();
}

interface Task<T> {

    // Метода запускает таск на выполнение
    void execute();

    // Возвращает результат выполнения
    T getResult();
}

interface Validator<T> {

    // Валидирует переданое значение
    boolean isValid(T result);

}

class IntegerTask implements Task<Integer> {
    private int value, result;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public IntegerTask(int value) {
        this.value = value;
    }

    @Override
    public void execute() {
        Double d = value * Math.random();
        result = d.intValue();
    }

    @Override
    public Integer getResult() {
        return result;
    }
}

class LongTask implements Task<Long> {
    private Long value, result;

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public LongTask(long aValue) {
        value = aValue;
    }

    @Override
    public void execute() {
        Double d = value * Math.random();
        result = d.longValue();
    }

    @Override
    public Long getResult() {
        return result;
    }
}

class FloatTask implements Task<Float> {
    private Float value, result;

    public FloatTask(Float value) {
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public void execute() {
        Double d = value * Math.random();
        result = d.floatValue();
    }

    @Override
    public Float getResult() {
        return result;
    }
}

class NumberValidator implements Validator<Number> {
    @Override
    public boolean isValid(Number number) {
        if (number.doubleValue() < 0) return false;
        return true;
    }
}

class ExecutorImpl implements Executor<Number> {

    private List validResults;
    private List invalidResults;
    List<TaskToExecute> tasksToExecute;
    private boolean isExecuted = false;

    public ExecutorImpl() {
        this.validResults = new ArrayList<>();
        this.invalidResults = new ArrayList<>();
        this.tasksToExecute = new ArrayList<>();
    }

    @Override
    public void addTask(Task<? extends Number> task) throws Exception {
        if (isExecuted) throw new Exception();
        tasksToExecute.add(new TaskToExecute(task, null));
    }

    @Override
    public void addTask(Task<? extends Number> task, Validator<Number> validator) throws Exception {
        if (isExecuted) throw new Exception();
        tasksToExecute.add(new TaskToExecute(task, validator));
    }

    @Override
    public void execute() {
        for (TaskToExecute taskToExecute : tasksToExecute) {
            taskToExecute.task.execute();
            if (taskToExecute.validator == null) {
                validResults.add(taskToExecute.task.getResult());
            } else {
                if (taskToExecute.validator.isValid(taskToExecute.task.getResult()))
                    validResults.add(taskToExecute.task.getResult());
                else invalidResults.add(taskToExecute.task.getResult());
            }
        }
    }

    @Override
    public List getValidResults() {
        return validResults;
    }

    @Override
    public List getInvalidResults() {
        return invalidResults;
    }
}

class TaskToExecute {
    Task task;
    Validator validator;

    public TaskToExecute(Task<? extends Number> task, Validator validator) {
        this.task = task;
        this.validator = validator;
    }
}
