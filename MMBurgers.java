
import java.util.*;

public class MMBurgers implements MMBurgersInterface {

    int TIMER, K, M, occupied, onWait;
    int TOTAL_CUSTOMERS, TOTAL_WAIT_TIME;
    Queue<cookingQueue> qu1 = new LinkedList<>();
    Queue<griddleQueue> qu2 = new LinkedList<>();
    Queue<deliveryQueue> qu3 = new LinkedList<>();
    BillingHeap bh = new BillingHeap();
    CustomerTimeHeap th = new CustomerTimeHeap();
    CustomerAvl ct = new CustomerAvl();


    public boolean isEmpty(){
        if(qu3.isEmpty() && qu2.isEmpty() && qu1.isEmpty() && th.isEmpty())
        {
            // System.out.println(true);
            return true;
        }
        else
        {
            // System.out.println(false);
            return false;
        }
    } 
    
    public void setK(int k) throws IllegalNumberException{
        this.K=k;
        for(int i=1;i<=K;i++) // billing heap initialized
        {
            bh.insert(i, 0);
        }
    }   
    
    public void setM(int m) throws IllegalNumberException{
        this.M=m;
    } 

    public void advanceTime(int t) throws IllegalNumberException{
        // 1. Billing specialist prints an order and sends it to the chef; customer leaves the queue.
        // 2. A cooked patty is removed from the griddle.
        // 3. The chef puts another patty on the griddle.
        // 4. A newly arrived customer joins a queue.
        // 5. Cooked burgers are delivered to customers.
        if(t < TIMER)
        {
            throw new IllegalNumberException("time cannot be lower than the time mentioned in the previous command");
        }
        
        // System.out.println("entering into advance time with timer = " + TIMER);
        while(true)
        {
            if(!th.isEmpty())
            {
                // if(t==0)
                // {
                //     System.out.println("entered here in th");
                // }
                while(!th.isEmpty() && th.returnMin().time == TIMER)
                {
                    // System.out.println("called atleast for one time");
                    CustomerTimeHeap.Node temp = th.returnMin();
                    // System.out.println(temp.cid+" got removed : with exit time = "+ temp.time + " and wq = " + temp.qn);
                    // System.out.println(temp.cid + ", " + temp.time);
                    int tempcid = temp.cid;
                    int tempqn = temp.qn;
                    cookingQueue toAdd = new cookingQueue(tempcid, ct.getNode(tempcid).burgersAsked);
                    qu1.add(toAdd);
                    onWait+=toAdd.burgersAsked;
                    th.deleteMin();
                    bh.changeNode(tempqn, -1, TIMER);
                    // CustomerAvl.Node temp2 = ct.getNode(tempcid);
                    // System.out.println("checking : " + temp2.cid);
                    ct.getNode(tempcid).isWaitingInQueue=false;
                }
            }
            
            if(!qu2.isEmpty())
            {
                // if(t==0)
                // {
                //     System.out.println("entered here in qu2");
                // }
                while(!qu2.isEmpty() && qu2.peek().time == TIMER)
                {
                    griddleQueue temp = qu2.remove();
                    deliveryQueue temp2 = new deliveryQueue(temp.cid, TIMER+1, temp.nb);
                    qu3.add(temp2);
                    occupied-=temp.nb;
                }
            }

            if(!qu1.isEmpty())
            {
                // if(t==0)
                // {
                //     System.out.println("entered here in qu1");
                // }
                while(!qu1.isEmpty() && occupied < M)
                {
                    cookingQueue temp = qu1.peek();
                    // if(TIMER==11)
                    // {
                    //     System.out.println(temp.cid + "showed up");
                    // }
                    if(temp.burgersAsked <= M-occupied)
                    {
                        qu1.remove();
                        griddleQueue temp2 = new griddleQueue(temp.cid, TIMER+10, temp.burgersAsked);
                        occupied+=temp.burgersAsked;
                        onWait-=temp.burgersAsked;
                        qu2.add(temp2);
                    }
                    else
                    {
                        // if(temp.cid == 2)
                        // {
                        //     System.out.println("here for cid=2 : ");
                        // }
                        temp.burgersAsked-=(M-occupied);
                        griddleQueue temp2 = new griddleQueue(temp.cid, TIMER+10, M-occupied);
                        
                        // if(temp.cid == 2)
                        // {
                        //     System.out.println("befeoreor put : " + onWait);
                        // }
                        onWait-=(M-occupied);
                        occupied=M;
                        // if(temp.cid == 2)
                        // {
                        //     System.out.println("afte put : " + onWait);
                        // }
                        qu2.add(temp2);
                    }
                }
            }
            
            if(!qu3.isEmpty())
            {
                // if(t==0)
                // {
                //     System.out.println("entered here in qu3");
                // }
                while(!qu3.isEmpty() && qu3.peek().time == TIMER)
                {
                    deliveryQueue temp2 = qu3.remove();
                    // if(temp2.cid == 2)
                    // {
                    //     System.out.println("delelering to 2 : " + temp2.nb + " at : " + TIMER);
                    // }
                    // if(temp2.cid == 1)
                    // {
                    //     System.out.println("delelering to 1 : " + temp2.nb + " at " + TIMER);
                    // }
                    CustomerAvl.Node temp3 = ct.getNode(temp2.cid);
                    temp3.burgersCompleted+=temp2.nb;
                    if(temp3.burgersCompleted == temp3.burgersAsked)
                    {
                        // if(temp2.cid == 2)
                        // {
                        //     System.out.println("delelering to 2 finally : " + temp2.nb + " at " + TIMER);
                        // }
                        // if(temp2.cid == 1)
                        // {
                        //     System.out.println("delelering to 1 finally : " + temp2.nb + " at " + TIMER);
                        // }
                        temp3.exitTime=TIMER;
                        TOTAL_WAIT_TIME+=(temp3.exitTime - temp3.entryTime);
                    }
                }
            }
            if(TIMER == t)
            {
                break;
            }
            TIMER++;
        }
        
    } 

