package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }


        File report = new File("report.txt");
        if (report.createNewFile()) {
            System.out.println("File created: " + report.getName());
        } else {
                System.out.println("File already exists.");
        }
        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        PublicElementsPrinter.resultatTotal += "Attributs privés de classes publiques sans getters \n \n";

        root.parse("", (localPath, absolutePath, result) -> {
            PublicElementsPrinter.resultatTotal += "Fichier : " + localPath + "\n";
            result.ifSuccessful(unit -> unit.accept(printer, null)); //on accepte le visiteur et on commence le traitement
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        try {
            FileWriter writer = new FileWriter(report);
            writer.write(PublicElementsPrinter.resultatTotal);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
