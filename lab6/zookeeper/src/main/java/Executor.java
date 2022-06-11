import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import utils.TreeNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Executor implements Watcher {
    private final String zNode;
    private ZooKeeper zooKeeper;

    public Executor(String IPAndPort, String zNode, String[] exec)
    {
        this.zNode = zNode;

        try
        {
            this.zooKeeper = new ZooKeeper(IPAndPort, 5000, this);
            DataMonitor dataMonitor = new DataMonitor(zNode, zooKeeper, exec);
            dataMonitor.startWatch();
        }
        catch(IOException e)
        { e.printStackTrace(); }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
    }

    private void listChildren() {
        try
        {
            TreeNode node = createTreeNode(zNode, zNode);

            System.out.println("zNode " + zNode + " ancestors tree:");
            System.out.println(node);
        }
        catch (InterruptedException e)
        { e.printStackTrace(); }
        catch (KeeperException e)
        { System.out.println("zNode does not exist!"); }
    }

    private TreeNode createTreeNode(String base, String name) throws InterruptedException, KeeperException {
        List<String> children = this.zooKeeper.getChildren(base, false);

        if (children.size() == 0) {
            return new TreeNode(name, Collections.emptyList());
        }

        List<TreeNode> treeNodes = new LinkedList<>();
        for (String child : children)
        {
            String path = base + "/" + child;
            treeNodes.add(createTreeNode(path, child));
        }
        return new TreeNode(name, treeNodes);
    }

    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.err.println("INVALID USAGE: <IP>:<PORT> <path_to_program>");
            System.exit(1);
        }

        String IPAndPort = args[0];
        String[] exec = new String[args.length - 1];
        String zNode = "/z";
        System.arraycopy(args, 1, exec, 0, exec.length);

        Executor executor = new Executor(IPAndPort, zNode, exec);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("EXECUTOR READY");
        System.out.println("Write 'tree' to print current znode tree");
        while (true)
        {
            try
            {
                String line = br.readLine();
                if (line.equals("tree"))
                    executor.listChildren();
                else
                    System.out.println("ERROR: unknown command");
            }
            catch (IOException e)
            { e.printStackTrace(); }
        }
    }
}