    public void arriveCustomer(int id, int t, int numb) throws IllegalNumberException{
        if(ct.find(id) || t < TIMER || numb <= 0)
        {
            throw new IllegalNumberException("Customer id already exists || time is less than TIMER || numb <= 0");
        }
        //some commands have to be written
        // System.out.println("before"+id);
        advanceTime(t);
        // System.out.println("after"+id);
        TOTAL_CUSTOMERS++;
        BillingHeap.Node temp = bh.returnMin();
        // if(id == 2)
        // {
        //     System.out.println(temp.qn + "for twooo");
        // }
        int tempQ = temp.qn;
        // if(id == 3)
        // {
        //     System.out.println("billingcounter for id = 3 with tempstarter = "+ temp.timerStarter + " with nc = " + temp.nc);
        // }
        if(temp.nc == 0)
        {
            th.insert(temp.qn, t + (temp.nc+1)*temp.qn, id);
        }
        else
        {
            th.insert(temp.qn, temp.timerStarter + (temp.nc+1)*temp.qn, id);
        }
        // if(id == 1)
        // {
        //     th.show();
        // }
        bh.changeNode(temp.qn, 1, t);
        // if(id == 1)
        // {
        //     bh.show();
        // }
        ct.insert(id, t, numb, tempQ);

        //some commands have to be written
        // if(!qu3.isEmpty())
        // {
        //     while(qu3.peek().time == t)
        //     {
        //         deliveryQueue temp2 = qu3.remove();
        //         CustomerAvl.Node temp3 = ct.getNode(temp2.cid);
        //         temp3.burgersCompleted+=temp2.nb;
        //         if(temp3.burgersCompleted == temp3.burgersAsked)
        //         {
        //             temp3.exitTime=t;
        //             TOTAL_WAIT_TIME+=(temp3.entryTime - temp3.exitTime);
        //         }
        //     }
        // }
    } 

    public int customerState(int id, int t) throws IllegalNumberException{
        if(t < TIMER)
        {
            throw new IllegalNumberException("time cannot be lower than the time mentioned in the previous command");
        }
        // System.out.println("before advance time : ");;
        advanceTime(t);
        // System.out.println("after advance time : ");
        CustomerAvl.Node temp = ct.getNode(id);
        // if(id == 1 && t==5)
        // {
        //     System.out.println("from customer state : " + temp.cid + " , " + temp.entryTime + ", " + temp.wq + " , " + temp.isWaitingInQueue);
        // }
        if(temp == null)
        {
            // System.out.println(0);
            return 0;
        }
        else 
        {
            if(temp.isWaitingInQueue)
            {
                // System.out.println(temp.wq);
                return temp.wq;
            }
            else if(temp.burgersAsked == temp.burgersCompleted)
            {
                // System.out.println(K+2);
                return K+2;
            }
            else
            {
                // System.out.println(K+1);
                return K+1;
            }
        }
    } 

