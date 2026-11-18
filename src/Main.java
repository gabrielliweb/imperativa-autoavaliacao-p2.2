import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static final int TAM_CONTAS = 50;
    public static final int TAM_OP = 1000;

    public static class Conta {
        int id;
        String cliente;
        double saldo;
        double limite;
    }

    public static class Operacao {
        int id;
        int idConta;
        char tipo; // {'D', 'S', 'T'}
        double valor;
    }

    public static int buscaId(Conta[] v, int tam, int x) {
        for (int i = 0; i < tam; i += 1) {
            if (v[i].id == x) {
                return i;
            }
        }
        return -1;
    }

    // Q1
    public static int cadastrarConta(Conta[] v, int tam) {
        if (tam >= TAM_CONTAS) {
            return tam;
        }
        int idEntrada = sc.nextInt();
        if (buscaId(v, tam, idEntrada) != -1) {
            return tam;
        }
        sc.nextLine();
        String cliente = sc.nextLine();
        double saldo = sc.nextDouble();
        double limite = sc.nextDouble();
        if (saldo < 0 || limite < 0) {
            return tam;
        }
        v[tam] = new Conta();
        v[tam].id = idEntrada;
        v[tam].cliente = cliente;
        v[tam].saldo = saldo;
        v[tam].limite = limite;
        return tam + 1;
    }

    // Q2
    public static int buscaBinariaCliente(Conta[] v, int tam, String x) {
        int esquerda = 0, direita = tam - 1, meio;
        while (esquerda <= direita) {
            meio = (esquerda + direita) / 2;
            if (v[meio].cliente.equals(x)) {
                return meio;
            } else if (v[meio].cliente.compareTo(x) < 0) {
                esquerda = meio + 1;
            } else {
                direita = meio - 1;
            }
        }
        return -1;
    }

    // Q3
    public static int insertionSortCliente(Conta[] v, int tam) {
        for (int i = 1; i <= tam - 1; i += 1) {
            Conta chave = v[i];
            int j = i - 1;
            while (j >= 0 && v[j].cliente.compareTo(chave.cliente) > 0) {
                v[j + 1] = v[j];
                j -= 1;
            }
            v[j + 1] = chave;
        }
        return tam;
    }

    // Q4
    public static int filtraOperacoes(Operacao[] vOp, int tamOp, Operacao[] vOpFiltrado, int x) {
        int tamFiltrado = 0;
        for (int i = 0; i <= tamOp - 1; i += 1) {
            if (vOp[i].idConta == x) {
                vOpFiltrado[tamFiltrado] = vOp[i];
                tamFiltrado += 1;
            }
        }
        return tamFiltrado;
    }

    // Q5
    public static String letraCD(double valor) {
        if (valor >= 0) {
            return "C";
        }
        return "D";
    }

    public static String nomeOperacao(char tipo) {
        if (tipo == 'D') {
            return "Depósito";
        }
        if (tipo == 'S') {
            return "Saque";
        }
        return "Transferência";
    }

    public static void extrato(Conta[] vContas, int tamConta, Operacao[] vOp, int tamOp) {
        int contaIdEntrada = sc.nextInt();
        int posicao = buscaId(vContas, tamConta, contaIdEntrada);
        if (posicao == -1) {
            System.out.println("O id dessa conta não existe");
            return;
        }
        Operacao[] vOpFiltrado = new Operacao[TAM_OP];
        int tamFiltrado = filtraOperacoes(vOp, tamOp, vOpFiltrado, contaIdEntrada);

        int inicio = tamFiltrado - 5;
        if (inicio < 0) {
            inicio = 0;
        }

        double saldo = 0;
        for (int i = 0; i < inicio; i += 1) {
            saldo += vOpFiltrado[i].valor;
        }

        System.out.printf("CONTA N.%03d%n", vContas[posicao].id);
        System.out.println("CLIENTE: " + vContas[posicao].cliente);
        System.out.printf("Saldo Anterior: %.2f%s%n", Math.abs(saldo), letraCD(saldo));

        for (int i = inicio; i < tamFiltrado; i += 1) {
            saldo += vOpFiltrado[i].valor;
            System.out.printf("%04d | %s | %.2f%s | %.2f%s%n",
                    vOpFiltrado[i].id,
                    nomeOperacao(vOpFiltrado[i].tipo),
                    Math.abs(vOpFiltrado[i].valor), letraCD(vOpFiltrado[i].valor),
                    Math.abs(saldo), letraCD(saldo));
        }

        System.out.printf("Saldo Atual: %.2f%s%n", Math.abs(saldo), letraCD(saldo));
    }

    public static Operacao novaOp(int id, int idConta, char tipo, double valor) {
        Operacao op = new Operacao();
        op.id = id;
        op.idConta = idConta;
        op.tipo = tipo;
        op.valor = valor;
        return op;
    }

    public static void main(String[] args) {
        Conta[] contas = new Conta[TAM_CONTAS];
        Operacao[] ops = new Operacao[TAM_OP];
        int tamContas = 0;
        int tamOps = 0;

        System.out.println("Cadastre 3 contas (id, cliente, saldo, limite):");
        for (int k = 0; k < 3; k += 1) {
            tamContas = cadastrarConta(contas, tamContas);
        }

        insertionSortCliente(contas, tamContas);
        System.out.println("\nContas ordenadas por cliente:");
        for (int i = 0; i < tamContas; i += 1) {
            System.out.println(contas[i].id + " - " + contas[i].cliente);
        }

        System.out.println("\nNome do cliente para buscar:");
        sc.nextLine();
        String procurado = sc.nextLine();
        System.out.println("Posição: " + buscaBinariaCliente(contas, tamContas, procurado));

        double[] valores = {1000, -200, 300, -50, 400, -100, 250};
        char[] tipos = {'D', 'S', 'D', 'S', 'D', 'S', 'D'};
        for (int i = 0; i < valores.length; i += 1) {
            ops[tamOps] = novaOp(i + 1, 1, tipos[i], valores[i]);
            tamOps += 1;
        }

        System.out.println("\nId da conta para o extrato (as operações de teste são da conta 1):");
        extrato(contas, tamContas, ops, tamOps);
    }
}