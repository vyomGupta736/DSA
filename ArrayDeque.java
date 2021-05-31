public class ArrayDeque implements DequeInterface {
  public Object a[];
  public int f;
  public int r;
  public int size_of_queue;
  public String s;
  public ArrayDeque()
  {
    a = new Object[1];
    f=0;r=0;size_of_queue=0;
    s="";
  }

  public void insertFirst(Object o){
     if(size_of_queue == a.length-1)
     {
         Object temp[] = new Object[a.length*2];
         int counter=1;
         int i=f;
         while(i!=r)
         {
             temp[counter++]=a[i];
             i=(i+1)%a.length;
         }
         temp[0]=o;
         size_of_queue++;
         r=size_of_queue;
         f=0;
         a=temp;
         temp=null;
     }
     else
     {
        if(f-1 < 0)
        {
            f=(a.length-1-f);
            a[f]=o;
        }
        else
        {
           f--;
           a[f]=o;
        }
        size_of_queue++;
     }
     s=o+", "+s;
  }
  
  public void insertLast(Object o){
     if(size_of_queue == a.length-1) 
     {
        int counter=0;
        Object temp[] = new Object[a.length*2];
        for(int i=f;i!=r;i=(i+1)%a.length)
        {
           temp[counter++]=a[i];
        }
        temp[counter++]=o;
        size_of_queue++;
        r=counter;
        f=0;
        a=temp;
        temp=null;
     }else
     {
        a[r]=o;
        r=(r+1)%a.length;
        size_of_queue++;
     }
     s=s+o+", ";
  }
  
  public Object removeFirst() throws EmptyDequeException{
     if(size_of_queue == 0)
     {
        throw new EmptyDequeException("Empty Deque");
     }
     else
     {
         Object temp=a[f];
         f=(f+1)%a.length;
         size_of_queue--;
         s=s.substring(3);
         return temp;
     }
  }
  
  public Object removeLast() throws EmptyDequeException{
    if(size_of_queue == 0)
    {
       throw new EmptyDequeException("Empty Deque");
    }
    if(r==0)
    {
      r=a.length-1;
    }
    else
    {
      r--;
    }
    size_of_queue--;
    Object temp=a[r];
    s=s.trim();
    s=s.substring(0, s.length()-2);
    return temp;
  }

  public Object first() throws EmptyDequeException{
    if(size_of_queue == 0)
    {
       throw new EmptyDequeException("Empty Deque");
    }
    else
    {
      return a[f];
    }
  }
  
  public Object last() throws EmptyDequeException{
    if(size_of_queue == 0)
    {
       throw new EmptyDequeException("Empty Deque");
    }
    else
    {
       if(r==0)
       {
          return a[a.length-1];
       }
       else
       {
         return a[r-1];
       }
    }
  }
  
  public int size(){
      return size_of_queue;
  }
  
  public boolean isEmpty(){
      return (size_of_queue > 0)?false:true;
  }

  public String toString(){
      if(s.charAt(0) != '[')
      {
         s=s.trim();
         s='['+s;
      }
      if(s.charAt(s.length()-1) != ']')
      {
         s=s.trim();
         s=s.substring(0, s.length()-1);
         s=s+']';
      }
      return s;
  }  
}