    public int griddleState(int t) throws IllegalNumberException{
        if(t < TIMER)
        {
            throw new IllegalNumberException("time cannot be lower than the time mentioned in the previous command");
        }
        advanceTime(t);
        return occupied;
    } 

    public int griddleWait(int t) throws IllegalNumberException{
        if(t < TIMER)
        {
            throw new IllegalNumberException("time cannot be lower than the time mentioned in the previous command");
        }
        advanceTime(t);
        return onWait;
    } 

    public int customerWaitTime(int id) throws IllegalNumberException{
        if(!ct.find(id))
        {
            throw new IllegalNumberException("No such Customer is found");
        }
        CustomerAvl.Node temp = ct.getNode(id);
        return temp.exitTime-temp.entryTime;
    } 

	public float avgWaitTime(){
        return (float)TOTAL_WAIT_TIME/TOTAL_CUSTOMERS;
    } 

    static class cookingQueue
    {
        int cid;
        int burgersAsked;
        cookingQueue(int cid, int burgersAsked)
        {
            this.cid=cid;
            this.burgersAsked=burgersAsked;
        }
    }

    static class griddleQueue
    {
        int cid;
        int time;
        int nb;
        griddleQueue(int cid, int time, int nb)
        {
            this.cid=cid;
            this.nb=nb;
            this.time=time;
        }
    }

    static class deliveryQueue
    {
        int cid;
        int time;
        int nb;
        deliveryQueue(int cid, int time, int nb)
        {
            this.cid=cid;
            this.time=time;
            this.nb=nb;
        }
    }
    
}




class BillingHeap
{
    Vector<Node> v = new Vector<>();
    int size;

    public void insert(int qn, int nc)
    {
        Node toAdd = new Node(qn, nc);
        v.add(size, toAdd);
        size++;
        percolateUp(size-1);
    }

    public void deleteMin()
    {
        changeInfo(v.elementAt(0), v.elementAt(size-1));
        size--;
        percolateDown(0);
    }

    public Node returnMin()
    {
        return v.elementAt(0);
    }

    public boolean isEmpty()
    {
        return (size == 0)?true:false;
    }

    public void changeNode(int id, int changeInNc, int t)
    {
        boolean isFound = false;
        int position=0;
        for(int i=0;i<size;i++)
        {
            if(v.elementAt(i).qn == id)
            {
                isFound=true;
                position=i;
            }
        }
        if(!isFound)
        {
            return;
        }
        if(v.elementAt(position).nc == 0 && changeInNc > 0)
        {
            v.elementAt(position).timerStarter = t;
        }
        v.elementAt(position).nc+=changeInNc;
        
        if(changeInNc > 0)
        {
            percolateDown(position);
        }
        else
        {
            percolateUp(position);
        }
    }

    public void percolateUp(int position)
    {
        while(position!=0)
        {
            Node a = v.elementAt(position);
            Node b = v.elementAt(position/2);
            if(shouldExc(a, b)) // here a is child and b is its parent
            {
                changeInfo(a, b);
                position/=2;
            }
            else
            {
                break;
            }
        }
    }

    public void percolateDown(int position)
    {
        int pos=position;
        while(true)
        {
            Node temp = v.elementAt(pos);
            int leftPos = pos*2+1;
            int rightPos = pos*2+2;
            if(leftPos >= size)
            {
                break;
            }
            else if(leftPos < size && rightPos < size)
            {
                Node leftNode = v.elementAt(leftPos);
                Node rightNode = v.elementAt(rightPos);
                Node bestNode = bestPrior(leftNode, rightNode);
                if(shouldExc(bestNode, temp))
                {
                    if(bestNode == leftNode)
                    {
                        //change with left
                        changeInfo(temp, bestNode);
                        pos=leftPos;
                    }
                    else
                    {
                        //change with right
                        changeInfo(temp, bestNode);
                        pos=rightPos;
                    }
                }
                else
                {
                    break;
                }
            }
            else
            {
                Node leftNode = v.elementAt(leftPos);
                if(shouldExc(leftNode, temp))
                {
                    //change with left
                    changeInfo(temp, leftNode);
                    pos=leftPos;
                }
                else
                {
                    break;
                }
            }
        }
    }

