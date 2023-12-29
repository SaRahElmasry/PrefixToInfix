package com.mycompany.newprefixtoinfix;


import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.Stack;

class XmlElements {
    private String value;
    private boolean isOperator;

    public XmlElements(String value, boolean isOperator) {
        this.value = value;
        this.isOperator = isOperator;
    }

    public boolean isOperator() {
        return isOperator;
    }

    public double getNumericValue() {
        return Double.parseDouble(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
public class NewPrefixToInfix {
    public static void main(String[] args) {
        try {
            // Read the XML File
            File xmlFile = new File("Path");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            Element root = document.getDocumentElement();

            // Process the XML data and store them in PrefixStack
            Stack<XmlElements> PrefixStack = processElement(root, true);
            
            // Access Elements of PrefixStack and store them in preExpression then print it
            String preExpression = GetpreExpression(PrefixStack);
            System.out.println("Prefix Equation: " + preExpression.toString());
            
            // Get Infix and store it in InfixStack as one String
            Stack<String> InfixStack = new Stack<>();
            GetInExpression(PrefixStack,InfixStack);
            
            // Get Infix and Store it in infixEquation then print it
            String infixEquation = InfixStack.pop();
            System.out.println("Infix Equation: " + infixEquation);
            
            // Get the Result
            evaluatePrefix(PrefixStack);
            
        } catch (Exception e) {
            System.out.println("This XML file is not valid");
        }
    }

    private static Stack<XmlElements> processElement(Element element, boolean isRoot) {
        Stack<XmlElements> PrefixStack = new Stack<>();

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                String childTagName = childElement.getTagName();
                if (childTagName.equals("operator") || childTagName.equals("atom")) {
                    String value = childElement.getAttribute("value");
                    PrefixStack.push(new XmlElements(value, childTagName.equals("operator")));
                } else {
                    PrefixStack.addAll(processElement(childElement, false));
                }
            }
        }
        
        return PrefixStack;
    }
    
    public static String GetpreExpression(Stack<XmlElements> PrefixStack){
        StringBuilder preExpression = new StringBuilder();
        
        for (XmlElements element : PrefixStack) {
            preExpression.append(element);
        }
        
        String prefix = preExpression.toString();
        
        return prefix;
    }
    
    private static void GetInExpression(Stack<XmlElements> PrefixStack, Stack<String> InfixStack) {
        
        Stack<XmlElements> copyStack = new Stack<>();
        for (XmlElements p : PrefixStack) {
            copyStack.push(p);
        }
        
        while (!copyStack.isEmpty()) {
            XmlElements el = copyStack.pop();
            if (el.isOperator()) {
                String operand1 = InfixStack.pop();
                String operand2 = InfixStack.pop();
                String infixExpression = "(" + operand1 + el + operand2 + ")";
                InfixStack.push(infixExpression);
            } else {
                InfixStack.push(el.toString());
            }
        }
    }

    private static void evaluatePrefix(Stack<XmlElements> PrefixStack) {
        Stack<Double> stack = new Stack<>();
        while (!PrefixStack.isEmpty()) {
            XmlElements el = PrefixStack.pop();
            if (el.isOperator()) {
                double operand1 = stack.pop();
                double operand2 = stack.pop();
                double result = applyOperator(el.toString(), operand1, operand2);
                stack.push(result);
            } else {
                stack.push(el.getNumericValue());
            }
        }

        double result = stack.pop();
        System.out.println("Result: " + result);
    }

    private static double applyOperator(String operator, double operand1, double operand2) {
        double num1 = operand1;
        double num2 = operand2;

        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}