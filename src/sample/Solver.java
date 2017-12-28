package sample;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * @author Stas.Maslov
 * @author Ilya.Tyugashev
 * @version 1.2.1
 * \brief Данный класс отыскивает и выводит кратчайший путь
 * до финального состояния, с помощью алгоритма поиска
 * A*(эвристический поиск кратчайших путей в графе)
 */
public class Solver {
    private Node target = null;
    private Board init;
    private boolean solvable = false;
    /**
     * \brief Данный класс, представляет из себя узёл, котоырй в дальнейшем будет
     * использоваться в алгоритме A*(star), для создания очередей.
     *
     */
    public class Node implements Comparable<Node> {
        private final Board board;
        private final int moves;
        private final Node prev;
        private final int priority;

        /**
         * \brief данный конструктор присваивает значения private final
         * переменным, тем самым создаёт узел.
         * \details Узлу присваиваются значения  входящего board
         * устанавливается указатель на предыдуищй узел, также находится количество
         * движений пустой ячейки, с помощью которых мы попали в данный board.
         * также устанавливается приоритетность данного узла.
         *
         * @param newBoard
         * @param previous
         */
        public Node(Board newBoard, Node previous) {
            this.board = newBoard;
            this.prev = previous;
            if (previous == null)
                this.moves = 0;
            else this.moves = previous.moves + 1;
            this.priority = newBoard.manhattan() + this.moves;
        }
        @Override
        /**
         * \brief Данный метод сравнивает приоритеты двух узлов.
         */
        public int compareTo(Node that) {
            if (this.priority > that.priority) return 1;
            if (this.priority < that.priority) return -1;
            return 0;
        }
    }

    /**
     * Данный конструктор имеет входящие данные - изначальынй board.
     * После проверяется, является ли данный board уже решённым,
     * если да, то переменнаая target создаёт итоговый узел.
     * Если нет, то запускается решение данного board и вызывается
     * функция startSolving().
     * @param initial
     */
    public Solver(Board initial) {
        this.init = initial;
        if (initial.isGoal()) {
            target = new Node (initial, null);
        }
        else {
            this.solvable = this.isSolvable();
            if (this.solvable)
                target = startSolving(initial);
        }
    }

    /**
     * \brief Данный метод запускает решение головоломки с помощью алгоритма A*.
     * создаются две приоритетные очереди, оригинал и twin,
     * как мы помним twin отличается от оригинала тем, что
     * в нём изменена пара элементом(местами друг с другом, не должны изменяться нулевая ячеейка
     * и изменение происходит в одой строке).
     *
     * \details Изначально в обе очереди вставляются, данные Boardы,
     * после чего начинается цикл, и соответсвенно поиск кратчайщего решения.
     * Цикл, нахождения соседей, их добавления в приоритетные очереди и
     * удаления узлов  из очередей с минимальным приоритетом, и если twinTree isGoal() – true,
     * то данная board initial – не решаема,
     * где board initial – это изначально введённый board.
     *
     * @param initial
     *
     *
     * @return min
     */
    private Node startSolving(Board initial) {
        Node min;
        MinPQ<Node> Tree = new MinPQ<Node>();
        Tree.insert(new Node(initial, null));
        while (true) {
            min = Tree.delMin();
            if (min.board.isGoal()) {
                break;
            }
            saveNeighbors(min, Tree);
        }
        return min;
    }

    /**
     * Данный метод сохраняет всех полученных соседей.
     * при том не добавляет данного
     * соседа если он равен предыдущему.
     *
     * @param min
     * @param currTree
     */
    private void saveNeighbors(Node min, MinPQ<Node> currTree) {
        for (Board n : min.board.neighbors()) {
            if (min.prev == null) {
                currTree.insert(new Node(n, min));
            } else {
                if (!n.equals(min.prev.board))
                    currTree.insert(new Node(n, min));
            }
        }
    }

    /**
     * С помощью данного метода проверяется решена ли игра или нет
     *
     * @return target;
     */
    public boolean isSolvable(){
        int sum = 0;
        int zeroRow = 0;
        for (int i = 0; i < this.init.dimension(); i++) {
            for (int j = 0; j < this.init.dimension(); j++) {
                if (this.init.board[i][j] == 0) {
                    zeroRow = i + 1;
                }
                if (this.init.board[i][j] != 0) {
                    for (int row = 0; row <= i; row++) {
                        for (int coll = 0; row == i ? coll < j : coll < this.init.dimension(); coll++) {
                                if (this.init.board[i][j] < this.init.board[row][coll])
                                    sum++;
                        }
                    }
                }
            }
        }
        if (this.init.dimension() % 2 == 0) {
            sum += zeroRow;
        }
        return sum % 2 == 0;
    }

    /**
     * С помощью данного метода мы находим количество движений,
     * которые мы совершили для решения данной головоломки.
     *
     * @return target.moves;
     */
    public int moves(){
        if (target == null) return -1;
        return target.moves;
    }

    /**
     * Данный метод, записывает в стэк последавтельность board,
     * который приведут к решению данной игры за минимальное количество ходов.
     * Т.е. записывает решение.
     *
     * @return solutionStack;
     */
    public Iterable<Board> solution() {
        if (!this.isSolvable())
            return null;

        Stack<Board> solutionStack = new Stack<Board>();
        for (Node curr = target; curr != null; curr = curr.prev) {
            solutionStack.push(curr.board);
        }
        return solutionStack;
    }

    /**
     * Данный метод считывает исходный двумерный массив, после чего создаётся board
     * с данным двумерным массивом. И запускается решение данной игры.
     *
     * После выводится Решение данной головоломки.
     * @param args
     */
    public static void main(String[] args) {
        In in = new In();
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println("Manhatten = " + board.manhattan());
                StdOut.println("Hamming = " + board.hamming());
                StdOut.println(board);
            }
        }
    }
}
