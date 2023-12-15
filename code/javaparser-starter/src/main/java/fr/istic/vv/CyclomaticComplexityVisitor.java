package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class CyclomaticComplexityVisitor extends VoidVisitorWithDefaults<Void> {
    public static String resultatTotal = "";
    public String resultat = "";
    private String className;
    private Set<String> methods;

    public CyclomaticComplexityVisitor() {
        className = "";
        Set<String> methods = new HashSet<>();
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

        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof FieldDeclaration) // FieldDeclaration comprend la déclaration des champs de la classe (attributs)
                member.accept(this, arg);
        }

        this.writeResult(); //on écrit notre résultat de la classe dans le doc global

        //parcours classes
        for (BodyDeclaration<?> classes: declaration.getMembers()) {
            if (classes instanceof ClassOrInterfaceDeclaration) { //Si on rentre dans une classe on recrée un Visiteur et on refait le même traitement sur la classe fille
                CyclomaticComplexityVisitor p = new CyclomaticComplexityVisitor();
                classes.accept(p, null);
            }
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(EnumDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    @Override
    public void visit(MethodDeclaration declaration, Void arg) {
        //Quand on visite une méthode, on regarde tous ses membres et si on trouve des if, for, foreach, while on ajoute 1
        if(declaration.getBody().isPresent()){
            for(Statement e : declaration.getBody().get().getStatements()){
        
            }
        }
    }

    public void visit(FieldDeclaration declaration, Void arg) {
    }
    public void visit(PackageDeclaration declaration, Void arg) {
    }

    /**
     * Récupère les résultats du parser (les attributs) et
     */
    public void writeResult() {
        System.out.println("Writing...");
        resultatTotal += resultat;
    }
}
