package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

/// Uma aplicação desktop (usando a biblioteca [OpenJFX (JavaFX)](http://openjfx.io))
/// que desenha polígonos na tela e calcula o perímetro de cada um:
/// a soma da distância entre todos os seus pontos.
public class PoligonosApp extends Application {
    /**
     * Lista onde cada elemento representa os pontos que formam um polígono.
     * Cada elemento é então uma lista de pontos com coordenadas x,y.
     * Assim, cada polígono é formado por uma lista de pontos.
     */
    private final List<List<Point>> pontosPoligonos = List.of(
        // Quadrilátero (Quadrado)
        List.of(
            new Point( 50,  50),
            new Point(150,  50),
            new Point(150, 150),
            new Point( 50, 150)
        ),

        // Quadrilátero (Retângulo)
        List.of(
            new Point(200,  50),
            new Point(400,  50),
            new Point(400, 100),
            new Point(200, 100)
        ),

        // Triângulo
        List.of(
            new Point(300, 250),
            new Point(350, 150),
            new Point(400, 250)
        ),

        // Pentágono
        List.of(
            new Point(200, 250),
            new Point(250, 300),
            new Point(250, 350),
            new Point(150, 350),
            new Point(150, 300)
        ),

        // Hexágono
        List.of(
                new Point(320, 270),
                new Point(370, 320),
                new Point(370, 370),
                new Point(320, 420),
                new Point(270, 370),
                new Point(270, 320)
        )
    );

    /**
     * Executa a aplicação
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicia a apresentação da interface gráfica da aplicação.
     * @param mainStage janela inicial da aplicação
     */
    @Override
    public void start(final Stage mainStage) {
        final var root = new Pane();
        final var scene = new Scene(root, 800, 600);

        for (final var listaPontos : pontosPoligonos) {
            final var poligono = new Polygon();
            for (final Point point : listaPontos) {
                poligono.getPoints().addAll(point.x(), point.y());
            }

            poligono.setFill(Color.BLUE);
            poligono.setStroke(Color.BLACK);
            root.getChildren().add(poligono);
        }

        final List<String> perimetros = perimetros().stream().map(p -> String.format("%.1f", p)).toList();
        final var label1 = newLabel("Perímetro dos Polígonos: " + perimetros, 500);
        final var label2 = newLabel("Tipo dos Polígonos: " + tipoPoligonos(), 530);
        root.getChildren().addAll(label1, label2);

        mainStage.setTitle("Polígonos");
        mainStage.setScene(scene);
        mainStage.setAlwaysOnTop(true);
        mainStage.show();
    }

    private static Label newLabel(final String title, final int y) {
        final var label = new Label(title);
        label.setLayoutX(10);
        label.setLayoutY(y);
        return label;
    }

    protected List<String> tipoPoligonos() {
        return pontosPoligonos.stream()
                .flatMap(poligono -> Stream.of(poligono.size()))
                .map(qtd -> switch (qtd) {
                    case 3 -> "Triângulo";
                    case 4 -> "Quadrilátero";
                    case 5 -> "Pentágono";
                    case 6 -> "Hexágono";
                    default -> "Polígono";
                })
                .toList();
    }

    protected List<Double> perimetros() {
        return pontosPoligonos.stream()
                .flatMap(poligono -> {
                    Point primeiro = poligono.get(0);
                    Point ultimo = poligono.get(poligono.size() - 1);

                    Point resultado = poligono.stream()
                            .reduce(
                                    new Point(ultimo, primeiro),
                                    (anterior, atual) -> new Point(anterior, atual)
                            );

                    return Stream.of(resultado.distance());
                })
                .toList();
    }
}