    public void changeInfo(Node a, Node b)
    {
        int tempqn = a.qn;
        int tempnc = a.nc;
        a.qn=b.qn;
        a.nc=b.nc;
        b.qn=tempqn;
        b.nc=tempnc;
    }

    public boolean shouldExc(Node a, Node b) // here a is temp and b is parent
    {
        if(a.nc < b.nc)
        {
            return true;
        }
        else if( a.nc == b.nc && a.qn < b.qn)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Node bestPrior(Node a, Node b)
    {
        if(a.nc < b.nc)
        {
            return a;
        }
        else if(b.nc < a.nc)
        {
            return b;
        }
        else
        {
            return (a.qn < b.qn)?a:b;
        }
    }

    public void show()
    {
        for(int i=0;i<size;i++)
        {
            System.out.println(v.elementAt(i).qn + ", "+ v.elementAt(i).nc);
        }
    }

    static class Node
    {
        int qn;
        int nc;
        int timerStarter;
        Node(int qn, int nc)
        {
            this.qn=qn;
            this.nc=nc;
            this.timerStarter=0;
        }
    }

}


class CustomerTimeHeap
{
    Vector<Node> v = new Vector<>();
    int size;

    public void insert(int qn, int time, int cid)
    {
        Node toAdd = new Node(qn, time, cid);
        v.add(size, toAdd);
        size++;
        percolateUp(size-1);
    }

    public void deleteMin()
    {
        changeInfo(v.elementAt(0), v.elementAt(size-1));
        size--;
        percolateDown(0);
    }

    public Node returnMin()
    {
        return v.elementAt(0);
    }

    public boolean isEmpty()
    {
        return (size == 0)?true:false;
    }

    public void percolateUp(int position)
    {
        while(position!=0)
        {
            Node a = v.elementAt(position);
            Node b = v.elementAt(position/2);
            if(shouldExc(a, b)) // here a is child and b is its parent
            {
                changeInfo(a, b);
                position/=2;
            }
            else
            {
                break;
            }
        }
    }

    public void percolateDown(int position)
    {
        int pos=position;
        while(true)
        {
            Node temp = v.elementAt(pos);
            int leftPos = pos*2+1;
            int rightPos = pos*2+2;
            if(leftPos >= size)
            {
                break;
            }
            else if(leftPos < size && rightPos < size)
            {
                Node leftNode = v.elementAt(leftPos);
                Node rightNode = v.elementAt(rightPos);
                Node bestNode = bestPrior(leftNode, rightNode);
                if(shouldExc(bestNode, temp))
                {
                    if(bestNode == leftNode)
                    {
                        //change with left
                        changeInfo(temp, bestNode);
                        pos=leftPos;
                    }
                    else
                    {
                        //change with right
                        changeInfo(temp, bestNode);
                        pos=rightPos;
                    }
                }
                else
                {
                    break;
                }
            }
            else
            {
                Node leftNode = v.elementAt(leftPos);
                if(shouldExc(leftNode, temp))
                {
                    //change with left
                    changeInfo(temp, leftNode);
                    pos=leftPos;
                }
                else
                {
                    break;
                }
            }
        }
    }

    public void changeInfo(Node a, Node b)
    {
        int tempqn = a.qn;
        int temptime = a.time;
        int tempcid = a.cid;
        a.qn=b.qn;
        a.time=b.time;
        a.cid=b.cid;
        b.qn=tempqn;
        b.time=temptime;
        b.cid=tempcid;
    }

    public boolean shouldExc(Node a, Node b) // here a is temp and b is parent
    {
        if(a.time < b.time)
        {
            return true;
        }
        else if( a.time == b.time && a.qn > b.qn)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Node bestPrior(Node a, Node b)
    {
        if(a.time < b.time)
        {
            return a;
        }
        else if(b.time < a.time)
        {
            return b;
        }
        else
        {
            return (a.qn > b.qn)?a:b;
        }
    }

    public void show()
    {
        for(int i=0;i<size;i++)
        {
            System.out.println("queue no: "+ v.elementAt(i).qn + ", time: "+ v.elementAt(i).time + ", cid: "+ v.elementAt(i).cid);
        }
    }

    static class Node
    {
        int time;
        int qn;
        int cid;
        Node(int qn, int time, int cid)
        {
            this.time=time;
            this.qn=qn;
            this.cid=cid;
        }
    }

}


class CustomerAvl
{
     Node root;
	 int size;
    
