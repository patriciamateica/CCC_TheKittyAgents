import java.util.List;

public class Level1 implements Level {

    // here solve problems! lol

    @Override
    public String solve(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        int n = Integer.parseInt(lines.get(0).trim());

        for(int i = 1; i <= n; i++){
            String[] parts = lines.get(i).trim().split("\\s+");
            int radius = Integer.parseInt(parts[0]);

            int columns = 2 + radius * 2 + radius * 2 - 1;
            int rows = (int)Math.ceil((double)columns / 2);

            System.out.println(rows);
            System.out.println(columns);

            char[][] board = new char[rows][columns];

            int nrLines = 0;
            int nrZeroes = radius * 2 - 1;
            int nrCurrentLines = 0;
            boolean lastStar = false;

            for (int r = (int)Math.ceil((double) rows / 2) - 1; r >= 0; r--) {
                nrCurrentLines = 0;
//                int c = 0;
//                while (c < columns) {
//                    while (nrCurrentLines < nrLines) {
//                        populate(board, rows, columns, r, c, '-');
//                        nrCurrentLines++;
//                        c++;
//                    }
//
//                    populate(board, rows, columns, r, c, '*');
//                    c++;
//
//                    while (c <= (int)Math.ceil((double) columns / 2)) {
//                        if (board[r][c - 1] != '-') {
//                            populate(board, rows, columns, r, c, '-');
//                        } else if (r != 0) {
//                            populate(board, rows, columns, r, c, 'O');
//                        } else {
//                            populate(board, rows, columns, r, c, '*');
//                        }
//
//                        c++;
//                    }
//                }

//                nrCurrentLines = -1;
                lastStar = false;
                for (int c = 0; c <= (int)Math.ceil((double) columns / 2); c++) {
                    if (nrCurrentLines < nrLines) {
                        board[r][c] = '-';
                        board[r][columns - c - 1] = '-';
                        board[rows - r - 1][c] = '-';
                        board[rows - r - 1][columns - c - 1] = '-';

                        nrCurrentLines++;
                    } else if (!lastStar) {
                        board[r][c] = '*';
                        board[r][columns - c - 1] = '*';
                        board[rows - r - 1][c] = '*';
                        board[rows - r - 1][columns - c - 1] = '*';

                        lastStar = true;
                    } else if (r == 0) {
                        if (board[r][c - 1] != '-') {
                            board[r][c] = '-';
                            board[r][columns - c - 1] = '-';
                            board[rows - r - 1][c] = '-';
                            board[rows - r - 1][columns - c - 1] = '-';
                        } else {
                            board[r][c] = '*';
                            board[r][columns - c - 1] = '*';
                            board[rows - r - 1][c] = '*';
                            board[rows - r - 1][columns - c - 1] = '*';
                        }
                    } else {
                        if (board[r][c - 1] != '-') {
                            board[r][c] = '-';
                            board[r][columns - c - 1] = '-';
                            board[rows - r - 1][c] = '-';
                            board[rows - r - 1][columns - c - 1] = '-';
                        } else {
                            board[r][c] = 'O';
                            board[r][columns - c - 1] = 'O';
                            board[rows - r - 1][c] = 'O';
                            board[rows - r - 1][columns - c - 1] = 'O';
                        }
                    }
                }
                nrLines++;
            }


            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    sb.append(board[r][c]);
                }
                sb.append('\n');
            }

            sb.append('\n');
        }
        return sb.toString();
    }

    static void populate(char[][] board, int rows, int columns, int r, int c, char symbol) {
        board[r][c] = symbol;
                        board[r][columns - c - 1] = symbol;
                        board[rows - r - 1][c] = symbol;
                        board[rows - r - 1][columns - c - 1] = symbol;
    }

}
