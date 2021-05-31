import java.util.*;
public class newtrie
{
    Node root;

    public newtrie()
    {
        root=new Node();
    }

    public int dfs(Node v)
    {
        boolean isLast=true;
        for(int i=0;i<26;i++)
        {
            if(v.children[i] != null)
            {
                isLast=false;
            }
        }
        if(isLast)  
        {
            return 1;
        }
        if(v.hasSentinel)
        {
            int count=1;
            for(int i=0;i<26;i++)
            {
                if(v.children[i] != null)
                {
                    count+=dfs(v.children[i]);
                }
            }  
            return count;
        }
        else
        {
            int count=0;
            for(int i=0;i<26;i++)
            {
                if(v.children[i] != null)
                {
                    count+=dfs(v.children[i]);
                }
            }  
            return count;
        }
    }

    public void add(String s) //returns if prefix present or not
    {
         Node temp=root;
         for(int i=0;i<s.length();i++)          
         {
             if(temp.children[s.charAt(i) - 'a'] == null)
             {
                 temp.children[s.charAt(i) - 'a'] = new Node();
             }
             //counting the number of words added below the node "temp"
             temp.nobelow++;
             temp=temp.children[s.charAt(i) - 'a'];
         }
         if(temp.hasSentinel)
         {
            temp.frequency++;
         }
         temp.hasSentinel=true;
         temp.nobelow++;
    }

    public int findFull(String s)
    {
        // int count=0;
        Node temp=root;
        for(int i=0;i<s.length();i++)
        {
            if(temp.children[s.charAt(i) - 'a'] == null)
            {
                return 0;
            }
            else
            {
                temp=temp.children[s.charAt(i) - 'a'];
            }
        }
        // count+=dfs(temp);
        return temp.nobelow;
    }

    public int isPrefixPresent(String s)
    {
        Node temp=root;
        int count=0;
        for(int i=0;i<s.length();i++)
        {
            if(temp.children[s.charAt(i) - 'a'] == null)
            {
                break;
            } 
            temp=temp.children[s.charAt(i) - 'a'];
            if(temp.hasSentinel)
            {
                count++;
            }
        }
        return count;
    }

    public boolean find(String s)
    {
        Node temp=root;
        for(int i=0;i<s.length();i++)
        {
            if(temp.children[s.charAt(i) - 'a'] == null)
            {
                return false;
            } 
            temp=temp.children[s.charAt(i) - 'a'];
        }
        if(temp.hasSentinel)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static class Node
    {
        Node children[];
        boolean hasSentinel;
        int frequency;
        int nobelow;
        public Node()
        {
            children = new Node[26];
        }
    }

    public static void main(String args[])
    {
        Scanner in = new Scanner(System.in);
        newtrie ob = new newtrie();

        // int n=in.nextInt();
        // in.nextLine();
        ob.add("ask");
        ob.add("ast");
        ob.add("afar");
        ob.add("afair");
        ob.add("book");
        System.out.println(ob.findFull("as"));
        // for(int i=1;i<=n;i++)
        // {
        //     String s = in.nextLine();
        //     if(s.substring(0, s.indexOf(' ')).equals("add"))
        //     {
        //         ob.add(s.substring(s.indexOf(' ')+1));
        //     }
        //     else
        //     {
        //         System.out.println(ob.findFull(s.substring(s.indexOf(' ')+1)));
        //     }
        // }
    }
}