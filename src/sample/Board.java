package sample;

import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 *  @author Stas.Maslov
 *  @author Ilya.Tyugashev
 *  @version 1.2.1
 *
 *  \brief Данный класс осуществляет преобразование массива в board, для дальнейшего использования
 *  \details Класс Board.java создаёт board, и находит соседей исходящего board
*/
public class Board {
    public int[][] board;
    private int dimension;
    private int manhattanNumber = -1;
    private int hammingNumber = -1;

    /**
     * @param array входящий массив
     *
     * \brief Данный конструктор создаёт board из двумерного массива. Также в конструкторе используются две функции
     * hamming() и manhattan(), для дальнейшего определения приоритета данного board.
     */
    public Board(int[][] array) {
        this.dimension = array.length;
        this.board = new int[this.dimension][this.dimension];
        for(int i = 0; i < this.dimension; i++)
        {
            for (int j = 0; j < this.dimension; j++)
            {
                this.board[i][j] = array[i][j];
            }
        }
        this.hammingNumber = this.hamming();
        this.manhattanNumber = this.manhattan();
    }

    /**
     *
     * @param size
     *
     * \brief Данный конструктор создаёт board,
     * заполняет его цифрами от 1 до N^2-1, после чего перемешивает постановку чисел в board.
     */
    public Board(int size) {
        this.dimension = size;
        this.board = new int[size][size];
        int tempValue = 1;
        for (int i = 0; i < this.dimension; i++){
            for (int j = 0; j < this.dimension; j++){
                if (tempValue < size * size)
                    this.board[i][j] = tempValue;
                tempValue++;
            }
        }
        this.makeItRandom();
    }

    /**
     * Данная функция перемешивает постановку чисел в board.
     *
     */
    private void makeItRandom() {
        for (int i = 0; i < 100; i++) {
            Board temp[];
            temp = new Board[4];
            int j = 0;
            for(Board n : this.neighbors()) {
                temp[j] = n;
                j++;
            }
            int random = ((int) (Math.random() * 10)) % j;
            this.board = temp[random].board;
        }
    }

    /**
     *  Данная функция получает значение размерности доски
     * @return this.dimension;
     */
    public int dimension() {
        return this.dimension;
    }
    /**
     *   \brief Данная метод получает значение манхэтеновского числа.
     *
     * @return this.manhattanNumber;
     */
    public int getManhattanNumber(){
        return this.manhattanNumber;
    }
    /**
     *  \brief Данная метод получает значение число хэмминга.
     *
     *
     * @return this.manhattanNumber;
     */
    public int getHammingNumber() {
        return this.hammingNumber;
    }

    /**
     * \brief Данный метод находит число хэмминга
     * \details Это количество блоков в неправильном положении,
     *  а также количество шагов, сделанных до сих пор, чтобы добраться до узла поиска.
     * @return result;
     */
    public int hamming(){
        int result = 0;
        int expectedValue = 1;
        for(int i = 0; i < this.dimension; i++)
        {
            for(int j = 0; j < this.dimension; j++)
            {
                if(this.board[i][j] != expectedValue)
                    result++;
                expectedValue++;
            }
        }
        result -= 1;
        if (this.hammingNumber != result)
        {
            this.hammingNumber = result;
        }
        return result;
    }

    /**
     * \brief  Данный метод высчитывает манхэтеновское число
     * \details  манхэтеновское число -сумма манхэттенский расстояний (сумма вертикального и горизонтального расстояния)
     *  от блоков до их положения, также количество шагов, сделанных до сих пор,
     *  чтобы добраться до узла поиска.
     *
     * @return result
     */
    public int manhattan() {
        int result = 0;
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                int currentVal = this.board[i][j];
                if (currentVal == 0)
                    continue;
                int goalRow = (currentVal - 1) / this.dimension;
                int goalCol = (currentVal - 1) % this.dimension;
                result += Math.abs(goalRow - i) + Math.abs(goalCol - j);
            }
        }
        this.manhattanNumber = result;
        return result;
    }

    /**
     * Данный метод, возвращает значение в зависимости от того закончена игра или нет.
     * @return true or false
     */
    public boolean isGoal(){
        return this.hamming() == 0;
    }

    /**
     * \brief Данный метод делает копию board, и меняет пару элементов местами(не пустые),
     * пара находится в одной строке.
     * \details Для чего делается данный клон? Для того, чтобы в дальнейшем делать проверку решаемости исходного board
     * Т.к. если хотя бы один из созданных twin - решаем, то оригинальный board не решаем.
     *
     * @return twin;
     */
