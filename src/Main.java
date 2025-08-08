import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String apiKey = "b29aad9ad312050002f3db30";
        String textoMenu = """
                *******************************************************************************
                [1]- USD (DÓLAR AMERICANO)               [5]- GBP (REINO UNIDO)
                [2]- EUR (EURO)                          [6]- JPY (JAPÃO)
                [3]- BRL (REAL)                          [7]- CHF (SUÍÇA)
                [4]- CNY (YUAN CHINÊS)                   [8]- CAD (CANADÁ)
                *******************************************************************************
                Escolha a moeda:
                """;
        boolean continuar = true;

        while (continuar) {
            try {
                System.out.println(textoMenu);
                int escolhaBase = scanner.nextInt();
                String moedaBase = MoedaUtil.getMoedaPorNumero(escolhaBase);

                String endereco = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + moedaBase;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endereco))
                        .header("apikey", apiKey)
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

                System.out.println("Digite a segunda moeda: ");
                int escolhaDestino = scanner.nextInt();
                String moedaDestino = MoedaUtil.getMoedaPorNumero(escolhaDestino);

                System.out.println("Valor: ");
                double valorEmMoedaBase = scanner.nextDouble();

                double taxaDestino = jsonObject
                        .getAsJsonObject("conversion_rates")
                        .get(moedaDestino)
                        .getAsDouble();

                double valorConvertido = valorEmMoedaBase * taxaDestino;

                System.out.printf("%s %.2f = %s %.2f%n",
                        moedaBase.toUpperCase(), valorEmMoedaBase, moedaDestino, valorConvertido);

                System.out.println("Deseja fazer outra conversão? (S/N)");
                String resposta = scanner.next();
                if (!resposta.equalsIgnoreCase("S")) {
                    continuar = false;
                }

            } catch (InputMismatchException e) {
                System.out.println("Erro: você digitou um valor inválido. Tente novamente.");
                scanner.nextLine();
            } catch (IOException | InterruptedException e) {
                System.out.println("Erro ao acessar a API: " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("Erro: moeda não encontrada.");
            }
        }
        System.out.println("Programa encerrado.");
    }
}