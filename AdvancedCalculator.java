import java.awt.*;
import java.io.*;
import javax.swing.*;

public class AdvancedCalculator {
    private static boolean isHistoryOn = true;
    private static String lastResult = "0"; // Default to 0


    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Scientific Calculator");
        frame.setSize(500, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        // Output display
        JTextField display = new JTextField("");
        display.setFont(new Font("Arial", Font.BOLD, 20));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(Color.cyan);
        display.setPreferredSize(new Dimension(80, 80));
        display.setEditable(false);
        frame.add(display, BorderLayout.NORTH);

        // Main panel to hold both sections
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));

        // History Panel
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        historyPanel.setBackground(Color.LIGHT_GRAY);

        JLabel historyLabel = new JLabel("History:");
        historyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JButton toggleHistoryButton = new JButton("ON");
        JButton viewHistoryButton = new JButton("View History");
        historyPanel.add(historyLabel);
        historyPanel.add(toggleHistoryButton);
        historyPanel.add(viewHistoryButton);

        // Toggle history functionality
        toggleHistoryButton.addActionListener(e -> {
            isHistoryOn = !isHistoryOn;
            toggleHistoryButton.setText(isHistoryOn ? "ON" : "OFF");
        });

        // View history functionality
        viewHistoryButton.addActionListener(e -> {
            try {
                File historyFile = new File("history.txt");
                if (!historyFile.exists()) {
                    JOptionPane.showMessageDialog(frame, "No history available.", "History", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                StringBuilder historyContent = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(historyFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    historyContent.append(line).append("\n");
                }
                reader.close();
                JTextArea historyTextArea = new JTextArea(historyContent.toString(), 20, 30);
                historyTextArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(historyTextArea);
                JOptionPane.showMessageDialog(frame, scrollPane, "Calculation History", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error reading history file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        


                // Add the "Clear History" button in the historyPanel
        JButton clearHistoryButton = new JButton("Clear History");
        historyPanel.add(clearHistoryButton);
            
        // Clear history functionality
        clearHistoryButton.addActionListener(e -> {
            try {
                File historyFile = new File("history.txt");
                if (!historyFile.exists()) {
                    JOptionPane.showMessageDialog(frame, "No history available to clear.", "History", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            
                // Clear the content of the history file
                BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile));
                writer.write("");  // Writing an empty string to clear the file
                writer.close();
            
                JOptionPane.showMessageDialog(frame, "History cleared successfully.", "History", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error clearing history file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        JButton cashClearButton = new JButton("c");
        historyPanel.add(cashClearButton);
        cashClearButton.addActionListener(e->{
            lastResult = "0";
        });



        // Advanced options panel
        JPanel advancedPanel = new JPanel();
        advancedPanel.setLayout(new GridBagLayout());
        advancedPanel.setPreferredSize(new Dimension(500, 200));

        String[] advancedButtons = {"log", "lnx", "sin", "cos", "tan", "x^2", "π", "%", "sin⁻¹", "cos⁻¹", "tan⁻¹", "x^3",
                "√", "Cbr", "Rund", "Bin", "Oct", "Hex"};

        for (int i = 0; i < advancedButtons.length; i++) {
            JButton button = new JButton(advancedButtons[i]);
            styleAdvancedButton(button);

            // Set GridBag constraints for advanced buttons
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = i % 6; // Columns
            gbc.gridy = i / 6; // Rows
            advancedPanel.add(button, gbc);
            addButtonListener(button, display);
        }

        // Basic panel
        JPanel basicPanel = new JPanel();
        basicPanel.setLayout(new GridBagLayout());
        basicPanel.setPreferredSize(new Dimension(500, 300));

        String[][] basicButtons = {
                {"7", "8", "9", "DEL", "AC"},
                {"4", "5", "6", "×", "÷"},
                {"1", "2", "3", "+", "-"},
                {"0", ".", "x10^x", "Ans", "="}
        };

        for (int row = 0; row < basicButtons.length; row++) {
            for (int col = 0; col < basicButtons[row].length; col++) {
                String label = basicButtons[row][col];
                JButton button = new JButton(label);

                // Style buttons
                if (label.equals("AC") || label.equals("DEL")) {
                    styleACDelButton(button);
                } else if (label.equals("=")) {
                    button.setBackground(new Color(255, 165, 0));
                    button.setForeground(Color.BLACK);
                } else {
                    styleBasicButton(button);
                }

                // Add button to panel with constraints
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = col;
                gbc.gridy = row;
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                basicPanel.add(button, gbc);
                addButtonListener(button, display);
            }
        }

        // Add panels to main panel
        mainPanel.add(historyPanel, BorderLayout.NORTH);
        mainPanel.add(advancedPanel, BorderLayout.CENTER);
        mainPanel.add(basicPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // Final setup
        frame.pack();
        frame.setVisible(true);
    }

    // Style AC and DEL buttons
    private static void styleACDelButton(JButton button) {
        if (button.getText().equals("AC")) {
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);
        } else if (button.getText().equals("DEL")) {
            button.setBackground(Color.ORANGE);
            button.setForeground(Color.BLACK);
        }
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(80, 60));
    }

    // Style advanced buttons
    private static void styleAdvancedButton(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(80, 60));
    }

    // Style basic buttons
    private static void styleBasicButton(JButton button) {
        if (button.getText().matches("[0-9]")) {
            button.setBackground(new Color(0, 128, 128));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
        }
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(80, 60));
    }

    // Add functionality to buttons
    private static void addButtonListener(JButton button, JTextField display) {
        button.addActionListener(e -> {
            String command = button.getText();
            try {
                String input = display.getText();
                String operation = null; // To track the operation
                String result = null; // To track the result
    
                switch (command) {
                    case "AC" -> {
                        // lastResult = "0";
                        display.setText("");
                        
                    }
                    case "DEL" -> {
                        if (!input.isEmpty()) {
                            display.setText(input.substring(0, input.length() - 1));
                        }
                    }
                    case "=" -> {
                        result = evaluateExpression(input);  // Perform the calculation
                        operation = input + " = " + result;
                        lastResult = result;  // Save the result to lastResult
                        display.setText(result);  // Display the result
                    }
                    
                    case "log" -> {
                        result = String.valueOf(Math.log10(Double.parseDouble(input)));
                        operation = "log(" + input + ") = " + result;
                    }
                    case "lnx" -> {
                        result = String.valueOf(Math.log(Double.parseDouble(input)));
                        operation = "ln(" + input + ") = " + result;
                    }
                    case "sin" -> {
                        result = String.valueOf(Math.sin(Math.toRadians(Double.parseDouble(input))));
                        operation = "sin(" + input + ") = " + result;
                    }
                    case "cos" -> {
                        result = String.valueOf(Math.cos(Math.toRadians(Double.parseDouble(input))));
                        operation = "cos(" + input + ") = " + result;
                    }
                    case "tan" -> {
                        result = String.valueOf(Math.tan(Math.toRadians(Double.parseDouble(input))));
                        operation = "tan(" + input + ") = " + result;
                    }
                    case "sin⁻¹" -> {
                        double value = Double.parseDouble(input);
                        if (value < -1 || value > 1) {
                            display.setText("Invalid Input");
                            return;
                        }
                        result = String.valueOf(Math.toDegrees(Math.asin(value)));
                        operation = "sin^-1(" + input + ") = " + result;
                    }
                    case "cos⁻¹" -> {
                        double value = Double.parseDouble(input);
                        if (value < -1 || value > 1) {
                            display.setText("Invalid Input");
                            return;
                        }
                        result = String.valueOf(Math.toDegrees(Math.acos(value)));
                        operation = "cos^-1(" + input + ") = " + result;
                    }
                    case "tan⁻¹" -> {
                        result = String.valueOf(Math.toDegrees(Math.atan(Double.parseDouble(input))));
                        operation = "tan^-1(" + input + ") = " + result;
                    }
                    case "x^2" -> {
                        result = String.valueOf(Math.pow(Double.parseDouble(input), 2));
                        operation = input + "^2 = " + result;
                    }
                    case "x^3" -> {
                        result = String.valueOf(Math.pow(Double.parseDouble(input), 3));
                        operation = input + "^3 = " + result;
                    }
                    case "√" -> {
                        if (Double.parseDouble(input) < 0) {
                            display.setText("Invalid Input");
                            return;
                        }
                        result = String.valueOf(Math.sqrt(Double.parseDouble(input)));
                        operation = "√(" + input + ") = " + result;
                    }
                    case "%" -> {
                        result = String.valueOf(Double.parseDouble(input) / 100);
                        operation = input + "% = " + result;
                    }
                    case "π" -> {
                        display.setText(input + Math.PI);
                        return;
                    }
                    case "Bin" -> {
                        result = Integer.toBinaryString((int) Double.parseDouble(input));
                        operation = "Bin(" + input + ") = " + result;
                    }
                    case "Oct" -> {
                        result = Integer.toOctalString((int) Double.parseDouble(input));
                        operation = "Oct(" + input + ") = " + result;
                    }
                    case "Hex" -> {
                        result = Integer.toHexString((int) Double.parseDouble(input));
                        operation = "Hex(" + input + ") = " + result;
                    }

                    case "x10^x" -> {
                        result = String.valueOf(Math.pow(10, Double.parseDouble(input)));
                        operation = "10^" + input + " = " + result;
                        lastResult = result; // Save result for Ans
                        display.setText(result); // Show the result
                    }
                    
                    case "Ans" -> {
                        display.setText(input.isEmpty() ? lastResult : input + lastResult);
                    }
                    
                    

                    default -> {
                        display.setText(input + command);
                        return; // No calculation or history update for these buttons
                    }
                }
    
                // Save the operation to history if applicable
                if (operation != null) {
                    if (isHistoryOn) saveToHistory(operation);
                    display.setText(result);
                }
            } catch (Exception ex) {
                display.setText("Error");
            }
        });
    }
    
    

    // Save calculation to history file
    private static void saveToHistory(String calculation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("history.txt", true))) {
            writer.write(calculation + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Method to evaluate the expression
    // Add this to your code

private static String evaluateExpression(String expression) {
    try {
        // Replace '×' and '÷' with '*' and '/' for proper evaluation
        expression = expression.replace("×", "*").replace("÷", "/");
        
        // Evaluate the expression and return the result
        return String.valueOf(eval(expression));
    } catch (Exception e) {
        return "Error";
    }
}

// Updated eval method to correctly parse and evaluate expressions
    private static double eval(final String str) {
        return new Object() {
            int pos = -1, c;

            void nextChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (c == ' ') nextChar();
                if (c == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) c);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // Addition
                    else if (eat('-')) x -= parseTerm(); // Subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // Multiplication
                    else if (eat('/')) x /= parseFactor(); // Division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // Unary plus
                if (eat('-')) return -parseFactor(); // Unary minus

                double x;
                int startPos = pos;
                if (eat('(')) { // Parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((c >= '0' && c <= '9') || c == '.') { // Number
                    StringBuilder sb = new StringBuilder();
                    while ((c >= '0' && c <= '9') || c == '.') {
                        sb.append((char) c);
                        nextChar();
                    }
                    x = Double.parseDouble(sb.toString());
                } else {
                    throw new RuntimeException("Unexpected: " + (char) c);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // Exponentiation
                return x;
            }
        }.parse();
    }
}

