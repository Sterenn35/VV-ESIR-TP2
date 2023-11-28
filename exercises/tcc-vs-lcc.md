# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

## Answer
```java
public class Example {

    // Attribut lié à la tâche spécifique
    private int tcc;

    // Attribut avec des responsabilités différentes
    private List<String> lcc;

    // Méthodes liées à la tâche spécifique
    public void tacheTCC() {
        // Logique de la tâche spécifique
        tcc = 0;
    }

    // Méthodes avec des responsabilités différentes
    public void tacheLCC(String data) {
        lcc.add(data);
    }
}
```

