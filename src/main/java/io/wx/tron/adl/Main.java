package io.wx.tron.adl;

import io.wx.tron.antlr.adl.AdlL;
import io.wx.tron.antlr.adl.AdlP;
import io.wx.tron.antlr.adl.AdlP.AdlContext;
import io.wx.tron.antlr.adl.AdlPBaseListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static String indent(int level) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < level; i++) {
            buf.append("\t");
        }
        return buf.toString();
    }

    public static void main(String... args) throws IOException {
        Path file = Paths.get(args[0]);
        AdlL lexer = new AdlL(CharStreams.fromPath(file));
        AdlP parser = new AdlP(new CommonTokenStream(lexer));

        AdlContext parseTree = parser.adl();

        AtomicInteger indentLevel = new AtomicInteger();

        PrintStream out = System.out;

        AdlPBaseListener listener = new AdlPBaseListener() {
            @Override
            public void enterEveryRule(ParserRuleContext ctx) {
                out.println(indent(indentLevel.get()) + ">> " + ctx.getClass().getName());
                indentLevel.incrementAndGet();
            }

            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                indentLevel.decrementAndGet();
            }

            @Override
            public void visitTerminal(TerminalNode node) {
                //out.println(indent(indentLevel.get()) + "   >> " + node.toString());
            }

            @Override
            public void visitErrorNode(ErrorNode node) {
                System.err.println("Error: " + node);
            }
        };

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parseTree);
    }
}
