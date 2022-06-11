package utils;

import java.util.Iterator;
import java.util.List;

public record TreeNode(String name, List<TreeNode> children) {

    public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(name);
        buffer.append('\n');
        for (Iterator<TreeNode> it = children.iterator(); it.hasNext(); ) {
            TreeNode next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "|---", childrenPrefix + "|   ");
            } else {
                next.print(buffer, childrenPrefix + "|---", childrenPrefix + "    ");
            }
        }
    }
}
