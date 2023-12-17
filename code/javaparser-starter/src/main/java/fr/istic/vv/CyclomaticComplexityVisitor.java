package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexityVisitor extends VoidVisitorWithDefaults<Void> {
    public static String resultatTotal = "";
    public String resultat;
    private String className;
    private String methodName;
    private int cpt;

    public CyclomaticComplexityVisitor() {
        resultat = "";
        className = "";
        methodName = "";
        cpt = 1; //compteur de complexité cyclomatique (nb de comditions + 1)
    }
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        className = declaration.getFullyQualifiedName().orElse("[Anonymous]");
        resultat += "\n" + className + "\n";

        //Parcours des méthodes
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }

        this.writeResult(); //on écrit notre résultat de la classe dans le doc global

        //parcours classes
        for (BodyDeclaration<?> classes: declaration.getMembers()) {
            if (classes instanceof ClassOrInterfaceDeclaration) { //Si on rentre dans une classe on recrée un Visiteur et on refait le même traitement sur la classe fille
                //@TODO ptet mettre un reset des attributs
                classes.accept(this, null);
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        //Quand on visite une méthode, on regarde tous ses membres et si on trouve des if, for, foreach, while on ajoute 1
        cpt = 1;
        methodName = declaration.getDeclarationAsString();
        if(declaration.getBody().isPresent()){
            for(Statement statement: declaration.getBody().get().getStatements()){
                   statement.accept(this, arg);
            }
        }
        resultat = methodName + " : " + cpt;
    }

    public void visit(IfStmt statement, Void arg) {
        cpt++;
        System.out.println("if");
        if(statement.hasThenBlock()){
            for(Node node : statement.getThenStmt().getChildNodes()){
                node.accept(this, arg);
            }
        }
        if(statement.hasElseBlock()){
            for(Node node : statement.getElseStmt().get().getChildNodes()){
                node.accept(this, arg);
            }
        }
    }
    public void visit(ForStmt statement, Void arg) {
        cpt++;
        System.out.println("for");
        if(!statement.hasEmptyBody()){
            for(Node node : statement.getBody().getChildNodes()){
                node.accept(this, arg);
            }
        }
    }

    public void visit(ForEachStmt statement, Void arg) {
        cpt++;
        System.out.println("foreach");
        if(!statement.hasEmptyBody()){
            for(Node node : statement.getBody().getChildNodes()){
                node.accept(this, arg);
            }
        }
    }

    public void visit(WhileStmt statement, Void arg) {
        cpt++;
        System.out.println("while");
        if(!statement.hasEmptyBody()){
            for(Node node : statement.getBody().getChildNodes()){
                node.accept(this, arg);
            }
        }
    }



        /**
         * Récupère les résultats du parser (les attributs) et
         */
    public void writeResult() {
        System.out.println("Writing...");
        resultatTotal += resultat;
    }
}
