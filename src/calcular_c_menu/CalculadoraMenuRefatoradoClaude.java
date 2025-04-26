
package calcular_c_menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CalculadoraMenuRefatoradoClaude {

    // Constantes para faixas de INSS
    private static final double FAIXA1_INSS = 1302.00;
    private static final double FAIXA2_INSS = 2571.00;
    private static final double FAIXA3_INSS = 3856.00;
    private static final double ALIQUOTA1_INSS = 0.08;
    private static final double ALIQUOTA2_INSS = 0.09;
    private static final double ALIQUOTA3_INSS = 0.11;
    private static final double ALIQUOTA4_INSS = 0.14;
    private static final double TETO_INSS = 570.88;
    
    // Constantes para faixas de IR
    private static final double FAIXA1_IR = 1903.98;
    private static final double FAIXA2_IR = 2826.65;
    private static final double FAIXA3_IR = 3751.05;
    private static final double FAIXA4_IR = 4664.68;
    private static final double ALIQUOTA1_IR = 0.075;
    private static final double ALIQUOTA2_IR = 0.15;
    private static final double ALIQUOTA3_IR = 0.225;
    private static final double ALIQUOTA4_IR = 0.275;
    private static final double DEDUCAO1_IR = 142.80;
    private static final double DEDUCAO2_IR = 354.80;
    private static final double DEDUCAO3_IR = 636.13;
    private static final double DEDUCAO4_IR = 869.36;
    
    // Constantes para valores fixos
    private static final double VALOR_DEPENDENTE = 189.59;
    private static final double PERCENTUAL_VT = 0.06;
    private static final double VALOR_VA = 200.0;
    private static final double VALOR_VR = 250.0;
    private static final double VALOR_PLANO_BASICO = 150.0;
    private static final double VALOR_PLANO_AVANCADO = 300.0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            exibirMenu();
            try {
                int opcao = lerInteiro(sc, "Escolha uma opção: ");
                
                switch (opcao) {
                    case 1:
                        calcularSalarioLiquido(sc);
                        break;
                    case 2:
                        System.out.println("Encerrando o programa. Até mais!");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número inteiro.");
                sc.nextLine(); // Limpa o buffer
            }
        }

        sc.close();
    }
    
    private static void exibirMenu() {
        System.out.println("\n========== MENU ==========");
        System.out.println("1. Calcular salário líquido");
        System.out.println("2. Sair");
    }
    
    private static int lerInteiro(Scanner sc, String mensagem) {
        System.out.print(mensagem);
        int valor = sc.nextInt();
        sc.nextLine(); // Limpa o buffer
        return valor;
    }
    
    private static double lerDouble(Scanner sc, String mensagem) {
        System.out.print(mensagem);
        double valor = sc.nextDouble();
        sc.nextLine(); // Limpa o buffer
        return valor;
    }
    
    private static String lerOpcao(Scanner sc, String mensagem, String... opcoesValidas) {
        System.out.print(mensagem);
        String opcao = sc.nextLine().toUpperCase().trim();
        
        boolean opcaoValida = false;
        for (String op : opcoesValidas) {
            if (opcao.equals(op)) {
                opcaoValida = true;
                break;
            }
        }
        
        while (!opcaoValida) {
            System.out.print("Opção inválida! Tente novamente: ");
            opcao = sc.nextLine().toUpperCase().trim();
            
            for (String op : opcoesValidas) {
                if (opcao.equals(op)) {
                    opcaoValida = true;
                    break;
                }
            }
        }
        
        return opcao;
    }

    // Método separado para os cálculos
    public static void calcularSalarioLiquido(Scanner sc) {
        try {
            double salarioBruto = lerDouble(sc, "\nInforme o salário bruto: ");
            int dependentes = lerInteiro(sc, "Número de dependentes (ou 0 se não tiver): ");
            
            String planoSaude = lerOpcao(sc, "Plano de Saúde (B = Básico | A = Avançado | N = Nenhum): ", "B", "A", "N");
            String vTransporte = lerOpcao(sc, "Usa Vale Transporte? (S/N): ", "S", "N");
            String vAlimentacao = lerOpcao(sc, "Usa Vale Alimentação? (S/N): ", "S", "N");
            String vRefeicao = lerOpcao(sc, "Usa Vale Refeição? (S/N): ", "S", "N");
            
            double descontoINSS = calcularINSS(salarioBruto);
            double baseIR = calcularBaseIR(salarioBruto, dependentes);
            double descontoIR = calcularIR(baseIR);
            double descontoVT = calcularValeTransporte(salarioBruto, vTransporte);
            double descontoVA = calcularValeAlimentacao(vAlimentacao);
            double descontoVR = calcularValeRefeicao(vRefeicao);
            double descontoPlano = calcularPlanoSaude(planoSaude);
            
            double totalDescontos = descontoINSS + descontoIR + descontoVT + descontoVA + descontoVR + descontoPlano;
            double percentualDesconto = (totalDescontos / salarioBruto) * 100;
            double salarioLiquido = salarioBruto - totalDescontos;
            
            exibirResultado(salarioBruto, descontoINSS, baseIR, descontoIR, descontoPlano, 
                          descontoVT, descontoVA, descontoVR, totalDescontos, percentualDesconto, salarioLiquido);
            
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Por favor, insira um valor numérico válido.");
            sc.nextLine(); // Limpa o buffer
        }
    }
    
    private static double calcularINSS(double salarioBruto) {
        double descontoINSS;
        
        if (salarioBruto <= FAIXA1_INSS) {
            descontoINSS = salarioBruto * ALIQUOTA1_INSS;
        } else if (salarioBruto <= FAIXA2_INSS) {
            descontoINSS = salarioBruto * ALIQUOTA2_INSS;
        } else if (salarioBruto <= FAIXA3_INSS) {
            descontoINSS = salarioBruto * ALIQUOTA3_INSS;
        } else {
            descontoINSS = salarioBruto * ALIQUOTA4_INSS;
            if (descontoINSS > TETO_INSS) {
                descontoINSS = TETO_INSS;
            }
        }
        
        return descontoINSS;
    }
    
    private static double calcularBaseIR(double salarioBruto, int dependentes) {
        return salarioBruto - (dependentes * VALOR_DEPENDENTE);
    }
    
    private static double calcularIR(double baseIR) {
        if (baseIR <= FAIXA1_IR) {
            return 0;
        } else if (baseIR <= FAIXA2_IR) {
            return (baseIR * ALIQUOTA1_IR) - DEDUCAO1_IR;
        } else if (baseIR <= FAIXA3_IR) {
            return (baseIR * ALIQUOTA2_IR) - DEDUCAO2_IR;
        } else if (baseIR <= FAIXA4_IR) {
            return (baseIR * ALIQUOTA3_IR) - DEDUCAO3_IR;
        } else {
            return (baseIR * ALIQUOTA4_IR) - DEDUCAO4_IR;
        }
    }
    
    private static double calcularValeTransporte(double salarioBruto, String vTransporte) {
        return vTransporte.equals("S") ? salarioBruto * PERCENTUAL_VT : 0;
    }
    
    private static double calcularValeAlimentacao(String vAlimentacao) {
        return vAlimentacao.equals("S") ? VALOR_VA : 0;
    }
    
    private static double calcularValeRefeicao(String vRefeicao) {
        return vRefeicao.equals("S") ? VALOR_VR : 0;
    }
    
    private static double calcularPlanoSaude(String planoSaude) {
        if (planoSaude.equals("B")) {
            return VALOR_PLANO_BASICO;
        } else if (planoSaude.equals("A")) {
            return VALOR_PLANO_AVANCADO;
        }
        return 0;
    }
    
    private static void exibirResultado(double salarioBruto, double descontoINSS, double baseIR, 
                                      double descontoIR, double descontoPlano, double descontoVT, 
                                      double descontoVA, double descontoVR, double totalDescontos, 
                                      double percentualDesconto, double salarioLiquido) {
        
        System.out.println("\n===============================================");
        System.out.printf(" Desconto do INSS => R$ %.2f\n", descontoINSS);
        System.out.printf(" Base para IR => R$ %.2f\n", baseIR);
        System.out.printf(" Desconto do IR => R$ %.2f\n", descontoIR);
        System.out.printf(" Plano de Saúde => R$ %.2f\n", descontoPlano);
        System.out.printf(" Vale Transporte => R$ %.2f\n", descontoVT);
        System.out.printf(" Vale Alimentação => R$ %.2f\n", descontoVA);
        System.out.printf(" Vale Refeição => R$ %.2f\n", descontoVR);
        System.out.println("===============================================");
        System.out.println("                 RESUMO FINAL");
        System.out.printf(" Salário bruto => R$ %.2f\n", salarioBruto);
        System.out.printf(" Total de descontos => R$ %.2f\n", totalDescontos);
        System.out.printf(" Percentual de desconto => %.2f%%\n", percentualDesconto);
        System.out.printf(" Salário líquido => R$ %.2f\n", salarioLiquido);
    }
}
