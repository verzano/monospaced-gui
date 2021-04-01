package dev.verzano.monospaced.gui.terminal;

public class TixelGridMaker {
    public static Tixel[][] makeNoStyleTixelGrid(String input) {
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
}
