package parser;
import java_cup.runtime.*;
import java.io.Reader;
import ctds_pcr.ast.*;      
%% //inicio de opciones
      
/* 
    Nombre de la clase .java que crea
*/
%public %class AnalizadorLexico

/*
    Activar el contador de lineas, variable yyline
    Activar el contador de columna, variable yycolumn
*/
%line
%column
    
/* 
   Activamos la compatibilidad con Java CUP
*/
%cup
   
/*
    Declaraciones
    El codigo entre %{  y %} sera copiado integramente en el 
    analizador generado.
*/
%{    
    /*  Generamos un java_cup.Symbol para guardar el tipo de token 
        encontrado */
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    /* Generamos un Symbol para el tipo de token encontrado 
       junto con su valor */
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}
    
Espacio= [ \t\r\n ] 
Digit = [0-9]
Alpha = [a-zA-Z_]
ASCII = [^"\""] //Todos los caracteres del codigo ASCII que consideramos validos.
ComentarioLinea= "//".*\n 
%state COMENTARIO
%%

<YYINITIAL> {
    /* Regresa que el token la palabra reservada. */

    "else" {  return  symbol(sym.ELSE);   }
    "if" {  return  symbol(sym.IF);   }
    "return" {  return  symbol(sym.RETURN,new ReturnStmt(yyline,yycolumn));   }
    "while" {  return  symbol(sym.WHILE);   }

    // Comentarios 
    {ComentarioLinea} { }

    //Ignoro espacios
    {Espacio} { }


    //Op_Aritemetico. 
    "+" {  return  symbol(sym.PLUS, BinOpType.PLUS);   }


    //Asignaciones
    "=" {  return  symbol(sym.ASSIG, AssignOpType.ASSIGN);}  

    //Condicionales
    "&&" {  return  symbol(sym.AND,BinOpType.AND);   }
    "||" {  return  symbol(sym.OR,BinOpType.OR);   }
     "!" {  return  symbol(sym.NEG,UnaryOpType.NOT);   }

    //Delimitadores
    "(" {  return  symbol(sym.PARENIZQ);   }
    ")" {  return  symbol(sym.PARENDER);   }
    ";" {  return  symbol(sym.PUNTOCOMA, new SecStmt(yyline,yycolumn));}
    "," {  return  symbol(sym.COMA);   }

    //Tipos
    "int" {Type t=Type.INT; return symbol(sym.RESERV_INT,t);}

    
    //Llaves.
    "{" {  return  symbol(sym.LLAB);   }
    "}" {  return  symbol(sym.LLCER);   }
    "[" {  return  symbol(sym.CORAB);   }
    "]" {  return  symbol(sym.CORCER);   }

    //Identificador  
    {Alpha} ({Alpha}|{Digit})* {VarLocation value= new VarLocation(yytext(),yyline,yycolumn);
                                return symbol(sym.ID,value);   
                                }

    //Literales
    {Digit}{Digit}* {  return symbol(sym.INT,new IntLiteral(yytext(),yyline,yycolumn));   }
    {Digit} {Digit}* "." {Digit} {Digit}* {  return symbol(sym.FLOAT, new FloatLiteral(yytext(),yyline,yycolumn));   }
    "\""{ASCII}*"\"" {  return symbol(sym.STRING, new StringLiteral(yytext(),yyline,yycolumn));  }
    "/*"        {yybegin(COMENTARIO);     }

      .   {   System.out.println ("Caracter ilegal!!!   " + yytext() + " en linea " + yyline + " columna " + yycolumn);
      }    
}

    <COMENTARIO> {
         {Espacio}  {}
        "*/"       {yybegin(YYINITIAL); }
        .           {}

     }