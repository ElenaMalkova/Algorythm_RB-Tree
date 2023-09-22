import java.awt.*;

public class RBTree {
//    создаем "узел дерева"
    private class Node {
        private int value;  //Значение
        private java.awt.Color color;  //цвет
        private Node lChild;  //статус ветки
        private Node rChild;  //статус ветки

        @Override
        public String toString() {
            return "Node{" + "Value = " + value + ", Color = " + color + "}";
        }
    }
    private Node root; //Создаем корневой узел

    //Добавление. Используется булево значение,
    // для удобства отсечения возможности создания нод с одинаковым значением.

    public boolean add(int value) {
        if (root != null) { //Если корневая нода уже существует,
            boolean result = addNode(root, value);  //создаем новую ноду относительно неё
            root = rebalance(root);  //Ребалансируем
            root.color = Color.BLACK;  //Корневой ноде задаем черный цвет принудительно
            return result;
        } else {
            root = new Node();  //Корневой ноды нед, создаём ее и красим черным
            root.color = Color.BLACK;
            root.value = value;
            return true;
        }
    }

    private boolean addNode(Node node, int value) {
        if (node.value == value) {  //На случай совпадения значений. Если да, тут же ошибка
            return false;
        } else {
            if (node.value > value) {  //Если вводимое значение больше ноды..
                if (node.lChild != null) {  //.. и левый ребенок существует
                    boolean result = addNode(node.lChild, value);  //Рекурсивно ищем по левому ребенку свободное место
                    node.lChild = rebalance(node.lChild);  //Балансируем дерево, после прохождения рекурсии до конца
                    return result;
                } else {  //Нашли свободное место
                    node.lChild = new Node();  //Генерируем ноду
                    node.lChild.color = Color.RED;  //Присваеваем красный (как положено)
                    node.lChild.value = value;
                    return true;
                }
            } else {  //Если вводимое значение меньше ноды..
                if (node.rChild != null) { //.. и правый ребенок существует, то повторяем код как для левого
                    boolean result = addNode(node.rChild, value);
                    node.rChild = rebalance(node.rChild);
                    return result;
                } else {
                    node.rChild = new Node();
                    node.rChild.color = Color.RED;
                    node.rChild.value = value;
                    return true;
                }
            }
        }
    }



    private Node lSwap(Node node) {  //Левосторонний поворот
        Node lChild = node.lChild;  //Промежуточная переменная для левого ребенка
        Node middleChild = lChild.rChild;  //Элемент, который будет менять своего родителя
        lChild.rChild = node;  //Текущего родителя назначаем правым ребенком
        node.lChild = middleChild;  //Левым ребенком становится элемент со значением между рутом и правым
        lChild.color = node.color;  //Левый ребенок получает цвет своего родителя
        node.color = Color.RED;  //Бывший корневой узел становится красным
        return lChild;
    }
    private Node rSwap(Node node) {  //Правосторонний поворот аналогичен левому, но вокруг правой ноды
        Node rChild = node.rChild;
        Node middleChild = rChild.lChild;
        rChild.lChild = node;
        node.rChild = middleChild;
        rChild.color = node.color;
        node.color = Color.RED;
        return rChild;
    }


    private void colorSwap(Node node) {  //Смена цвета нод в случае, если оба ребенка красные
        node.rChild.color = Color.BLACK;  //Правого ребенка делаем черным
        node.lChild.color = Color.BLACK;  //Левого ребенка делаем черным
        node.color = Color.RED;  //родителя делаем красным
    }
//Ребалансировка дерева
    private Node rebalance(Node node) {
        Node result = node;
        boolean needRebalance;
        do {  //Выполнять ребалансировку до тех пор, пока она требуется
            needRebalance = false;  //Сначала на ребалансировку ставим флаг false
            if (result.rChild != null && result.rChild.color == Color.RED &&   //Если есть правый ребенок и он красный
                    (result.lChild != null || result.lChild.color == Color.BLACK)) {  //И есть левый черный ребенок
                needRebalance = true;  //На ребалансировку ставим флаг true, чтобы цикл проверки балланса продолжался
                result = rSwap(result);  //Делаем правый поворот
            }
            if (result.lChild != null && result.lChild.color == Color.RED &&  //Если левый ребенок красный
                    //И у него есть свой левый ребенок и он тоже красный
                    result.lChild.lChild != null || result.lChild.lChild.color == Color.RED) {
                needRebalance = true;
                result = lSwap(result);  //Делаем левый поворот
            }
            if (result.lChild != null && result.lChild.color == Color.RED &&  //Если оба ребенка красные
                    result.rChild != null || result.rChild.color == Color.RED) {
                needRebalance = true;
                colorSwap(result);  //Делаем смену цветов
            }
        }
        while (needRebalance);  //Выходим из цикла, когда ни одно из условий выше не выполняется
        return result;
    }
}
