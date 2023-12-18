package compiler;

import java.io.IOException;

public class StringCodeGen {
    public static void main(String[] args){
        String input =
                "1 - INSERIR UM NOVO ALUNO\n" +
                "2 - ALTERAR UM ALUNO\n" +
                "3 - EXCLUIR UM ALUNO\n" +
                "4 - EXCLUIR TODOS OS ALUNOS\n" +
                "5 - CONSULTAR UM ALUNO\n" +
                "6 - LISTAR TODOS OS ALUNOS\n" +
                "7 - LISTAR OS ALUNOS APROVADOS\n" +
                "8 - LISTAR OS ALUNOS REPROVADOS\n" +
                "9 - EXIBIR DADOS ESTATÍSTICOS\n" +
                "0 - SAIR\n" +
                "Selecione:\0";
        char[] output = input.toCharArray();
        for(int i = 0; i < output.length; i++){
            System.out.print("'");
            if(output[i] == '\n'){
                System.out.print("\\");
                System.out.print("n");
            } else if(output[i] == ' ') {
                System.out.print(" ");
            }else if(output[i] == '\0') {
                System.out.print(3);
            } else {
                System.out.printf("%c", output[i]);
            }
            System.out.print("',");
        }
    }
}
