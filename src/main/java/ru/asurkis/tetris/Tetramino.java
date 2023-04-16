package ru.asurkis.tetris;

public enum Tetramino {
    O {
        @Override
        public int[][] coords(int quarters) {
            return new int[][] {{0, 0}, {1, 0}, {1, 1}, {0, 1}};
        }
    },
    I {
        @Override
        public int[][] coords(int quarters) {
            return new int[][][] {
                    {{-1, 0}, {0, 0}, {1, 0}, {2, 0}},
                    {{1, -1}, {1, 0}, {1, 1}, {1, 2}},
                    {{-1, 1}, {0, 1}, {1, 1}, {2, 1}},
                    {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
            }[quarters];
        }
    },
    S {
        @Override
        public int[][] coords(int quarters) {
            return rotateCoords(new int[][] {{-1, 0}, {0, 0}, {0, 1}, {1, 1}}, quarters);
        }
    },
    Z {
        @Override
        public int[][] coords(int quarters) {
            return rotateCoords(new int[][] {{1, 0}, {0, 0}, {0, 1}, {-1, 1}}, quarters);
        }
    },
    T {
        @Override
        public int[][] coords(int quarters) {
            return rotateCoords(new int[][] {{0, 0}, {1, 0}, {0, 1}, {-1, 0}}, quarters);
        }
    },
    L {
        @Override
        public int[][] coords(int quarters) {
            return rotateCoords(new int[][] {{-1, 0}, {0, 0}, {1, 0}, {1, 1}}, quarters);
        }
    },
    J {
        @Override
        public int[][] coords(int quarters) {
            return rotateCoords(new int[][] {{-1, 1}, {-1, 0}, {0, 0}, {1, 0}}, quarters);
        }
    };

    private static int[][] rotateCoords(int[][] coords, int quarters) {
        for (int q = 0; q < quarters; q++) {
            for (int i = 0; i < 4; i++) {
                int x = coords[i][0];
                int y = coords[i][1];
                coords[i][0] = -y;
                coords[i][1] = x;
            }
        }
        return coords;
    }

    public abstract int[][] coords(int quarters);
}
