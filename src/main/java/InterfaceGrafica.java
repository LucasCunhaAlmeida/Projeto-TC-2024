import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class InterfaceGrafica {

    JFrame frame;

    /**
     * Construtor chamando todos os métodos necessários para
     * gerar os componentes da interface gráfica.
     */
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

        // Define o layout nulo para permitir o posicionamento manual dos componentes
        frame.setLayout(null);

        // Cria um painel com degradê
        DegradePanel painelDegrade = new DegradePanel();
        painelDegrade.setLayout(null); // Usar layout nulo para adicionar componentes manualmente
        painelDegrade.setBounds(0, 0, width, height); // Define o tamanho do painel

        // Adiciona o painel com degradê à janela
        frame.setContentPane(painelDegrade);

        // Exibe a janela
        frame.setVisible(true);
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
                /*
                // Emitindo um aviso na tela
                JOptionPane.showMessageDialog(null,
                        "PARA REALIZAR ESSA OPERAÇÃO, VOCÊ PRECISA DE DOIS ARQUIVOS .JFF \n",
                        "Aviso",
                        JOptionPane.INFORMATION_MESSAGE);
                // Pedindo para escolher o primeiro arquivo a ser lido.
                File file = pedirEnderecoArquivo();
                LeitorXML leitorXML = new LeitorXML();
                leitorXML.lerArquivo(file.getAbsolutePath());

                JOptionPane.showMessageDialog(null,
                        "SELECIONE O SEGUNDO ARQUIVO!",
                        "Aviso",
                        JOptionPane.INFORMATION_MESSAGE);

                file = pedirEnderecoArquivo();
                leitorXML.lerArquivo(file.getAbsolutePath());
                */
            }
        });
    }


    public void botaoInterseccao(){
        JButton botao = new JButton("INTERSECÇÃO");
        botao.setBounds(20, 175, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    public void botaoConcatenacao(){
        JButton botao = new JButton("CONCATENAÇÃO");
        botao.setBounds(20, 250, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    public void botaoComplemento(){
        JButton botao = new JButton("COMPLEMENTO");
        botao.setBounds(20, 325, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    public void botaoEstrela(){
        JButton botao = new JButton("ESTRELA");
        botao.setBounds(20, 400, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    public void botaoAfnParaAfd(){
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
                LeitorXML leitorXML = new LeitorXML();
                leitorXML.lerArquivo(file.getAbsolutePath());
            }
        });
    }

    public void botaoAfdMinimizado(){
        JButton botao = new JButton("AFD MINIMIZADO");
        botao.setBounds(20, 550, 350, 50);
        botao.setFont(new Font("Arial", Font.BOLD, 25));
        botao.setForeground(new Color(255, 255, 255));
        botao.setBackground(new Color(0x484142));
        frame.add(botao);
    }

    // Jlabels "Caixa de texto"
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

    public File pedirEnderecoArquivo(){
        JFileChooser arquivoEscolhido = new JFileChooser();
        int enviou = arquivoEscolhido.showOpenDialog(frame);
        if (enviou == JFileChooser.APPROVE_OPTION) {
            File file = arquivoEscolhido.getSelectedFile();
            // Aqui você pode adicionar o código para processar o arquivo selecionado
            System.out.println("Arquivo selecionado1: " + file.getAbsolutePath());
            return file;
        }

        return null;
    }
}
