package com.zinglabs.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;


public class SixtyDecimal {
	//ip后三位
   	private static String addr=null;
	
	 public static AtomicLong nowSecSeed = new AtomicLong(System.currentTimeMillis()/1000L);
	 final static char[] digits = {
			'0' , '1' , '2' , '3' , '4' , '5' ,
			'6' , '7' , '8' , '9' , 'a' , 'b' ,
			'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
			'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
			'o' , 'p' , 'q' , 'r' , 's' , 't' ,
			'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
			'A' , 'B' , 'C' , 'D' , 'E' , 'F' , 
			'G' , 'H' , 'I' , 'J' , 'K' , 'L' ,
			'M' , 'N' , 'O' , 'P' , 'Q' , 'R' ,
			'S' , 'T' , 'U' , 'V' , 'W' , 'X' ,
			'Y' , 'Z' , '-',  '%' , '*' , '#'
		    };
	/**
	 * @param i
	 * @param shift
	 * @return
	 */
	private static String toUnsignedString(long i, int shift) {
			char[] buf = new char[128];
			int charPos = 64;
			int radix = 1 << shift;
			long mask = radix - 1;
			do {
			    buf[--charPos] = SixtyDecimal.digits[(int)(i & mask)];
			    i >>>= shift;
			}while (i != 0);
			  return new String(buf, charPos, (64 - charPos));
		    }
	   
	   public static String to64(long i) {
   		    return toUnsignedString(i, 6);
   	    }
	   
	
		public synchronized static String getUinAA() throws UnknownHostException {
	        String ret = null;
	        long seedGet = nowSecSeed.incrementAndGet();
	        //System.out.println(digits.length);
	        // ret=""+System.currentTimeMillis()+(seedGet);
	        if(addr==null||"".equals(addr)){
	        	addr=InetAddress.getLocalHost().getHostAddress().toString();
				addr=addr.substring(addr.length()-3, addr.length());
	        }
	    
	        ret = addr+ to64(seedGet);
//	        colineNOSeed.set(seedGet > 1000000 ? 500000 : seedGet);
	        return ret;
	    }
      public static void main(String[] args) {

    	  Set<String>set =new HashSet<String>();
    	  for(int i=0;i<1000000;i++){
    	 	  String x=null;
    			try {
    				x = getUinAA();
    				set.add(x);
    			} catch (UnknownHostException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	    	  
    	  }
   System.out.println(set.size());
    }
      
      
}