	public int size()
	{
		return size;
	}

	public Node insert(int cid, int t, int numb, int wq)
	{
	   Node toAdd = new Node(cid, t, numb, wq);
       if(root == null)
       {
		  root=toAdd;
		  size++;
          return toAdd;
	   }
	   
	   Node temp = root;
	  
	   while(temp.left!=null || temp.right!= null)
	   {
		   if(cid < temp.cid)
		   {
			   if(temp.left != null)
			   {
				   temp=temp.left;
			   }
			   else
			   {
				   break;
			   }
		   }
		   else if(cid > temp.cid)
		   {
			   if(temp.right != null)
			   {
				   temp=temp.right;
			   }
			   else
			   {
				   break;
			   }
		   }
	   }
	   if(cid < temp.cid)
	   {
		   temp.left=toAdd;
		   toAdd.parent=temp;
		   temp=temp.left;
		   size++;
	   }
	   else
	   {
		   temp.right=toAdd;
		   toAdd.parent=temp;
		   temp=temp.right;
		   size++;
	   }

		if(size <= 2)
		{
			updateHeight(temp.parent);
		}
		
		if(size >= 3)
		{
			updateHeight(temp.parent);
			while(true)
			{
				if(temp == root || temp.parent == root)
				{
					break;
				}
				Node x = temp;
				Node y = temp.parent;
				Node z = temp.parent.parent;
				if(temp.parent != root)
				{
					updateHeight(temp.parent.parent);
				}
				if(temp.parent != root && heightDiff(temp.parent.parent.left, temp.parent.parent.right) >= 2)
				{
					balance(x, y, z);
					break;
				}
				if(temp.parent.parent == root)
				{
					break;
				}
				temp=temp.parent;
			}
		}

		return toAdd;
	}

	public void balance(Node x, Node y, Node z)
	{
        if(linearLeft(x, y, z))
		{
			boolean isLeft = false;
				Node tempChildy = y.right; 
				Node tempParentz = z.parent;
				if(tempParentz!=null)
			{
				isLeft=(tempParentz.left == z)?true:false;
				if(isLeft)
				{
					tempParentz.left=y;
				}
				else
				{
					tempParentz.right=y;
				}
			}
				y.right = z;
				z.parent=y;
				z.left=tempChildy;
				if(tempChildy != null)
				{
					tempChildy.parent=z;
				}
				y.parent = tempParentz;
				if(z==root)
				{
					root=y;
				}
				updateHeight(z);
				updateHeight(x);
				updateHeight(y);
				updateHeight(y.parent);
		}
		else if(linearRight(x, y, z))
		{
			boolean isRight=false;
			Node tempChildy = y.left;
			Node tempParentz = z.parent;
			if(tempParentz!=null)
			{
				isRight=(tempParentz.right == z)?true:false;
				if(isRight)
				{
					tempParentz.right=y;
				}
				else
				{
					tempParentz.left=y;
				}
			}
			y.left = z;
			z.parent=y;
			z.right=tempChildy;
			if(tempChildy!=null)
			{
				tempChildy.parent=z;
			}
			y.parent = tempParentz;
			if(z==root)
				{
					root=y;
				}
				updateHeight(z);
				updateHeight(x);
				updateHeight(y);
				updateHeight(y.parent);
		}
		else if(bendLeft(x, y, z))
		{
			boolean isLeft=false;
			Node tempChildLeftX = x.left;
			Node tempChildRightX = x.right;
			Node tempParentz = z.parent;
			if(tempParentz!=null)
			{
				isLeft=(tempParentz.left == z)?true:false;
				if(isLeft)
				{
					tempParentz.left=x;
				}
				else
				{
					tempParentz.right=x;
				}
			}
			x.parent=tempParentz;
			x.left=y;
			y.parent=x;
			x.right=z;
			z.parent=x;
			y.right=tempChildLeftX;
			if(tempChildLeftX!=null)
			{
				tempChildLeftX.parent=y;
			}
			z.left=tempChildRightX;
			if(tempChildRightX!=null)
			{
				tempChildRightX.parent=z;
			}
			if(z==root)
			{
				root=x;
			}
			updateHeight(z);
			updateHeight(x);
			updateHeight(y);
			updateHeight(y.parent);
		}
		else
		{
			boolean isRight=false;
			Node tempChildLeftX = x.left;
			Node tempChildRightX = x.right;
			Node tempParentz = z.parent;
			if(tempParentz!=null)
			{
				isRight=(tempParentz.right == z)?true:false;
				if(isRight)
				{
					tempParentz.right=x;
				}
				else
				{
					tempParentz.left=x;
				}
			}
			x.parent=tempParentz;
			x.left=z;
			z.parent=x;
			x.right=y;
			y.parent=x;
			y.left=tempChildRightX;
			if(tempChildRightX!=null)
			{
				tempChildRightX.parent=y;
			}
			z.right=tempChildLeftX;
			if(tempChildLeftX!=null)
			{
				tempChildLeftX.parent=z;
			}
			if(z==root)
			{
				root=x;
			}
			updateHeight(z);
			updateHeight(x);
			updateHeight(y);
			updateHeight(y.parent);
		}
	}

