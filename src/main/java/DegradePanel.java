import javax.swing.*;
import java.awt.*;

public class DegradePanel extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Obtém o objeto Graphics2D para desenhar o gradiente
        Graphics2D g2d = (Graphics2D) g;

        //#004aad
        //#cb6ce6

        //#fff7ad
        //#ffa9f9
        // Define as cores do degradê
        Color corInicio = Color.decode("#cdffd8"); // Cor inicial
        Color corFim = Color.decode("#94b9ff");   // Cor final

        // Cria um gradiente vertical
        GradientPaint gradiente = new GradientPaint(
                0, 0, corInicio, // Ponto inicial e cor inicial
                getWidth(), getHeight(), // Ponto final
                corFim // Cor final
        );

        // Aplica o gradiente ao Graphics2D
        g2d.setPaint(gradiente);

        // Preenche o painel com o gradiente
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
