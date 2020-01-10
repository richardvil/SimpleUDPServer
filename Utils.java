class Utils {

    static String convertByteArrayToHexString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    static String[] convertByteArrayToHexStringArray(byte[] arr) {
        return convertByteArrayToHexString(arr).split("\\s+");
    }

    static byte[] convertHexStringToByteArray(String s) {
        s = s.replaceAll("\\s","");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    static String convertByteArrayToUnsignedString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            int n = b & 0xFF;
            sb.append(n).append(" ");
        }

        return sb.toString();
    }
}