	public Node delete(int cid)
	{
		if(!find((cid)))
		{
			return null;
		}
		if(root == null)   
		{
			return null;
		}
		Node temp=root;
		
		while(temp != null)
		{
			if(temp.cid == cid)
			{
				break;
			}
			else if(cid < temp.cid)
			{
				temp=temp.left;
			}
			else
			{
				temp=temp.right;
			}
		}
		Node tempParent=null;

		if(temp.left == null && temp.right == null)
		{
			tempParent = delNodeWithNoChildren(temp);
		}
		else if(temp.left == null || temp.right == null)
		{
			tempParent = delNodeWithOneChild(temp);
		}
		else if(temp.left !=null && temp.right != null)
		{
			tempParent = delNodeWithTwoChildren(temp);
		}

		size--;
		
		if(tempParent != null)
		{
			while(tempParent != null)
			{
				updateHeight(tempParent);
				if(heightDiff(tempParent.left, tempParent.right) >= 2)
				{
					Node z = tempParent;
					Node y=null;
					Node x=null;
					boolean isLeftTaller = (getHeight(tempParent.left) > getHeight(tempParent.right))?true:false;
					if(isLeftTaller)
					{
						y = tempParent.left;
						x = (getHeight(y.left) > getHeight(y.right))?y.left:y.right;
					}
					else
					{
						y = tempParent.right;
						x = (getHeight(y.left) > getHeight(y.right))?y.left:y.right;
					}
					balance(x, y, z);
				}
				tempParent=tempParent.parent;
			}
		}

		return temp;
		
	}

	public Node delNodeWithNoChildren(Node temp)
	{
		System.out.println("del node with no child");
		if(temp == root)
		{
			root=null;
			return null;
		}
		boolean isRight = (temp.parent.right == temp)?true:false;
		if(isRight)
		{
			temp.parent.right=null;
		}
		else
		{
			temp.parent.left=null;
		}
		Node tempP = temp.parent;
		temp=null;
		return tempP;
	}

	public Node delNodeWithOneChild(Node temp)
	{
		System.out.println("entered delwith one node");
		boolean isLeftChildNull = (temp.left == null)?true:false;
		if(temp == root)
		{
			if(isLeftChildNull)
			{
				root=temp.right;
				temp.right.parent=root;
			}
			else
			{
				root=temp.left;
				temp.left.parent=root;
			}
			return null;
		}
		boolean isRight = (temp.parent.right == temp)?true:false;
		if(isRight)
		{
			if(isLeftChildNull)
			{
				temp.parent.right=temp.right;
				temp.right.parent=temp.parent;
			}
			else
			{
				temp.parent.right=temp.left;
				temp.left.parent=temp.parent;
			}
		}
		else
		{
		   if(isLeftChildNull)
		   {
			   temp.parent.left=temp.right;
			   temp.right.parent=temp.parent;
		   }
		   else
		   {
			   temp.parent.left=temp.left;
			   temp.left.parent=temp.parent;
		   }
		}
		Node tempP = temp.parent;
		temp=null;
		return tempP;
	}

