import java.util.*;

public class BT_02_DynamicBinaryTreeCreation {
    public static void main(String[] args) {

        BinaryTree binaryTree = new BinaryTree();
        Scanner sc = new Scanner(System.in);

        int choice = 0;
        while(choice < 3)
        {
            System.out.println("""
                Choice 1 to add node
                Choice 2 to see tree node in Level order
                Choice 3 to Exit
                    """);            
            System.out.print("Give your Choice :");
            choice = sc.nextInt();
 
            switch (choice) {
                case 1:
                    System.out.println("Enter node value");
                    binaryTree.addNode(sc.nextInt());
                    break;
                case 2:
                    System.out.println("Binary Tree :");
                    binaryTree.printNodes();
                    break;

                default :
                    choice = 3;

            }
                
        }

        sc.close();
        


    }
}

class TreeNode {
    int value;
    TreeNode left, right;

    TreeNode(int value){
        this.value = value;
        left = null;
        right = null;
    }
}

class BinaryTree {
    TreeNode rootNode;

    BinaryTree(){
        rootNode = null;
    }

    public void addNode(int value){
        TreeNode newNode = new TreeNode(value);

        if(rootNode == null){
            rootNode = newNode;
            return;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(rootNode);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();

            if(current.left == null){
                current.left = newNode;
                return;
            } else {
                queue.offer(current.left);
            }

            if(current.right == null){
                current.right = newNode;
                return;
            } else {
                queue.offer(current.right);
            }
        }
    }

    public void printNodes(){
        if(rootNode == null){
            System.out.println("[]");
            return;
        }

        //Level order Traversal
        List<List<Integer>> answer = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> levelNodes = new ArrayList<>();
            for(int i = 0; i < size; i++){
                TreeNode curr = queue.poll(); //retrieve and remove
                levelNodes.add(curr.value);

                if(curr.left != null) queue.add(curr.left);
                if(curr.right != null) queue.add(curr.right);
            }
            answer.add(levelNodes);
        }

        System.out.print("[ ");
        for(int i = 0 ; i < answer.size(); i++){
            System.out.print("[ ");
            for(int j = 0 ; j < answer.get(i).size(); j++){
                System.out.print(answer.get(i).get(j) + " ");
            }
            System.out.print("] ");
        }
        System.out.print("]");

    }
}
