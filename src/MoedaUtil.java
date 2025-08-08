public class MoedaUtil {
    public static String getMoedaPorNumero(int numero) {
        return switch (numero) {
            case 1 -> "USD";
            case 2 -> "EUR";
            case 3 -> "BRL";
            case 4 -> "CNY";
            case 5 -> "GBP";
            case 6 -> "JPY";
            case 7 -> "CHF";
            case 8 -> "CAD";
            default -> null;
        };
    }
}
