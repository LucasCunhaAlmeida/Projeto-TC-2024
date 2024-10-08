import Equipe1.FuncionalidadeEquipe1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class InterfaceGrafica {

    JFrame frame;

    public InterfaceGrafica() {
        janela();
        botaoUniao();
        botaoInterseccao();
        botaoConcatenacao();
        botaoComplemento();
        botaoEstrela();
        botaoAfnParaAfd();
        botaoAfdMinimizado();
        textoProjeto();
    }

    public void janela() {
        frame = new JFrame("Fila de Banco");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Obtém o tamanho da tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // Define o tamanho da janela para 100% do tamanho da tela
        frame.setSize(width, height);

        // Centraliza a janela na tela
        frame.setLocationRelativeTo(null);

        // Cria um painel com degradê
        DegradePanel painelDegrade = new DegradePanel();
        painelDegrade.setLayout(null); // Usar layout nulo para adicionar componentes manualmente
        painelDegrade.setBounds(0, 0, width, height); // Define o tamanho do painel

        // Define o painel com degradê como o conteúdo da janela
        frame.setContentPane(painelDegrade);

        // Exibe a janela
        frame.setVisible(true);
    }

    private class DegradePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            desenharRetanguloComSombra(g, 50, 50, 200, 100, 20);
        }

        private void desenharRetanguloComSombra(Graphics g, int x, int y, int largura, int altura, int arredondamento) {
            Graphics2D g2d = (Graphics2D) g;

            // Criar a sombra
            g2d.setColor(new Color(0, 0, 0, 50)); // Cor preta com 50 de opacidade
            RoundRectangle2D sombra = new RoundRectangle2D.Double(x + 5, y + 5, largura, altura, arredondamento, arredondamento);
            g2d.fill(sombra);

            // Criar o retângulo com bordas arredondadas
            g2d.setColor(Color.WHITE); // Cor do retângulo
            RoundRectangle2D retangulo = new RoundRectangle2D.Double(x, y, largura, altura, arredondamento, arredondamento);
            g2d.fill(retangulo);

            // Desenhar a borda do retângulo
            g2d.setColor(Color.GRAY); // Cor da borda
            g2d.draw(retangulo);
        }
    }

    // Botões da interface gráfica
    public void botaoUniao() {
        JButton botao = new JButton("UNIÃO");
        botao.setBounds(20, 100, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);

        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ação do botão UNIÃO
            }
        });
    }

    public void botaoInterseccao() {
        JButton botao = new JButton("INTERSECÇÃO");
        botao.setBounds(20, 175, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    public void botaoConcatenacao() {
        JButton botao = new JButton("CONCATENAÇÃO");
        botao.setBounds(20, 250, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    public void botaoComplemento() {
        JButton botao = new JButton("COMPLEMENTO");
        botao.setBounds(20, 325, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    public void botaoEstrela() {
        JButton botao = new JButton("ESTRELA");
        botao.setBounds(20, 400, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    public void botaoAfnParaAfd() {
        JButton botao = new JButton("AFN PARA AFD");
        botao.setBounds(20, 475, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);

        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pedindo para escolher o arquivo a ser lido.
                File file = pedirEnderecoArquivo();
                // Aqui você pode adicionar o código para processar o arquivo selecionado
                Controlador control = new Controlador(file); // Executa toda conversão
            }
        });
    }

    public void botaoAfdMinimizado() {
        JButton botao = new JButton("AFD MINIMIZADO");
        botao.setBounds(20, 550, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);

        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FuncionalidadeEquipe1 equipe1 = new FuncionalidadeEquipe1();
                equipe1.executar();
            }
        });
    }

    // JLabels "Caixa de texto"
    public void textoProjeto() {
        JLabel texto = new JLabel("Calculadora de Automatos", JLabel.CENTER);

        // Calcula a posição horizontal para centralizar o texto
        int frameWidth = (int) (frame.getWidth() * 1.2);
        int labelWidth = 400; // Largura do JLabel
        int x = (frameWidth - labelWidth) / 2;
        int y = 40; // Posição vertical fixa

        texto.setBounds(x, y, 400, 50);
        texto.setFont(new Font("Arial", Font.BOLD, 30));
        texto.setForeground(new Color(0, 0, 0));
        frame.add(texto);
    }

    public File pedirEnderecoArquivo() {
        JFileChooser arquivoEscolhido = new JFileChooser();
        int enviou = arquivoEscolhido.showOpenDialog(frame);
        if (enviou == JFileChooser.APPROVE_OPTION) {
            File file = arquivoEscolhido.getSelectedFile();
            System.out.println("Arquivo: " + file);
            return file;
        }
        return null;
    }
}