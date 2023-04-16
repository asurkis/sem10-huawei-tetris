package ru.asurkis.tetris;

public enum Tetramino {
    O {
        @Override
        public int[][] coords(int quarters) {
            return rotateCoords(new int[][] {{0, 0}, {1, 0}, {1, 1}, {0, 1}}, quarters);
        }
    },
    I {
        @Override
        public int[][] coords(int quarters) {
            return rotateCoords(new int[][] {{-1, 0}, {0, 0}, {1, 0}, {2, 0}}, quarters);
        }
    },
    S {
        @Override
        public int[][] coords(int quarters) {
            return rotateCoords(new int[][] {{0, 0}, {1, 0}, {}}, quarters);
        }
    },
    Z {
        @Override
        public int[][] coords(int quarters) {
            return new int[0][];
        }
    },
    T {
        @Override
        public int[][] coords(int quarters) {
            return new int[0][];
        }
    },
    L {
        @Override
        public int[][] coords(int quarters) {
            return new int[0][];
        }
    },
    J {
        @Override
        public int[][] coords(int quarters) {
            return new int[0][];
        }
    };

    private static int[][] rotateCoords(int[][] coords, int quarters) {
        int[][] result = new int[4][2];
        for (int i = 0; i < 4; i++) {
            int x = 2 * coords[i][0] - 1;
            int y = 2 * coords[i][1] - 1;
            result[i][0] = (1 - y) / 2;
            result[i][1] = (1 + x) / 2;
        }
        return result;
    }

    public abstract int[][] coords(int quarters);
}
