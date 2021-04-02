package dev.verzano.monospaced.gui.terminal;

import static dev.verzano.monospaced.core.ansi.sgr.SgrFormat.normalSgrFormat;

public class TixelUtils {
    public static Tixel[][] toNoStyleGrid(String input) {
        var longestRow = 0;
        var rows = input.split("\n");
        for (var row : rows) {
            longestRow = Math.max(row.length(), longestRow);
        }

        var grid = new Tixel[rows.length][longestRow];
        for (var r = 0; r < rows.length; r++) {
            var c = 0;
            for (; c < rows[r].length(); c++) {
                grid[r][c] = new Tixel(rows[r].charAt(c));
            }

            for (; c < longestRow; c++) {
                grid[r][c] = new Tixel(' ');
            }
        }

        return grid;
    }

    public static String asString(Tixel[][] tixels) {
        StringBuilder sb = new StringBuilder();
        for (var row : tixels) {
            for (var t : row) {
                sb.append(t == null ? normalSgrFormat() + " " + normalSgrFormat() : t.asString());
            }
            sb.append('\n');
        }

        return sb.toString();
    }

    public static boolean notSame(Tixel[][] a, Tixel[][] b) {
        if (b.length != a.length || b[0].length != a[0].length) {
            return true;
        }

        for (var r = 0; r < a.length; r++) {
            for (var c = 0; c < a[r].length; c++) {
                var at = a[r][c];
                var bt = b[r][c];

                if (at == null && bt != null
                        || at != null && bt == null
                        || at != null && !bt.equals(at)) {
                    return true;
                }
            }
        }

        return false;
    }
}
