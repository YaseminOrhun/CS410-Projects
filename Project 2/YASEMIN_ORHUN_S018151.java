import java.io.File;
import java.util.*;

public class YASEMIN_ORHUN_S018151 {
    public List<String> nonTerminals = new ArrayList<>();
    public List<String> terminals = new ArrayList<>();
    public List<String> rules = new ArrayList<>();
    public String start;

    public void setNonTerminals(List<String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public void setTerminals(List<String> terminals) {
        this.terminals = terminals;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public List<String> getRules() {
        return rules;
    }

    public String getStart() {
        return start;
    }

    public static List<String> addNewStartingVariable(List<String> rules) {
        rules.add(0, "S0:S");
        return rules;
    }

    public static List<String> removeEpsilonRules(List<String> rules) {

        Iterator<String> iter = rules.iterator();
        List<String> nullableVariables = new ArrayList<>();
        while (iter.hasNext()) {
            String str = iter.next();
            if (str.contains("e")) {  //if the rule has epsilon
                String[] divideRule = str.split(":");
                nullableVariables.add(divideRule[0]);
                if (divideRule[1].length() <= 1) {
                    iter.remove();
                } else {
                    String strNew = divideRule[1].replaceAll("e", "");
                    rules.set(rules.indexOf(str), divideRule[0] + ":" + strNew);
                }
            }
        }
        Iterator<String> iter2 = rules.iterator();
        while (iter2.hasNext()) {
            String str = iter2.next();
            String[] divideRule = str.split(":");
            if (equalsAnyOfTheArray(divideRule[1], nullableVariables.toArray(new String[0]))) {
                nullableVariables.add(divideRule[0]);
            }

        }


        rules = addNullableVariableSubset(rules, nullableVariables);

        System.out.println("Nullable variables =" + nullableVariables);
        return rules;
    }

    public static HashMap<String, String> handleUnitRulesV1(HashMap<String, String> rules, String start) { //Remove unit rules S → S, shown on the left, and S0 → S

        for (Map.Entry<String, String> set :
                rules.entrySet()) {

            List<String> newValue = new ArrayList<>();
            String[] rulesOfKey = set.getValue().split("\\|");

            for (String rule : rulesOfKey) {
                if (rule.equals(start)) {
                    String equivalentRule = rules.get(start);
                    newValue.add(equivalentRule);
                } else {
                    newValue.add(rule);
                }

            }
            rules.put(set.getKey(), convertToRuleFormat(newValue));
        }


        return rules;
    }

    public static HashMap<String, String> handleUnitRulesV2(HashMap<String, String> rules, List<String> nonTerminals) { //Remove unit rules A→B and A→S etc.

        for (Map.Entry<String, String> set : rules.entrySet()) {

            List<String> newValue = new ArrayList<>();
            String[] rulesOfKey = set.getValue().split("\\|");

            for (String rule : rulesOfKey) {
                String equivalentRule = rule;
                for (String nonTerminal : nonTerminals) {
                    if (rule.equals(nonTerminal)) {
                        equivalentRule = rules.get(nonTerminal);
                    }
                }
                newValue.add(equivalentRule);

            }
            rules.put(set.getKey(), convertToRuleFormat(newValue));
        }

        return rules;
    }

    public static HashMap<String, String> convertRemainingRules(HashMap<String, String> rules, List<String> nonTerminals) { //Remove unit rules A→B and A→S etc.


        return rules;
    }


    public static HashMap<String, String> mapRules(List<String> rules, List<String> nonTerminals) { // After removing the epsiolons we will map the rules
        HashMap<String, String> mappedRules = new HashMap<>();
        String[] nonTerminalArray = nonTerminals.toArray(new String[0]);
        Iterator<String> iter = rules.iterator();

        while (iter.hasNext()) {
            String str = iter.next();
            String[] divideRule = str.split(":");
            if (containsAnyOfTheArray(divideRule[0], nonTerminalArray)) {
                if (mappedRules.containsKey(divideRule[0])) {
                    String newRule = mappedRules.get(divideRule[0]) + "|" + divideRule[1];
                    if (!mappedRules.get(divideRule[0]).contains(divideRule[1])) {
                        mappedRules.put(divideRule[0], newRule);
                    }
                } else {
                    mappedRules.put(divideRule[0], divideRule[1]);
                }
            }
        }

        return mappedRules;
    }

    public static void printChomskyNormalForm(List<String> rules) {

        Iterator<String> iter = rules.iterator();

        while (iter.hasNext()) {
            String str = iter.next();
            System.out.println(str);
        }

    }

    public static boolean containsAnyOfTheArray(String string, String[] stringArray) {
        return Arrays.stream(stringArray).anyMatch(string::contains);
    }

    public static boolean equalsAnyOfTheArray(String string, String[] stringArray) {
        return Arrays.stream(stringArray).anyMatch(string::equals);
    }

    public static void printMap(HashMap<String, String> map) {
        for (String name : map.keySet()) {
            String key = name;
            String value = map.get(name);
            System.out.println(key + ":" + value);
        }
    }

    public static void deleteDuplicateRules(HashMap<String, String> map) {
        for (String name : map.keySet()) {
            String key = name;
            String value = map.get(name);
            String[] valuesArray = value.split("\\|");
            valuesArray = Arrays.stream(valuesArray).distinct().toArray(String[]::new);
            map.put(key, String.join("|", valuesArray));
        }
    }

    public static String convertToRuleFormat(List<String> list) {
        String newValue = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : list) {
            stringBuilder.append(value);
            if (!value.equals(list.get(list.size() - 1))) {
                stringBuilder.append("|");
            }
        }
        newValue = stringBuilder.toString();
        return newValue;
    }

    public static HashMap<String, String> addNewRulesToRuleSet(HashMap<String, String> map, List<String> nonTerminals, List<String> terminals, String start) { // Collect All rules and if the rule is in form A->BC or A->a if not add a new ruleset
        ArrayList<String> allRules = new ArrayList<>();
        for (String name : map.keySet()) {
            String key = name;
            String value = map.get(name);
            String[] allValues = value.split("\\|");
            Collections.addAll(allRules, allValues);
        }

        String[] allRuleSet = Arrays.stream(allRules.toArray()).distinct().toArray(String[]::new);
        ArrayList<String> rulesThatConflictWithCNF = new ArrayList<>();
        HashMap<String, String> newRulesMap = new HashMap<>();
        String newIndex = "X";
        int Sx = 1; //S1
        for (String s : allRuleSet) {
            String[] rule = s.split("");
            if (rule.length == 2) {
                if (equalsAnyOfTheArray(rule[0], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], terminals.toArray(new String[0]))) { //aa
                    if (!newRulesMap.containsValue(rule[0]) && !newRulesMap.containsValue(rule[1]) && !rule[1].equals(rule[0])) { //ab
                        newRulesMap.put(newIndex + Sx, rule[0]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                        newRulesMap.put(newIndex + Sx, rule[1]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                        newRulesMap.put(newIndex + Sx, getKFValue(newRulesMap, rule[0]).toString() + getKFValue(newRulesMap, rule[1]).toString());
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    } else if (!newRulesMap.containsValue(rule[0]) && rule[1].equals(rule[0])) { //aa
                        newRulesMap.put(newIndex + Sx, rule[0]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                        newRulesMap.put(newIndex + Sx, getKFValue(newRulesMap, rule[0]).toString() + getKFValue(newRulesMap, rule[0]).toString());
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                } else if (equalsAnyOfTheArray(rule[0], nonTerminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], terminals.toArray(new String[0]))) { //Aa
                    if (!newRulesMap.containsValue(rule[1])) {
                        newRulesMap.put(newIndex + Sx, rule[1]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                } else if (equalsAnyOfTheArray(rule[0], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], nonTerminals.toArray(new String[0]))) { //aA
                    if (!newRulesMap.containsValue(rule[0])) {
                        newRulesMap.put(newIndex + Sx, rule[0]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                }

            } else if (rule.length == 3) {
                if (equalsAnyOfTheArray(rule[0], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[2], terminals.toArray(new String[0]))) { //aaa
                    if (!newRulesMap.containsValue(s)) { // if aaa does not exist already
                        newRulesMap.put(newIndex + Sx, s);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                } else if (equalsAnyOfTheArray(rule[0], nonTerminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[2], nonTerminals.toArray(new String[0]))) { //AaA
                    if (!newRulesMap.containsValue(rule[0] + rule[1])) { // if aA does not exist already
                        newRulesMap.put(newIndex + Sx, rule[0] + rule[1]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    } else {
                        if (!newRulesMap.containsValue(rule[1] + rule[0])) { // if Aa does not exist already
                            newRulesMap.put(newIndex + Sx, rule[1] + rule[0]);
                            nonTerminals.add(newIndex + Sx);
                            Sx = Sx + 1;
                        }

                    }
                } else if (equalsAnyOfTheArray(rule[0], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], nonTerminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[2], terminals.toArray(new String[0]))) { //aAa
                    if (!newRulesMap.containsValue(rule[0])) {
                        newRulesMap.put(newIndex + Sx, rule[0]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                        if (!newRulesMap.containsValue(rule[1] + rule[2])) {
                            newRulesMap.put(newIndex + Sx, rule[1] + rule[2]);
                            nonTerminals.add(newIndex + Sx);
                            Sx = Sx + 1;
                        }
                    } else if (!newRulesMap.containsValue(rule[1] + rule[2])) {
                        newRulesMap.put(newIndex + Sx, rule[1] + rule[2]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                } else if (equalsAnyOfTheArray(rule[0], nonTerminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], nonTerminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[2], terminals.toArray(new String[0]))) { //AAa
                    if (!newRulesMap.containsValue(rule[1] + rule[2])) {
                        newRulesMap.put(newIndex + Sx, rule[1] + rule[2]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                } else if (equalsAnyOfTheArray(rule[0], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], nonTerminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[2], nonTerminals.toArray(new String[0]))) { //aAA
                    if (!newRulesMap.containsValue(rule[0])) {
                        newRulesMap.put(newIndex + Sx, rule[0]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                        newRulesMap.put(newIndex + Sx, getKFValue(newRulesMap, rule[0]).toString() + rule[1]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                } else if (equalsAnyOfTheArray(rule[0], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[2], nonTerminals.toArray(new String[0]))) { //aaA
                    if (!newRulesMap.containsValue(rule[0]) && !newRulesMap.containsValue(rule[1]) && !rule[1].equals(rule[0])) { //abA
                        newRulesMap.put(newIndex + Sx, rule[0]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                        newRulesMap.put(newIndex + Sx, rule[1]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                        newRulesMap.put(newIndex + Sx, getKFValue(newRulesMap, rule[0]).toString() + getKFValue(newRulesMap, rule[1]).toString());
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    } else if (!newRulesMap.containsValue(rule[0]) && rule[1].equals(rule[0])) { //aaA
                        newRulesMap.put(newIndex + Sx, rule[0]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                        newRulesMap.put(newIndex + Sx, getKFValue(newRulesMap, rule[0]).toString() + getKFValue(newRulesMap, rule[0]).toString());
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                } else if (equalsAnyOfTheArray(rule[0], nonTerminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[1], terminals.toArray(new String[0])) && equalsAnyOfTheArray(rule[2], terminals.toArray(new String[0]))) { //Aaa
                    if (!newRulesMap.containsValue(rule[1] + rule[2])) {
                        newRulesMap.put(newIndex + Sx, rule[1] + rule[2]);
                        nonTerminals.add(newIndex + Sx);
                        Sx = Sx + 1;
                    }
                }
            }

        }
        System.out.println("newRulesMap");

        printMap(newRulesMap);
        return newRulesMap;

    }

    public static List<String> addNullableVariableSubset(List<String> ruleSet, List<String> nullableVariable) {

        Iterator<String> iter = ruleSet.iterator();

        while (iter.hasNext()) {
            String str = iter.next();
            String[] divideRule = str.split(":");
            for (int i = 0; i < nullableVariable.size(); i++) {
                if (divideRule[1].contains(nullableVariable.get(i))) {
                    int numberOfNullableChar = 0;
                    ArrayList<Integer> indexOfMatches = new ArrayList<>();
                    for (int k = 0; k < divideRule[1].length(); k++) {
                        if (divideRule[1].charAt(k) == nullableVariable.get(i).charAt(0)) {
                            numberOfNullableChar++;
                            indexOfMatches.add(k);
                        }
                    }

                    if (divideRule[1].length() == 2) {
                        if (numberOfNullableChar == 1) {
                            ruleSet.set(ruleSet.indexOf(str), divideRule[0] + ":" + divideRule[1] + "|" + divideRule[1].replace(nullableVariable.get(i), ""));
                        } else if (numberOfNullableChar == 2) {
                            StringBuilder newRule = new StringBuilder(divideRule[1]);
                            String firstSubset = newRule.deleteCharAt(indexOfMatches.get(0)).toString();
                            String secondSubset = newRule.deleteCharAt(indexOfMatches.get(1)).toString();
                            ruleSet.set(ruleSet.indexOf(str), divideRule[0] + ":" + divideRule[1]
                                    + "|" + divideRule[1].replaceAll(nullableVariable.get(i), "")
                                    + "|" + firstSubset
                                    + "|" + secondSubset);
                        }
                    }

                    if (divideRule[1].length() == 3) {
                        if (numberOfNullableChar == 1) {
                            ruleSet.set(ruleSet.indexOf(str), divideRule[0] + ":" + divideRule[1] + "|" + divideRule[1].replace(nullableVariable.get(i), ""));
                        } else if (numberOfNullableChar == 2) {
                            StringBuilder newRule = new StringBuilder(divideRule[1]);
                            String firstSubset = newRule.deleteCharAt(indexOfMatches.get(0)).toString();
                            StringBuilder newRule2 = new StringBuilder(divideRule[1]);
                            String secondSubset = newRule2.deleteCharAt(indexOfMatches.get(1)).toString();
                            String newValue = "";
                            if (!divideRule[1].replaceAll(nullableVariable.get(i), "").equals(divideRule[0])) {
                                newValue = divideRule[1]
                                        + "|" + divideRule[1].replaceAll(nullableVariable.get(i), "")
                                        + "|" + firstSubset
                                        + "|" + secondSubset;
                            } else {
                                newValue = divideRule[1]
                                        + "|" + firstSubset
                                        + "|" + secondSubset;
                            }


                            ruleSet.set(ruleSet.indexOf(str), divideRule[0] + ":" + newValue);
                        }
                    }
                }

            }

        }

        return ruleSet;
    }

    public static HashMap<String, String> finalizeChomskyNormalForm(HashMap<String, String> map, HashMap<String, String> newSubsetMap, List<String> nonTerminals, List<String> terminals, String start) { // Finalize Chomsky Normal Form
        addNewSubsetToMap(map, newSubsetMap, nonTerminals, terminals);

        for (String name : newSubsetMap.keySet()) {
            String key = name;
            String value = newSubsetMap.get(name);
            map.put(key, value);
        }

        return map;

    }

    private static void addNewSubsetToMap(HashMap<String, String> map, HashMap<String, String> newSubsetMap, List<String> nonTerminals, List<String> terminals) {
        ArrayList<String> allNewRules = new ArrayList<>();
        for (String name : newSubsetMap.keySet()) {
            String key = name;
            String value = newSubsetMap.get(name);
            String[] allValues = value.split("\\|");
            Collections.addAll(allNewRules, allValues);
        }

        for (String name : map.keySet()) {
            String key = name;
            String value = map.get(name);

            String[] allNewRuleSet = Arrays.stream(allNewRules.toArray()).distinct().toArray(String[]::new);

            String[] allValues = value.split("\\|");
            for (int i = 0; i < allValues.length; i++) {
                for (String newSubset : allNewRuleSet) {
                    if (allValues[i].contains(newSubset) && allValues[i] != newSubset && (allValues[i].length() != 1 && !equalsAnyOfTheArray(allValues[i], terminals.toArray(String[]::new)) || (allValues[i].length() != 1 && !equalsAnyOfTheArray(allValues[i], nonTerminals.toArray(String[]::new))))) {
                        for (String n : newSubsetMap.keySet()) {
                            if (newSubsetMap.get(n) == newSubset) {
                                String subset = n;
                                String replaceString = allValues[i].replace(newSubset, subset);
                                allValues[i] = replaceString;
                            }
                        }
                    }
                }
            }
            map.put(key, String.join("|", allValues));
        }
    }

    public static Object getKFValue(Map map, Object v) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(v)) {
                return o;
            }
        }
        return null;
    }

    public static void convert(List<String> nonTerminals, List<String> terminals, List<String> rules, String start) {
        System.out.println(rules);
        System.out.println("Non-terminals: " + nonTerminals);
        System.out.println("Terminals: " + terminals);
        System.out.println("_________________________________________________________");
        System.out.println("Start Process... ");
        System.out.println("_________________________________________________________");

        System.out.println("**** 1. Add New Start Variable **** ");
        addNewStartingVariable(rules);
        HashMap<String, String> map1 = mapRules(rules, nonTerminals);
        printMap(map1);

        System.out.println("**** 2. Remove Epsilon **** ");
        rules = removeEpsilonRules(rules);
        HashMap<String, String> map = mapRules(rules, nonTerminals);
        deleteDuplicateRules(map);
        printMap(map);

        System.out.println("**** 3.a Handle S0-->S, A-->S and B-->S **** ");
        map = handleUnitRulesV1(map, start);
        deleteDuplicateRules(map);
        printMap(map);

        System.out.println("**** 3.b Handle A-->B and B-->A etc. **** ");
        map = handleUnitRulesV2(map, nonTerminals);
        deleteDuplicateRules(map);
        printMap(map);

        System.out.println("**** 4. Convert remaining rules **** ");
        HashMap<String, String> newRuleSet = addNewRulesToRuleSet(map, nonTerminals, terminals, start);
        HashMap<String, String> CNF = finalizeChomskyNormalForm(map, newRuleSet, nonTerminals, terminals, start);

        System.out.println("**** 4. Finalized Chomsky Normal Form **** ");
        printMap(CNF);
    }


    public static void main(String[] args) {
        List<String> nonTerminals = new ArrayList<>();
        List<String> terminals = new ArrayList<>();
        List<String> rules = new ArrayList<>();
        String start = "";

        start = readAlphabet(nonTerminals, terminals, rules, start);

        try {
            convert(nonTerminals, terminals, rules, start);
        } catch (Exception e) {
            System.out.println("An error occurred during cfg to cnf conversion.");
            e.printStackTrace();
        }

    }

    private static String readAlphabet(List<String> nonTerminals, List<String> terminals, List<String> rules, String start) {
        try {
            File inputText = new File("G2.txt");
            Scanner scanner = new Scanner(inputText);
            String src = "";

            while (scanner.hasNextLine()) {
                String next = scanner.nextLine();

                if (next.contains("START") || next.contains("RULES") || next.contains("TERMINAL") ||
                        next.contains("NON-TERMINAL")) {
                    src = next;
                } else if (src.contains("START")) {
                    start = next;
                } else if (src.contains("RULES")) {
                    rules.add(next);
                } else if (src.contains("NON-TERMINAL")) {
                    nonTerminals.add(next);
                } else if (src.contains("START")) {
                    start = next;
                } else if (src.contains("TERMINAL")) {
                    terminals.add(next);
                }
            }
            scanner.close();

        } catch (Exception e) {
            System.out.println("An error occurred during file input.");
            e.printStackTrace();
        }
        return start;
    }

}
