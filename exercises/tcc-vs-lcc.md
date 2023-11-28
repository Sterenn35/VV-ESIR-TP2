# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer
```java
public class Example {

    // Attributs liés à la tâche spécifique
    private int taskSpecificData;
    private String taskSpecificResult;

    // Attribut avec des responsabilités différentes
    private List<String> additionalData;

    // Méthodes liées à la tâche spécifique
    public void performTask() {
        // Logique de la tâche spécifique
        // Utilisation de taskSpecificData et additionalData
        taskSpecificResult = "Task completed successfully.";
    }

    // Méthodes avec des responsabilités différentes
    public void addAdditionalData(String data) {
        additionalData.add(data);
    }
}
```

