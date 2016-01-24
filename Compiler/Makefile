#
#	Makefile for Assignment 15
#
#----------------------------------
#
#	Rules

%.class: %.java
	javac $<

#	Targets

all: parser.class sym.class Yylex.class SymbolTable.class \
TreeNode.class Ops.class SemanticAction.class TreeOps.class \
Utility.class CodeGenerator.class IdEntry.class Err.class \
Warning.class mycompiler.class BackpatchNode.class

#	Build the parser and the sym class

parser.class sym.class: Yylex.java

Yylex.class: parser.java sym.java

parser.java sym.java: grammar.cup
	java java_cup.Main -expect 4 < grammar.cup

#	Build the Yylex class

Yylex.java: tokens.flex
	java jflex.Main tokens.flex
