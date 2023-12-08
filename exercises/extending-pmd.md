# Extending PMD

Use XPath to define a new rule for PMD to prevent complex code. The rule should detect the use of three or more nested `if` statements in Java programs so it can detect patterns like the following:

```Java
if (...) {
    ...
    if (...) {
        ...
        if (...) {
            ....
        }
    }

}
```
Notice that the nested `if`s may not be direct children of the outer `if`s. They may be written, for example, inside a `for` loop or any other statement.
Write below the XML definition of your rule.

You can find more information on extending PMD in the following link: https://pmd.github.io/latest/pmd_userdocs_extending_writing_rules_intro.html, as well as help for using `pmd-designer` [here](https://github.com/selabs-ur1/VV-ISTIC-TP2/blob/master/exercises/designer-help.md).

Use your rule with different projects and describe you findings below. See the [instructions](../sujet.md) for suggestions on the projects to use.

## Answer
Voici la règle XML : 
```xml
<rule name="ThreeNestedIf"
      language="java"
      message="MoreThanTwoNestedIf"
      class="net.sourceforge.pmd.lang.rule.XPathRule">
   <description>

   </description>
   <priority>3</priority>
   <properties>
      <property name="version" value="3.1"/>
      <property name="xpath">
         <value>
<![CDATA[
 //IfStatement[descendant::IfStatement[descendant::IfStatement]]
]]>
         </value>
      </property>
   </properties>
</rule>

```
La règle Xpath est la suivante : ```//IfStatement[descendant::IfStatement[descendant::IfStatement]]```
Elle permet de vérifier à partir de n'importe quel If, s'il n'y a pas deux If parmi ces descendants. 

On applique cette règle lors de l'analyse statique pmd sur le projet apache collections, on obtient ce résultat :
 ->  CollectionUtils.java:1503:	3IF:	MoreThanTwoNestedIf
 ->  CollectionUtils.java:1505:	3IF:	MoreThanTwoNestedIf
 ->  CollectionUtils.java:1507:	3IF:	MoreThanTwoNestedIf
 ->  CollectionUtils.java:1509:	3IF:	MoreThanTwoNestedIf
 ->  MapUtils.java:226:	3IF:	MoreThanTwoNestedIf
 ->  MapUtils.java:926:	3IF:	MoreThanTwoNestedIf
[...]
 ->  trie\AbstractPatriciaTrie.java:163:	3IF:	MoreThanTwoNestedIf
 ->  trie\AbstractPatriciaTrie.java:887:	3IF:	MoreThanTwoNestedIf
 ->  trie\AbstractPatriciaTrie.java:1217:	3IF:	MoreThanTwoNestedIf

On remarque qu'a de nombreux endroits on retrouve 3 if imbriqués.
