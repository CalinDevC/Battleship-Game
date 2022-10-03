package battleship;


import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PlayerBoard {
    private final int BOARD_ROW = 11;
    private final int BOARD_COLUMN = 11;
    private final int USER_Input_Length = 3;
    private final String[][] initialBoard = new String[BOARD_ROW][BOARD_COLUMN];
    private Scanner scanner ;
    private String[]userInput = new String[USER_Input_Length];

    protected void generateInitialBoard() {
        String[] alphabet = new String[]{"X", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        for (int row = 0; row < initialBoard.length; row++) {
            for (int column = 0; column < initialBoard[row].length; column++) {
                if ((row == 0) && (column == 0)) {
                    initialBoard[row][column] = "  ";
                } else if (row == 0) {
                    initialBoard[row][column] = column + " ";
                } else if (column == 0) {
                    initialBoard[row][column] = alphabet[row];
                } else {
                    initialBoard[row][column] = " ~";
                }
            }
        }
    }
    protected void printBoard() {
        for (String[] strings : initialBoard) {
            for (String string : strings) {
                System.out.print(string);
            }
            System.out.println();
        }
    }
    private String[] stringConverter(String input) {
        input = input.trim();
        String[] arrayString = input.split(" ");
        String[] coordinates = new String[4];

        Pattern patternLetters = Pattern.compile("[A-J]");
        Pattern patternNumbers = Pattern.compile("\\d\\d?");
        Matcher matcherLetters = patternLetters.matcher(arrayString[0]);
        Matcher matcherNumbers = patternNumbers.matcher(arrayString[0]);

        while (matcherLetters.find()) {
            coordinates[0] = matcherLetters.group();
        }
        matcherLetters = patternLetters.matcher(arrayString[1]);
        while (matcherLetters.find()) {
            coordinates[2] = matcherLetters.group();
        }
        while (matcherNumbers.find()) {
            coordinates[1] = matcherNumbers.group();
        }
        matcherNumbers = patternNumbers.matcher(arrayString[1]);
        while (matcherNumbers.find()) {
            coordinates[3] = matcherNumbers.group();
        }
        return coordinates;
    }

    private void printCoordinates(String[] somthInput) {
        for (String s : somthInput) {
            System.out.println(s);
        }
    }
    private boolean updateArray(String[] input, Ships ship) {
        char firstLetter = input[0].toUpperCase().charAt(0);
        int firstAscii = (int) firstLetter - 64;
        char secondLetter = input[2].charAt(0);
        int secondAscii = (int) secondLetter - 64;

        int firstNumber = Integer.parseInt(input[1]);
        int secondNumber = Integer.parseInt(input[3]);

        boolean checkedHorizontal = false;
        boolean checkedVertical = false;

        if (firstAscii == secondAscii) {
            while (!checkedHorizontal) {
                checkedHorizontal = checkHorizontalNeighbours(firstAscii, firstNumber, secondNumber, ship);
                if (!checkedHorizontal) {
                    return handleShipsTooClose(ship);
                }
            }
            return updateArrayByRow(ship, firstAscii, firstNumber, secondNumber);
        } else if (firstNumber == secondNumber) {
            while (!checkedVertical){
                checkedVertical = checkVerticalNeighbours(firstNumber, firstAscii, secondAscii, ship);
                if (!checkedVertical) {
                    return handleShipsTooClose(ship);
                }
            }
            return updateArrayByColumn(ship, firstAscii, secondAscii, firstNumber);
        } else {
            return handleWrongShipLocation(ship);
        }
    }

    private boolean updateArrayByColumn(Ships ship, int firstAscii, int secondAscii, int firstNumber) {
        int startPoint = Math.min(firstAscii, secondAscii);
        int finishPoint = Math.max(firstAscii, secondAscii);

        if (finishPoint - startPoint == ship.size -1) {
            for (int row = startPoint; row <= finishPoint; row++){
                initialBoard[row][firstNumber] = " O";
            }
        }else {
            return handleWrongShipLength(ship);
        }
        return true;
    }
    private boolean updateArrayByRow(Ships ship, int firstAscii, int firstNumber, int secondNumber) {
        int startPoint = Math.min(firstNumber, secondNumber);
        int finishPoint = Math.max(firstNumber, secondNumber);
        if (finishPoint - startPoint == ship.size - 1) {
            for (int column = startPoint; column <= finishPoint; column++){
                initialBoard[firstAscii][column] = " O";
            }
        }
        else {
            return handleWrongShipLength(ship);
        }
        return true;
    }
    private boolean handleWrongShipLength(Ships ship) {
        System.out.println();
        System.out.println("Error! Wrong length of the " + ship.name + " ! Try again:");
        System.out.println();
        return false;
    }

    private boolean handleWrongShipLocation(Ships ship) {
        System.out.println();
        System.out.println("Error! Wrong ship location! Try again:");
        System.out.println();
        return false;
    }
    private boolean handleShipsTooClose(Ships ship) {
        System.out.println();
        System.out.println("Error! You placed it too close to another one. Try again:");
        return false;
    }
    private boolean checkHorizontalNeighbours(int constant, int firstNumber, int secondNumber, Ships ship) {
        int rowMinusOne = constant - 1;
        int rowPlusOne = constant + 1;
        int startingPoint = Math.min(firstNumber, secondNumber);
        int finishPoint = Math.max(firstNumber, secondNumber);
        boolean isCheckedHorizontalNeighbours = false;

        if (finishPoint < initialBoard.length - 1) {
            for (int column = startingPoint - 1; column <= finishPoint + 1; column++) {
                isCheckedHorizontalNeighbours = horizontalLogicCheck(constant, ship, rowMinusOne, rowPlusOne, column);
            }
        } else {
            for (int column = startingPoint - 1; column <= finishPoint; column++){
                isCheckedHorizontalNeighbours = horizontalLogicCheck(constant, ship, rowMinusOne, rowPlusOne, column);
            }
        }
        return isCheckedHorizontalNeighbours;
    }
    private boolean horizontalLogicCheck(int constant, Ships ship, int rowMinusOne, int rowPlusOne, int column) {
        boolean isLogicAccepted = true;
        if (constant < initialBoard.length - 1) {
            if ((initialBoard[rowMinusOne][column].equals(" O")) ||
                    (initialBoard[constant][column].equals(" O")) ||
                    (initialBoard[rowPlusOne][column].equals(" O"))) {

                isLogicAccepted = false;
            }
        } else {
            if ((initialBoard[rowMinusOne][column].equals(" O")) ||
                    (initialBoard[constant][column].equals(" O"))) {

                isLogicAccepted = false;
            }
        }
        return isLogicAccepted;
    }
    private boolean checkVerticalNeighbours(int constant, int firstNumber, int secondNumber, Ships ship) {
        int columnMinusOne = constant - 1;
        int columnPlusOne = constant + 1;
        int startingPoint = Math.min(firstNumber, secondNumber);
        int finishPoint = Math.max(firstNumber, secondNumber);

        boolean isCheckedVerticalNeighbours = false;

        if (finishPoint < initialBoard.length - 1) {
            for (int row = startingPoint - 1; row <= finishPoint + 1; row++) {
                isCheckedVerticalNeighbours = verticalLogicCheck(constant, ship, columnMinusOne, columnPlusOne, row);
            }
        } else {
            for (int row = startingPoint - 1; row <= finishPoint; row++) {
                isCheckedVerticalNeighbours = verticalLogicCheck(constant, ship, columnMinusOne, columnPlusOne, row);
                if(!isCheckedVerticalNeighbours) {
                    return false;
                }
            }
        }
        return isCheckedVerticalNeighbours;
    }
    private boolean verticalLogicCheck(int constant, Ships ship, int columnMinusOne, int columnPlusOne, int row) {
        boolean isLogicAccepted = true;
        if (constant < initialBoard.length - 1) {
            if (initialBoard[row][columnMinusOne].equals(" O") ||
                    (initialBoard[row][constant].equals(" O")) ||
                    (initialBoard[row][columnPlusOne].equals(" O"))) {

                isLogicAccepted = false;
            }
        } else {
            if (initialBoard[row][columnMinusOne].equals(" O") ||
                    (initialBoard[row][constant].equals(" O"))) {

                isLogicAccepted = false;
            }
        }
        return isLogicAccepted;
    }

    protected void getUserInput() {
        generateInitialBoard();
        for (Ships ship : Ships.values()) {
            System.out.println("Enter the coordinates of the " + ship.name + " (" + ship.size + " cells):");
            boolean shipSuccessfullyDeployed = false;
            while(!shipSuccessfullyDeployed) {
                shipSuccessfullyDeployed = getInputAndUpdate(ship);
            }
            printBoard();
        }
    }

    public static void main(String[] args) {
        new PlayerBoard().getUserInput();
    }

    private boolean getInputAndUpdate(Ships ship) {
        scanner = new Scanner(System.in);
        try {
            userInput = stringConverter(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Error in the input string please try again");
            return false;
        }
        return updateArray(userInput, ship);
    }

    enum Ships {
        AIRCRAFT("Aircraft Carrier", 5),
        BATTLESHIP("Battleship", 4),
        SUBMARINE("Submarine", 3),
        CRUISER("Cruiser", 3),
        DESTROYER("Destroyer", 2);

        private final String name;
        private final int size;

        Ships(String name, int size) {
            this.name = name;
            this.size = size;
        }

        public String getShortName() {
            return name;
        }

        public String getFullName() {
            return name;
        }

        public int getCode() {
            return size;
        }

    }
}
