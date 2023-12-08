package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.HashSet;
import java.util.Set;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    private Set<String> getters;
    private Set<String> attributes;

    public PublicElementsPrinter() {
        getters = new HashSet<>();
        attributes = new HashSet<>();
    }
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return;
        for(MethodDeclaration method : declaration.getMethods()) {
            method.accept(this, arg);
        }
        // Printing nested types in the top level
        for(BodyDeclaration<?> member : declaration.getMembers()) {
            if (member instanceof FieldDeclaration) // FieldDeclaration comprend la déclaration des champs de la classe (attributs)
                member.accept(this, arg);
        }
        // On va retourner uniquement les attributs privés qui n'ont pas de getters
        for(String attribute : attributes) {
            String method = "get"+attribute+"()";
            if (!getters.contains(method)) {
                System.out.println("Voici les attributs privés qui n'ont pas de getter :" +attribute);
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
        if(!declaration.isPublic()) return;
        String[] mots = declaration.getDeclarationAsString(true, true).split(" ");
        getters.add(mots[2].toLowerCase());
    }

    public void visit(FieldDeclaration declaration, Void arg) {
        String[] mots = declaration.toString().split(" ");
        if(mots[0].equals("private")) {
            mots[2] = mots[2].substring(0, mots[2].length() - 1);
            attributes.add(mots[2]);
        }
    }

}
