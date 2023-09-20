package compilador;


import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


public class Semantico implements Constants
{
    private String operador;
    private String tipo_var;
    private int qtdeRotulos = 0;
    private StringBuilder codigo = new StringBuilder();

    private Stack<Tipo> pilhaTipos = new Stack<>();
    private Stack<String> pilhaRotulos = new Stack<>();
    private LinkedList<String> lista_id = new LinkedList<>();
    private HashMap<String, String> tabela_simbolos = new HashMap<>();

    private static final String QUEBRA_LINHA = "\n";
    private static final String TAB = "\t";

    public void executeAction(int action, Token token)	throws SemanticError
    {
        System.out.println("Ação #"+action+", Token: "+token);
        switch (action) {
            case 1:
                executarAcaoSemantica1(token);
                break;
            case 2:
                executarAcaoSemantica2(token);
                break;
            case 3:
                executarAcaoSemantica3(token);
                break;
            case 4:
                executarAcaoSemantica4(token);
                break;
            case 5:
                executarAcaoSemantica5(token);
                break;
            case 6:
                executarAcaoSemantica6(token);
                break;
            case 7:
                executarAcaoSemantica7(token);
                break;
            case 8:
                executarAcaoSemantica8(token);
                break;
            case 9:
                operador = token.getLexeme();
                break;
            case 10:
                executarAcaoSemantica10(token);
                break;
            case 11:
                executarAcaoSemantica11();
                break;
            case 12:
                executarAcaoSemantica12();
                break;
            case 13:
                executarAcaoSemantica13(token);
                break;
            case 14:
                executarAcaoSemantica14();
                break;
            case 15:
                executarAcaoSemantica15();
                break;
            case 16:
                executarAcaoSemantica16();
                break;
            case 17:
                executarAcaoSemantica17();
                break;
            case 18:
                executarAcaoSemantica18(token);
                break;
            case 19:
                executarAcaoSemantica19(token);
                break;
            case 20:
                executarAcaoSemantica20(token);
                break;
            case 21:
                executarAcaoSemantica21(token);
                break;
            case 22:
                executarAcaoSemantica22(token);
                break;
            case 24:
                executarAcaoSemantica24();
                break;
            case 25:
                executarAcaoSemantica25();
                break;
            case 26:
                executarAcaoSemantica26();
                break;
            case 27:
                executarAcaoSemantica27();
                break;
            case 28:
                executarAcaoSemantica28();
                break;
            case 30:
                executarAcaoSemantica30(token);
                break;
            case 31:
                executarAcaoSemantica31();
                break;
            case 32:
                lista_id.add(token.getLexeme());
                break;
            case 33:
                executarAcaoSemantica33(token);
                break;   
            case 34:
                executarAcaoSemantica34();
                break;    
            case 35:
                executarAcaoSemantica35();
                break;    
        }
    }

    private void executarAcaoSemantica35() {
        for (String id : lista_id) {
            String classe = "";
            String tipoid = tabela_simbolos.get(id);
            Tipo tipo = verificaTipo(tipoid);

            if (tipo == Tipo.int64) {
                classe = "Int64";
            } else if (tipo == Tipo.float64) {
                classe = "Double";
            }
            codigo.append(TAB).append("call string [mscorlib]System.Console::ReadLine()").append(QUEBRA_LINHA);
            codigo.append(TAB).append("call ").append(tipoid).append(" [mscorlib]System.").append(classe).append("::Parse(string)").append(QUEBRA_LINHA);
            codigo.append(TAB).append("stloc ").append(id).append(QUEBRA_LINHA);
        }
        lista_id.clear();
    }