	public Node delNodeWithTwoChildren(Node temp)
	{
	   // System.out.println("entered into del two nodes");
	   Node temp2=temp;
	   temp2=temp2.right;
	   while(temp2.left != null)
	   {
		   temp2=temp2.left;
	   }
	   temp.cid=temp2.cid;
	   temp.burgersAsked=temp2.burgersAsked;
	   temp.burgersCompleted=temp2.burgersCompleted;
	   temp.entryTime=temp2.entryTime;
       temp.exitTime=temp2.exitTime;
       temp.wq=temp2.wq;
	   if(temp2.left == null && temp2.right == null)
	   {
		   return delNodeWithNoChildren(temp2);
	   }
	   else
	   {
		   return delNodeWithOneChild(temp2);
	   }
	}

	public void updateHeight(Node node)
	{
		if(node == null)
		{
			return;
		}
		if(node.left == null && node.right == null)
		{
			node.height = 0;
		}
		else if(node.left != null && node.right != null)
		{
			node.height = 1 + Math.max(node.left.height, node.right.height);
		}
		else if(node.left == null)
		{
			node.height = 1 + node.right.height;
		}
		else
		{
			node.height = 1 + node.left.height;
		}
	}

	public boolean linearLeft(Node x, Node y, Node z)
	{
	   if(z.cid > y.cid && y.cid > x.cid)
	   {
		   return true;
	   }
	   else
	   {
		   return false;
	   }
	}

	public boolean linearRight(Node x, Node y, Node z)
	{
	   if(z.cid < y.cid && y.cid < x.cid)
	   {
		   return true;
	   }
	   else
	   {
		   return false;
	   }
	}

	public boolean bendLeft(Node x, Node y, Node z)
	{
	   if(z.cid > x.cid && x.cid > y.cid)
	   {
		   return true;
	   }
	   else
	   {
		   return false;
	   }
	}

	public boolean bendRight(Node x, Node y, Node z)
	{
	   if(z.cid < x.cid && x.cid < y.cid)
	   {
		   return true;
	   }
	   else
	   {
		   return false;
	   }
	}

	public boolean find(int cid)
	{
	   Node temp = root;
	   while(temp != null)
	   {
		   if(temp.cid == cid)
		   {
			   return true;
		   }
		   else if(cid < temp.cid)
		   {
               temp=temp.left;
		   }
		   else
		   {
			   temp=temp.right;
		   }
	   }
	   return false;
    }
    
    public Node getNode(int cid)
    {
        Node temp = root;
        while(temp != null)
        {
            if(temp.cid == cid)
            {
                return temp;
            }
            else if(cid < temp.cid)
            {
                temp=temp.left;
            }
            else
            {
                temp=temp.right;
            }
        }
        return null;
    }

	public void show(Node root)
	{
		if(root == null)
		{
			return;
		}
        else if(root.left == null && root.right == null)
        {
        	System.out.println("cid: "+root.cid+" , height : "+ root.height +" , height diff: " + heightDiff(root.left, root.right));
        }
        else
        {
        	show(root.left);
        	System.out.println("cid: "+root.cid+" , height : "+ root.height +" , height diff: " + heightDiff(root.left, root.right));
			show(root.right);
        }
	}

	
	

	public void print()
	{
		show(root);
	}

	

	public int heightDiff(Node a, Node b)
	{
		if(a == null && b == null)	
		{
			return 0;
		}
		else if(a != null && b != null)
		{
			return (int)(Math.abs(a.height-b.height));
		}
		else if(a == null)
		{
			return b.height+1;
		}
		else
		{
			return a.height+1;
		}
	}

	public int getHeight(Node a)
	{
		if(a == null)
		{
			return -1;
		}
		else
		{
			return a.height;
		}
	}


	static class Node
	{
		int height; // info related to bst
		Node left;   // info related to bst
		Node right;  // info related to bst
		Node parent;  // info related to bst
		int cid;  // info related to bst (key)
		int burgersAsked;  // level in the company
		int entryTime;
		int exitTime;
        int burgersCompleted;
        int wq;
        boolean isWaitingInQueue;
		public Node(int cid, int t, int numb, int wq)
		{
			height=0;
			this.left = null;
			this.right = null;
			this.parent=null;
			this.cid=cid;
			this.entryTime=t;
            this.burgersAsked=numb;
            this.wq=wq;
            this.isWaitingInQueue=true;
		}
	}


}





