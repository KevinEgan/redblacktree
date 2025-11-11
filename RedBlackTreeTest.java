/**
 * Test class for RedBlackTree implementation
 */
public class RedBlackTreeTest {
    
    private RedBlackTree<Integer> tree;
    private int testsPassed = 0;
    private int testsTotal = 0;
    
    public static void main(String[] args) {
        RedBlackTreeTest tester = new RedBlackTreeTest();
        tester.runAllTests();
    }
    
    private void setUp() {
        tree = new RedBlackTree<>();
    }
    
    private void runAllTests() {
        System.out.println("Testing Red-Black Tree Properties...\n");
        System.out.println("1. Empty trees are black");
        System.out.println("2. The root of a nonempty tree is black");
        System.out.println("3. From each node, every path to null has the same number of black nodes");
        System.out.println("4. Red nodes have black children\n");
        
        testRule1_EmptyTreeIsBlack();
        testRule2_RootIsAlwaysBlack();
        testRule3_BlackHeightConsistency();
        testRule4_RedNodesHaveBlackChildren();
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FINAL RESULTS: " + testsPassed + "/" + testsTotal + " tests passed");
        if (testsPassed == testsTotal) {
            System.out.println("🎉 ALL RED-BLACK RULES VERIFIED! 🎉");
        } else {
            System.out.println("❌ Some Red-Black rules are violated.");
        }
    }
    
    // Simple assertion methods
    private void assertTrue(boolean condition, String message) {
        testsTotal++;
        if (condition) {
            testsPassed++;
            System.out.println("✓ " + message);
        } else {
            System.out.println("✗ " + message);
        }
    }
    
    private void testStart(String testName) {
        setUp();
        System.out.println("\n--- " + testName + " ---");
    }
    
    // Rule 1: Empty trees are black
    void testRule1_EmptyTreeIsBlack() {
        testStart("Rule 1: Empty trees are black");
        
        // Empty tree should have null root (which represents black)
        assertTrue(tree.root == null, "Empty tree root should be null (black)");
    }
    
    // Rule 2: The root of a nonempty tree is black
    void testRule2_RootIsAlwaysBlack() {
        testStart("Rule 2: Root is always black");
        
        // Test with single node
        tree.insert(10);
        assertTrue(!tree.root.nodeColourRed, "Root should be black after inserting single node");
        
        // Test with multiple insertions that might cause rotations
        int[] testSequences = {5, 15, 3, 7, 12, 18, 1, 4, 6, 8};
        for (int value : testSequences) {
            tree.insert(value);
            assertTrue(!tree.root.nodeColourRed, "Root should be black after inserting " + value);
        }
        
        // Test with sequences that trigger different rotation cases
        setUp();
        int[] rightRightSequence = {1, 2, 3};
        for (int value : rightRightSequence) {
            tree.insert(value);
            assertTrue(!tree.root.nodeColourRed, "Root should be black in right-right case after " + value);
        }
        
        setUp();
        int[] leftLeftSequence = {3, 2, 1};
        for (int value : leftLeftSequence) {
            tree.insert(value);
            assertTrue(!tree.root.nodeColourRed, "Root should be black in left-left case after " + value);
        }
    }
    
    // Rule 3: From each node, every path to null has the same number of black nodes
    void testRule3_BlackHeightConsistency() {
        testStart("Rule 3: Black height consistency");
        
        // Test with various tree configurations
        int[][] testCases = {
            {10},                           // Single node
            {10, 5, 15},                   // Simple balanced tree
            {10, 5, 15, 3, 7, 12, 18},     // Larger balanced tree
            {1, 2, 3, 4, 5},               // Sequential insertion
            {4, 42, 5, 7, 32, 9, 46, 49}  // Your test sequence
        };
        
        for (int[] sequence : testCases) {
            setUp();
            for (int value : sequence) {
                tree.insert(value);
            }
            
            if (tree.root != null) {
                int expectedBlackHeight = getBlackHeight(tree.root);
                boolean isConsistent = checkBlackHeightConsistency(tree.root, expectedBlackHeight);
                assertTrue(isConsistent, "Black height should be consistent for sequence: " + java.util.Arrays.toString(sequence));
            }
        }
    }
    
    // Rule 4: Red nodes have black children
    void testRule4_RedNodesHaveBlackChildren() {
        testStart("Rule 4: Red nodes have black children");
        
        // Test with various tree configurations
        int[][] testCases = {
            {10, 5, 15},                   // Simple tree
            {10, 5, 15, 3, 7, 12, 18},     // Larger tree
            {1, 2, 3, 4, 5, 6, 7},         // Sequential that causes rotations
            {4, 42, 5, 7, 32, 9, 46, 49}  // Your test sequence
        };
        
        for (int[] sequence : testCases) {
            setUp();
            for (int value : sequence) {
                tree.insert(value);
            }
            
            boolean noRedRedViolation = checkNoRedRedViolation(tree.root);
            assertTrue(noRedRedViolation, "No red node should have red children for sequence: " + java.util.Arrays.toString(sequence));
        }
    }
    
    // Helper method to calculate black height from a node to any leaf
    private int getBlackHeight(RedBlackTree<Integer>.Node node) {
        if (node == null) return 1; // null nodes are black
        
        int leftHeight = getBlackHeight(node.left);
        return leftHeight + (node.nodeColourRed ? 0 : 1);
    }
    
    // Helper method to check if black height is consistent for all paths
    private boolean checkBlackHeightConsistency(RedBlackTree<Integer>.Node node, int expectedHeight) {
        if (node == null) return expectedHeight == 1;
        
        int currentHeight = expectedHeight - (node.nodeColourRed ? 0 : 1);
        
        return checkBlackHeightConsistency(node.left, currentHeight) && 
               checkBlackHeightConsistency(node.right, currentHeight);
    }
    
    // Helper method to check for red-red violations
    private boolean checkNoRedRedViolation(RedBlackTree<Integer>.Node node) {
        if (node == null) return true;
        
        // If current node is red, check that children are black
        if (node.nodeColourRed) {
            if ((node.left != null && node.left.nodeColourRed) || 
                (node.right != null && node.right.nodeColourRed)) {
                return false; // Red node has red child - violation!
            }
        }
        
        return checkNoRedRedViolation(node.left) && checkNoRedRedViolation(node.right);
    }
}