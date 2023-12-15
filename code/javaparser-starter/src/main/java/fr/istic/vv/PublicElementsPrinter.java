package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


// This class visits a compilation unit and
// prints all public enum, classes or interfaces along with their public methods
public class PublicElementsPrinter extends VoidVisitorWithDefaults<Void> {

    public static String resultatTotal = "";
    public String resultat = "";
    private File file;
    private Set<String> getters;
    private Set<String> attributes;

    private Set<String> attributesWithoutGetters;
    private String packageName;
    private String className;

    public PublicElementsPrinter() {
        getters = new HashSet<>();
        attributes = new HashSet<>();
        attributesWithoutGetters = new HashSet<>();
        className = "";
    }
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        if(!declaration.isPublic()) return; //si la classe n'est pas publique on ne l'analyse pas
        //resultat += packageName;
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

        // On va retourner uniquement les attributs privés qui n'ont pas de getters
        for(String attribute : attributes) {
            String method = "get"+attribute+"()";
            if (!getters.contains(method)) {
               attributesWithoutGetters.add(attribute);
               resultat += " - " + attribute + "\n";
            }
        }
        this.writeResult(); //on écrit notre résultat de la classe dans le doc global

        for (BodyDeclaration<?> classes: declaration.getMembers()) {
            if (classes instanceof ClassOrInterfaceDeclaration) { //Si on rentre dans une classe on recrée un Visiteur et on refait le même traitement sur la classe fille
                PublicElementsPrinter p = new PublicElementsPrinter();
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
    public void visit(PackageDeclaration declaration, Void arg) {
        String mot = declaration.getNameAsString();
        System.out.println(mot);
    }

    /**
     * Récupère les résultats du parser (les attributs) et
     */
    public void writeResult() {
        System.out.println("Writing...");
        resultatTotal += resultat;
        /*try {
            FileWriter report = new FileWriter(file);
            *//*report.write("Package : " + packageName +"\n");
            report.write("Classe : " + className +"\n");
            report.write("Attributs : ");
            for (String a: attributesWithoutGetters) {

                report.write(a + ",");
            }*//*
            report.write(resultat);
            report.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}