    private void executarAcaoSemantica34() {
        String id2 = lista_id.pop();
        String tipoid = tabela_simbolos.get(id2);
        Tipo tipo = verificaTipo(tipoid);
        pilhaTipos.pop();
        if (tipo == Tipo.int64) {
            codigo.append(TAB).append("conv.i8").append(QUEBRA_LINHA);
        }

        codigo.append(TAB).append("stloc ").append(id2).append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica33(Token token) {
        String id = token.getLexeme();
        String tipoId = tabela_simbolos.get(id);
        Tipo tipo = verificaTipo(tipoId);
        pilhaTipos.push(tipo);
        codigo.append(TAB).append("ldloc ").append(id).append(QUEBRA_LINHA);
        if (tipo == Tipo.int64) {
            codigo.append(TAB).append("conv.i8").append(QUEBRA_LINHA);
        }   
    }

    private void executarAcaoSemantica31() {
        for (String id : lista_id) {
            tabela_simbolos.put(id, tipo_var);
            codigo.append(TAB).append(".locals(").append(tipo_var).append(" ").append(id).append(")").append(QUEBRA_LINHA);
        }
        lista_id.clear();
    }

    private void executarAcaoSemantica30(Token token) {
        switch (token.getLexeme().toUpperCase()) {
            case "INT":
               tipo_var = "int64";
               break;
            case "FLOAT":
                tipo_var = "float64";
                break;
            case "BOOL":
                tipo_var = "bool";
                break;
            default:
                tipo_var = token.getLexeme();
                break;
        }
    }

    private void executarAcaoSemantica28() {
        codigo.append(TAB).append("brtrue ").append(pilhaRotulos.pop()).append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica27() {
        codigo.append(TAB).append(getRotulo()).append(":").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica26() {
        codigo.append(TAB).append(pilhaRotulos.pop()).append(":").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica25() {
        String rotulo1= pilhaRotulos.pop();
        String rotulo2 = getRotulo();
        codigo.append(TAB).append("br ").append(rotulo2).append(QUEBRA_LINHA);
        codigo.append(TAB).append(rotulo1).append(":").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica24() {
        String rotulo = getRotulo();
        codigo.append(TAB).append("brfalse ").append(rotulo).append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica22(Token token) {
        pilhaTipos.push(Tipo.string);
        codigo.append(TAB).append("ldstr ").append(token.getLexeme()).append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica21(Token token) {
        pilhaTipos.push(Tipo.character);
        String chartxt;

        if(token.getLexeme().equals("\\n")) {
            chartxt = "\"\\n\"";
        } else if (token.getLexeme().equals("\\s")) {
            chartxt = "\" \"";
        } else {
            chartxt = "\"\\t\"";
        }
        codigo.append(TAB).append(chartxt).append(QUEBRA_LINHA);

    }

    private void executarAcaoSemantica20(Token token) throws SemanticError {
        Tipo tipo1 = pilhaTipos.pop();
        Tipo tipo2 = pilhaTipos.pop();

        if(tipo1 == Tipo.int64 && tipo2 == Tipo.int64) {
             pilhaTipos.push(Tipo.int64);
            
        } else {
            throw new SemanticError("tipos incompatíveis em expressão aritmética",token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("div").append(QUEBRA_LINHA);
        
    }

    private void executarAcaoSemantica19(Token token) throws SemanticError {
        Tipo tipo1 = pilhaTipos.pop();
        Tipo tipo2 = pilhaTipos.pop();
        if (Tipo.bool.equals(tipo1) && Tipo.bool.equals(tipo2)) {
            pilhaTipos.push(Tipo.bool);
            pilhaTipos.push(Tipo.bool);
        } else {
            throw new SemanticError("tipos incompatíveis em expressão lógica", token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("or").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica18(Token token) throws SemanticError {
        Tipo tipo1 = pilhaTipos.pop();
        Tipo tipo2 = pilhaTipos.pop();
        if (Tipo.bool.equals(tipo1) && Tipo.bool.equals(tipo2)) {
            pilhaTipos.push(Tipo.bool);
            pilhaTipos.push(Tipo.bool);
        } else {
            throw new SemanticError("tipos incompatíveis em expressão lógica", token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("and").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica17() {
        codigo.append(TAB).append("ldstr \"\\n\"").append(QUEBRA_LINHA);
        codigo.append(TAB).append("call void [mscorlib]System.Console::Write(string)").append(QUEBRA_LINHA);

    }

    private void executarAcaoSemantica16() {
        codigo.append(TAB).append("ret").append(QUEBRA_LINHA);
        codigo.append(TAB).append("}").append(QUEBRA_LINHA);
        codigo.append("}").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica15() {
        codigo.append(".assembly extern mscorlib {}").append(QUEBRA_LINHA);
        codigo.append(".assembly _codigo_objeto{}").append(QUEBRA_LINHA);
        codigo.append(".module   _codigo_objeto.exe").append(QUEBRA_LINHA);
        codigo.append(QUEBRA_LINHA).append(".class public _UNICA{ ").append(QUEBRA_LINHA);
        codigo.append(".method static public void _principal() {").append(QUEBRA_LINHA);
        codigo.append(TAB).append(".entrypoint").append(QUEBRA_LINHA);
    }


    private void executarAcaoSemantica14() {
        Tipo tipo = pilhaTipos.pop();
        if(tipo == Tipo.int64) {
            codigo.append(TAB).append("conv.i8").append(QUEBRA_LINHA);
        }

        codigo.append(TAB).append("call void [mscorlib]System.Console::Write(").append(tipo.name()).append(")").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica13(Token token) throws SemanticError {
        Tipo tipo = pilhaTipos.pop();
        if(Tipo.bool.equals(tipo)) {
            pilhaTipos.push(tipo);
        } else {
            throw new SemanticError("tipo incompatível em expressão lógica",token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("Idc.i4.1").append(QUEBRA_LINHA);
        codigo.append(TAB).append("xor").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica12() {
        pilhaTipos.push(Tipo.bool);
        codigo.append(TAB).append("Idc.i4.0").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica11() {
        pilhaTipos.push(Tipo.bool);
        codigo.append(TAB).append("Idc.i4.1").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica10(Token token) throws SemanticError {
        Tipo tipo1 = pilhaTipos.pop();
        Tipo tipo2 = pilhaTipos.pop();

        List<String> operadores = Arrays.asList("=", "!=", "<", ">");

       if (((Tipo.int64.equals(tipo1) || Tipo.float64.equals(tipo1)) && (Tipo.int64.equals(tipo2) || Tipo.float64.equals(tipo2)) && operadores.contains(operador))
            || (Tipo.string.equals(tipo1)  && Tipo.string.equals(tipo2) && operadores.contains(operador))) {
                pilhaTipos.push(Tipo.bool);
        } else {
            throw new SemanticError("tipos incompatíveis em expressão relacional",token.getPosition(), token.getLine());
        }

        switch (operador) {
            case ">":
                codigo.append(TAB).append("cgt").append(QUEBRA_LINHA);
                break;
            case "<":
                codigo.append(TAB).append("clt").append(QUEBRA_LINHA);
                break;
            case "=":
                codigo.append(TAB).append("ceq").append(QUEBRA_LINHA);
                break;
            case "!=":
                codigo.append(TAB).append("ceq").append(QUEBRA_LINHA);
                codigo.append(TAB).append("ldc.i4 0").append(QUEBRA_LINHA);
                codigo.append(TAB).append("ceq").append(QUEBRA_LINHA);
                break;
        }

    }

    private void executarAcaoSemantica8(Token token) throws SemanticError {
        Tipo tipo = pilhaTipos.pop();

        if(tipo == Tipo.float64 || tipo == Tipo.int64) {
            pilhaTipos.push(tipo);
        } else {
            throw new SemanticError("tipo incompatível em expressão aritmética",token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("Idc.i8 -1").append(QUEBRA_LINHA);

        if(tipo == Tipo.int64) {
            codigo.append(TAB).append("conv.r8").append(QUEBRA_LINHA);
        }

        codigo.append(TAB).append("mul").append(QUEBRA_LINHA);

    }

    private void executarAcaoSemantica7(Token token) throws SemanticError {
        Tipo tipo = pilhaTipos.pop();

        if(tipo == Tipo.float64 || tipo == Tipo.int64) {
            pilhaTipos.push(tipo);
        } else {
            throw new SemanticError("tipo incompatível em expressão aritmética",token.getPosition(), token.getLine());
        }
    }

    private void executarAcaoSemantica6(Token token) {
        pilhaTipos.push(Tipo.float64);
        codigo.append(TAB).append("Idc.r8 ").append(token.getLexeme().replaceAll(",",".")).append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica5(Token token) {
        pilhaTipos.push(Tipo.int64);
        codigo.append(TAB).append("Idc.i8 ").append(token.getLexeme()).append(QUEBRA_LINHA);
        codigo.append(TAB).append("conv.r8").append(QUEBRA_LINHA);
    }

    private void executarAcaoSemantica4(Token token) throws SemanticError {
        Tipo tipo1 = pilhaTipos.pop();
        Tipo tipo2 = pilhaTipos.pop();

        if((tipo1 == Tipo.float64 || tipo1 == Tipo.int64) && (tipo2 == Tipo.float64 || tipo2 == Tipo.int64)) {
            if((tipo1 == Tipo.float64 || tipo2 == Tipo.float64)) {
                pilhaTipos.push(Tipo.float64);
            } else {
                pilhaTipos.push(Tipo.int64);
            }
        } else {
            throw new SemanticError("tipos incompatíveis em expressão aritmética",token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("div").append(QUEBRA_LINHA);

    }	

    private void executarAcaoSemantica3(Token token) throws SemanticError {
        Tipo tipo1 = pilhaTipos.pop();
        Tipo tipo2 = pilhaTipos.pop();

        if((tipo1 == Tipo.float64 || tipo1 == Tipo.int64) && (tipo2 == Tipo.float64 || tipo2 == Tipo.int64)) {
            if((tipo1 == Tipo.float64 || tipo2 == Tipo.float64)) {
                pilhaTipos.push(Tipo.float64);
            } else {
                pilhaTipos.push(Tipo.int64);
            }
        } else {
            throw new SemanticError("tipos incompatíveis em expressão aritmética",token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("mul").append(QUEBRA_LINHA);

    }	

    private void executarAcaoSemantica2(Token token) throws SemanticError {
        Tipo tipo1 = pilhaTipos.pop();
        Tipo tipo2 = pilhaTipos.pop();

        if((tipo1 == Tipo.float64 || tipo1 == Tipo.int64) && (tipo2 == Tipo.float64 || tipo2 == Tipo.int64)) {
            if((tipo1 == Tipo.float64 || tipo2 == Tipo.float64)) {
                pilhaTipos.push(Tipo.float64);
            } else {
                pilhaTipos.push(Tipo.int64);
            }
        } else {
            throw new SemanticError("tipos incompatíveis em expressão aritmética",token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("sub").append(QUEBRA_LINHA);

    }	

    private void executarAcaoSemantica1(Token token) throws SemanticError {
        Tipo tipo1 = pilhaTipos.pop();
        Tipo tipo2 = pilhaTipos.pop();

        if((tipo1 == Tipo.float64 || tipo1 == Tipo.int64) && (tipo2 == Tipo.float64 || tipo2 == Tipo.int64)) {
            if((tipo1 == Tipo.float64 || tipo2 == Tipo.float64)) {
                pilhaTipos.push(Tipo.float64);
            } else {
                pilhaTipos.push(Tipo.int64);
            }
        } else {
            throw new SemanticError("tipos incompatíveis em expressão aritmética",token.getPosition(), token.getLine());
        }
        codigo.append(TAB).append("add").append(QUEBRA_LINHA);

    }

    private Tipo verificaTipo(String tipoString) {
        Tipo tipo;
        switch (tipoString) {
            case "int64":
                tipo = Tipo.int64;
                break;
            case "float64":
                tipo = Tipo.float64;
                break;
            case "bool":
                tipo = Tipo.bool;
                break;
            default:
                tipo = Tipo.string;
                break;
        }
        return tipo;
    }
    
    private String getRotulo() {
        qtdeRotulos++;
        String rotulo = "L" + qtdeRotulos;
        pilhaRotulos.push(rotulo);
        return rotulo;
    }

    public String getCodigo() {
        return codigo.toString();
    }

}

enum Tipo {

    int64, float64, string, character, bool, 
    
}
