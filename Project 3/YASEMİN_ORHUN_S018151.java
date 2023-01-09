import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class YASEMİN_ORHUN_S018151 {
    public static void main(String[] args) {
        try {
            Alphabet alphabet = new Alphabet();
            readAlphabetFromInputFile(alphabet, "Input_YASEMİN_ORHUN_S018151.txt");
            List<String> tape = createTape(alphabet);
            String result = simulateTuringMachine(alphabet, tape);
            writeResultsToConsole(alphabet,result);

        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static List<String> createTape(Alphabet alphabet) {
        String[] stringDetected = alphabet.getStringToBeDetected().split("");
        String[] tapeArray = new String[50 + stringDetected.length];
        for (int i = 0; i < 50 + stringDetected.length; i++) {
            if (i < stringDetected.length) {
                tapeArray[i] = stringDetected[i];
            } else {
                tapeArray[i] = alphabet.getBlankSymbol();
            }
        }
        List<String> tape = Arrays.asList(tapeArray);
        return tape;
    }

    public static String simulateTuringMachine(Alphabet alphabet, List<String> tape) {
        String state = alphabet.getStartState();
        String route = "";

        int loopLimit = 100000;
        int tapeHeadIndex = 0;
        int currentLoopIndex = 0;

        for (currentLoopIndex = 0; currentLoopIndex < loopLimit; currentLoopIndex++) {
            if (!state.equals(alphabet.getAcceptState()) && !state.equals(alphabet.getRejectState())) {
                for (String currentTransition : alphabet.getGoalStates()) {
                    String[] transitionInfo = currentTransition.split(" ");

                    String firstState = transitionInfo[0];
                    String symbolToRead = transitionInfo[1];
                    String symbolToWrite = transitionInfo[2];
                    String direction = transitionInfo[3];
                    String secondState = transitionInfo[4];
                    String tapeNext = tape.get(tapeHeadIndex);

                    if (symbolToRead.equals(tapeNext) && firstState.equals(state)) {
                        tape.set(tapeHeadIndex, symbolToWrite);
                        tapeHeadIndex = moveTapeIndex(tapeHeadIndex, direction);
                        state = secondState;
                        route = route.concat(state + " ");
                    }
                }
            }
        }
        return state + "/" + route;
    }

    private static void writeResultsToConsole(Alphabet alphabet, String resultset) {
        String[] results = resultset.split("/");
        String state = results[0];
        String result = results[1];

        if (state.equals(alphabet.getAcceptState())) {
            System.out.println("ROUTE: " + alphabet.getStartState() + " " + result);
            System.out.println("RESULT: " + "accepted");
        } else if (state.equals(alphabet.getRejectState())) {
            System.out.println("ROUTE: " + alphabet.getStartState() + " " + result);
            System.out.println("RESULT: " + "rejected");
        } else {
            System.out.println("ROUTE: " + result);
            System.out.println("RESULT: " + "looped");
        }
    }

    private static int moveTapeIndex(int tapeHeadIndex, String direction) {
        if (direction.equals("R")) {
            tapeHeadIndex++;
        } else if (direction.equals("L") && tapeHeadIndex != 0) {
            tapeHeadIndex--;
        }
        return tapeHeadIndex;
    }

    private static void readAlphabetFromInputFile(Alphabet alphabet, String path) {
        try {
            File inputText = new File(path);
            Scanner scanner = new Scanner(inputText);
            String src = "";
            List<String> goalStateList = new ArrayList<>();
            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (lineNumber == 1) {
                    alphabet.setNumberOfVariablesInput(Integer.parseInt(line));
                } else if (lineNumber == 2) {
                    List<String> alphabetInput = Arrays.asList(line.split(" "));
                    alphabet.setAlphabetInput(alphabetInput);
                } else if (lineNumber == 3) {
                    alphabet.setNumberOfVariablesTape(Integer.parseInt(line));
                } else if (lineNumber == 4) {
                    List<String> alphabetTape = Arrays.asList(line.split(" "));
                    alphabet.setAlphabetTape(alphabetTape);
                } else if (lineNumber == 5) {
                    alphabet.setBlankSymbol(line);
                } else if (lineNumber == 6) {
                    alphabet.setNumberOfStates(Integer.parseInt(line));
                } else if (lineNumber == 7) {
                    List<String> states = Arrays.asList(line.split(" "));
                    alphabet.setStates(states);
                } else if (lineNumber == 8) {
                    alphabet.setStartState(line);
                } else if (lineNumber == 9) {
                    alphabet.setAcceptState(line);
                } else if (lineNumber == 10) {
                    alphabet.setRejectState(line);
                } else if (lineNumber > 10 && !line.equals("STRING")) {
                    goalStateList.add(line);
                } else if (line.equals("STRING")) {
                    lineNumber++;
                    line = scanner.nextLine();
                    alphabet.setGoalStates(goalStateList);
                    alphabet.setStringToBeDetected(line);
                }
                lineNumber++;
            }
            scanner.close();

        } catch (Exception e) {
            System.out.println("An error occurred during file input.");
            e.printStackTrace();
        }
    }

    public static class Alphabet {
        public int numberOfVariablesInput;
        public int numberOfVariablesTape;
        public List<String> alphabetInput;
        public List<String> alphabetTape;
        public String blankSymbol;
        public int numberOfStates;
        public List<String> states;
        public String startState;
        public String acceptState;
        public String rejectState;
        public List<String> goalStates;
        public String stringToBeDetected;

        public Alphabet() {
        }

        public String getStringToBeDetected() {
            return stringToBeDetected;
        }

        public void setStringToBeDetected(String stringToBeDetected) {
            this.stringToBeDetected = stringToBeDetected;
        }

        public List<String> getGoalStates() {
            return goalStates;
        }

        public void setGoalStates(List<String> goalStates) {
            this.goalStates = goalStates;
        }

        public int getNumberOfVariablesInput() {
            return numberOfVariablesInput;
        }

        public void setNumberOfVariablesInput(int numberOfVariablesInput) {
            this.numberOfVariablesInput = numberOfVariablesInput;
        }

        public int getNumberOfVariablesTape() {
            return numberOfVariablesTape;
        }

        public void setNumberOfVariablesTape(int numberOfVariablesTape) {
            this.numberOfVariablesTape = numberOfVariablesTape;
        }

        public List<String> getAlphabetInput() {
            return alphabetInput;
        }

        public void setAlphabetInput(List<String> alphabetInput) {
            this.alphabetInput = alphabetInput;
        }

        public List<String> getAlphabetTape() {
            return alphabetTape;
        }

        public void setAlphabetTape(List<String> alphabetTape) {
            this.alphabetTape = alphabetTape;
        }

        public String getBlankSymbol() {
            return blankSymbol;
        }

        public void setBlankSymbol(String blankSymbol) {
            this.blankSymbol = blankSymbol;
        }

        public int getNumberOfStates() {
            return numberOfStates;
        }

        public void setNumberOfStates(int numberOfStates) {
            this.numberOfStates = numberOfStates;
        }

        public List<String> getStates() {
            return states;
        }

        public void setStates(List<String> states) {
            this.states = states;
        }

        public String getStartState() {
            return startState;
        }

        public void setStartState(String startState) {
            this.startState = startState;
        }

        public String getAcceptState() {
            return acceptState;
        }

        public void setAcceptState(String acceptState) {
            this.acceptState = acceptState;
        }

        public String getRejectState() {
            return rejectState;
        }

        public void setRejectState(String rejectState) {
            this.rejectState = rejectState;
        }
    }
}
