package ch.fhnw.cpib.platform.parser;

import ch.fhnw.cpib.platform.parser.abstracttree.AbstractTree;
import ch.fhnw.cpib.platform.parser.concretetree.ConcreteTree;
import ch.fhnw.cpib.platform.scanner.Scanner;
import ch.fhnw.cpib.platform.scanner.tokens.TokenList;
import ch.fhnw.cpib.platform.utils.ReaderUtils;
import ch.fhnw.cpib.vm.IVirtualMachine;
import ch.fhnw.cpib.vm.VirtualMachine;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ParserTest {

    // Overflow.iml and TypeConversions.iml were dropped due their use of pre- and post increment/decrement
    private static final List<String> filenames = Arrays.asList(
        "/Team/Program1.iml",
        "/Team/Program2.iml",
        "/Existing/Assoc.iml",
        "/Existing/Cube.iml",
        "/Existing/EuclidExtended.iml",
        "/Existing/EuclidExtendedV2.iml",
        "/Existing/Expr.iml",
        "/Existing/Extreme.iml",
        "/Existing/Factorial.iml",
        "/Existing/Globals.iml",
        "/Existing/IntDiv.iml",
        "/Existing/IntDivCast.iml",
        "/Existing/intDivFun.iml",
        "/Existing/intDivMain.iml",
        "/Existing/ModInverse.iml",
        "/Existing/MultiAssi.iml",
        "/Existing/mutRec.iml",
        "/Existing/OutCopyTypeConversion.iml",
        "/Existing/OverwritingOutParams.iml",
        "/Existing/Parameters.iml",
        "/Existing/Parameters02.iml",
        "/Existing/RefParams.iml",
        "/Existing/RSAExampleGallier.iml",
        "/Existing/SameOutInit.iml",
        "/Existing/Scopes.iml",
        "/Existing/ScopesEdit.iml",
        "/Existing/ScopesImport.iml",
        "/Existing/ScopesImportInit.iml",
        "/Existing/test.iml",
        "/Existing/test01.iml",
        "/Existing/test2.iml",
        "/Existing/test02.iml",
        "/Existing/test3.iml",
        "/Existing/test4.iml",
        "/Existing/test5.iml",
        "/Existing/test6.iml",
        "/Existing/test7.iml",
        "/Existing/test08.iml",
        "/Existing/test10.iml",
        "/Existing/TestDivMod.iml",
        "/Existing/TruthTable.iml"
    );

    @Test
    public void testParser() throws Exception {
        // Create the scanner and parser
        Scanner scanner = new Scanner();
        Parser parser = new Parser();

        // Parse the files
        for (String filename : filenames) {
            // Load the program
            String content = ReaderUtils.getContentFromInputStream(getClass().getResourceAsStream(filename), StandardCharsets.UTF_8);
            Assert.assertFalse(content.isEmpty());

            // Scan the program
            TokenList tokenlist = scanner.scanString(content);
            Assert.assertTrue(tokenlist.getSize() > 0);

            // Parse the token list
            ConcreteTree.Program program = parser.parseTokenList(tokenlist);
            Assert.assertTrue(program.toString().length() > 0);
        }
    }

    @Test
    public void testAbstractTree() throws Exception {
        // Fake the standard input and provide a value for debugin
        ByteArrayInputStream in = new ByteArrayInputStream("42\n".getBytes());
        System.setIn(in);

        // Create the scanner and parser
        Scanner scanner = new Scanner();
        Parser parser = new Parser();

        // Create the virtual machine
        int codesize = 1000;
        int storesize = 1000;
        IVirtualMachine machine = new VirtualMachine(codesize, storesize);

        // Load the program
        String filename = "/Team/Program3.iml";
        String content = ReaderUtils.getContentFromInputStream(getClass().getResourceAsStream(filename), StandardCharsets.UTF_8);
        Assert.assertFalse(content.isEmpty());

        // Scan the program
        TokenList tokenlist = scanner.scanString(content);
        Assert.assertTrue(tokenlist.getSize() > 0);
        System.out.println(tokenlist);
        System.out.println();

        // Parse the token list
        ConcreteTree.Program concreteprogram = parser.parseTokenList(tokenlist);
        Assert.assertTrue(concreteprogram.toString().length() > 0);
        //System.out.println(concreteprogram);
        //System.out.println();

        // Make the parse tree abstract
        AbstractTree.Program abstractprogram = concreteprogram.toAbstract();
        System.out.println(abstractprogram);
        System.out.println();

        // Check the abstract tree
        abstractprogram.check();

        // Generate the code for the abstract tree
        //abstractprogram.code(machine, 0);

        // Generate the code by hand
        machine.IntLoad(0, 995);
        machine.IntInput(1, "x");
        machine.IntLoad(2, 995);
        machine.Deref(3);
        machine.IntOutput(4, "x");
        machine.Stop(5);

        // Execute the code for the abstract tree
        machine.execute();
    }
}
