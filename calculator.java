package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by cuichengrui on 17/06/2018.
 * 简单计算器 （涉及中缀表达式与前缀、后缀表达式之间的转换）
 */
public class calculator {

    private static int priority(char op) throws Exception {// 定义优先级
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '(':
            case '#':
                return 0;
        }
        throw new Exception("Illegal operator");
    }

    private static boolean isOperator(String str) {
        return str.equals("+") || str.equals("-") || str.equals("*")
                || str.equals("/");
    }

    private static double doOperate(double n1, double n2, String operator) {
        if (operator.equals("+"))
            return n1 + n2;
        else if (operator.equals("-"))
            return n1 - n2;
        else if (operator.equals("*"))
            return n1 * n2;
        else
            return n1 / n2;
    }

    /**
     * 将中缀表达式转化为后缀表达式
     */
    public static List toReversePolishNotation(String expression) throws Exception {
        expression = expression + "#";
        //存放转化后的后缀表达式的链表
        List postExpression = new ArrayList();
        //保存一个数
        StringBuffer numBuffer = new StringBuffer();
        Stack opStack = new Stack();
        char ch;
        String preChar;
        opStack.push("#");
        try {
            for (int i = 0; i < expression.length(); ) {
                ch = expression.charAt(i);
                switch (ch) {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        preChar = opStack.peek().toString();
                        //如果栈里面的操作符优先级比当前的大，则把栈中优先级大的都添加到后缀表达式列表中
                        while (priority(preChar.charAt(0)) > priority(ch)) {
                            postExpression.add(preChar);
                            opStack.pop();
                            preChar = opStack.peek().toString();
                        }
                        opStack.push("" + ch);
                        i++;
                        break;
                    case '(':
                        // 左括号直接压栈
                        opStack.push("" + ch);
                        i++;
                        break;
                    case ')':
                        // 右括号则把直接把栈中左括号前面的弹出，并加入后缀表达式链表中
                        String c = opStack.pop().toString();
                        while (c.charAt(0) != '(') {
                            postExpression.add(c + "");
                            c = opStack.pop().toString();
                        }
                        i++;
                        break;
                    case '#':
                        // #号，代表表达式结束，可以直接把操作符栈中剩余的操作符全部弹出，并加入后缀表达式链表中
                        String c1;
                        while (!opStack.empty()) {
                            c1 = opStack.pop().toString();
                            if (c1.charAt(0) != '#')
                                postExpression.add("" + c1);
                        }
                        i++;
                        break;
                    case ' ':
                    case '\t':
                        //过滤空白符
                        i++;
                        break;
                    default:
                        //数字凑成一个整数，加入后缀表达式链表中
                        if (Character.isDigit(ch) || ch == '.') {
                            while (Character.isDigit(ch) || ch == '.') {
                                numBuffer.append(ch);
                                ch = expression.charAt(++i);
                            }
                            postExpression.add(numBuffer.toString());
                            //numBuffer = new StringBuffer();
                            numBuffer.setLength(0);//效率最高
                            //numBuffer.delete(0, numBuffer.length());
                        } else {
                            throw new Exception("illegal operator");
                        }
                }
            }
        } catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
        return postExpression;
    }

    public static String calculate(List list) {
        Stack stack = new Stack();
        String element;
        double n1, n2, result;
        try {
            for (int i = 0; i < list.size(); i++) {
                element = list.get(i).toString();
                if (isOperator(element)) {
                    n1 = Double.parseDouble(stack.pop().toString());
                    n2 = Double.parseDouble(stack.pop().toString());
                    result = doOperate(n2, n1, element);
                    stack.push(result);
                } else {
                    stack.push(element);
                }
            }
            return stack.pop() + "";
        } catch (NumberFormatException e) {
            throw new RuntimeException("exception");
        }
    }

    /**
     * 检查表达式里正反括号是否匹配
     *
     * @param
     * @throws Exception
     */
    public static boolean checkExpression(String expression) {
        int top = 0;//栈顶指针
        for (int i = 0; i < expression.length(); i++) {
            int c = isBracket(expression.charAt(i));
            if (c == 0) {
                continue;
            } else if (c == 1) {
                top++;
            } else if (c == 2) {
                top--;
                if (top < 0) {
                    //此时说明反括号比正括号多
                    return false;
                }
            }
        }
        if (top == 0) {
            return true;
        } else {
            return false;
        }

    }

    public static int isBracket(char a) {
        if (a == '{' || a == '[' || a == '(') {
            return 1;
        } else if (a == '}' || a == ']' || a == ')') {
            return 2;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(toReversePolishNotation("16+2*(3/4)"));
        System.out.println(calculate(toReversePolishNotation("16+2*(3/4)")));
        System.out.println(checkExpression("[a+b*(5-4)]{x+b+b(({1+2)}}"));
    }

}
