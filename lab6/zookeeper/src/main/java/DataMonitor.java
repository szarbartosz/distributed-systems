import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class DataMonitor implements AsyncCallback.StatCallback, AsyncCallback.AllChildrenNumberCallback {
    private final ZooKeeper zooKeeper;
    private int lastCount;
    private final String zNode;
    private final String[] exec;
    private Process process = null;

    DataMonitor(String zNode, ZooKeeper zooKeeper, String[] exec)
    {
        this.zooKeeper = zooKeeper;
        this.zNode = zNode;
        this.exec = exec;
    }

    public void startWatch()
    {
        zooKeeper.exists(zNode, true, this, null);
        zooKeeper.getAllChildrenNumber(zNode, this, null);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat)
    {
        if (rc == KeeperException.Code.OK.intValue())
        {
            if (process == null)
            {
                System.out.println("Added zNode, starting program.");
                zooKeeper.getAllChildrenNumber(zNode, this, null);

                ProcessBuilder pb = new ProcessBuilder();
                pb.command(exec);

                try
                { this.process = pb.start(); }
                catch (IOException e)
                {e.printStackTrace(); }
            }
        }
        else if (rc == KeeperException.Code.NONODE.intValue())
        {
            if (this.process != null)
            {
                System.out.println("Deleted zNode, stopping program.");
                this.process.destroy();
                this.process = null;
            }
        }
        else
            System.err.println("EXCEPTION: " + rc);

        zooKeeper.exists(zNode, true, this, null);
    }

    @Override
    public void processResult(int i, String s, Object o, int i1) {
        if (lastCount != i1) {
            lastCount = i1;
            System.out.println("zNode tree change detected - current number of ancestors: " + i1);
        }

        zooKeeper.getAllChildrenNumber(zNode, this, null);
    }
}