//    public Board twin(){
//        int row;
//        Board twin = new Board(copyArray(this.board));
//        boolean containsZero;
//
//        do {
//            containsZero = false;
//            row = StdRandom.uniform(twin.dimension);
//            for(int col = 0; col < twin.dimension; col++) {
//                if (twin.board[row][col] == 0) {
//                    containsZero = true;
//                    break;
//                }
//            }
//        }while(containsZero);
//
//        int col1 = twin.dimension / twin.dimension;
//        int col2 = twin.dimension % twin.dimension;
//
//        int temp = twin.board[row][col1 ];
//        twin.board[row][col1] = twin.board[row][col2];
//        twin.board[row][col2] = temp;
//
//        return twin;
//    }

    /**
     * Данный метод создан для того чтобы копировать массив.
     *
     * @param original
     *
     * @return copy
     */
    private int[][] copyArray(int[][] original) {
        int len = original.length;
        int[][] copy = new int[len][len];
        for (int i = 0; i < len; i++)
            for (int j = 0; j < len; j++)
                copy[i][j] = original[i][j];
        return copy;
    }

    /**
     * Данный метод, проверяет равен один board другому board или нет
     *
     * @param y
     *
     * @return true - если да or false - если нет
     */

    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board that = (Board) y;
        if (this.dimension != that.dimension)
            return false;
        for(int i = 0; i < this.dimension; i++) {
            if(!Arrays.equals(this.board[i], that.board[i]))
                return false;
        }
        return true;
    }

    /**
     * \breif Данный метод создаёт соседей исходного board.
     *
     * \details создаёт очередь и добавляет в очередь соседей – это board,
     * который отличается от данного(входного board) состояния,
     * если бы мы выполнили ход пустой ячейки во все возможные стороны.
     *
     * @return neighborQ - очередь полученных соседей;
     *
     */
    public Iterable<Board> neighbors() {
        Queue<Board> neighborQ = new Queue<Board>();
        int zRow = 0;  // row that contains zero
        int zCol = 0;  // col that contains zero
        foundZero:
        for (int row = 0; row < this.dimension; row++) {
            for (int col = 0; col < this.dimension; col++) {
                if(this.board[row][col] == 0) {
                    zRow = row;
                    zCol = col;
                    break foundZero;
                }
            }
        }
        if (zRow > 0) {
            Board n1 = new Board(copyArray(this.board));
            n1.board[zRow][zCol] = n1.board[zRow - 1][zCol];
            n1.board[zRow - 1][zCol] = 0;
            neighborQ.enqueue(n1);
        }
        if (zRow < this.dimension - 1) {
            Board n2 = new Board(copyArray(this.board));
            n2.board[zRow][zCol] = n2.board[zRow + 1][zCol];
            n2.board[zRow + 1][zCol] = 0;
            neighborQ.enqueue(n2);
        }
        if (zCol > 0) {
            Board n3 = new Board(copyArray(this.board));
            n3.board[zRow][zCol] = n3.board[zRow][zCol - 1];
            n3.board[zRow][zCol - 1] = 0;
            neighborQ.enqueue(n3);
        }
        if (zCol < this.dimension - 1) {
            Board n4 = new Board(copyArray(this.board));
            n4.board[zRow][zCol] = n4.board[zRow][zCol + 1];
            n4.board[zRow][zCol + 1] = 0;
            neighborQ.enqueue(n4);
        }
        return neighborQ;
    }

    /**
     * Данный метод преобразует board  в строковую форму представления с определнный форматом.
     *
     * @return s.toString();
     */

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dimension + "\n");
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                s.append(String.format("%2d ", this.board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Данный метод, является главным в данном классе,
     * с помощью него проводится тестирование данного класса.
     *
     * @param args
     */
    public static void main(String[] args) {
        Board temp = new Board(3);
        StdOut.println("Manhatten = " + temp.manhattan());
        StdOut.println("Hamming = " + temp.hamming());
        StdOut.println(temp);
    }
}
